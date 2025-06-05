package org.bsc.langgraph4j.spring.ai.agentexecutor;

import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.action.AsyncNodeActionWithConfig;
import org.bsc.langgraph4j.action.EdgeAction;
import org.bsc.langgraph4j.prebuilt.MessagesState;
import org.bsc.langgraph4j.spring.ai.generators.StreamingChatGenerator;
import org.bsc.langgraph4j.spring.ai.serializer.std.SpringAIStateSerializer;
import org.bsc.langgraph4j.spring.ai.tool.SpringAIToolService;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.Channel;
import org.bsc.langgraph4j.state.Channels;
import org.bsc.langgraph4j.utils.EdgeMappings;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.ToolResponseMessage;

import java.util.*;
import java.util.concurrent.CompletableFuture;

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
     * Class responsible for building a state graph.
     */
    class Builder extends AgentExecutorBuilder<Builder, State> {

        /**
         * Builds and returns a StateGraph with the specified configuration.
         * Initializes the stateSerializer if it's null. Then, constructs a new StateGraph object using the provided schema
         * and serializer, adds an initial edge from the START node to "agent", and then proceeds to add nodes for "agent" and
         * "action". It also sets up conditional edges from the "agent" node based on whether or not to continue.
         *
         * @return A configured StateGraph object.
         * @throws GraphStateException If there is an issue with building the graph state.
         */
        public StateGraph<State> build() throws GraphStateException {

            if (stateSerializer == null) {
                stateSerializer = new SpringAIStateSerializer<>(AgentExecutorEx.State::new);
            }

            var chatService = new ChatService(this);

            var tools = chatService.tools();

            final var toolService = new SpringAIToolService(tools);

            AsyncNodeActionWithConfig<State> callModelAction = CallModel.of( chatService, streaming );

            AsyncNodeAction<State> dispatchToolsAction = node_async(AgentExecutorEx::dispatchTools);

            final EdgeAction<State> dispatchAction = (state) ->
                    state.nextAction().orElse("model");

            var graph = new StateGraph<>(State.SCHEMA, stateSerializer)
                    .addNode("model",callModelAction )
                    .addNode("action_dispatcher", dispatchToolsAction)
                    .addEdge(START, "model")
                    .addConditionalEdges("model",
                            edge_async(AgentExecutorEx::shouldContinue),
                            EdgeMappings.builder()
                                    .to("action_dispatcher", "continue")
                                    .toEND("end" )
                                    .build()) ;

            var actionMappingBuilder  =  EdgeMappings.builder()
                    .to( "model")
                    .toEND();

            for (var tool : tools) {

                var tool_name = tool.getToolDefinition().name();

                actionMappingBuilder.to(tool_name );

                graph.addNode(tool_name,
                        state -> executeTools( state, toolService, tool_name));
                graph.addEdge(tool_name, "action_dispatcher");

            }

            return   graph.addConditionalEdges( "action_dispatcher",
                    edge_async(dispatchAction),
                    actionMappingBuilder.build())
                    ;
        }

    }

    /**
     * Returns a new instance of {@link Builder}.
     *
     * @return a new {@link Builder} object
     */
    static Builder builder() {
        return new Builder();
    }

    /**
     * Represents the state of an agent in a system.
     * This class extends {@link AgentState} and defines constants for keys related to input, agent outcome,
     * and intermediate steps. It includes a static map schema that specifies how these keys should be handled.
     */
    class State extends MessagesState<Message> {

        static final Map<String, Channel<?>> SCHEMA = mergeMap(
                MessagesState.SCHEMA,
                Map.of( "tool_execution_results", Channels.appender(ArrayList::new) ) );

        /**
         * Constructs a new State with the given initialization data.
         *
         * @param initData the initialization data
         */
        public State(Map<String, Object> initData) {
            super(initData);
        }

        public List<ToolResponseMessage> toolExecutionResults() {
            return this.<List<ToolResponseMessage>>value("tool_execution_results")
                    .orElseThrow(() -> new RuntimeException("messages not found"));
        }

        public Optional<String> nextAction() {
            return value("next_action");
        }

    }

    private static Map<String, Object> dispatchTools(State state ) {
        log.trace( "DispatchTools" );

        var toolExecutionRequests = state.lastMessage()
                .filter( m -> MessageType.ASSISTANT==m.getMessageType() )
                .map( AssistantMessage.class::cast )
                .filter(AssistantMessage::hasToolCalls)
                .map(AssistantMessage::getToolCalls);

        if( toolExecutionRequests.isEmpty() ) {
            return Map.of("agent_response", "no tool execution request found!" );
        }

        var requests = toolExecutionRequests.get();

        return requests.stream()
                .filter( request -> state.toolExecutionResults().stream()
                        .flatMap( r -> r.getResponses().stream() )
                        .noneMatch( r -> Objects.equals(r.name(), request.name())))
                .findFirst()
                .map( result -> Map.<String,Object>of( "next_action", result.name() ))
                .orElseGet( () ->  mapOf("messages",  state.toolExecutionResults(),
                        "tool_execution_results", null, /* reset results */
                        "next_action", null  /* remove element */ ));

    }
    /**
     * Executes tools based on the provided state.
     *
     * @param state The current state containing necessary information to execute tools.
     * @return A CompletableFuture containing a map with the intermediate steps, if successful. If there is no agent outcome or the tool service execution fails, an appropriate exception will be thrown.
     */
    static CompletableFuture<Map<String, Object>> executeTools(State state, SpringAIToolService toolService, String actionName ) {
        log.trace( "ExecuteTool" );

        var toolCalls = state.lastMessage()
                .filter( m -> MessageType.ASSISTANT==m.getMessageType() )
                .map( AssistantMessage.class::cast )
                .filter(AssistantMessage::hasToolCalls)
                .map(AssistantMessage::getToolCalls)
                .map( requests -> requests.stream()
                        .filter( req -> Objects.equals(req.name(), actionName)).toList())
                .orElseGet(List::of)
                ;

        if( toolCalls.isEmpty() ) {
            return CompletableFuture.failedFuture( new IllegalArgumentException("no tool execution request found!") );
        }

        return toolService.executeFunctions( toolCalls )
                .thenApply(result -> Map.of("tool_execution_results", result));

    }

    /**
     * Determines whether the game should continue based on the current state.
     *
     * @param state The current state of the game.
     * @return "end" if the game should end, otherwise "continue".
     */
    static String shouldContinue(State state) {

        var message = state.lastMessage().orElseThrow();

        var finishReason = message.getMetadata().getOrDefault("finishReason", "");

        if (Objects.equals(finishReason, "STOP")) {
            return "end";
        }

        if (message instanceof AssistantMessage assistantMessage) {
            if (assistantMessage.hasToolCalls()) {
                return "continue";
            }
        }
        return "end";
    }
}