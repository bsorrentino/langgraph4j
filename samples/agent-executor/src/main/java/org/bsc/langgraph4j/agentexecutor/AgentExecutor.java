package org.bsc.langgraph4j.agentexecutor;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.service.tool.ToolExecutor;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.bsc.langgraph4j.*;
import org.bsc.langgraph4j.agentexecutor.actions.CallAgent;
import org.bsc.langgraph4j.agentexecutor.actions.ExecuteTools;
import org.bsc.langgraph4j.agentexecutor.actions.ShouldContinue;
import org.bsc.langgraph4j.agentexecutor.serializer.jackson.JSONStateSerializer;
import org.bsc.langgraph4j.agentexecutor.serializer.std.STDStateSerializer;
import org.bsc.langgraph4j.agentexecutor.state.AgentOutcome;
import org.bsc.langgraph4j.agentexecutor.state.IntermediateStep;
import org.bsc.langgraph4j.serializer.StateSerializer;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AppenderChannel;
import org.bsc.langgraph4j.state.Channel;
import org.bsc.langgraph4j.langchain4j.tool.ToolNode;

import java.util.*;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;

public interface AgentExecutor {

    class State extends AgentState {
        static Map<String, Channel<?>> SCHEMA = Map.of(
                "intermediate_steps", AppenderChannel.<IntermediateStep>of(ArrayList::new)
        );

        public State(Map<String, Object> initData) {
            super(initData);
        }

        public Optional<String> input() {
            return value("input");
        }
        public Optional<AgentOutcome> agentOutcome() {
            return value("agent_outcome");
        }
        public List<IntermediateStep> intermediateSteps() {
            return this.<List<IntermediateStep>>value("intermediate_steps").orElseGet(ArrayList::new);
        }
    }

    enum Serializers {

        STD( new STDStateSerializer() ),
        JSON( new JSONStateSerializer() );

        private final StateSerializer<AgentExecutor.State> serializer;

        Serializers(StateSerializer<AgentExecutor.State> serializer) {
            this.serializer = serializer;
        }

        public StateSerializer<AgentExecutor.State> object() {
            return serializer;
        }

    }

    class Builder {
        private StreamingChatLanguageModel streamingChatLanguageModel;
        private ChatLanguageModel chatLanguageModel;
        private final ToolNode.Builder toolNodeBuilder = ToolNode.builder();
        private StateSerializer<State> stateSerializer;

        public Builder chatLanguageModel(ChatLanguageModel chatLanguageModel) {
            this.chatLanguageModel = chatLanguageModel;
            return this;
        }
        public Builder chatLanguageModel(StreamingChatLanguageModel streamingChatLanguageModel) {
            this.streamingChatLanguageModel = streamingChatLanguageModel;
            return this;
        }
        @Deprecated
        public Builder objectsWithTools(List<Object> objectsWithTools) {
            objectsWithTools.forEach(toolNodeBuilder::specification);
            return this;
        }
        public Builder toolSpecification(Object objectsWithTool) {
            toolNodeBuilder.specification( objectsWithTool );
            return this;
        }
        public Builder toolSpecification(ToolSpecification spec, ToolExecutor executor) {
            toolNodeBuilder.specification( spec, executor );
            return this;
        }
        public Builder toolSpecification(ToolNode.Specification toolSpecifications) {
            toolNodeBuilder.specification( toolSpecifications );
            return this;
        }

        public Builder stateSerializer(StateSerializer<State> stateSerializer) {
            this.stateSerializer = stateSerializer;
            return this;
        }

        public StateGraph<State> build() throws GraphStateException {

            if( streamingChatLanguageModel != null && chatLanguageModel != null ) {
                throw new IllegalArgumentException("chatLanguageModel and streamingChatLanguageModel are mutually exclusive!");
            }
            if( streamingChatLanguageModel == null && chatLanguageModel == null ) {
                throw new IllegalArgumentException("a chatLanguageModel or streamingChatLanguageModel is required!");
            }

            final var toolNode = toolNodeBuilder.build();

            var agent = Agent.builder()
                    .chatLanguageModel(chatLanguageModel)
                    .streamingChatLanguageModel(streamingChatLanguageModel)
                    .tools( toolNode.toolSpecifications() )
                    .build();

            if( stateSerializer == null ) {
                stateSerializer = Serializers.STD.object();
            }

            return new StateGraph<>(State.SCHEMA, stateSerializer)
                    .addNode( "agent", CallAgent.of( agent ) )
                    .addNode( "action", ExecuteTools.of( agent, toolNode ) )
                    .addEdge(START,"agent")
                    .addConditionalEdges("agent",
                            ShouldContinue.of(),
                            Map.of("continue", "action", "end", END)
                    )
                    .addEdge("action", "agent")
                    ;
        }
    }

    static Builder builder() {
        return new Builder();
    }

}


