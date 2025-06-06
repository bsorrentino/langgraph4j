package org.bsc.langgraph4j;

import org.bsc.langgraph4j.action.AsyncCommandAction;
import org.bsc.langgraph4j.action.AsyncEdgeAction;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.action.AsyncNodeActionWithConfig;
import org.bsc.langgraph4j.internal.edge.Edge;
import org.bsc.langgraph4j.internal.edge.EdgeCondition;
import org.bsc.langgraph4j.internal.edge.EdgeValue;
import org.bsc.langgraph4j.internal.node.Node;
import org.bsc.langgraph4j.internal.node.SubCompiledGraphNode;
import org.bsc.langgraph4j.internal.node.SubStateGraphNode;
import org.bsc.langgraph4j.serializer.StateSerializer;
import org.bsc.langgraph4j.serializer.std.ObjectStreamStateSerializer;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AgentStateFactory;
import org.bsc.langgraph4j.state.Channel;

import java.util.*;

import static java.lang.String.format;
import static java.util.Collections.unmodifiableMap;

/**
 * Represents a state graph with nodes and edges.
 *
 * @param <State> the type of the state associated with the graph
 */
public class StateGraph<State extends AgentState> {
    /**
     * Enum representing various error messages related to graph state.
     */
    public enum Errors {
        invalidNodeIdentifier("END is not a valid node id!"),
        invalidEdgeIdentifier("END is not a valid edge sourceId!"),
        duplicateNodeError("node with id: %s already exist!"),
        duplicateEdgeError("edge with id: %s already exist!"),
        duplicateConditionalEdgeError("conditional edge from '%s' already exist!"),
        edgeMappingIsEmpty("edge mapping is empty!"),
        missingEntryPoint("missing Entry Point"),
        entryPointNotExist("entryPoint: %s doesn't exist!"),
        finishPointNotExist("finishPoint: %s doesn't exist!"),
        missingNodeReferencedByEdge("edge sourceId '%s' refers to undefined node!"),
        missingNodeInEdgeMapping("edge mapping for sourceId: %s contains a not existent nodeId %s!"),
        invalidEdgeTarget("edge sourceId: %s has an initialized target value!"),
        duplicateEdgeTargetError("edge [%s] has duplicate targets %s!"),
        unsupportedConditionalEdgeOnParallelNode("parallel node doesn't support conditional branch, but on [%s] a conditional branch on %s have been found!"),
        illegalMultipleTargetsOnParallelNode("parallel node [%s] must have only one target, but %s have been found!"),
        interruptionNodeNotExist( "node '%s' configured as interruption doesn't exist!")
        ;

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
        public GraphStateException exception(Object... args) {
            return new GraphStateException(format(errorMessage, (Object[]) args));
        }
    }

    public static String END = "__END__";
    public static String START = "__START__";

    final Nodes<State> nodes = new Nodes<>();
    final Edges<State> edges = new Edges<>();

    private final Map<String, Channel<?>> channels;

    private final StateSerializer<State> stateSerializer;

    /**
     *
     * @param channels the state's schema of the graph
     * @param stateSerializer the serializer to serialize the state
     */
    public StateGraph(Map<String, Channel<?>> channels,
                      StateSerializer<State> stateSerializer) {
        this.channels = channels;
        this.stateSerializer = Objects.requireNonNull(stateSerializer, "stateSerializer cannot be null");
    }

    /**
     * Constructs a new StateGraph with the specified serializer.
     *
     * @param stateSerializer the serializer to serialize the state
     */
    public StateGraph( StateSerializer<State> stateSerializer) {
        this( Map.of(), stateSerializer );

    }

    /**
     * Constructs a new StateGraph with the specified state factory.
     *
     * @param stateFactory the factory to create agent states
     */
    public StateGraph(AgentStateFactory<State> stateFactory) {
        this( Map.of(), stateFactory);

    }

    /**
     *
     * @param channels the state's schema of the graph
     * @param stateFactory the factory to create agent states
     */
    public StateGraph(Map<String, Channel<?>> channels, AgentStateFactory<State> stateFactory) {
        this( channels, new ObjectStreamStateSerializer<>(stateFactory) );
    }

    public StateSerializer<State> getStateSerializer() {
         return stateSerializer;
    }

    public final AgentStateFactory<State> getStateFactory() {
        return stateSerializer.stateFactory();
    }

    public Map<String, Channel<?>> getChannels() {
        return unmodifiableMap(channels);
    }

    /**
     * Adds a node to the graph.
     *
     * @param id     the identifier of the node
     * @param action the action to be performed by the node
     * @throws GraphStateException if the node identifier is invalid or the node already exists
     */
    public StateGraph<State> addNode(String id, AsyncNodeAction<State> action) throws GraphStateException {
        return addNode( id,  AsyncNodeActionWithConfig.of(action) );
    }

