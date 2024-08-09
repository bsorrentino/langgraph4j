package dev.langchain4j.agentexecutor;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.FinishReason;
import lombok.var;
import org.bsc.langgraph4j.*;
import org.bsc.langgraph4j.checkpoint.BaseCheckpointSaver;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AppenderChannel;
import org.bsc.langgraph4j.state.Channel;

import java.util.*;
import java.util.stream.Collectors;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import static org.bsc.langgraph4j.utils.CollectionsUtils.mapOf;

public class AgentExecutor {

    public class GraphBuilder {
        private BaseCheckpointSaver checkpointSaver;
        private ChatLanguageModel chatLanguageModel;
        private List<Object> objectsWithTools;

        public GraphBuilder checkpointSaver(BaseCheckpointSaver checkpointSaver) {
            this.checkpointSaver = checkpointSaver;
            return this;
        }
        public GraphBuilder chatLanguageModel(ChatLanguageModel chatLanguageModel) {
            this.chatLanguageModel = chatLanguageModel;
            return this;
        }
        public GraphBuilder objectsWithTools(List<Object> objectsWithTools) {
            this.objectsWithTools = objectsWithTools;
            return this;
        }

        public CompiledGraph<State> build() throws GraphStateException {
            Objects.requireNonNull(objectsWithTools, "objectsWithTools is required!");
            Objects.requireNonNull(chatLanguageModel, "chatLanguageModel is required!");


            var toolInfoList = ToolInfo.fromList( objectsWithTools );

            final List<ToolSpecification> toolSpecifications = toolInfoList.stream()
                    .map(ToolInfo::specification)
                    .collect(Collectors.toList());

            var agentRunnable = Agent.builder()
                    .chatLanguageModel(chatLanguageModel)
                    .tools( toolSpecifications )
                    .build();

            CompileConfig.Builder config = new CompileConfig.Builder();

            if( checkpointSaver != null ) {
                config.checkpointSaver(checkpointSaver);
            }

            return new StateGraph<>(State.SCHEMA,State::new)
                    .addEdge(START,"agent")
                    .addNode( "agent", node_async( state ->
                            runAgent(agentRunnable, state))
                    )
                    .addNode( "action", node_async( state ->
                            executeTools(toolInfoList, state))
                    )
                    .addConditionalEdges(
                            "agent",
                            edge_async(AgentExecutor.this::shouldContinue),
                            mapOf("continue", "action", "end", END)
                    )
                    .addEdge("action", "agent")
                    .compile( config.build() );

        }
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

    Map<String,Object> runAgent( Agent agentRunnable, State state ) throws Exception {

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
    Map<String,Object> executeTools( List<ToolInfo> toolInfoList, State state ) throws Exception {

        var agentOutcome = state.agentOutcome().orElseThrow(() -> new IllegalArgumentException("no agentOutcome provided!"));

        if (agentOutcome.action() == null) {
            throw new IllegalStateException("no action provided!" );
        }

        var toolExecutionRequest = agentOutcome.action().toolExecutionRequest();

        var tool = toolInfoList.stream()
                            .filter( v -> v.specification().name().equals(toolExecutionRequest.name()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException("no tool found for: " + toolExecutionRequest.name()));

        var result = tool.executor().execute( toolExecutionRequest, null );

        return mapOf("intermediate_steps", new IntermediateStep( agentOutcome.action(), result ) );

    }

    String shouldContinue(State state) {

        if (state.agentOutcome().map(AgentOutcome::finish).isPresent()) {
            return "end";
        }
        return "continue";
    }

}
