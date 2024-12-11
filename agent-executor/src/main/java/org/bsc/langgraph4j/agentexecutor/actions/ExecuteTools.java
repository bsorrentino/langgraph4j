package org.bsc.langgraph4j.agentexecutor.actions;

import dev.langchain4j.data.message.ToolExecutionResultMessage;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.bsc.langgraph4j.agentexecutor.Agent;
import org.bsc.langgraph4j.agentexecutor.state.AgentAction;
import org.bsc.langgraph4j.agentexecutor.AgentExecutor;
import org.bsc.langgraph4j.agentexecutor.state.IntermediateStep;
import org.bsc.langgraph4j.langchain4j.tool.ToolNode;

import java.util.Map;

import static java.util.Optional.ofNullable;

@Slf4j
public class ExecuteTools implements NodeAction<AgentExecutor.State> {

    final Agent agent;
    final ToolNode toolNode;

    public ExecuteTools(@NonNull Agent agent, @NonNull ToolNode toolNode) {
        this.agent = agent;
        this.toolNode = toolNode;
    }

    @Override
    public Map<String,Object> apply(AgentExecutor.State state )  {
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
