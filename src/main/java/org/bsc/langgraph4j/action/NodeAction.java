package org.bsc.langgraph4j.action;

import org.bsc.langgraph4j.state.AgentState;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

record NodeAsyncActionAdapter<State extends AgentState>(NodeAction<State> action) implements NodeAsyncAction<State> {
    @Override public CompletableFuture<Map<String, Object>> apply(State t)  {
        var result = new CompletableFuture<Map<String, Object>>();
        try {
            result.complete(action.apply(t));
        } catch (Exception e) {
            result.completeExceptionally(e);
        }
        return result;
    }
}

public interface NodeAction <T extends AgentState> {
    Map<String, Object> apply(T t) throws Exception;

    static <T extends AgentState> NodeAsyncAction<T> node_async(NodeAction<T> action ) {
        return new NodeAsyncActionAdapter<>( action );
    }

}
