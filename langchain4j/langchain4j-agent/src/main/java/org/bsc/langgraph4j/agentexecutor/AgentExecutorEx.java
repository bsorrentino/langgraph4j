package org.bsc.langgraph4j.agentexecutor;

import dev.langchain4j.data.message.*;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.output.FinishReason;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.RunnableConfig;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.AsyncNodeActionWithConfig;
import org.bsc.langgraph4j.action.EdgeAction;
import org.bsc.langgraph4j.action.NodeAction;
import org.bsc.langgraph4j.action.NodeActionWithConfig;
import org.bsc.langgraph4j.langchain4j.generators.StreamingChatGenerator;
import org.bsc.langgraph4j.langchain4j.serializer.jackson.LC4jJacksonStateSerializer;
import org.bsc.langgraph4j.langchain4j.serializer.std.LC4jStateSerializer;
import org.bsc.langgraph4j.langchain4j.tool.LC4jToolMapBuilder;
import org.bsc.langgraph4j.langchain4j.tool.LC4jToolService;
import org.bsc.langgraph4j.prebuilt.MessagesState;
import org.bsc.langgraph4j.serializer.StateSerializer;
import org.bsc.langgraph4j.state.Channel;
import org.bsc.langgraph4j.state.Channels;
import org.bsc.langgraph4j.utils.EdgeMappings;

import java.util.*;

import static java.util.Optional.ofNullable;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import static org.bsc.langgraph4j.utils.CollectionsUtils.mapOf;
import static org.bsc.langgraph4j.utils.CollectionsUtils.mergeMap;

/**
 * Interface representing an Agent Executor (AKA ReACT agent).
 * This implementation make in evidence the tools execution using and action dispatcher node
 *              ┌─────┐
 *              │start│
 *              └─────┘
 *                 |
 *              ┌─────┐
 *              │model│
 *              └─────┘
 *                |
 *          ┌─────────────────┐
 *          │action_dispatcher│
 *          └─────────────────┘_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _
 *          |                 \              \                    \
 *       ┌────┐         ┌─────────────┐ ┌─────────────┐      ┌─────────────┐
 *       │stop│         │ tool_name 1 │ │ tool_name 2 │......│ tool_name N │
 *       └────┘         └─────────────┘ └─────────────┘      └─────────────┘
 */
