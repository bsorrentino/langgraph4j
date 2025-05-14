package org.bsc.langgraph4j.spring.ai.agentexecutor;

import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.prebuilt.MessagesState;
import org.bsc.langgraph4j.serializer.StateSerializer;
import org.bsc.langgraph4j.spring.ai.agentexecutor.std.AgentStateSerializer;
import org.bsc.langgraph4j.spring.ai.generators.StreamingChatGenerator;
import org.bsc.langgraph4j.spring.ai.tool.SpringAIToolService;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.utils.EdgeMappings;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

/**
 * Represents the core component responsible for executing agent logic.
 * It includes methods for building and managing the execution graph,
 * as well as handling agent actions and state transitions.
 *
 * @author lambochen
 */
public interface AgentExecutor {

    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AgentExecutor.class);

    /**
     * Class responsible for building a state graph.
     */
    class Builder {
        StateSerializer<State> stateSerializer;
        ChatModel chatModel;
        ChatService chatService;
        String systemMessage;
        boolean streaming = false;

        final List<ToolCallback> tools = new ArrayList<>();

        /**
         * Sets the state serializer for the graph builder.
         *
         * @param stateSerializer the state serializer to set
         * @return the current instance of GraphBuilder for method chaining
         */
        public Builder stateSerializer(StateSerializer<State> stateSerializer) {
            this.stateSerializer = stateSerializer;
            return this;
        }

        public Builder chatModel(ChatModel chatModel) {
            this.chatModel = chatModel;
            return this;
        }

        public Builder streamingChatModel(ChatModel chatModel) {
            this.chatModel = chatModel;
            this.streaming = true;
            return this;
        }

        public Builder defaultSystem(String systemMessage) {
            this.systemMessage = systemMessage;
            return this;
        }

        public Builder tool(ToolCallback tool) {
            this.tools.add(Objects.requireNonNull(tool, "tool cannot be null!"));
            return this;
        }

        public Builder tools(List<ToolCallback> tools) {
            this.tools.addAll(Objects.requireNonNull(tools, "tools cannot be null!"));
            return this;
        }

        public Builder tools(ToolCallbackProvider toolCallbackProvider) {
            Objects.requireNonNull(toolCallbackProvider, "toolCallbackProvider cannot be null!");
            var toolCallbacks = toolCallbackProvider.getToolCallbacks();
            if (toolCallbacks.length == 0) {
                throw new IllegalArgumentException("toolCallbackProvider.getToolCallbacks() cannot be empty!");
            }
            this.tools.addAll(List.of(toolCallbacks));
            return this;
        }

        public Builder toolsFromObject(Object objectWithTools) {
            var tools = ToolCallbacks.from(Objects.requireNonNull(objectWithTools, "objectWithTools cannot be null"));
            this.tools.addAll(List.of(tools));
            return this;
        }

        @Deprecated(forRemoval = true)
        public Builder chatService(ChatService chatService) {
            this.chatService = chatService;
            return this;
        }

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
                stateSerializer = new AgentStateSerializer();
            }

            if (chatService == null) {
                chatService = new DefaultChatService(this);
            }

            final var toolService = new SpringAIToolService(chatService.tools());

            AsyncNodeAction<State> callAgentAction = node_async(state ->
                    AgentExecutor.callAgent(state, chatService, streaming));

            AsyncNodeAction<State> executeToolsAction = (state ->
                    AgentExecutor.executeTools(state, toolService));

            return new StateGraph<>(State.SCHEMA, stateSerializer)
                    .addEdge(START, "agent")
                    .addNode("agent", callAgentAction)
                    .addNode("action", executeToolsAction)
                    .addConditionalEdges(
                            "agent",
                            edge_async(AgentExecutor::shouldContinue),
                            EdgeMappings.builder()
                                    .to("action", "continue")
                                    .toEND("end")
                                    .build()
                    )
                    .addEdge("action", "agent")
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

        /**
         * Constructs a new State object using the initial data provided in the initData map.
         *
         * @param initData the map containing the initial settings for this state
         */
        public State(Map<String, Object> initData) {
            super(initData);
        }

    }

    /**
     * Calls an agent with the given state.
     *
     * @param state The current state containing input and intermediate steps.
     * @return A map containing the outcome of the agent call, either an action or a finish.
     */
    static Map<String, Object> callAgent(State state, ChatService chatService, boolean streaming) {
        log.trace("callAgent");

        var messages = state.messages();

        if (messages.isEmpty()) {
            throw new IllegalArgumentException("no input provided!");
        }

        if (streaming) {
            var flux = chatService.streamingExecute(messages);

            var generator = StreamingChatGenerator.builder()
                    .startingNode("agent")
                    .startingState(state)
                    .mapResult(response -> Map.of("messages", response.getResult().getOutput()))
                    .build(flux);

            return Map.of("messages", generator);
        } else {
            var response = chatService.execute(messages);

            var output = response.getResult().getOutput();

            return Map.of("messages", output);
        }

    }

    /**
     * Executes tools based on the provided state.
     *
     * @param state The current state containing necessary information to execute tools.
     * @return A CompletableFuture containing a map with the intermediate steps, if successful. If there is no agent outcome or the tool service execution fails, an appropriate exception will be thrown.
     */
    static CompletableFuture<Map<String, Object>> executeTools(State state, SpringAIToolService toolService) {
        log.trace("executeTools");

        var futureResult = new CompletableFuture<Map<String, Object>>();

        var message = state.lastMessage();

        if (message.isEmpty()) {
            futureResult.completeExceptionally(new IllegalArgumentException("no input provided!"));
        } else if (message.get() instanceof AssistantMessage assistantMessage) {
            if (assistantMessage.hasToolCalls()) {

                return toolService.executeFunctions(assistantMessage.getToolCalls())
                        .thenApply(result -> Map.of("messages", result));

            }
        } else {
            futureResult.completeExceptionally(new IllegalArgumentException("no AssistantMessage provided!"));
        }

        return futureResult;

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