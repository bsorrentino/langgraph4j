package org.bsc.langgraph4j;

import lombok.Value;
import lombok.var;
import org.bsc.langgraph4j.action.AsyncEdgeAction;
import org.bsc.langgraph4j.action.AsyncEdgeAction;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.async.AsyncIterator;
import org.bsc.langgraph4j.async.AsyncQueue;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AgentStateFactory;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.String.format;


@Value
class EdgeCondition<S extends AgentState> {
    AsyncEdgeAction<S> action;
    Map<String,String> mappings;
}

@Value
class EdgeValue<State extends AgentState> {
    String id;
    EdgeCondition<State> value;
}


public class GraphState<State extends AgentState> {
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


    public class Runnable {


        @Value
        public class NodeOutput<S extends AgentState> {
            String node;
            S state;
        }

        final Map<String, AsyncNodeAction<State>> nodes = new HashMap<>();
        final Map<String, EdgeValue<State>> edges = new HashMap<>();

        Runnable() {

            GraphState.this.nodes.forEach( n ->
                nodes.put(n.getId(), n.getAction())
            );

            GraphState.this.edges.forEach( e ->
                edges.put(e.getSourceId(), e.getTarget())
            );
        }

        private State mergeState( State currentState, Map<String,Object> partialState) {
            Objects.requireNonNull(currentState, "currentState");

            if( partialState == null || partialState.isEmpty() ) {
                return currentState;
            }
            var mergedMap = Stream.concat(currentState.getData().entrySet().stream(), partialState.entrySet().stream())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (oldValue, newValue) -> newValue));

            return stateFactory.apply(mergedMap);
        }

        private String nextNodeId( String nodeId , State state ) throws Exception {

            var route = edges.get(nodeId);
            if( route == null ) {
                throw RunnableErrors.missingEdge.exception(nodeId);
            }
            if( route.getId() != null ) {
                return route.getId();
            }
            if( route.getValue() != null ) {
                var condition = route.getValue().getAction();

                var newRoute = condition.apply(state).get();

                var result = route.getValue().getMappings().get(newRoute);
                if( result == null ) {
                    throw RunnableErrors.missingNodeInEdgeMapping.exception(nodeId, newRoute);
                }
            }

            throw RunnableErrors.executionError.exception( format("invalid edge value for nodeId: %s !", nodeId) );

        }



        public AsyncIterator<NodeOutput<State>> stream(Map<String,Object> inputs ) throws Exception {

            var queue = new AsyncQueue<NodeOutput<State>>(java.lang.Runnable::run);

            var executor = Executors.newSingleThreadExecutor();

            executor.submit(() -> {
                var currentState = stateFactory.apply(inputs);
                var currentNodeId = entryPoint;
                Map<String, Object> partialState;

                try {
                    do {
                        var action = nodes.get(currentNodeId);
                        if (action == null) {
                            queue.closeExceptionally(RunnableErrors.missingNode.exception(currentNodeId));
                            break;
                        }

                        partialState = action.apply(currentState).get();

                        currentState = mergeState(currentState, partialState);

                        queue.put(new NodeOutput<>(currentNodeId, currentState));

                        if (Objects.equals(currentNodeId, finishPoint)) {
                            break;
                        }

                        currentNodeId = nextNodeId(currentNodeId, currentState);

                    } while (!Objects.equals(currentNodeId, END));

                } catch (Exception e) {
                    queue.closeExceptionally(e);
                }
                finally {
                    executor.shutdown();
                    queue.tryClose();
                }

            });

            return queue;
        }

        public Optional<State> invoke( Map<String,Object> inputs ) throws Exception {

            var sourceIterator = stream(inputs).iterator();

            var result = StreamSupport.stream(
                    Spliterators.spliteratorUnknownSize(sourceIterator, Spliterator.ORDERED),
                    false);

            return  result.reduce((a, b) -> b).map( NodeOutput::getState);
        }
    }
    public static String END = "__END__";

    @Value
    class Node<S extends AgentState> {
        String id;
        AsyncNodeAction<State> action;
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
    @Value
    class Edge<S extends AgentState> {
        String sourceId;
        EdgeValue<State> target;
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

    public void addConditionalEdge(String sourceId, AsyncEdgeAction<State> condition, Map<String,String> mappings ) throws GraphStateException {
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

    public Runnable compile() throws GraphStateException {
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

            if( !nodes.contains( makeFakeNode(edge.sourceId) ) ) {
                throw Errors.missingNodeReferencedByEdge.exception(edge.sourceId);
            }

            if( edge.target.getId() != null ) {
                if(!Objects.equals(edge.target.getId(), END) && !nodes.contains( makeFakeNode(edge.target.getId()) ) ) {
                    throw Errors.missingNodeReferencedByEdge.exception(edge.target.getId());
                }
            }
            else if( edge.target.getValue() != null ) {
                for ( String nodeId: edge.target.getValue().getMappings().values() ) {
                    if(!Objects.equals(nodeId, END) && !nodes.contains( makeFakeNode(nodeId) ) ) {
                        throw Errors.missingNodeInEdgeMapping.exception(edge.sourceId, nodeId);
                    }
                }
            }
            else {
                throw Errors.invalidEdgeTarget.exception(edge.sourceId);
            }


        }

        return new Runnable();
    }
}
