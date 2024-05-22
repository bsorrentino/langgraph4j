package org.bsc.langgraph4j;

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

@Slf4j
public class CompiledGraph<State extends AgentState> {

    final StateGraph<State> stateGraph;
    final Map<String, AsyncNodeAction<State>> nodes = new LinkedHashMap<>();
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

    /**
     * Determines the next node ID based on the current node ID and state.
     *
     * @param nodeId the current node ID
     * @param state the current state
     * @return the next node ID
     * @throws Exception if there is an error determining the next node ID
     */
    private String nextNodeId(String nodeId, State state) throws Exception {

        var route = edges.get(nodeId);
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
     * Creates an AsyncGenerator stream of NodeOutput based on the provided inputs.
     *
     * @param inputs the input map
     * @return an AsyncGenerator stream of NodeOutput
     * @throws Exception if there is an error creating the stream
     */
    public AsyncGenerator<NodeOutput<State>> stream(Map<String,Object> inputs ) throws Exception {

        return AsyncGeneratorQueue.of(new LinkedBlockingQueue<>(), queue -> {

            var currentState = stateGraph.getStateFactory().apply(inputs);
            var currentNodeId = stateGraph.getEntryPoint();
            Map<String, Object> partialState;

            try  {
                for(int i = 0; i < maxIterations &&  !Objects.equals(currentNodeId, StateGraph.END); ++i ) {
                    var action = nodes.get(currentNodeId);
                    if (action == null) {
                        throw StateGraph.RunnableErrors.missingNode.exception(currentNodeId);
                    }

                    partialState = action.apply(currentState).get();

                    currentState = currentState.mergeWith(partialState, stateGraph.getStateFactory());

                    var data = new NodeOutput<>(currentNodeId, currentState);

                    queue.add( AsyncGenerator.Data.of( completedFuture(data) ));

                    if (Objects.equals(currentNodeId, stateGraph.getFinishPoint())) {
                        break;
                    }

                    currentNodeId = nextNodeId(currentNodeId, currentState);

                }

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
     * Processes a node for graph representation.
     *
     * @param node the node to be processed
     */
    private void processNodeForGraph(String node) {
        // Implementation to be added
    }

    /**
     * Generates a drawable graph representation of the state graph.
     *
     * @return a DrawableGraph representing the state graph
     */
    public DrawableGraph getGraph() {
        StringBuilder sb = new StringBuilder()
        .append( "@startuml unnamed.puml\n" )
        .append("skinparam usecaseFontSize 14\n")
        .append("skinparam usecaseStereotypeFontSize 12\n")
        .append("skinparam hexagonFontSize 14\n" )
        .append("skinparam hexagonStereotypeFontSize 12\n")
        .append("title \"Graph Diagram\"\n" )
        .append("footer\n\n")
        .append("powered by langgraph4j\n")
        .append("end footer\n")
        .append("circle start<<input>>\n")
        .append("circle stop\n");

        nodes.keySet()
                .forEach( s -> sb.append( format( "usecase \"%s\"<<Node>>\n", s ) ) );

        final int[] conditionalEdgeCount = { 0 };

        edges.forEach( (k, v) -> {
                    if( v.value() != null ) {
                        conditionalEdgeCount[0] += 1;
                        sb.append(format("hexagon \"check state\" as condition%d<<Condition>>\n", conditionalEdgeCount[0]));
                    }
                });

        sb.append( format("start -down-> \"%s\"\n", stateGraph.getEntryPoint() ));

        conditionalEdgeCount[0] = 0; // reset

        edges.forEach( (k,v) -> {
                    if( v.id() != null ) {
                        sb.append( format( "\"%s\" -down-> \"%s\"\n", k,  v.id() ) );
                        return;
                    }
                    else if( v.value() != null ) {
                        conditionalEdgeCount[0] += 1;
                        sb.append(format("\"%s\" -down-> condition%d\n", k, conditionalEdgeCount[0]));

                        var mappings = v.value().mappings();
                        mappings.forEach( (cond, to) -> {
                            if( to.equals(StateGraph.END) ) {
                                sb.append( format( "condition%d --> stop: \"%s\"\n", conditionalEdgeCount[0], cond ) );
                            }
                            else {
                                sb.append( format( "condition%d --> \"%s\": \"%s\"\n", conditionalEdgeCount[0], to, cond ) );
                            }
                        });
                    }
                });
        if( stateGraph.getFinishPoint() != null ) {
            sb.append( format( "\"%s\" -down-> stop\n", stateGraph.getFinishPoint() ) );
        }
        sb.append( "@enduml\n" );

        return DrawableGraph.PLANTUML.withContent( sb.toString() );
    }
}
