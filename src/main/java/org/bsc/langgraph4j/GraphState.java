package org.bsc.langgraph4j;

import org.bsc.langgraph4j.action.EdgeAction;
import org.bsc.langgraph4j.action.EdgeAsyncAction;
import org.bsc.langgraph4j.action.NodeAsyncAction;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AgentStateFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.lang.String.format;

enum GraphStateError {
    invalidNodeIdentifier( "END is not a valid node id!"),
    invalidEdgeIdentifier( "END is not a valid edge sourceId!"),
    duplicateNodeError("node with id: %s already exist!"),
    duplicateEdgeError("edge with id: %s already exist!"),
    edgeMappingIsEmpty( "edge mapping is empty!" ),
    missingEntryPoint( "missing Entry Point" ),
    entryPointNotExist("entryPoint: %s doesn't exist!" ),
    finishPointNotExist( "finishPoint: %s doesn't exist!"),
    missingNodeReferencedByEdge( "edge sourceId: %s reference a not existent node!"),
    missingNodeInEdgeMapping( "edge mapping for sourceId: %s contains a not existent nodeId %s!"),
    invalidEdgeTarget( "edge sourceId: %s has an initialized target value!" )
    ;
    private final String errorMessage;

    GraphStateError(String errorMessage ) {
        this.errorMessage = errorMessage;
    }

    GraphStateException exception(String... args ) {
        return new GraphStateException( format(errorMessage, (Object[]) args) );
    }
}

record EdgeCondition<S extends AgentState>(EdgeAsyncAction<S> action, Map<String,String> mappings) {}
record EdgeValue<State extends AgentState>(String id, EdgeCondition<State> value) {}
public class GraphState<State extends AgentState> {

    public class Runnable {

    }
    public static String END = "__END__";

    record Node<State extends AgentState>(String id, NodeAsyncAction<State> action) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?> node = (Node<?>) o;
            return Objects.equals(id, node.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
    record Edge<State extends AgentState>(String sourceId, EdgeValue<State> target) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge<?> node = (Edge<?>) o;
            return Objects.equals(sourceId, node.sourceId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sourceId);
        }

    }

    Set<Node<State>> nodes = new HashSet<>();
    Set<Edge<State>> edges = new HashSet<>();

    String entryPoint;
    String finishPoint;

    AgentStateFactory<State> stateFactory;

    public GraphState( AgentStateFactory<State> stateFactory ) {
        this.stateFactory = stateFactory;
    }

    public void setEntryPoint(String entryPoint) {
        this.entryPoint = entryPoint;
    }

    public void setFinishPoint(String finishPoint) {
        this.finishPoint = finishPoint;
    }

    public void addNode(String id, NodeAsyncAction<State> action) throws GraphStateException {
        if( Objects.equals( id, END)) {
            throw GraphStateError.invalidNodeIdentifier.exception(END);
        }
        var node = new Node<State>(id, action);

        if( nodes.contains(node ) ) {
            throw GraphStateError.duplicateNodeError.exception(id);
        }

        nodes.add( node );
    }

    public void addEdge(String sourceId, String targetId) throws GraphStateException {
        if( Objects.equals( sourceId, END)) {
            throw GraphStateError.invalidEdgeIdentifier.exception(END);
        }
        var edge = new Edge<State>(sourceId, new EdgeValue<>(targetId, null) );

        if( edges.contains(edge ) ) {
            throw GraphStateError.duplicateEdgeError.exception(sourceId);
        }

        edges.add( edge );
    }

    public void addConditionalEdge(String sourceId, EdgeAsyncAction<State> condition, Map<String,String> mappings ) throws GraphStateException {
        if( Objects.equals( sourceId, END)) {
            throw GraphStateError.invalidEdgeIdentifier.exception(END);
        }
        if( mappings == null || mappings.isEmpty() ) {
            throw GraphStateError.edgeMappingIsEmpty.exception(sourceId);
        }
        var edge = new Edge<State>(sourceId, new EdgeValue<>(null, new EdgeCondition<>(condition, mappings)) );

        if( edges.contains(edge ) ) {
            throw GraphStateError.duplicateEdgeError.exception(sourceId);
        }

        edges.add( edge );
    }

    private Node<State> makeFakeNode(String id) {
        return new Node<>(id, null);
    }

    public Runnable compile() throws GraphStateException {
        if( entryPoint == null ) {
            throw GraphStateError.missingEntryPoint.exception();
        }

        if( !nodes.contains( makeFakeNode(entryPoint) ) ) {
            throw GraphStateError.entryPointNotExist.exception(entryPoint);
        }

        if( finishPoint!= null ) {
            if( !nodes.contains( makeFakeNode(entryPoint) ) ) {
                throw GraphStateError.finishPointNotExist.exception(entryPoint);
            }
        }

        for ( Edge<State> edge: edges ) {

            if( !nodes.contains( makeFakeNode(edge.sourceId) ) ) {
                throw GraphStateError.missingNodeReferencedByEdge.exception(edge.sourceId);
            }

            if( edge.target.id() != null ) {
                if(!Objects.equals(edge.target.id(), END) && !nodes.contains( makeFakeNode(edge.target.id()) ) ) {
                    throw GraphStateError.missingNodeReferencedByEdge.exception(edge.target.id());
                }
            }
            else if( edge.target.value() != null ) {
                for ( String nodeId: edge.target.value().mappings().values() ) {
                    if(!Objects.equals(nodeId, END) && !nodes.contains( makeFakeNode(nodeId) ) ) {
                        throw GraphStateError.missingNodeInEdgeMapping.exception(edge.sourceId, nodeId);
                    }
                }
            }
            else {
                throw GraphStateError.invalidEdgeTarget.exception(edge.sourceId);
            }


        }

        return new Runnable();
    }
}
