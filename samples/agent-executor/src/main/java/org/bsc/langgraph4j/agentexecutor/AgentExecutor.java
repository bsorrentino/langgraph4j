package org.bsc.langgraph4j.agentexecutor;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.output.Response;
import lombok.extern.slf4j.Slf4j;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.FinishReason;
import org.bsc.langgraph4j.*;
import org.bsc.langgraph4j.agentexecutor.serializer.json.JSONStateSerializer;
import org.bsc.langgraph4j.agentexecutor.serializer.std.STDStateSerializer;
import org.bsc.langgraph4j.langchain4j.generators.LLMStreamingGenerator;
import org.bsc.langgraph4j.serializer.StateSerializer;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AppenderChannel;
import org.bsc.langgraph4j.state.Channel;

import java.util.*;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import org.bsc.langgraph4j.langchain4j.tool.ToolNode;

@Slf4j
public class AgentExecutor {
    public enum Serializers {

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

    public class GraphBuilder {
        private StreamingChatLanguageModel streamingChatLanguageModel;
        private ChatLanguageModel chatLanguageModel;
        private List<Object> objectsWithTools;
        private StateSerializer<State> stateSerializer;

        public GraphBuilder chatLanguageModel(ChatLanguageModel chatLanguageModel) {
            this.chatLanguageModel = chatLanguageModel;
            return this;
        }
        public GraphBuilder chatLanguageModel(StreamingChatLanguageModel streamingChatLanguageModel) {
            this.streamingChatLanguageModel = streamingChatLanguageModel;
            return this;
        }
        public GraphBuilder objectsWithTools(List<Object> objectsWithTools) {
            this.objectsWithTools = objectsWithTools;
            return this;
        }

        public GraphBuilder stateSerializer( StateSerializer<State> stateSerializer) {
            this.stateSerializer = stateSerializer;
            return this;
        }

        public StateGraph<State> build() throws GraphStateException {
            Objects.requireNonNull(objectsWithTools, "objectsWithTools is required!");
            if( streamingChatLanguageModel != null && chatLanguageModel != null ) {
                throw new IllegalArgumentException("chatLanguageModel and streamingChatLanguageModel are mutually exclusive!");
            }
            if( streamingChatLanguageModel == null && chatLanguageModel == null ) {
                throw new IllegalArgumentException("a chatLanguageModel or streamingChatLanguageModel is required!");
            }

            var toolNode = ToolNode.of( objectsWithTools );

            final List<ToolSpecification> toolSpecifications = toolNode.toolSpecifications();

            var agentRunnable = Agent.builder()
                    .chatLanguageModel(chatLanguageModel)
                    .streamingChatLanguageModel(streamingChatLanguageModel)
                    .tools( toolSpecifications )
                    .build();

            if( stateSerializer == null ) {
                stateSerializer = Serializers.STD.object();
            }

            return new StateGraph<>(State.SCHEMA, stateSerializer)
                    .addEdge(START,"agent")
                    .addNode( "agent", node_async( state ->
                            callAgent(agentRunnable, state))
                    )
                    .addNode( "action", node_async( state ->
                            executeTools(toolNode, state))
                    )
                    .addConditionalEdges(
                            "agent",
                            edge_async(AgentExecutor.this::shouldContinue),
                            Map.of("continue", "action", "end", END)
                    )
                    .addEdge("action", "agent")
                    ;

        }
    }

    public final GraphBuilder graphBuilder() {
        return new GraphBuilder();
    }

    public static class State extends AgentState {
        static Map<String, Channel<?>> SCHEMA = Map.of(
            "intermediate_steps", AppenderChannel.<IntermediateStep>of(ArrayList::new)
        );

        public State(Map<String, Object> initData) {
            super(initData);
        }

        Optional<String> input() {
            return value("input");
        }
        Optional<AgentOutcome> agentOutcome() {
            return value("agent_outcome");
        }
        List<IntermediateStep> intermediateSteps() {
            return this.<List<IntermediateStep>>value("intermediate_steps").orElseGet(ArrayList::new);
        }

    }

    Map<String,Object> callAgent(Agent agentRunnable, State state )  {
        log.trace( "callAgent" );
        var input = state.input()
                        .orElseThrow(() -> new IllegalArgumentException("no input provided!"));

        var intermediateSteps = state.intermediateSteps();

        final Function<Response<AiMessage>, Map<String,Object>> mapResult = response -> {

            if (response.finishReason() == FinishReason.TOOL_EXECUTION) {

                var toolExecutionRequests = response.content().toolExecutionRequests();
                var action = new AgentAction(toolExecutionRequests.get(0), "");

                return Map.of("agent_outcome", new AgentOutcome(action, null));

            } else {
                var result = response.content().text();
                var finish = new AgentFinish(Map.of("returnValues", result), result);

                return Map.of("agent_outcome", new AgentOutcome(null, finish));
            }
        };

        if(agentRunnable.isStreaming()) {

            var generator = LLMStreamingGenerator.<AiMessage, State>builder()
                    .mapResult(mapResult)
                    .startingNode("agent")
                    .startingState( state )
                    .build();
            agentRunnable.execute(input, intermediateSteps, generator.handler());

            return Map.of( "agent_outcome", generator);
        }
        else {
            var response = agentRunnable.execute(input, intermediateSteps);

            return mapResult.apply(response);
        }

    }

    Map<String,Object> executeTools( ToolNode toolNode, State state )  {
        log.trace( "executeTools" );

        var agentOutcome = state.agentOutcome().orElseThrow(() -> new IllegalArgumentException("no agentOutcome provided!"));

        var toolExecutionRequest = ofNullable(agentOutcome.action())
                                        .map(AgentAction::toolExecutionRequest)
                                        .orElseThrow(() -> new IllegalStateException("no action provided!" ))
                                        ;
        var result = toolNode.execute( toolExecutionRequest )
                .map( ToolExecutionResultMessage::text )
                .orElseThrow(() -> new IllegalStateException("no tool found for: " + toolExecutionRequest.name()));

        return Map.of("intermediate_steps", new IntermediateStep( agentOutcome.action(), result ) );

    }

    String shouldContinue(State state) {

        return state.agentOutcome()
                .map(AgentOutcome::finish)
                .map( finish -> "end" )
                .orElse("continue");
    }

}
