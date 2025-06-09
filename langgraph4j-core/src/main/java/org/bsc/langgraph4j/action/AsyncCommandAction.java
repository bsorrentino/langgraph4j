package org.bsc.langgraph4j.action;

import org.bsc.langgraph4j.RunnableConfig;
import org.bsc.langgraph4j.state.AgentState;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;


public interface AsyncCommandAction<S extends AgentState> extends BiFunction<S, RunnableConfig, CompletableFuture<Command>> {

    static <S extends AgentState> AsyncCommandAction<S> node_async(CommandAction<S> syncAction) {
        return (state, config ) -> {
            var result = new CompletableFuture<Command>();
            try {
                result.complete(syncAction.apply(state, config));
            } catch (Exception e) {
                result.completeExceptionally(e);
            }
            return result;
        };
    }

    static <S extends AgentState> AsyncCommandAction<S> of(AsyncEdgeAction<S> action) {
        return (state, config) ->
                action.apply(state).thenApply(Command::new);
    }

}
