package dev.langchain4j.agentexecutor;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agentexecutor.serializer.AgentActionSerializer;
import dev.langchain4j.agentexecutor.serializer.AgentFinishSerializer;
import dev.langchain4j.agentexecutor.serializer.AgentOutcomeSerializer;
import dev.langchain4j.agentexecutor.serializer.IntermediateStepSerializer;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.FinishReason;
import org.bsc.langgraph4j.*;
import org.bsc.langgraph4j.serializer.StateSerializer;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AppenderChannel;
import org.bsc.langgraph4j.state.Channel;

import java.util.*;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import static org.bsc.langgraph4j.utils.CollectionsUtils.mapOf;
import org.bsc.langgraph4j.langchain4j.tool.ToolNode;

public class AgentExecutor {

    public class GraphBuilder {
        private ChatLanguageModel chatLanguageModel;
        private List<Object> objectsWithTools;

        public GraphBuilder chatLanguageModel(ChatLanguageModel chatLanguageModel) {
            this.chatLanguageModel = chatLanguageModel;
            return this;
        }
        public GraphBuilder objectsWithTools(List<Object> objectsWithTools) {
            this.objectsWithTools = objectsWithTools;
            return this;
        }

        public StateGraph<State> build() throws GraphStateException {
            Objects.requireNonNull(objectsWithTools, "objectsWithTools is required!");
            Objects.requireNonNull(chatLanguageModel, "chatLanguageModel is required!");


            var toolNode = ToolNode.of( objectsWithTools );

            final List<ToolSpecification> toolSpecifications = toolNode.toolSpecifications();

            var agentRunnable = Agent.builder()
                    .chatLanguageModel(chatLanguageModel)
                    .tools( toolSpecifications )
                    .build();


            return new StateGraph<>(State.SCHEMA,State::new)
                    .addEdge(START,"agent")
                    .addNode( "agent", node_async( state ->
                            runAgent(agentRunnable, state))
                    )
                    .addNode( "action", node_async( state ->
                            executeTools(toolNode, state))
                    )
                    .addConditionalEdges(
                            "agent",
                            edge_async(AgentExecutor.this::shouldContinue),
                            mapOf("continue", "action", "end", END)
                    )
                    .addEdge("action", "agent")
                    ;

        }
    }

    public AgentExecutor() {
        StateSerializer.register(IntermediateStep.class, new IntermediateStepSerializer());
        StateSerializer.register(AgentAction.class, new AgentActionSerializer());
        StateSerializer.register(AgentFinish.class, new AgentFinishSerializer());
        StateSerializer.register(AgentOutcome.class, new AgentOutcomeSerializer());
    }

    public final GraphBuilder graphBuilder() {
        return new GraphBuilder();
    }

    public static class State extends AgentState {
        static Map<String, Channel<?>> SCHEMA = mapOf(
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

    Map<String,Object> runAgent( Agent agentRunnable, State state )  {

        var input = state.input()
                        .orElseThrow(() -> new IllegalArgumentException("no input provided!"));

        var intermediateSteps = state.intermediateSteps();

        var response = agentRunnable.execute( input, intermediateSteps );

        if( response.finishReason() == FinishReason.TOOL_EXECUTION ) {

            var toolExecutionRequests = response.content().toolExecutionRequests();
            var action = new AgentAction( toolExecutionRequests.get(0), "");

            return mapOf("agent_outcome", new AgentOutcome( action, null ) );

        }
        else {
            var result = response.content().text();
            var finish = new AgentFinish( mapOf("returnValues", result), result );

            return mapOf("agent_outcome", new AgentOutcome( null, finish ) );
        }

    }
    Map<String,Object> executeTools( ToolNode toolNode, State state )  {

        var agentOutcome = state.agentOutcome().orElseThrow(() -> new IllegalArgumentException("no agentOutcome provided!"));

        if (agentOutcome.action() == null) {
            throw new IllegalStateException("no action provided!" );
        }

        var toolExecutionRequest = agentOutcome.action().toolExecutionRequest();

        var result = toolNode.execute( toolExecutionRequest )
                .map( ToolExecutionResultMessage::text )
                .orElseThrow(() -> new IllegalStateException("no tool found for: " + toolExecutionRequest.name()));;

        return Map.of("intermediate_steps", new IntermediateStep( agentOutcome.action(), result ) );

    }

    String shouldContinue(State state) {

        if (state.agentOutcome().map(AgentOutcome::finish).isPresent()) {
            return "end";
        }
        return "continue";
    }

}
