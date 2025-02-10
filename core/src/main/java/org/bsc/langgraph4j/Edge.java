package org.bsc.langgraph4j;

import lombok.NonNull;
import org.bsc.langgraph4j.action.AsyncEdgeAction;
import org.bsc.langgraph4j.state.AgentState;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.bsc.langgraph4j.StateGraph.START;

/**
 * Represents an edge in a graph with a source ID and a target value.
 *
 * @param <State> the type of the state associated with the edge
 * @param sourceId The ID of the source node.
 * @param targets The targets value associated with the edge.
 */
record Edge<State extends AgentState>(String sourceId, List<EdgeValue<State>> targets) {

    public Edge(String sourceId, EdgeValue<State> target) {
        this(sourceId, List.of(target));
    }

    public Edge(String id) {
        this(id, List.of());
    }

    public boolean isParallel() {
        return targets.size() > 1;
    }

    public EdgeValue<State> target() {
        if( isParallel() ) {
            throw new IllegalStateException( format("Edge '%s' is parallel", sourceId));
        }
        return targets.get(0);
    }

    public boolean anyMatchByTargetId( String targetId ) {
        return  targets().stream().anyMatch(v ->
                        ( v.id() != null ) ?
                                Objects.equals( v.id(), targetId ) :
                                v.value().mappings().containsValue( targetId )

                );
    }

    public Edge<State> withSourceAndTargetIdsUpdated(Node<State> node,
                                                     Function<String,String> newSourceId,
                                                     Function<String,EdgeValue<State>> newTarget ) {

        var newTargets = targets().stream()
                .map( t -> t.withTargetIdsUpdated( newTarget ))
                .toList();
        return new Edge<>( newSourceId.apply(sourceId), newTargets);

    }

    public void validate( @NonNull StateGraph.Nodes<State> nodes ) throws GraphStateException {
        if ( !Objects.equals(sourceId(),START) && !nodes.anyMatchById(sourceId())) {
            throw StateGraph.Errors.missingNodeReferencedByEdge.exception(sourceId());
        }

        if( isParallel() ) { // check for duplicates targets
            Set<String> duplicates = targets.stream()
                    .collect(Collectors.groupingBy(EdgeValue::id, Collectors.counting())) // Group by element and count occurrences
                    .entrySet()
                    .stream()
                    .filter(entry -> entry.getValue() > 1) // Filter elements with more than one occurrence
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());
            if( !duplicates.isEmpty() ) {
                throw StateGraph.Errors.duplicateEdgeTargetError.exception(sourceId(), duplicates);
            }
        }

        for( EdgeValue<State> target : targets ) {
            validate(target, nodes);
        }

    }

    private void validate( EdgeValue<State> target, StateGraph.Nodes<State> nodes ) throws GraphStateException {
        if (target.id() != null) {
            if (!Objects.equals(target.id(), StateGraph.END) && !nodes.anyMatchById(target.id())) {
                throw StateGraph.Errors.missingNodeReferencedByEdge.exception(target.id());
            }
        } else if (target.value() != null) {
            for (String nodeId : target.value().mappings().values()) {
                if (!Objects.equals(nodeId, StateGraph.END) && !nodes.anyMatchById(nodeId)) {
                    throw StateGraph.Errors.missingNodeInEdgeMapping.exception(sourceId(), nodeId);
                }
            }
        } else {
            throw StateGraph.Errors.invalidEdgeTarget.exception(sourceId());
        }

    }

    /**
     * Checks if this edge is equal to another object.
     *
     * @param o the object to compare with
     * @return true if this edge is equal to the specified object, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge<?> node = (Edge<?>) o;
        return Objects.equals(sourceId, node.sourceId);
    }

    /**
     * Returns the hash code value for this edge.
     *
     * @return the hash code value for this edge
     */
    @Override
    public int hashCode() {
        return Objects.hash(sourceId);
    }

}

/**
 *
 * @param <State>
 * @param id The unique identifier for the edge value.
 * @param value The condition associated with the edge value.
 */
record EdgeValue<State extends AgentState>( String id, EdgeCondition<State> value) {

    public EdgeValue( String id ) {
        this( id, null );
    }

    public EdgeValue( EdgeCondition<State> value  ) {
        this( null, value );
    }
    EdgeValue<State> withTargetIdsUpdated(Function<String, EdgeValue<State>> target) {
        if( id != null ) {
            return target.apply( id );
        }

        var newMappings = value.mappings().entrySet().stream()
                .collect(Collectors.toMap( Map.Entry::getKey,
                        e -> {
                                                var v = target.apply( e.getValue() );
                                                return ( v.id() != null ) ? v.id() : e.getValue();
                                            }));

        return new EdgeValue<>(null, new EdgeCondition<>( value.action(), newMappings));

    }


 }

/**
 * Represents a condition associated with an edge in a graph.
 *
 * @param <S> the type of the state associated with the edge
 * @param action The action to be performed asynchronously when the edge condition is met.
 * @param mappings A map of string key-value pairs representing additional mappings for the edge condition.
 */
record EdgeCondition<S extends AgentState>( AsyncEdgeAction<S> action, Map<String, String> mappings ) {

    @Override
    public String toString() {
        return format( "EdgeCondition[ %s, mapping=%s",
                action!=null ? "action" : "null",
                mappings);
    }

}
