package org.bsc.langgraph4j;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.bsc.async.AsyncGenerator;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.action.AsyncNodeActionWithConfig;
import org.bsc.langgraph4j.checkpoint.BaseCheckpointSaver;
import org.bsc.langgraph4j.checkpoint.Checkpoint;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.StateSnapshot;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
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
    public enum StreamMode {
        VALUES,
        SNAPSHOTS
    }
    final StateGraph<State> stateGraph;
    @Getter
    final Map<String, AsyncNodeActionWithConfig<State>> nodes = new LinkedHashMap<>();
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

    public Collection<StateSnapshot<State>> getStateHistory( RunnableConfig config ) {
        BaseCheckpointSaver saver = compileConfig.checkpointSaver().orElseThrow( () -> (new IllegalStateException("Missing CheckpointSaver!")) );

        return saver.list(config).stream()
                .map( checkpoint -> StateSnapshot.of( checkpoint, config, stateGraph.getStateFactory() ) )
                .collect(Collectors.toList());
    }


    /**
     * Same of {@link #stateOf(RunnableConfig)} but throws an IllegalStateException if checkpoint is not found.
     *
     * @param config the RunnableConfig
     * @return the StateSnapshot of the given RunnableConfig
     * @throws IllegalStateException if the saver is not defined, or no checkpoint is found
     */
    public StateSnapshot<State> getState( RunnableConfig config ) {
        return stateOf(config).orElseThrow( () -> (new IllegalStateException("Missing Checkpoint!")) );
    }


    /**
     * Get the StateSnapshot of the given RunnableConfig.
     *
     * @param config the RunnableConfig
     * @return an Optional of StateSnapshot of the given RunnableConfig
     * @throws IllegalStateException if the saver is not defined
     */
    public Optional<StateSnapshot<State>> stateOf( RunnableConfig config ) {
        BaseCheckpointSaver saver = compileConfig.checkpointSaver().orElseThrow( () -> (new IllegalStateException("Missing CheckpointSaver!")) );

        return saver.get(config)
                .map( checkpoint -> StateSnapshot.of( checkpoint, config, stateGraph.getStateFactory() ) );

    }

    /**
     * Update the state of the graph with the given values.
     * If asNode is given, it will be used to determine the next node to run.
     * If not given, the next node will be determined by the state graph.
     * 
     * @param config the RunnableConfig containg the graph state
     * @param values the values to be updated
     * @param asNode the node id to be used for the next node. can be null
     * @return the updated RunnableConfig
     * @throws Exception when something goes wrong
     */
    public RunnableConfig updateState( RunnableConfig config, Map<String,Object> values, String asNode ) throws Exception {
        BaseCheckpointSaver saver = compileConfig.checkpointSaver().orElseThrow( () -> (new IllegalStateException("Missing CheckpointSaver!")) );

        // merge values with checkpoint values
        Checkpoint branchCheckpoint = saver.get(config)
                            .map(Checkpoint::new)
                            .map( cp -> cp.updateState(values, stateGraph.getChannels()) )
                            .orElseThrow( () -> (new IllegalStateException("Missing Checkpoint!")) );

        String nextNodeId = null;
        if( asNode != null ) {
            nextNodeId = nextNodeId( asNode, branchCheckpoint.getState() );
        }
        // update checkpoint in saver
        RunnableConfig newConfig = saver.put( config, branchCheckpoint );

        return RunnableConfig.builder(newConfig)
                                .checkPointId( branchCheckpoint.getId() )
                                .nextNode( nextNodeId )
                                .build();
    }

    /***
     * Update the state of the graph with the given values.
     *
     * @param config the RunnableConfig containg the graph state
     * @param values the values to be updated
     * @return the updated RunnableConfig
     * @throws Exception when something goes wrong
     */
    public RunnableConfig updateState( RunnableConfig config, Map<String,Object> values ) throws Exception {
        return updateState(config, values, null);
    }

    @Deprecated
    public EdgeValue<State> getEntryPoint() {
        return stateGraph.getEntryPoint();
    }

    @Deprecated
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

    private String nextNodeId( EdgeValue<State> route , Map<String,Object> state, String nodeId ) throws Exception {

        if( route == null ) {
            throw StateGraph.RunnableErrors.missingEdge.exception(nodeId);
        }
        if( route.id() != null ) {
            return route.id();
        }
        if( route.value() != null ) {
            State derefState = stateGraph.getStateFactory().apply(state);
            org.bsc.langgraph4j.action.AsyncEdgeAction<State> condition = route.value().action();
            String newRoute = condition.apply(derefState).get();
            String result = route.value().mappings().get(newRoute);
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
    private String nextNodeId(String nodeId, Map<String,Object> state) throws Exception {
        return nextNodeId(edges.get(nodeId), state, nodeId);

    }

    private String getEntryPoint( Map<String,Object> state ) throws Exception {
        return nextNodeId(stateGraph.getEntryPoint(), state, "entryPoint");
    }

    private boolean shouldInterruptBefore(@NonNull String nodeId, String previousNodeId ) {
        if( previousNodeId == null ) { // FIX RESUME ERROR
            return false;
        }
        return Arrays.asList(compileConfig.getInterruptBefore()).contains(nodeId);
    }

    private boolean shouldInterruptAfter(String nodeId, String previousNodeId ) {
        if( nodeId == null ) { // FIX RESUME ERROR
            return false;
        }
        return Arrays.asList(compileConfig.getInterruptAfter()).contains(nodeId);
    }

    private Optional<Checkpoint> addCheckpoint( RunnableConfig config, String nodeId, Map<String,Object> state, String nextNodeId ) throws Exception {
        if( compileConfig.checkpointSaver().isPresent() ) {
            Checkpoint cp =  Checkpoint.builder()
                                .nodeId( nodeId )
                                .state( cloneState(state) )
                                .nextNodeId( nextNodeId )
                                .build();
            compileConfig.checkpointSaver().get().put( config, cp );
            return Optional.of(cp);
        }
        return Optional.empty();

    }

    Map<String,Object> getInitialStateFromSchema() {
        return  stateGraph.getChannels().entrySet().stream()
                .filter( c -> c.getValue().getDefault().isPresent() )
                .collect(Collectors.toMap(Map.Entry::getKey, e ->
                    e.getValue().getDefault().get().get()
                ));
    }

    Map<String,Object> getInitialState(Map<String,Object> inputs, RunnableConfig config) {

        return compileConfig.checkpointSaver()
                .flatMap( saver -> saver.get( config ) )
                .map( cp -> AgentState.updateState( cp.getState(), inputs, stateGraph.getChannels() ))
                .orElseGet( () -> AgentState.updateState(getInitialStateFromSchema(), inputs, stateGraph.getChannels() ));
    }

    State cloneState( Map<String,Object> data ) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        return stateGraph.getStateSerializer().cloneObject(data);
    }


    /**
     * Creates an AsyncGenerator stream of NodeOutput based on the provided inputs.
     *
     * @param inputs the input map
     * @param config the invoke configuration
     * @return an AsyncGenerator stream of NodeOutput
     * @throws Exception if there is an error creating the stream
     */
    public AsyncGenerator<NodeOutput<State>> stream( Map<String,Object> inputs, RunnableConfig config ) throws Exception {
        Objects.requireNonNull(config, "config cannot be null");
        final AsyncNodeGenerator<NodeOutput<State>> generator = new AsyncNodeGenerator<>( inputs, config );

        return new AsyncGenerator.WithEmbed<>( generator );
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

        Iterator<NodeOutput<State>> sourceIterator = stream(inputs, config).iterator();

        java.util.stream.Stream<NodeOutput<State>> result = StreamSupport.stream(
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
     * Creates an AsyncGenerator stream of NodeOutput based on the provided inputs.
     *
     * @param inputs the input map
     * @param config the invoke configuration
     * @return an AsyncGenerator stream of NodeOutput
     * @throws Exception if there is an error creating the stream
     */
    public AsyncGenerator<NodeOutput<State>> streamSnapshots( Map<String,Object> inputs, RunnableConfig config ) throws Exception {
        Objects.requireNonNull(config, "config cannot be null");

        final AsyncNodeGenerator<NodeOutput<State>> generator = new AsyncNodeGenerator<>( inputs, config.withStreamMode(StreamMode.SNAPSHOTS) );
        return new AsyncGenerator.WithEmbed<>( generator );
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

        String content = type.generator.generate( this.stateGraph, title, printConditionalEdges);

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

        String content = type.generator.generate( this.stateGraph, title, true);

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


    /**
     * Async Generator for streaming outputs.
     *
     * @param <Output> the type of the output
     */
    public class AsyncNodeGenerator<Output extends NodeOutput<State>> implements AsyncGenerator<Output> {

        Map<String,Object> currentState;
        String currentNodeId;
        String nextNodeId;
        int iteration = 0;
        RunnableConfig config;
        boolean resumedFromEmbed = false;

        protected AsyncNodeGenerator(Map<String,Object> inputs, RunnableConfig config ) throws Exception {
            final boolean isResumeRequest =  (inputs == null);

            if( isResumeRequest ) {

                log.trace( "RESUME REQUEST" );

                BaseCheckpointSaver saver = compileConfig.checkpointSaver()
                        .orElseThrow(() -> (new IllegalStateException("inputs cannot be null (ie. resume request) if no checkpoint saver is configured")));
                Checkpoint startCheckpoint = saver.get( config )
                        .orElseThrow( () -> (new IllegalStateException("Resume request without a saved checkpoint!")) );

                this.currentState = startCheckpoint.getState();

                // Reset checkpoint id
                this.config = config.withCheckPointId( null );


                this.nextNodeId = startCheckpoint.getNextNodeId();
                this.currentNodeId = null;
                log.trace( "RESUME FROM {}", startCheckpoint.getNodeId() );
            }
            else {

                log.trace( "START" );
                
                Map<String,Object> initState = getInitialState(inputs, config );
                // patch for backward support of AppendableValue
                State initializedState = stateGraph.getStateFactory().apply(initState);
                this.currentState = initializedState.data();
                this.nextNodeId = null;
                this.currentNodeId = START;
                this.config = config;
            }
        }

        @SuppressWarnings("unchecked")
        protected Output buildNodeOutput(String nodeId ) throws Exception {
            return  (Output)NodeOutput.of( nodeId, cloneState(currentState) );
        }

        @SuppressWarnings("unchecked")
        protected Output buildStateSnapshot( Checkpoint checkpoint ) throws Exception {
            return (Output)StateSnapshot.of( checkpoint, config, stateGraph.getStateFactory() ) ;
        }

        @SuppressWarnings("unchecked")
        private Optional<Data<Output>> getEmbedGenerator( Map<String,Object> partialState) {
            return partialState.entrySet().stream()
                    .filter( e -> e.getValue() instanceof AsyncGenerator)
                    .findFirst()
                    .map( e -> {
                        final AsyncGenerator<Output> generator = (AsyncGenerator<Output>) e.getValue();
                        return Data.composeWith( generator.map( n -> { n.setSubGraph(true); return n; } ), data -> {

                            if (data != null) {

                                if (!(data instanceof Map)) {
                                    throw new IllegalArgumentException("Embedded generator must return a Map");
                                }
                                currentState = AgentState.updateState(currentState, (Map<String, Object>) data, stateGraph.getChannels());
                            }

                            nextNodeId = nextNodeId(currentNodeId, currentState);
                            resumedFromEmbed = true;
                        });
                    })
                    ;
        }

        private CompletableFuture<Data<Output>> evaluateAction(AsyncNodeActionWithConfig<State> action, State withState ) {

                return action.apply( withState, config ).thenApply( partialState -> {
                    try {

                        Optional<Data<Output>> embed = getEmbedGenerator( partialState );
                        if( embed.isPresent() ) {
                            return embed.get();
                        }

                        currentState = AgentState.updateState(currentState, partialState, stateGraph.getChannels());
                        nextNodeId   = nextNodeId(currentNodeId, currentState);

                        return Data.of( getNodeOutput() );
                    }
                    catch (Exception e) {
                        throw new CompletionException(e);
                    }

                });
        }

        /**
         * evaluate Action without nested support
         */
        private CompletableFuture<Output> evaluateActionWithoutNested( AsyncNodeAction<State> action, State withState  ) {

            return action.apply( withState ).thenApply(  partialState -> {
                try {
                    currentState = AgentState.updateState(currentState, partialState, stateGraph.getChannels());
                    nextNodeId = nextNodeId(currentNodeId, currentState);

                    Optional<Checkpoint>  cp = addCheckpoint(config, currentNodeId, currentState, nextNodeId);
                    return ( cp.isPresent() && config.streamMode() == StreamMode.SNAPSHOTS) ?
                        buildStateSnapshot(cp.get()) :
                        buildNodeOutput( currentNodeId )
                            ;

                }
                catch (Exception e) {
                    throw new CompletionException(e);
                }
            });

        }

        private CompletableFuture<Output> getNodeOutput() throws Exception {
            Optional<Checkpoint>  cp = addCheckpoint(config, currentNodeId, currentState, nextNodeId);
            return completedFuture(( cp.isPresent() && config.streamMode() == StreamMode.SNAPSHOTS) ?
                    buildStateSnapshot(cp.get()) :
                    buildNodeOutput( currentNodeId ))
                    ;
        }

        @Override
        public Data<Output> next() {
            // GUARD: CHECK MAX ITERATION REACHED
            if( ++iteration > maxIterations ) {
                log.warn( "Maximum number of iterations ({}) reached!", maxIterations);
                return Data.done(currentState);
            }

            // GUARD: CHECK IF IT IS END
            if( nextNodeId == null &&  currentNodeId == null  ) return Data.done(currentState);

            try {
                // IS IT A RESUME FROM EMBED ?
                if(resumedFromEmbed) {
                    final CompletableFuture<Output> future = getNodeOutput();
                    resumedFromEmbed = false;
                    return Data.of( future );
                }

                if( START.equals(currentNodeId) ) {
                    nextNodeId = getEntryPoint( currentState );
                    currentNodeId = nextNodeId;
                    addCheckpoint( config, START, currentState, nextNodeId );
                    return Data.of( buildNodeOutput( START ) );
                }

                if( END.equals(nextNodeId) ) {
                    nextNodeId = null;
                    currentNodeId = null;
                    return Data.of( buildNodeOutput( END ) );
                }

                // check on previous node
                if( shouldInterruptAfter( currentNodeId, nextNodeId )) return Data.done();

                if( shouldInterruptBefore( nextNodeId, currentNodeId ) ) return Data.done();

                currentNodeId = nextNodeId;

                AsyncNodeActionWithConfig<State> action = nodes.get(currentNodeId);

                if (action == null)
                    throw StateGraph.RunnableErrors.missingNode.exception(currentNodeId);

                return evaluateAction(action, cloneState(currentState) ).get();
            }
            catch( Exception e ) {
                log.error( e.getMessage(), e );
                return Data.error(e);
            }


        }
    }



}
