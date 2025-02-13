package org.bsc.langgraph4j.studio.springboot;

import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.checkpoint.BaseCheckpointSaver;
import org.bsc.langgraph4j.checkpoint.MemorySaver;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.studio.LangGraphStreamingServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Optional.ofNullable;

public record LangGraphFlow(    List<LangGraphStreamingServer.ArgumentMetadata> inputArgs,
                                String title,
                                BaseCheckpointSaver saver,
                                StateGraph<? extends AgentState> stateGraph ) {

    public LangGraphFlow {
        Objects.requireNonNull( stateGraph, "stateGraph is null" );
    }

    public static LangGraphFlow.Builder builder() {
        return new LangGraphFlow.Builder();
    }

    public static class Builder {
        private final List<LangGraphStreamingServer.ArgumentMetadata> inputArgs = new ArrayList<>();
        private String title = null;
        private BaseCheckpointSaver saver;
        private StateGraph<? extends AgentState> stateGraph;


        /**
         * Sets the title for the server.
         *
         * @param title the title to be set
         * @return the Builder instance
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * Adds an input string argument to the server configuration.
         *
         * @param name     the name of the argument
         * @param required whether the argument is required
         * @return the Builder instance
         */
        public Builder addInputStringArg(String name, boolean required) {
            inputArgs.add(new LangGraphStreamingServer.ArgumentMetadata(name, LangGraphStreamingServer.ArgumentMetadata.ArgumentType.STRING, required));
            return this;
        }

        /**
         * Adds an input string argument to the server configuration with required set to true.
         *
         * @param name the name of the argument
         * @return the Builder instance
         */
        public Builder addInputStringArg(String name) {
            return addInputStringArg(name, true);
        }

        /**
         * Adds an input image argument to the server configuration.
         *
         * @param name     the name of the argument
         * @param required whether the argument is required
         * @return the Builder instance
         */
        public Builder addInputImageArg(String name, boolean required) {
            inputArgs.add(new LangGraphStreamingServer.ArgumentMetadata(name, LangGraphStreamingServer.ArgumentMetadata.ArgumentType.IMAGE, required));
            return this;
        }

        /**
         * Adds an input image argument to the server configuration with required set to true.
         *
         * @param name the name of the argument
         * @return the Builder instance
         */
        public Builder addInputImageArg(String name) {
            return addInputImageArg(name, true);
        }

        /**
         * Sets the checkpoint saver for the server.
         *
         * @param saver the checkpoint saver to be used
         * @return the Builder instance
         */
        public Builder checkpointSaver(BaseCheckpointSaver saver) {
            this.saver = saver;
            return this;
        }

        /**
         * Sets the state graph for the server.
         *
         * @param stateGraph the state graph to be used
         * @param <State>    the type of the state
         * @return the Builder instance
         */
        public <State extends AgentState> Builder stateGraph(StateGraph<State> stateGraph) {
            this.stateGraph = stateGraph;
            return this;
        }

        public LangGraphFlow build() {

            return new LangGraphFlow(inputArgs,
                    ofNullable(title).orElse("LangGraph Studio"),
                    ofNullable(saver).orElseGet(MemorySaver::new),
                    stateGraph);
        }
    }
}
