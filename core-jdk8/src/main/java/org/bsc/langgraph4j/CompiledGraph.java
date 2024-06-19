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

                var currentNodeId = this.getEntryPoint( currentState );

                Map<String, Object> partialState;

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
     * Generates a drawable graph representation of the state graph.
     *
     * @param type the type of graph representation to generate
     * @return a diagram code of the state graph
     */
    public GraphRepresentation getGraph( GraphRepresentation.Type type ) {

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


        var entryPoint = stateGraph.getEntryPoint();
        if( entryPoint.id() != null  ) {
            sb.append( format("start -down-> \"%s\"\n", entryPoint.id() ));
        }
        else if( entryPoint.value() != null ) {
            String conditionName = "startcondition";
            sb.append(format("hexagon \"check state\" as %s<<Condition>>\n", conditionName));
            sb.append( plantUML_EdgeCondition(entryPoint.value(), "start", conditionName) );
        }

        conditionalEdgeCount[0] = 0; // reset

        edges.forEach( (k,v) -> {
                    if( v.id() != null ) {
                        sb.append( format( "\"%s\" -down-> \"%s\"\n", k,  v.id() ) );
                        return;
                    }
                    else if( v.value() != null ) {
                        conditionalEdgeCount[0] += 1;
                        String conditionName = format("condition%d", conditionalEdgeCount[0]);
                        sb.append( plantUML_EdgeCondition(v.value(), k, conditionName ));

                    }
                });
        if( stateGraph.getFinishPoint() != null ) {
            sb.append( format( "\"%s\" -down-> stop\n", stateGraph.getFinishPoint() ) );
        }
        sb.append( "@enduml\n" );

        return new GraphRepresentation( type, sb.toString() );
    }

    private String plantUML_EdgeCondition( EdgeCondition<State> condition, String key, String conditionName ) {
        StringBuilder sb = new StringBuilder();
        sb.append(format("\"%s\" -down-> %s\n", key, conditionName));
        condition.mappings().forEach( (cond, to) -> {
                if( to.equals(StateGraph.END) ) {
                    sb.append( format( "%s --> stop: \"%s\"\n", conditionName, cond ) );
                }
                else {
                    sb.append( format( "%s --> \"%s\": \"%s\"\n", conditionName, to, cond ) );
                }
            });

        return sb.toString();
    }
}
