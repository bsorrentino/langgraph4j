package org.bsc.langgraph4j;

import lombok.NonNull;
import org.bsc.langgraph4j.action.AsyncNodeActionWithConfig;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.Channel;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static java.lang.String.format;

/**
 * Represents a node in a graph, characterized by a unique identifier and a factory for creating
 * actions to be executed by the node. This is a generic record where the state type is specified
 * by the type parameter {@code State}.
 *
 * @param <State> the type of the state associated with the node; it must extend {@link AgentState}.
 *
 */
class Node<State extends AgentState> {

    public static final String PARALLEL_PREFIX = "__PARALLEL__";

    interface ActionFactory<State extends AgentState> {
        AsyncNodeActionWithConfig<State> apply( CompileConfig config ) throws GraphStateException;
    }

    private final String id;
    private final ActionFactory<State> actionFactory;

    public Node(String id, ActionFactory<State> actionFactory ) {
       this.id = id;
       this.actionFactory = actionFactory;
    }

    /**
     * Constructor that accepts only the `id` and sets `actionFactory` to null.
     *
     * @param id the unique identifier for the node
     */
    public Node(String id) {
        this(id, null);
    }

    /**
     * id
     * @return  the unique identifier for the node.
     */
    public String id() {
        return id;
    }

    /**
     * actionFactory
     * @return a factory function that takes a {@link CompileConfig} and returns an
     *                   {@link AsyncNodeActionWithConfig} instance for the specified {@code State}.
     */
    public ActionFactory<State> actionFactory() {
        return actionFactory;
    }

    public boolean isParallel() {
        return id.startsWith(PARALLEL_PREFIX);
    }

    public Node<State> withIdUpdated( Function<String,String> newId ) {
        return new Node<>( newId.apply( id), actionFactory );
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
        if (o == null ) return false;
        if( o instanceof Node<?> node ) {
            return Objects.equals(id, node.id);
        }
        return false;

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

    @Override
    public String toString() {
        return format( "Node(%s,%s)", id, actionFactory!=null ? "actionFactory" : "null" );
    }
}


class ParallelNode<State extends AgentState> extends Node<State> {

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
}

class SubGraphNode<State extends AgentState> extends Node<State> {

    private final StateGraph<State> subGraph;

    public SubGraphNode(@NonNull String id, @NonNull  StateGraph<State> subGraph ) {
        super(id);
        this.subGraph = subGraph;
    }

    public StateGraph<State> subGraph() {
        return subGraph;
    }

    public String formatId( String nodeId ) {
        return format("%s@%s", id(), nodeId );
    }
}