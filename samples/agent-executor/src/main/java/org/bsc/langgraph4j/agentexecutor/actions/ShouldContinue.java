package org.bsc.langgraph4j.agentexecutor.actions;

import org.bsc.langgraph4j.action.AsyncEdgeAction;
import org.bsc.langgraph4j.agentexecutor.AgentExecutor;
import org.bsc.langgraph4j.agentexecutor.state.AgentOutcome;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class ShouldContinue implements AsyncEdgeAction<AgentExecutor.State> {

    public static ShouldContinue of() {
        return new ShouldContinue();
    }

    private ShouldContinue() {}

    @Override
    public CompletableFuture<String> apply(AgentExecutor.State state) {
        var shouldContinue = state.agentOutcome()
                .map(AgentOutcome::finish)
                .map( finish -> "end" )
                .orElse("continue");
        return completedFuture( shouldContinue );
    }


}