package org.bsc.langgraph4j;

import lombok.var;
import org.bsc.async.AsyncGenerator;
import org.bsc.async.AsyncGeneratorQueue;
import org.bsc.langgraph4j.action.AsyncEdgeAction;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AgentStateFactory;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.StreamSupport;

import static java.lang.String.format;
import static java.util.concurrent.CompletableFuture.completedFuture;

public class StateGraph<State extends AgentState> {
    enum Errors {
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

        Errors(String errorMessage ) {
            this.errorMessage = errorMessage;
        }

        GraphStateException exception(String... args ) {
            return new GraphStateException( format(errorMessage, (Object[]) args) );
        }

    }
    enum RunnableErrors {
        missingNodeInEdgeMapping("cannot find edge mapping for id: %s in conditional edge with sourceId: %s "),
        missingNode("node with id: %s doesn't exist!"),
        missingEdge("edge with sourceId: %s doesn't exist!"),
        executionError("%s")
        ;
        private final String errorMessage;

        RunnableErrors(String errorMessage ) {
            this.errorMessage = errorMessage;
        }

        GraphRunnerException exception(String... args ) {
            return new GraphRunnerException( format(errorMessage, (Object[]) args) );
        }

    }

    public static String END = "__END__";

    Set<Node<State>> nodes = new LinkedHashSet<>();
    Set<Edge<State>> edges = new LinkedHashSet<>();

    private String entryPoint;
    private String finishPoint;

    private final AgentStateFactory<State> stateFactory;

    public StateGraph(AgentStateFactory<State> stateFactory ) {
        this.stateFactory = stateFactory;
    }

    public AgentStateFactory<State> getStateFactory() { return stateFactory; }
    public String getEntryPoint() { return entryPoint; }
    public String getFinishPoint() { return finishPoint; }
    public void setEntryPoint(String entryPoint) {
        this.entryPoint = entryPoint;
    }
    public void setFinishPoint(String finishPoint) {
        this.finishPoint = finishPoint;
    }

    public void addNode(String id, AsyncNodeAction<State> action) throws GraphStateException {
        if( Objects.equals( id, END)) {
            throw Errors.invalidNodeIdentifier.exception(END);
        }
        var node = new Node<State>(id, action);

        if( nodes.contains(node ) ) {
            throw Errors.duplicateNodeError.exception(id);
        }

        nodes.add( node );
    }

    public void addEdge(String sourceId, String targetId) throws GraphStateException {
        if( Objects.equals( sourceId, END)) {
            throw Errors.invalidEdgeIdentifier.exception(END);
        }
        var edge = new Edge<State>(sourceId, new EdgeValue<>(targetId, null) );

        if( edges.contains(edge ) ) {
            throw Errors.duplicateEdgeError.exception(sourceId);
        }

        edges.add( edge );
    }

    public void addConditionalEdges(String sourceId, AsyncEdgeAction<State> condition, Map<String,String> mappings ) throws GraphStateException {
        if( Objects.equals( sourceId, END)) {
            throw Errors.invalidEdgeIdentifier.exception(END);
        }
        if( mappings == null || mappings.isEmpty() ) {
            throw Errors.edgeMappingIsEmpty.exception(sourceId);
        }
        var edge = new Edge<State>(sourceId, new EdgeValue<>(null, new EdgeCondition<>(condition, mappings)) );

        if( edges.contains(edge ) ) {
            throw Errors.duplicateEdgeError.exception(sourceId);
        }

        edges.add( edge );
    }

    private Node<State> makeFakeNode(String id) {
        return new Node<>(id, null);
    }

    public CompiledGraph<State> compile() throws GraphStateException {
        if( entryPoint == null ) {
            throw Errors.missingEntryPoint.exception();
        }

        if( !nodes.contains( makeFakeNode(entryPoint) ) ) {
            throw Errors.entryPointNotExist.exception(entryPoint);
        }

        if( finishPoint!= null ) {
            if( !nodes.contains( makeFakeNode(entryPoint) ) ) {
                throw Errors.finishPointNotExist.exception(entryPoint);
            }
        }

        for ( Edge<State> edge: edges ) {

            if( !nodes.contains( makeFakeNode(edge.sourceId()) ) ) {
                throw Errors.missingNodeReferencedByEdge.exception(edge.sourceId());
            }

            if( edge.target().id() != null ) {
                if(!Objects.equals(edge.target().id(), END) && !nodes.contains( makeFakeNode(edge.target().id()) ) ) {
                    throw Errors.missingNodeReferencedByEdge.exception(edge.target().id());
                }
            }
            else if( edge.target().value() != null ) {
                for ( String nodeId: edge.target().value().mappings().values() ) {
                    if(!Objects.equals(nodeId, END) && !nodes.contains( makeFakeNode(nodeId) ) ) {
                        throw Errors.missingNodeInEdgeMapping.exception(edge.sourceId(), nodeId);
                    }
                }
            }
            else {
                throw Errors.invalidEdgeTarget.exception(edge.sourceId());
            }

        }

        return new CompiledGraph<>(this);
    }
}
