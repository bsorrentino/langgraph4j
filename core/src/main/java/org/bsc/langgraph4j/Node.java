package org.bsc.langgraph4j;

import org.bsc.langgraph4j.action.AsyncNodeActionWithConfig;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.Channel;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static java.lang.String.format;
import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Represents a node in a graph, characterized by a unique identifier and a factory for creating
 * actions to be executed by the node. This is a generic record where the state type is specified
 * by the type parameter {@code State}.
 *
 * @param <State> the type of the state associated with the node; it must extend {@link AgentState}.
 * @param id the unique identifier for the node.
 * @param actionFactory a factory function that takes a {@link CompileConfig} and returns an
 *                      {@link AsyncNodeActionWithConfig} instance for the specified {@code State}.
 */
record Node<State extends AgentState>(String id, ActionFactory<State> actionFactory)  {

    public static final String PARALLEL_PREFIX = "__PARALLEL__";

    interface ActionFactory<State extends AgentState> {
        AsyncNodeActionWithConfig<State> apply( CompileConfig config ) throws GraphStateException;
    }

    /**
     * Constructor that accepts only the `id` and sets `actionFactory` to null.
     *
     * @param id the unique identifier for the node
     */
    public Node(String id) {
        this(id, null);
    }

    public boolean isParallel() {
        return id.startsWith(PARALLEL_PREFIX);
    }

    /**
     * Checks if this node is equal to another object.
     *
     * @param o the object to compare with
     * @return true if this node is equal to the specified object, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<?> node = (Node<?>) o;
        return Objects.equals(id, node.id);
    }

    /**
     * Returns the hash code value for this node.
     *
     * @return the hash code value for this node
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    static <State extends AgentState> Node<State> parallel(String id, List<AsyncNodeActionWithConfig<State>> actions, Map<String, Channel<?>> channels) {
        return new Node<>( format( "%s(%s)", PARALLEL_PREFIX, id), (config ) -> new AsyncParallelNodeAction<>( actions, channels ));
    }

}

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

