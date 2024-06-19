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

/**
 * Represents a state graph with nodes and edges.
 *
 * @param <State> the type of the state associated with the graph
 */
public class StateGraph<State extends AgentState> {
    /**
     * Enum representing various error messages related to graph state.
     */
    enum Errors {
        invalidNodeIdentifier("END is not a valid node id!"),
        invalidEdgeIdentifier("END is not a valid edge sourceId!"),
        duplicateNodeError("node with id: %s already exist!"),
        duplicateEdgeError("edge with id: %s already exist!"),
        edgeMappingIsEmpty("edge mapping is empty!"),
        missingEntryPoint("missing Entry Point"),
        entryPointNotExist("entryPoint: %s doesn't exist!"),
        finishPointNotExist("finishPoint: %s doesn't exist!"),
        missingNodeReferencedByEdge("edge sourceId: %s reference a not existent node!"),
        missingNodeInEdgeMapping("edge mapping for sourceId: %s contains a not existent nodeId %s!"),
        invalidEdgeTarget("edge sourceId: %s has an initialized target value!");

        private final String errorMessage;

        Errors(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        /**
         * Creates a new GraphStateException with the formatted error message.
         *
         * @param args the arguments to format the error message
         * @return a new GraphStateException
         */
        GraphStateException exception(String... args) {
            return new GraphStateException(format(errorMessage, (Object[]) args));
        }
    }

    /**
     * Enum representing various error messages related to graph runner.
     */
    enum RunnableErrors {
        missingNodeInEdgeMapping("cannot find edge mapping for id: %s in conditional edge with sourceId: %s "),
        missingNode("node with id: %s doesn't exist!"),
        missingEdge("edge with sourceId: %s doesn't exist!"),
        executionError("%s");

        private final String errorMessage;

        RunnableErrors(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        /**
         * Creates a new GraphRunnerException with the formatted error message.
         *
         * @param args the arguments to format the error message
         * @return a new GraphRunnerException
         */
        GraphRunnerException exception(String... args) {
            return new GraphRunnerException(format(errorMessage, (Object[]) args));
        }
    }

    public static String END = "__END__";

    Set<Node<State>> nodes = new LinkedHashSet<>();
    Set<Edge<State>> edges = new LinkedHashSet<>();

    private EdgeValue<State> entryPoint;
    private String finishPoint;

    private final AgentStateFactory<State> stateFactory;

    /**
     * Constructs a new StateGraph with the specified state factory.
     *
     * @param stateFactory the factory to create agent states
     */
    public StateGraph(AgentStateFactory<State> stateFactory) {
        this.stateFactory = stateFactory;
    }

    public AgentStateFactory<State> getStateFactory() {
        return stateFactory;
    }

    public EdgeValue<State> getEntryPoint() {
        return entryPoint;
    }

    public String getFinishPoint() {
        return finishPoint;
    }

    public void setEntryPoint(String entryPoint) {
        this.entryPoint = new EdgeValue<>(entryPoint, null);
    }
    public void setConditionalEntryPoint(AsyncEdgeAction<State> condition, Map<String, String> mappings) throws GraphStateException {
        if (mappings == null || mappings.isEmpty()) {
            throw Errors.edgeMappingIsEmpty.exception("entry point");
        }
        this.entryPoint = new EdgeValue<>(null, new EdgeCondition<>(condition, mappings));

    }

    public void setFinishPoint(String finishPoint) {
        this.finishPoint = finishPoint;
    }

    /**
     * Adds a node to the graph.
     *
     * @param id     the identifier of the node
     * @param action the action to be performed by the node
     * @throws GraphStateException if the node identifier is invalid or the node already exists
     */
    public void addNode(String id, AsyncNodeAction<State> action) throws GraphStateException {
        if (Objects.equals(id, END)) {
            throw Errors.invalidNodeIdentifier.exception(END);
        }
        var node = new Node<State>(id, action);

        if (nodes.contains(node)) {
            throw Errors.duplicateNodeError.exception(id);
        }

        nodes.add(node);
    }

    /**
     * Adds an edge to the graph.
     *
     * @param sourceId the identifier of the source node
     * @param targetId the identifier of the target node
     * @throws GraphStateException if the edge identifier is invalid or the edge already exists
     */
    public void addEdge(String sourceId, String targetId) throws GraphStateException {
        if (Objects.equals(sourceId, END)) {
            throw Errors.invalidEdgeIdentifier.exception(END);
        }
        var edge = new Edge<State>(sourceId, new EdgeValue<>(targetId, null));

        if (edges.contains(edge)) {
            throw Errors.duplicateEdgeError.exception(sourceId);
        }

        edges.add(edge);
    }

    /**
     * Adds conditional edges to the graph.
     *
     * @param sourceId  the identifier of the source node
     * @param condition the condition to determine the target node
     * @param mappings  the mappings of conditions to target nodes
     * @throws GraphStateException if the edge identifier is invalid, the mappings are empty, or the edge already exists
     */
    public void addConditionalEdges(String sourceId, AsyncEdgeAction<State> condition, Map<String, String> mappings) throws GraphStateException {
        if (Objects.equals(sourceId, END)) {
            throw Errors.invalidEdgeIdentifier.exception(END);
        }
        if (mappings == null || mappings.isEmpty()) {
            throw Errors.edgeMappingIsEmpty.exception(sourceId);
        }
        var edge = new Edge<State>(sourceId, new EdgeValue<>(null, new EdgeCondition<>(condition, mappings)));

        if (edges.contains(edge)) {
            throw Errors.duplicateEdgeError.exception(sourceId);
        }

        edges.add(edge);
    }

    /**
     * Creates a fake node with the specified identifier.
     *
     * @param id the identifier of the fake node
     * @return a new fake node
     */
    private Node<State> makeFakeNode(String id) {
        return new Node<>(id, null);
    }

    /**
     * Compiles the state graph into a compiled graph.
     *
     * @return a compiled graph
     * @throws GraphStateException if there are errors related to the graph state
     */
    public CompiledGraph<State> compile() throws GraphStateException {
        if (entryPoint == null) {
            throw Errors.missingEntryPoint.exception();
        }

        if( entryPoint.id()!=null && !nodes.contains(makeFakeNode(entryPoint.id()))) {
            throw Errors.entryPointNotExist.exception(entryPoint.id());
        }

        if (finishPoint != null) {
            if (!nodes.contains(makeFakeNode(finishPoint))) {
                throw Errors.finishPointNotExist.exception(finishPoint);
            }
        }

        for (Edge<State> edge : edges) {

            if (!nodes.contains(makeFakeNode(edge.sourceId()))) {
                throw Errors.missingNodeReferencedByEdge.exception(edge.sourceId());
            }

            if (edge.target().id() != null) {
                if (!Objects.equals(edge.target().id(), END) && !nodes.contains(makeFakeNode(edge.target().id()))) {
                    throw Errors.missingNodeReferencedByEdge.exception(edge.target().id());
                }
            } else if (edge.target().value() != null) {
                for (String nodeId : edge.target().value().mappings().values()) {
                    if (!Objects.equals(nodeId, END) && !nodes.contains(makeFakeNode(nodeId))) {
                        throw Errors.missingNodeInEdgeMapping.exception(edge.sourceId(), nodeId);
                    }
                }
            } else {
                throw Errors.invalidEdgeTarget.exception(edge.sourceId());
            }
        }

        return new CompiledGraph<>(this);
    }
}
