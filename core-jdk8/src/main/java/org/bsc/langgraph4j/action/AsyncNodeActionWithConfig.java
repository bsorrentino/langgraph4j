package org.bsc.langgraph4j.action;

import org.bsc.langgraph4j.RunnableConfig;
import org.bsc.langgraph4j.state.AgentState;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public interface AsyncNodeActionWithConfig<S extends AgentState> extends BiFunction<S, RunnableConfig,CompletableFuture<Map<String, Object>>> {

    /**
     * Applies this action to the given agent state.
     *
     * @param t the agent state
     * @return a CompletableFuture representing the result of the action
     */
    CompletableFuture<Map<String, Object>> apply(S t, RunnableConfig config);


    static <S extends AgentState> AsyncNodeActionWithConfig<S> node_async(NodeActionWithConfig<S> syncAction) {
        return (t, config ) -> {
            CompletableFuture<Map<String, Object>> result = new CompletableFuture<>();
            try {
                result.complete(syncAction.apply(t, config));
            } catch (Exception e) {
                result.completeExceptionally(e);
            }
            return result;
        };
    }

}
