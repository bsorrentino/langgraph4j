package org.bsc.langgraph4j.action;

import org.bsc.langgraph4j.state.AgentState;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;


@FunctionalInterface
public interface AsyncNodeAction<S extends AgentState> extends Function<S, CompletableFuture<Map<String, Object>>> {
    CompletableFuture<Map<String, Object>> apply(S t) ;

    static <S extends AgentState> AsyncNodeAction<S> node_async(NodeAction<S> syncAction ) {
        return t -> {
            CompletableFuture<Map<String, Object>> result = new CompletableFuture<>();
            try {
                result.complete(syncAction.apply(t));
            } catch (Exception e) {
                result.completeExceptionally(e);
            }
            return result;
        };
    }
}
