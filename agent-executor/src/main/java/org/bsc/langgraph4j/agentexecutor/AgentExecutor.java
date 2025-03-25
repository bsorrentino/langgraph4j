package org.bsc.langgraph4j.agentexecutor;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.service.tool.ToolExecutor;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.bsc.langgraph4j.*;
import org.bsc.langgraph4j.action.EdgeAction;
import org.bsc.langgraph4j.agentexecutor.actions.CallAgent;
import org.bsc.langgraph4j.agentexecutor.actions.ExecuteTools;
import org.bsc.langgraph4j.agentexecutor.serializer.jackson.JSONStateSerializer;
import org.bsc.langgraph4j.agentexecutor.serializer.std.STDStateSerializer;
import org.bsc.langgraph4j.prebuilt.MessagesState;
import org.bsc.langgraph4j.serializer.StateSerializer;
import org.bsc.langgraph4j.langchain4j.tool.ToolNode;

import java.util.*;

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
            return value("agent_response");
        }


    }

    /**
     * Enum representing different serializers for the agent state.
     */
    enum Serializers {

        STD(new STDStateSerializer()),
        JSON(new JSONStateSerializer());

        private final StateSerializer<AgentExecutor.State> serializer;

        /**
         * Constructs a new Serializers enum with the specified serializer.
         *
         * @param serializer the state serializer
         */
        Serializers(StateSerializer<AgentExecutor.State> serializer) {
            this.serializer = serializer;
        }

        /**
         * Retrieves the state serializer.
         *
         * @return the state serializer
         */
        public StateSerializer<AgentExecutor.State> object() {
            return serializer;
        }
    }


    /**
     * Builder class for constructing a graph of agent execution.
     */
    class Builder {
        private StreamingChatLanguageModel streamingChatLanguageModel;
        private ChatLanguageModel chatLanguageModel;
        private final ToolNode.Builder toolNodeBuilder = ToolNode.builder();
        private StateSerializer<State> stateSerializer;

        /**
         * Sets the chat language model for the graph builder.
         *
         * @param chatLanguageModel the chat language model
         * @return the updated GraphBuilder instance
         */
        public Builder chatLanguageModel(ChatLanguageModel chatLanguageModel) {
            this.chatLanguageModel = chatLanguageModel;
            return this;
        }

        /**
         * Sets the streaming chat language model for the graph builder.
         *
         * @param streamingChatLanguageModel the streaming chat language model
         * @return the updated GraphBuilder instance
         */
        public Builder chatLanguageModel(StreamingChatLanguageModel streamingChatLanguageModel) {
            this.streamingChatLanguageModel = streamingChatLanguageModel;
            return this;
        }

        /**
         * Sets the objects with tools for the graph builder (deprecated).
         *
         * @param objectsWithTools the list of objects with tools
         * @return the updated GraphBuilder instance
         */
        @Deprecated
        public Builder objectsWithTools(List<Object> objectsWithTools) {
            objectsWithTools.forEach(toolNodeBuilder::specification);
            return this;
        }

        /**
         * Sets the tool specification for the graph builder.
         *
         * @param objectsWithTool the tool specification
         * @return the updated GraphBuilder instance
         */
        public Builder toolSpecification(Object objectsWithTool) {
            toolNodeBuilder.specification(objectsWithTool);
            return this;
        }

        /**
         * Sets the tool specification with executor for the graph builder.
         *
         * @param spec    the tool specification
         * @param executor the tool executor
         * @return the updated GraphBuilder instance
         */
        public Builder toolSpecification(ToolSpecification spec, ToolExecutor executor) {
            toolNodeBuilder.specification(spec, executor);
            return this;
        }

        /**
         * Sets the tool specification for the graph builder.
         *
         * @param toolSpecifications the tool specifications
         * @return the updated GraphBuilder instance
         */
        public Builder toolSpecification(ToolNode.Specification toolSpecifications) {
            toolNodeBuilder.specification(toolSpecifications);
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

            if (streamingChatLanguageModel != null && chatLanguageModel != null) {
                throw new IllegalArgumentException("chatLanguageModel and streamingChatLanguageModel are mutually exclusive!");
            }
            if (streamingChatLanguageModel == null && chatLanguageModel == null) {
                throw new IllegalArgumentException("a chatLanguageModel or streamingChatLanguageModel is required!");
            }

            final var toolNode = toolNodeBuilder.build();

            var agent = Agent.builder()
                    .chatLanguageModel(chatLanguageModel)
                    .streamingChatLanguageModel(streamingChatLanguageModel)
                    .tools(toolNode.toolSpecifications())
                    .build();

            if (stateSerializer == null) {
                stateSerializer = Serializers.STD.object();
            }

            final var callAgent = new CallAgent(agent);
            final var executeTools = new ExecuteTools(toolNode);
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

    /**
     * Builder class for constructing a graph of agent execution.
     * @deprecated use {@link Builder}
     */
    @Deprecated
    class GraphBuilder extends Builder { }

    /**
     * Creates a new GraphBuilder instance.
     *
     * @return a new GraphBuilder
     * @deprecated use {@link #builder()}
     */
    @Deprecated
    static GraphBuilder graphBuilder() {
        return new GraphBuilder();
    }
}
