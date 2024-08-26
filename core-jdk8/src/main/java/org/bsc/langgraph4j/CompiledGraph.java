package org.bsc.langgraph4j;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.bsc.async.AsyncGenerator;
import org.bsc.async.AsyncGeneratorQueue;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.checkpoint.BaseCheckpointSaver;
import org.bsc.langgraph4j.checkpoint.Checkpoint;
import org.bsc.langgraph4j.serializer.MapSerializer;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.StateSnapshot;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.lang.String.format;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;

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
    private final CompileConfig compileConfig;

    /**
     * Constructs a CompiledGraph with the given StateGraph.
     *
     * @param stateGraph the StateGraph to be used in this CompiledGraph
     */
    protected CompiledGraph(StateGraph<State> stateGraph, CompileConfig compileConfig ) {
        this.stateGraph = stateGraph;
        this.compileConfig = compileConfig;
        stateGraph.nodes.forEach(n ->
                nodes.put(n.id(), n.action())
        );

        stateGraph.edges.forEach(e ->
                edges.put(e.sourceId(), e.target())
        );
    }

    public Collection<StateSnapshot> getStateHistory( RunnableConfig config ) {
        var saver = compileConfig.getCheckpointSaver().orElseThrow( () -> (new IllegalStateException("Missing CheckpointSaver!")) );

        return saver.list(config).stream().map( checkpoint -> StateSnapshot.of( checkpoint, config ) ).collect(Collectors.toList());
    }

    public StateSnapshot getState( RunnableConfig config ) {
        var saver = compileConfig.getCheckpointSaver().orElseThrow( () -> (new IllegalStateException("Missing CheckpointSaver!")) );

        var checkpoint = saver.get(config).orElseThrow( () -> (new IllegalStateException("Missing Checkpoint!")) );

        return StateSnapshot.of( checkpoint, config );
    }

    public RunnableConfig updateState( RunnableConfig config, Map<String,Object> values, String asNode ) throws Exception {
        var saver = compileConfig.getCheckpointSaver().orElseThrow( () -> (new IllegalStateException("Missing CheckpointSaver!")) );

        // merge values with checkpoint values
        var updatedCheckpoint = saver.get(config)
                            .map( cp -> cp.updateState(values, stateGraph.getChannels()) )
                            .orElseThrow( () -> (new IllegalStateException("Missing Checkpoint!")) );

        // update checkpoint in saver
        var newConfig = saver.put( config, updatedCheckpoint );

        return RunnableConfig.builder(newConfig)
                                .checkPointId( updatedCheckpoint.getId() )
                                .nextNode( asNode )
                                .build();
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
    public void setMaxIterations(int maxIterations) {
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

    private void addCheckpoint( RunnableConfig config, String nodeId, State state, String nextNodeId ) throws Exception {
        if( compileConfig.getCheckpointSaver().isPresent() ) {
            Checkpoint cp =  Checkpoint.builder()
                                .nodeId( nodeId )
                                .state( state )
                                .nextNodeId( nextNodeId )
                                .build();
            compileConfig.getCheckpointSaver().get().put( config, cp );
        }
    }

    Map<String,Object> getInitialStateFromSchema() {
        return  stateGraph.getChannels().entrySet().stream()
                .filter( c -> c.getValue().getDefault().isPresent() )
                .collect(Collectors.toMap(Map.Entry::getKey, e ->
                    e.getValue().getDefault().get().get()
                ));
    }

    State getInitialState(Map<String,Object> inputs, RunnableConfig config) {

        return compileConfig.getCheckpointSaver()
                .flatMap( saver -> saver.get( config ) )
                .map( cp ->
                    stateGraph.getStateFactory()
                            .apply( AgentState.updateState( cp.getState(), inputs, stateGraph.getChannels() ) )
                )
                .orElseGet( () ->
                    stateGraph.getStateFactory()
                            .apply( AgentState.updateState(getInitialStateFromSchema(), inputs, stateGraph.getChannels() ) )
                );
    }

    private State cloneState( Map<String,Object> data ) throws IOException, ClassNotFoundException {

        Map<String,Object> newData = MapSerializer.INSTANCE.cloneObject(data);

        return stateGraph.getStateFactory().apply(newData);
    }

    private State cloneState( State state ) throws IOException, ClassNotFoundException {
        return cloneState(state.data());
    }

    private void yieldData(BlockingQueue<AsyncGenerator.Data<NodeOutput<State>>> queue, NodeOutput<State> data) {
        queue.add( AsyncGenerator.Data.of( completedFuture( data) ) );
    }

    private void streamData(
                             State initialState,
                             String startNodeId,
                             RunnableConfig config,
                             Consumer<NodeOutput<State>> yieldData) throws Exception {

                var currentState = initialState;


//                if( initialNodeId != null ) {
//
//                    yieldData.accept( NodeOutput.of(initialNodeId, currentState));
//
//                    log.trace("START FROM NODE: {}", initialNodeId);
//                }


                var currentNodeId = startNodeId;
//                addCheckpoint( config, initialNodeId, currentState, currentNodeId );


                Map<String, Object> partialState;

                int iteration = 0;

                while( !Objects.equals(currentNodeId, END) ) {

                    log.trace( "NEXT NODE: {}", currentNodeId);

                    var action = nodes.get(currentNodeId);
                    if (action == null) {
                        throw StateGraph.RunnableErrors.missingNode.exception(currentNodeId);
                    }

                    partialState = action.apply(currentState).get();

                    currentState = cloneState( AgentState.updateState(currentState, partialState, stateGraph.getChannels()) );

                    yieldData.accept( NodeOutput.of(currentNodeId, currentState) );

                    if ( Objects.equals(currentNodeId, stateGraph.getFinishPoint()) ) {
                        addCheckpoint( config, currentNodeId, currentState, stateGraph.getFinishPoint() );
                        break;
                    }

                    final String nextNodeId = nextNodeId(currentNodeId, currentState);
                    addCheckpoint( config, currentNodeId, currentState, nextNodeId );

                    currentNodeId = nextNodeId;

                    if ( Objects.equals(currentNodeId, END) ) {
                        break;
                    }

                    if( ++iteration > maxIterations ) {
                        log.warn( "Maximum number of iterations ({}) reached!", maxIterations);
                        break;
                    }

                }

                yieldData.accept( NodeOutput.of(END, currentState) );

                addCheckpoint( config, END, currentState, END );

                log.trace( "STOP");

    }


    /**
     * Creates an AsyncGenerator stream of NodeOutput based on the provided inputs.
     *
     * @param inputs the input map
     * @param config the invoke configuration
     * @return an AsyncGenerator stream of NodeOutput
     * @throws Exception if there is an error creating the stream
     */
    public AsyncGenerator<NodeOutput<State>> stream(Map<String,Object> inputs, RunnableConfig config ) throws Exception {
        Objects.requireNonNull(config, "config cannot be null");

        final boolean isResumeRequest =  (inputs == null);

        if( isResumeRequest ) {

            BaseCheckpointSaver saver = compileConfig.getCheckpointSaver().orElseThrow(() -> (new IllegalStateException("inputs cannot be null (ie. resume request) if no checkpoint saver is configured")));

            Checkpoint startCheckpoint = saver.get( config ).orElseThrow( () -> (new IllegalStateException("Resume request without a saved checkpoint!")) );

            return AsyncGeneratorQueue.of(new LinkedBlockingQueue<>(), queue -> {

                try  {
                    State startState = stateGraph.getStateFactory().apply( startCheckpoint.getState().data() );
                    streamData( startState,
                                startCheckpoint.getNextNodeId(),
                                config,
                                data -> queue.add( AsyncGenerator.Data.of( completedFuture(data) ) )
                                );
                }
                catch (Throwable e) {
                    throw new RuntimeException( e );
                }

            });

        }

        return AsyncGeneratorQueue.of(new LinkedBlockingQueue<>(), queue -> {

            try  {

                var currentState = cloneState( getInitialState(inputs, config) ) ;

                yieldData( queue, NodeOutput.of( START, currentState ) );

                log.trace( "START");

                var currentNodeId = this.getEntryPoint( currentState );
                addCheckpoint( config, START, currentState, currentNodeId );

                Map<String, Object> partialState;

                int iteration = 0;

                while( !Objects.equals(currentNodeId, END) ) {

                    log.trace( "NEXT NODE: {}", currentNodeId);

                    var action = nodes.get(currentNodeId);
                    if (action == null) {
                        throw StateGraph.RunnableErrors.missingNode.exception(currentNodeId);
                    }

                    partialState = action.apply(currentState).get();

                    currentState = cloneState( AgentState.updateState(currentState, partialState, stateGraph.getChannels()) );

                    yieldData( queue, NodeOutput.of(currentNodeId, currentState) );

                    if ( Objects.equals(currentNodeId, stateGraph.getFinishPoint()) ) {
                        addCheckpoint( config, currentNodeId, currentState, stateGraph.getFinishPoint() );
                        break;
                    }

                    final String nextNodeId = nextNodeId(currentNodeId, currentState);
                    addCheckpoint( config, currentNodeId, currentState, nextNodeId );

                    currentNodeId = nextNodeId;

                    if ( Objects.equals(currentNodeId, END) ) {
                        break;
                    }

                    if( ++iteration > maxIterations ) {
                        log.warn( "Maximum number of iterations ({}) reached!", maxIterations);
                        break;
                    }

                }

                yieldData( queue, NodeOutput.of(END, currentState) );

                addCheckpoint( config, END, currentState, END );
                log.trace( "STOP");

            } catch (Exception e) {
                throw new RuntimeException( e );
            }

        });

    }

    /**
     * Creates an AsyncGenerator stream of NodeOutput based on the provided inputs.
     *
     * @param inputs the input map
     * @return an AsyncGenerator stream of NodeOutput
     * @throws Exception if there is an error creating the stream
     */
    public AsyncGenerator<NodeOutput<State>> stream(Map<String,Object> inputs ) throws Exception {
        return this.stream( inputs, RunnableConfig.builder().build() );
    }
    /**
     * Invokes the graph execution with the provided inputs and returns the final state.
     *
     * @param inputs the input map
     * @param config the invoke configuration
     * @return an Optional containing the final state if present, otherwise an empty Optional
     * @throws Exception if there is an error during invocation
     */
    public Optional<State> invoke(Map<String,Object> inputs, RunnableConfig config ) throws Exception {

        var sourceIterator = stream(inputs, config).iterator();

        var result = StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(sourceIterator, Spliterator.ORDERED),
                false);

        return  result.reduce((a, b) -> b).map( NodeOutput::state);
    }

    /**
     * Invokes the graph execution with the provided inputs and returns the final state.
     *
     * @param inputs the input map
     * @return an Optional containing the final state if present, otherwise an empty Optional
     * @throws Exception if there is an error during invocation
     */
    public Optional<State> invoke(Map<String,Object> inputs ) throws Exception {
        return this.invoke( inputs, RunnableConfig.builder().build() );
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

        String content = type.generator.generate( this, title, printConditionalEdges);

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

        String content = type.generator.generate( this, title, true);

        return new GraphRepresentation( type, content );
    }

    /**
     * Generates a drawable graph representation of the state graph with default title.
     *
     * @param type the type of graph representation to generate
     * @return a diagram code of the state graph
     */
    public GraphRepresentation getGraph( GraphRepresentation.Type type ) {
        return getGraph(type, "Graph Diagram", true);
    }

}
