package org.bsc.langgraph4j;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.bsc.async.AsyncGenerator;
import org.bsc.async.AsyncGeneratorQueue;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.state.AgentState;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.StreamSupport;

import static java.lang.String.format;
import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * Represents a compiled graph of nodes and edges.
 * This class manage the StateGraph execution
 * 
 * @param <State> the type of the state associated with the graph
 */
@Slf4j
public class CompiledGraph<State extends AgentState> {

    final StateGraph<State> stateGraph;
    @Getter
    final Map<String, AsyncNodeAction<State>> nodes = new LinkedHashMap<>();
    @Getter
    final Map<String, EdgeValue<State>> edges = new LinkedHashMap<>();

    private int maxIterations = 25;

    /**
     * Constructs a CompiledGraph with the given StateGraph.
     *
     * @param stateGraph the StateGraph to be used in this CompiledGraph
     */
    protected CompiledGraph(StateGraph<State> stateGraph) {
        this.stateGraph = stateGraph;
        stateGraph.nodes.forEach(n ->
                nodes.put(n.id(), n.action())
        );

        stateGraph.edges.forEach(e ->
                edges.put(e.sourceId(), e.target())
        );
    }

    public EdgeValue<State> getEntryPoint() {
        return stateGraph.getEntryPoint();
    }

    public String getFinishPoint() {
        return stateGraph.getFinishPoint();
    }
    /**
     * Sets the maximum number of iterations for the graph execution.
     *
     * @param maxIterations the maximum number of iterations
     * @throws IllegalArgumentException if maxIterations is less than or equal to 0
     */
    void setMaxIterations(int maxIterations) {
        if( maxIterations <= 0 ) {
            throw new IllegalArgumentException("maxIterations must be > 0!");
        }
        this.maxIterations = maxIterations;
    }

    private String nextNodeId( EdgeValue<State> route , State state, String nodeId ) throws Exception {

        if( route == null ) {
            throw StateGraph.RunnableErrors.missingEdge.exception(nodeId);
        }
        if( route.id() != null ) {
            return route.id();
        }
        if( route.value() != null ) {
            var condition = route.value().action();
            var newRoute = condition.apply(state).get();
            var result = route.value().mappings().get(newRoute);
            if( result == null ) {
                throw StateGraph.RunnableErrors.missingNodeInEdgeMapping.exception(nodeId, newRoute);
            }
            return result;
        }
        throw StateGraph.RunnableErrors.executionError.exception( format("invalid edge value for nodeId: [%s] !", nodeId) );
    }

    /**
     * Determines the next node ID based on the current node ID and state.
     *
     * @param nodeId the current node ID
     * @param state the current state
     * @return the next node ID
     * @throws Exception if there is an error determining the next node ID
     */
    private String nextNodeId(String nodeId, State state) throws Exception {
        return nextNodeId(edges.get(nodeId), state, nodeId);

    }

    private String getEntryPoint( State state ) throws Exception {
        return nextNodeId(stateGraph.getEntryPoint(), state, "entryPoint");
    }

    /**
     * Creates an AsyncGenerator stream of NodeOutput based on the provided inputs.
     *
     * @param inputs the input map
     * @return an AsyncGenerator stream of NodeOutput
     * @throws Exception if there is an error creating the stream
     */
    public AsyncGenerator<NodeOutput<State>> stream(Map<String,Object> inputs ) throws Exception {

        return AsyncGeneratorQueue.of(new LinkedBlockingQueue<>(), queue -> {

            try  {
                var currentState = stateGraph.getStateFactory().apply(inputs);

                queue.add( AsyncGenerator.Data.of( completedFuture( NodeOutput.of("start", currentState)) ));
                log.trace( "START");

                var currentNodeId = this.getEntryPoint( currentState );

                Map<String, Object> partialState;

                int iteration = 0;

                while( !Objects.equals(currentNodeId, StateGraph.END) ) {

                    log.trace( "NEXT NODE: {}", currentNodeId);

                    var action = nodes.get(currentNodeId);
                    if (action == null) {
                        throw StateGraph.RunnableErrors.missingNode.exception(currentNodeId);
                    }

                    partialState = action.apply(currentState).get();

                    currentState = currentState.mergeWith(partialState, stateGraph.getStateFactory());

                    queue.add( AsyncGenerator.Data.of( completedFuture( NodeOutput.of(currentNodeId, currentState) ) ));

                    if ( Objects.equals(currentNodeId, stateGraph.getFinishPoint()) ) {
                        break;
                    }

                    currentNodeId = nextNodeId(currentNodeId, currentState);

                    if ( Objects.equals(currentNodeId, StateGraph.END) ) {
                        break;
                    }

                    if( ++iteration > maxIterations ) {
                        log.warn( "Maximum number of iterations ({}) reached!", maxIterations);
                        break;
                    }

                }

                queue.add( AsyncGenerator.Data.of( completedFuture( NodeOutput.of("stop", currentState) ) ));
                log.trace( "STOP");

            } catch (Exception e) {
                throw new RuntimeException( e );
            }

        });

    }

    /**
     * Invokes the graph execution with the provided inputs and returns the final state.
     *
     * @param inputs the input map
     * @return an Optional containing the final state if present, otherwise an empty Optional
     * @throws Exception if there is an error during invocation
     */
    public Optional<State> invoke(Map<String,Object> inputs ) throws Exception {

        var sourceIterator = stream(inputs).iterator();

        var result = StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(sourceIterator, Spliterator.ORDERED),
                false);

        return  result.reduce((a, b) -> b).map( NodeOutput::state);
    }

    /**
     * Generates a drawable graph representation of the state graph.
     *
     * @param type the type of graph representation to generate
     * @param title the title of the graph
     * @return a diagram code of the state graph
     */
    public GraphRepresentation getGraph( GraphRepresentation.Type type, String title ) {

        String content = type.generator.generate( this,title);

        return new GraphRepresentation( type, content );
    }

    /**
     * Generates a drawable graph representation of the state graph with default title.
     *
     * @param type the type of graph representation to generate
     * @return a diagram code of the state graph
     */
    public GraphRepresentation getGraph( GraphRepresentation.Type type ) {
        return getGraph(type, "Graph Diagram");
    }

}