    /**
     *
     * @param id the identifier of the node
     * @param action the action to be performed by the node
     * @return this
     * @throws GraphStateException if the node identifier is invalid or the node already exists
     */
    public StateGraph<State> addNode(String id, AsyncNodeActionWithConfig<State> action) throws GraphStateException {
        if (Objects.equals(id, END)) {
            throw Errors.invalidNodeIdentifier.exception(END);
        }
        Node<State> node = new Node<>(id, (config ) -> action );

        if (nodes.elements.contains(node)) {
            throw Errors.duplicateNodeError.exception(id);
        }

        nodes.elements.add(node);
        return this;
    }

    /**
     * Adds a subgraph to the state graph by creating a node with the specified identifier.
     * This implies that Subgraph share the same state with parent graph
     *
     * @param id the identifier of the node representing the subgraph
     * @param subGraph the compiled subgraph to be added
     * @return this state graph instance
     * @throws GraphStateException if the node identifier is invalid or the node already exists
     */
    public StateGraph<State> addNode(String id, CompiledGraph<State> subGraph) throws GraphStateException {
        if (Objects.equals(id, END)) {
            throw Errors.invalidNodeIdentifier.exception(END);
        }

        var node = new SubCompiledGraphNode<>(id, subGraph);

        if (nodes.elements.contains(node)) {
            throw Errors.duplicateNodeError.exception(id);
        }

        nodes.elements.add(node);
        return this;

    }


    /**
     * Adds a subgraph to the state graph by creating a node with the specified identifier.
     * This implies that Subgraph share the same state with parent graph
     *
     * @param id the identifier of the node representing the subgraph
     * @param subGraph the compiled subgraph to be added
     * @return this state graph instance
     * @throws GraphStateException if the node identifier is invalid or the node already exists
     * @deprecated use {@code addNode( String, CompiledGraph<State> )} instead
     */
    @Deprecated
    public StateGraph<State> addSubgraph(String id, CompiledGraph<State> subGraph) throws GraphStateException {
        return addNode(id, subGraph);
    }

    /**
     * Adds a subgraph to the state graph by creating a node with the specified identifier.
     * This implies that Subgraph share the same state with parent graph
     *
     * @param id the identifier of the node representing the subgraph
     * @param subGraph the subgraph to be added. it will be compiled on compilation of the parent
     * @return this state graph instance
     * @throws GraphStateException if the node identifier is invalid or the node already exists
     */
    public StateGraph<State> addNode(String id, StateGraph<State> subGraph) throws GraphStateException {
        if (Objects.equals(id, END)) {
            throw Errors.invalidNodeIdentifier.exception(END);
        }

        subGraph.validateGraph();

        var node = new SubStateGraphNode<>( id, subGraph );

        if (nodes.elements.contains(node)) {
            throw Errors.duplicateNodeError.exception(id);
        }

        nodes.elements.add(node);
        return this;
    }

    /**
     * Adds a subgraph to the state graph by creating a node with the specified identifier.
     * This implies that Subgraph share the same state with parent graph
     *
     * @param id the identifier of the node representing the subgraph
     * @param subGraph the subgraph to be added. it will be compiled on compilation of the parent
     * @return this state graph instance
     * @throws GraphStateException if the node identifier is invalid or the node already exists
     * @Deprecated use {@code add( String id, StateGraph<State> )} instead
     */
    @Deprecated
    public StateGraph<State> addSubgraph(String id, StateGraph<State> subGraph) throws GraphStateException {
        return addNode( id, subGraph );
    }

    /**
     * Adds an edge to the graph.
     *
     * @param sourceId the identifier of the source node
     * @param targetId the identifier of the target node
     * @throws GraphStateException if the edge identifier is invalid or the edge already exists
     */
    public StateGraph<State> addEdge(String sourceId, String targetId) throws GraphStateException {
        if (Objects.equals(sourceId, END)) {
            throw Errors.invalidEdgeIdentifier.exception(END);
        }

        var newEdge = new Edge<>(sourceId, new EdgeValue<State>(targetId) );

        int index = edges.elements.indexOf( newEdge );
        if( index >= 0 ) {
            var newTargets = new ArrayList<>(edges.elements.get(index).targets());
            newTargets.add( newEdge.target() );
            edges.elements.set( index, new Edge<>(sourceId, newTargets) );
        }
        else {
            edges.elements.add( newEdge );
        }

        return this;
    }

