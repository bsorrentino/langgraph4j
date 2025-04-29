package org.bsc.langgraph4j.multi_agent.executor;

import dev.langchain4j.data.message.ChatMessage;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.EdgeAction;
import org.bsc.langgraph4j.langchain4j.serializer.jackson.LC4jJacksonStateSerializer;
import org.bsc.langgraph4j.langchain4j.serializer.std.LC4jStateSerializer;
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
     * Builder class for constructing a graph of agent execution.
     */
    class Builder extends Agent.Builder {

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

            final var toolNode = toolServiceBuilder.build();

            var agent = new Agent( this );

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

}
