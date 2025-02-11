package org.bsc.langgraph4j.internal.node;

import org.bsc.langgraph4j.RunnableConfig;
import org.bsc.langgraph4j.action.AsyncNodeActionWithConfig;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.Channel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.lang.String.format;

public class ParallelNode<State extends AgentState> extends Node<State> {
    public static final String PARALLEL_PREFIX = "__PARALLEL__";

    record AsyncParallelNodeAction<State extends AgentState>(
            List<AsyncNodeActionWithConfig<State>> actions,
            Map<String, Channel<?>> channels ) implements AsyncNodeActionWithConfig<State> {

        @Override
        public CompletableFuture<Map<String, Object>> apply(State state, RunnableConfig config) {
            final var partialMergedStates = new HashMap<String, Object>();
            var futures = actions.stream()
                    .map(action ->
                            action.apply(state, config).thenApply(partialState -> {
                                var updatedState = AgentState.updateState( partialMergedStates, partialState, channels);
                                partialMergedStates.putAll(updatedState);
                                return action;
                            }) )
                    //.map( future -> supplyAsync(future::join) )
                    .toList()
                    .toArray(new CompletableFuture[0]);
            return CompletableFuture.allOf(futures)
                    .thenApply((p) -> partialMergedStates);
        }

    }

    public ParallelNode(String id, List<AsyncNodeActionWithConfig<State>> actions, Map<String, Channel<?>> channels) {
        super(format( "%s(%s)", PARALLEL_PREFIX, id), (config ) -> new AsyncParallelNodeAction<>( actions, channels ));
    }

    @Override
    public final boolean isParallel() {
        return true;
    }

}
