package org.bsc.langgraph4j.multi_agent.executor;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.service.tool.ToolExecutor;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.EdgeAction;
import org.bsc.langgraph4j.action.NodeAction;
import org.bsc.langgraph4j.langchain4j.generators.StreamingChatGenerator;
import org.bsc.langgraph4j.langchain4j.serializer.jackson.LC4jJacksonStateSerializer;
import org.bsc.langgraph4j.langchain4j.serializer.std.LC4jStateSerializer;
import org.bsc.langgraph4j.langchain4j.tool.LC4jToolService;
import org.bsc.langgraph4j.prebuilt.MessagesState;
import org.bsc.langgraph4j.serializer.StateSerializer;

import java.util.Map;
import java.util.Optional;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

/**
 * Interface representing an Agent Executor.
 */
public interface AgentExecutor {

    /**
     * Represents the state of an agent.
     */
    class State extends MessagesState<ChatMessage> {

        public static final String FINAL_RESPONSE = "agent_response";

        /**
         * Constructs a new State with the given initialization data.
         *
         * @param initData the initialization data
         */
        public State(Map<String, Object> initData) {
            super(initData);
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
    class ExecuteTools implements NodeAction<State> {

        private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ExecuteTools.class);
        /**
         * The tool node that will be executed.
         */
        final LC4jToolService toolService;

        /**
         * Constructs an ExecuteTools instance with the specified agent and tool node.
         *
         * @param toolMap the tool node to be executed, must not be null
         */
        public ExecuteTools( Map<ToolSpecification, ToolExecutor> toolMap) {
            this.toolService = new LC4jToolService(toolMap);
        }

        /**
         * Applies the tool execution logic based on the provided agent state.
         *
         * @param state the current state of the agent executor
         * @return a map containing the intermediate steps of the execution
         * @throws IllegalArgumentException if no agent outcome is provided
         * @throws IllegalStateException if no action or tool is found for execution
         */
        @Override
        public Map<String,Object> apply(AgentExecutor.State state )  {
            log.trace( "executeTools" );

            var toolExecutionRequests = state.lastMessage()
                    .filter( m -> ChatMessageType.AI==m.type() )
                    .map( m -> (AiMessage)m )
                    .filter(AiMessage::hasToolExecutionRequests)
                    .map(AiMessage::toolExecutionRequests);
            //.orElseThrow(() -> new IllegalArgumentException("no tool execution request found!"));

            if( toolExecutionRequests.isEmpty() ) {
                return Map.of("agent_response", "no tool execution request found!" );
            }

            var result = toolExecutionRequests.get().stream()
                    .map(toolService::execute)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();

            return Map.of("messages", result );

        }

    }

    /**
     * The CallAgent class implements the NodeAction interface for handling
     * actions related to an AgentExecutor's state.
     */
    class CallAgent implements NodeAction<State> {

        private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CallAgent.class);

        final Agent agent;

        /**
         * Constructs a CallAgent with the specified agent.
         *
         * @param agent the agent to be associated with this CallAgent
         */
        CallAgent( Agent agent ) {
            this.agent = agent;
        }

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

        /**
         * Applies the action to the given state and returns the result.
         *
         * @param state the state to which the action is applied
         * @return a map containing the agent's outcome
         * @throws IllegalArgumentException if no input is provided in the state
         */
        @Override
        public Map<String,Object> apply( AgentExecutor.State state )  {
            log.trace( "callAgent" );
            var messages = state.messages();

            if( messages.isEmpty() ) {
                throw new IllegalArgumentException("no input provided!");
            }

            if( agent.isStreaming()) {

                var generator = StreamingChatGenerator.<AgentExecutor.State>builder()
                        .mapResult( this::mapResult )
                        .startingNode("agent")
                        .startingState( state )
                        .build();
                agent.execute(messages, generator.handler());

                return Map.of( "_generator", generator);


            }
            else {
                var response = agent.execute(messages);

                return mapResult(response);
            }

        }

    }


    /**
     * Builder class for constructing a graph of agent execution.
     */
    class Builder extends Agent.Builder<Builder> {

        private StateSerializer<State> stateSerializer;


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

            if (streamingChatLanguageModel != null && chatLanguageModel != null) {
                throw new IllegalArgumentException("chatLanguageModel and streamingChatLanguageModel are mutually exclusive!");
            }
            if (streamingChatLanguageModel == null && chatLanguageModel == null) {
                throw new IllegalArgumentException("a chatLanguageModel or streamingChatLanguageModel is required!");
            }

            var agent = new Agent( this );

            if (stateSerializer == null) {
                stateSerializer = Serializers.STD.object();
            }

            final var callAgent = new CallAgent(agent);
            final var executeTools = new ExecuteTools(toolMap());
            final EdgeAction<State> shouldContinue = (state) ->
                    state.finalResponse()
                            .map(res -> "end")
                            .orElse("continue");

            return new StateGraph<>(State.SCHEMA, stateSerializer)
                    .addNode("agent", node_async(callAgent))
                    .addNode("action", node_async(executeTools))
                    .addEdge(START, "agent")
                    .addConditionalEdges("agent",
                            edge_async(shouldContinue),
                            Map.of("continue", "action", "end", END)
                    )
                    .addEdge("action", "agent");
        }
    }

    /**
     * Creates a new GraphBuilder instance.
     *
     * @return a new Builder
     */
    static Builder builder() {
        return new Builder();
    }

}
