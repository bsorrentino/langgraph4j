package org.bsc.langgraph4j.action;

import org.bsc.langgraph4j.state.AgentState;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface AsyncEdgeAction<S extends AgentState> extends Function<S, CompletableFuture<String>> {

    CompletableFuture<String> apply(S t);

    static <S extends AgentState> AsyncEdgeAction<S> edge_async(EdgeAction<S> syncAction ) {
        return t -> {
            CompletableFuture<String> result = new CompletableFuture<>();
            try {
                result.complete(syncAction.apply(t));
            } catch (Exception e) {
                result.completeExceptionally(e);
            }
            return result;
        };
    }
}