    /**
     * Adds conditional edges to the graph.
     *
     * @param sourceId  the identifier of the source node
     * @param condition the condition to determine the target node
     * @param mappings  the mappings of conditions to target nodes
     * @throws GraphStateException if the edge identifier is invalid, the mappings are empty, or the edge already exists
     */
    public StateGraph<State> addConditionalEdges(String sourceId, AsyncCommandAction<State> condition, Map<String, String> mappings) throws GraphStateException {
        if (Objects.equals(sourceId, END)) {
            throw Errors.invalidEdgeIdentifier.exception(END);
        }
        if (mappings == null || mappings.isEmpty()) {
            throw Errors.edgeMappingIsEmpty.exception(sourceId);
        }

        var newEdge =  new Edge<>(sourceId, new EdgeValue<>( new EdgeCondition<>( condition, mappings)) );

        if( edges.elements.contains( newEdge ) ) {
            throw Errors.duplicateConditionalEdgeError.exception(sourceId);
        }
        else {
            edges.elements.add( newEdge );
        }
        return this;
    }

    /**
     * Adds conditional edges to the graph.
     *
     * @param sourceId  the identifier of the source node
     * @param condition the condition to determine the target node
     * @param mappings  the mappings of conditions to target nodes
     * @throws GraphStateException if the edge identifier is invalid, the mappings are empty, or the edge already exists
     */
    public StateGraph<State> addConditionalEdges(String sourceId, AsyncEdgeAction<State> condition, Map<String, String> mappings) throws GraphStateException {
        return addConditionalEdges( sourceId, AsyncCommandAction.of(condition), mappings);
    }

    void validateGraph( ) throws GraphStateException {
        var edgeStart = edges.edgeBySourceId(START)
                .orElseThrow(Errors.missingEntryPoint::exception);

        edgeStart.validate( nodes );

        for (Edge<State> edge : edges.elements) {
            edge.validate(nodes);
        }

    }

    /**
     * Compiles the state graph into a compiled graph.
     *
     * @param config the compile configuration
     * @return a compiled graph
     * @throws GraphStateException if there are errors related to the graph state
     */
    public CompiledGraph<State> compile( CompileConfig config ) throws GraphStateException {
        Objects.requireNonNull(config, "config cannot be null");

        validateGraph();

        return new CompiledGraph<>(this, config);
    }

    /**
     * Compiles the state graph into a compiled graph.
     *
     * @return a compiled graph
     * @throws GraphStateException if there are errors related to the graph state
     */
    public CompiledGraph<State> compile() throws GraphStateException {
        return compile(CompileConfig.builder().build());
    }

    /**
     * Generates a drawable graph representation of the state graph.
     *
     * @param type the type of graph representation to generate
     * @param title the title of the graph
     * @param printConditionalEdges whether to print conditional edges
     * @return a diagram code of the state graph
     */
    public GraphRepresentation getGraph( GraphRepresentation.Type type, String title, boolean printConditionalEdges ) {

        String content = type.generator.generate( nodes, edges, title, printConditionalEdges);

        return new GraphRepresentation( type, content );
    }

    /**
     * Generates a drawable graph representation of the state graph.
     *
     * @param type the type of graph representation to generate
     * @param title the title of the graph
     * @return a diagram code of the state graph
     */
    public GraphRepresentation getGraph( GraphRepresentation.Type type, String title ) {

        String content = type.generator.generate( nodes, edges, title, true);

        return new GraphRepresentation( type, content );
    }

    public static class Nodes<State extends AgentState> {
        public final Set<Node<State>> elements;

        public Nodes( Collection<Node<State>> elements ) {
            this.elements = new LinkedHashSet<>(elements);
        }

        public Nodes( ) {
            this.elements = new LinkedHashSet<>();
        }

        public boolean anyMatchById(String id ) {
            return elements.stream()
                    .anyMatch( n -> Objects.equals( n.id(), id) );
        }

        public List<SubStateGraphNode<State>> onlySubStateGraphNodes() {
            return elements.stream()
                    .filter(n -> n instanceof SubStateGraphNode<State>)
                    .map(n -> (SubStateGraphNode<State>) n)
                    .toList();
        }

        public List<Node<State>> exceptSubStateGraphNodes() {
            return elements.stream()
                    .filter(n ->  !(n instanceof SubStateGraphNode<State>) )
                    .toList();
        }
    }

    public static class Edges<State extends AgentState> {

        public final List<Edge<State>> elements;

        public Edges( Collection<Edge<State>> elements ) {
            this.elements = new LinkedList<>(elements);
        }

        public Edges( ) {
            this.elements = new LinkedList<>();
        }

        public Optional<Edge<State>> edgeBySourceId(String sourceId ) {
            return elements.stream()
                    .filter( e -> Objects.equals( e.sourceId(), sourceId ))
                    .findFirst();
        }

        public List<Edge<State>> edgesByTargetId(String targetId ) {
            return elements.stream()
                    .filter( e -> e.anyMatchByTargetId(targetId)).toList();
        }


    }

}

