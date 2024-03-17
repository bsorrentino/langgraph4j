package org.bsc.langgraph4j;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

record NodeAsyncActionAdapter<State extends AgentState>(NodeAction<State> action) implements NodeAsyncAction<State> {
    @Override public CompletableFuture<Map<String, Object>> apply(State t) throws Exception {
        return CompletableFuture.completedFuture(action.apply(t));
    }
}

public interface NodeAction <T extends AgentState> {
    Map<String, Object> apply(T t) throws Exception;

    static <T extends AgentState> NodeAsyncAction<T> async( NodeAction<T> action ) {
        return new NodeAsyncActionAdapter<>( action );
    }

}
