package dev.langchain4j.agentexecutor;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.FinishReason;
import lombok.var;
import org.bsc.async.AsyncGenerator;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.NodeOutput;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AppendableValue;

import java.util.*;
import java.util.stream.Collectors;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import static org.bsc.langgraph4j.utils.CollectionsUtils.mapOf;

public class AgentExecutor {

    public static class State extends AgentState {

        public State(Map<String, Object> initData) {
            super(initData);
        }

        Optional<String> input() {
            return value("input");
        }
        Optional<AgentOutcome> agentOutcome() {
            return value("agent_outcome");
        }
        AppendableValue<IntermediateStep> intermediateSteps() {
            return appendableValue("intermediate_steps");
        }


    }

    Map<String,Object> runAgent( Agent agentRunnable, State state ) throws Exception {

        var input = state.input()
                        .orElseThrow(() -> new IllegalArgumentException("no input provided!"));

        var intermediateSteps = state.intermediateSteps().values();

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

    public AsyncGenerator<NodeOutput<State>> execute(ChatLanguageModel chatLanguageModel, Map<String, Object> inputs, List<Object> objectsWithTools) throws Exception {


        var toolInfoList = ToolInfo.fromList( objectsWithTools );

        final List<ToolSpecification> toolSpecifications = toolInfoList.stream()
                .map(ToolInfo::specification)
                .collect(Collectors.toList());

        var agentRunnable = Agent.builder()
                                .chatLanguageModel(chatLanguageModel)
                                .tools( toolSpecifications )
                                .build();

        var workflow = new StateGraph<>(State::new);

        workflow.setEntryPoint("agent");

        workflow.addNode( "agent", node_async( state ->
            runAgent(agentRunnable, state))
        );

        workflow.addNode( "action", node_async( state ->
            executeTools(toolInfoList, state))
        );

        workflow.addConditionalEdges(
                "agent",
                edge_async(this::shouldContinue),
                mapOf("continue", "action", "end", END)
        );

        workflow.addEdge("action", "agent");

        var app = workflow.compile();

        return  app.stream( inputs );
    }
}
