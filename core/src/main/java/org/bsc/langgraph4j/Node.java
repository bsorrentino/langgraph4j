package org.bsc.langgraph4j;

import org.bsc.langgraph4j.action.AsyncNodeActionWithConfig;
import org.bsc.langgraph4j.state.AgentState;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

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
record Node<State extends AgentState>(String id, Function<CompileConfig,AsyncNodeActionWithConfig<State>> actionFactory) {

    /**
     * Constructor that accepts only the `id` and sets `actionFactory` to null.
     *
     * @param id the unique identifier for the node
     */
    public Node(String id) {
        this(id, null);
    }

    /**
     * Checks if this node is equal to another object.
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
}