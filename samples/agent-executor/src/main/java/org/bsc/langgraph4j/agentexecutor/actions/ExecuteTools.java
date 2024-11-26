package org.bsc.langgraph4j.agentexecutor.actions;

import dev.langchain4j.data.message.ToolExecutionResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.agentexecutor.Agent;
import org.bsc.langgraph4j.agentexecutor.state.AgentAction;
import org.bsc.langgraph4j.agentexecutor.AgentExecutor;
import org.bsc.langgraph4j.agentexecutor.state.IntermediateStep;
import org.bsc.langgraph4j.langchain4j.tool.ToolNode;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.completedFuture;

@Slf4j
public class ExecuteTools implements AsyncNodeAction<AgentExecutor.State>  {

    public static ExecuteTools of(Agent agent, ToolNode toolNode) {
        return new ExecuteTools(agent, toolNode);
    }

    final Agent agent;
    final ToolNode toolNode;

    private ExecuteTools(Agent agent, ToolNode toolNode) {
        this.agent = agent;
        this.toolNode = toolNode;
    }

    @Override
    public CompletableFuture<Map<String, Object>> apply(AgentExecutor.State state) {
        return completedFuture(executeTools(toolNode, state));
    }

    private Map<String,Object> executeTools(ToolNode toolNode, AgentExecutor.State state )  {
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

}
