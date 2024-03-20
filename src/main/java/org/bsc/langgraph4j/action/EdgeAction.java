package org.bsc.langgraph4j.action;

import lombok.Value;
import lombok.var;
import org.bsc.langgraph4j.state.AgentState;
import java.util.concurrent.CompletableFuture;

@Value
class EdgeAsyncActionAdapter<State extends AgentState> implements EdgeAsyncAction<State> {
    EdgeAction<State> action;

    @Override public CompletableFuture<String> apply(State t)  {
        var result = new CompletableFuture<String>();
        try {
            result.complete(action.apply(t));
        } catch (Exception e) {
            result.completeExceptionally(e);
        }
        return result;
    }
}

@FunctionalInterface
public interface EdgeAction<S extends AgentState> {

    String apply(S t) throws Exception;

    static <T extends AgentState> EdgeAsyncAction<T> edge_async(EdgeAction<T> action ) {
        return new EdgeAsyncActionAdapter<>( action );
    }
}