public interface AgentExecutorEx {

    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AgentExecutorEx.class);

    /**
     * Represents the state of an agent.
     */
    class State extends MessagesState<ChatMessage> {

        static final Map<String, Channel<?>> SCHEMA = mergeMap(
                MessagesState.SCHEMA,
                Map.of( "tool_execution_results", Channels.appender(ArrayList::new) ) );

        public static final String FINAL_RESPONSE = "agent_response";

        /**
         * Constructs a new State with the given initialization data.
         *
         * @param initData the initialization data
         */
        public State(Map<String, Object> initData) {
            super(initData);
        }

        public List<ToolExecutionResultMessage> toolExecutionResults() {
            return this.<List<ToolExecutionResultMessage>>value("tool_execution_results")
                    .orElseThrow(() -> new RuntimeException("messages not found"));
        }

        public Optional<String> nextAction() {
            return value("next_action");
        }

        /**
         * Retrieves the agent final response.
         *
         * @return an Optional containing the agent final response if present
         */
        public Optional<String> finalResponse() {
            return value(FINAL_RESPONSE);
        }


    }

    /**
     * Enum representing different serializers for the agent state.
     */
    enum Serializers {

        STD(new LC4jStateSerializer<>(State::new) ),
        JSON(new LC4jJacksonStateSerializer<>(State::new));

        private final StateSerializer<State> serializer;

        /**
         * Constructs a new Serializers enum with the specified serializer.
         *
         * @param serializer the state serializer
         */
        Serializers(StateSerializer<State> serializer) {
            this.serializer = serializer;
        }

        /**
         * Retrieves the state serializer.
         *
         * @return the state serializer
         */
        public StateSerializer<State> object() {
            return serializer;
        }
    }


    /**
     * The ExecuteTools class implements the NodeAction interface for handling
     * actions related to executing tools within an agent's context.
     */
    class DispatchTools implements NodeAction<State> {

        /**
         * Applies the tool execution logic based on the provided agent state.
         *
         * @param state the current state of the agent executor
         * @return a map containing the intermediate steps of the execution
         * @throws IllegalArgumentException if no agent outcome is provided
         * @throws IllegalStateException if no action or tool is found for execution
         */
        @Override
        public Map<String,Object> apply(AgentExecutorEx.State state )  {
            log.trace( "DispatchTools" );

            var toolExecutionRequests = state.lastMessage()
                    .filter( m -> ChatMessageType.AI==m.type() )
                    .map( m -> (AiMessage)m )
                    .filter(AiMessage::hasToolExecutionRequests)
                    .map(AiMessage::toolExecutionRequests);

            if( toolExecutionRequests.isEmpty() ) {
                return Map.of("agent_response", "no tool execution request found!" );
            }

            var requests = toolExecutionRequests.get();

            return requests.stream()
                    .filter( request -> state.toolExecutionResults().stream()
                            .noneMatch( r -> Objects.equals(r.toolName(), request.name())))
                    .findFirst()
                    .map( result -> Map.<String,Object>of( "next_action", result.name() ))
                    .orElseGet( () ->  mapOf("messages",  state.toolExecutionResults(),
                                            "tool_execution_results", null, /* reset results */
                                            "next_action", null  /* remove element */ ));
        }

    }


    class ExecuteTool implements NodeActionWithConfig<State> {

        public static AsyncNodeActionWithConfig<State> of( LC4jToolService toolService, String actionName ) {
            return AsyncNodeActionWithConfig.node_async(new ExecuteTool(toolService, actionName));
        }

        final LC4jToolService toolService;
        final String actionName;

        public ExecuteTool(LC4jToolService toolService, String actionName) {
            this.toolService = toolService;
            this.actionName = actionName;
        }

        @Override
        public Map<String, Object> apply(State state, RunnableConfig config) throws Exception {
            log.trace( "ExecuteTool" );
            var toolExecutionRequests = state.lastMessage()
                    .filter( m -> ChatMessageType.AI==m.type() )
                    .map( m -> (AiMessage)m )
                    .filter(AiMessage::hasToolExecutionRequests)
                    .map(AiMessage::toolExecutionRequests)
                    .map( requests -> requests.stream()
                            .filter( req -> Objects.equals(req.name(), actionName)).toList())
                    .orElseThrow(() -> new IllegalArgumentException("no tool execution request found!"))
                    ;

            var results = toolExecutionRequests.stream()
                    .map(toolService::execute)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList()
            ;

            return Map.of("tool_execution_results", results );
        }

    }

    /**
     * The CallAgent class implements the NodeAction interface for handling
     * actions related to an AgentExecutor's state.
     */
    class CallModel implements NodeAction<State> {

        private final ChatModel chatModel;
        private final StreamingChatModel streamingChatModel;
        private final SystemMessage systemMessage;

        final ChatRequestParameters parameters;

        public boolean isStreaming() {
            return streamingChatModel != null;
        }

        /**
         * Constructs a CallAgent with the specified agent.
         *
         * @param builder the builder used to construct the Agent
         */
        CallModel(Builder builder ) {
            this.chatModel = builder.chatModel;
            this.streamingChatModel = builder.streamingChatModel;
            this.systemMessage = ofNullable( builder.systemMessage ).orElseGet( () -> SystemMessage.from("You are a helpful assistant") );

            var parametersBuilder = ChatRequestParameters.builder()
                    .toolSpecifications( builder.toolMap().keySet().stream().toList() );

            if( builder.responseFormat != null ) {
                parametersBuilder.responseFormat(builder.responseFormat);
            }

            this.parameters =  parametersBuilder.build();        }

        /**
         * Maps the result of the response from an AI message to a structured format.
         *
         * @param response the response containing the AI message
         * @return a map containing the agent's outcome
         * @throws IllegalStateException if the finish reason of the response is unsupported
         */
        private Map<String,Object> mapResult( ChatResponse response )  {

            var content = response.aiMessage();

            if (response.finishReason() == FinishReason.TOOL_EXECUTION || content.hasToolExecutionRequests() ) {
                return Map.of("messages", content);
            }
            if( response.finishReason() == FinishReason.STOP || response.finishReason() == null  ) {
                return Map.of(State.FINAL_RESPONSE, content.text());
            }

            throw new IllegalStateException("Unsupported finish reason: " + response.finishReason() );
        }

        private ChatRequest prepareRequest(List<ChatMessage> messages ) {

            var reqMessages = new ArrayList<ChatMessage>() {{
                add(systemMessage);
                addAll(messages);
            }};

            return ChatRequest.builder()
                    .messages( reqMessages )
                    .parameters(parameters)
                    .build();
        }

        /**
         * Applies the action to the given state and returns the result.
         *
         * @param state the state to which the action is applied
         * @return a map containing the agent's outcome
         * @throws IllegalArgumentException if no input is provided in the state
         */
        @Override
        public Map<String,Object> apply( State state )  {
            log.trace( "callAgent" );
            var messages = state.messages();

            if( messages.isEmpty() ) {
                throw new IllegalArgumentException("no input provided!");
            }

            if( isStreaming()) {

                var generator = StreamingChatGenerator.<State>builder()
                        .mapResult( this::mapResult )
                        .startingNode("agent")
                        .startingState( state )
                        .build();
                streamingChatModel.chat(prepareRequest(messages),  generator.handler());

                return Map.of( "_generator", generator);


            }
            else {
                var response = chatModel.chat(prepareRequest(messages));

                return mapResult(response);
            }

        }

    }


    /**
     * Builder class for constructing a graph of agent execution.
     */
    class Builder extends LC4jToolMapBuilder<Builder> {

        private StateSerializer<State> stateSerializer;
        ChatModel chatModel;
        StreamingChatModel streamingChatModel;
        SystemMessage systemMessage;
        ResponseFormat responseFormat;

        public Builder chatModel( ChatModel chatModel ) {
            if( this.chatModel == null ) {
                this.chatModel = chatModel;
            }
            return this;
        }

        public Builder chatModel( StreamingChatModel streamingChatModel ) {
            if( this.streamingChatModel == null ) {
                this.streamingChatModel = streamingChatModel;
            }
            return this;
        }

        public Builder systemMessage( SystemMessage systemMessage ) {
            if( this.systemMessage == null ) {
                this.systemMessage = systemMessage;
            }
            return this;
        }

        public Builder responseFormat( ResponseFormat responseFormat ) {
            this.responseFormat = responseFormat;
            return this;
        }

        /**
         * Sets the state serializer for the graph builder.
         *
         * @param stateSerializer the state serializer
         * @return the updated GraphBuilder instance
         */
        public Builder stateSerializer(StateSerializer<State> stateSerializer) {
            this.stateSerializer = stateSerializer;
            return this;
        }

        /**
         * Builds the state graph.
         *
         * @return the constructed StateGraph
         * @throws GraphStateException if there is an error in the graph state
         */
        public StateGraph<State> build() throws GraphStateException {

            if (streamingChatModel != null && chatModel != null) {
                throw new IllegalArgumentException("chatLanguageModel and streamingChatLanguageModel are mutually exclusive!");
            }
            if (streamingChatModel == null && chatModel == null) {
                throw new IllegalArgumentException("a chatLanguageModel or streamingChatLanguageModel is required!");
            }

            if (stateSerializer == null) {
                stateSerializer = Serializers.STD.object();
            }

            var tools = toolMap();

            final LC4jToolService toolService = new LC4jToolService(tools);

            final var callModel = new CallModel(this);
            final var executeTools = new DispatchTools( );

            final EdgeAction<State> shouldContinue = (state) ->
                    state.finalResponse()
                            .map(res -> "end")
                            .orElse("continue");

            final EdgeAction<State> dispatchAction = (state) ->
                    state.nextAction().orElse("model");

            var graph = new StateGraph<>(State.SCHEMA, stateSerializer)
                    .addNode("model", node_async(callModel))
                    .addNode("action_dispatcher", node_async(executeTools))
                    .addEdge(START, "model")
                    .addConditionalEdges("model",
                            edge_async(shouldContinue),
                            EdgeMappings.builder()
                                    .to("action_dispatcher", "continue")
                                    .toEND("end" )
                                    .build()) ;

            var actionMappingBuilder  =  EdgeMappings.builder()
                    .to( "model")
                    .toEND();

            for (var tool : tools.entrySet()) {

                var tool_name = tool.getKey().name();

                actionMappingBuilder.to(tool_name );

                graph.addNode(tool_name,
                        ExecuteTool.of(toolService, tool_name));
                graph.addEdge(tool_name, "action_dispatcher");

            }

            return   graph.addConditionalEdges( "action_dispatcher",
                            edge_async(dispatchAction),
                            actionMappingBuilder.build())
                   ;
        }
    }

    /**
     *
     * @return a new Builder
     */
    static Builder builder() {
        return new Builder();
    }

}
