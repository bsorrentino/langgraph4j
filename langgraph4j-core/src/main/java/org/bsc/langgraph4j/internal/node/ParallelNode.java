package org.bsc.langgraph4j.internal.node;

import org.bsc.async.AsyncGenerator;
import org.bsc.langgraph4j.NodeOutput;
import org.bsc.langgraph4j.RunnableConfig;
import org.bsc.langgraph4j.action.AsyncCommandAction;
import org.bsc.langgraph4j.action.AsyncNodeActionWithConfig;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.Channel;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.concurrent.CompletableFuture.completedFuture;

public class ParallelNode<State extends AgentState> extends Node<State> {
    public static final String PARALLEL_PREFIX = "__PARALLEL__";

    record AsyncParallelNodeAction<State extends AgentState>(
            List<AsyncNodeActionWithConfig<State>> actions,
            Map<String, Channel<?>> channels ) implements AsyncNodeActionWithConfig<State> {

        private CompletableFuture<Map<String,Object>> evalGenerator(AsyncGenerator<NodeOutput<State>> generator, Map<String,Object> initPartialState )
        {
            return generator.collectAsync( new ArrayList<>(), ArrayList::add)
                    .thenApply( list -> {
                        Map<String,Object> result = initPartialState;
                        for( var output : list ) {
                            result = AgentState.updateState( result, output.state().data(), channels);
                        }
                        return result;
            });
        }

        @SuppressWarnings("unchecked")
        private CompletableFuture<Map<String,Object>> evalNodeAction( AsyncNodeActionWithConfig<State> action, State state, RunnableConfig config ) {
            return action.apply(state, config).thenCompose( partialState ->
                partialState.entrySet().stream()
                        .filter( e -> e.getValue() instanceof AsyncGenerator )
                        .findFirst()
                        .map( generatorEntry -> {

                            var partialStateWithoutGenerator = partialState.entrySet().stream()
                                    .filter( e -> !Objects.equals(e.getKey(),generatorEntry.getKey()))
                                    .collect( Collectors.toMap( Map.Entry::getKey, Map.Entry::getValue));
                            return evalGenerator( (AsyncGenerator<NodeOutput<State>>)generatorEntry.getValue(), partialStateWithoutGenerator );

                        })
                        .orElse( completedFuture(partialState) )
            );
        }

        @Override
        public CompletableFuture<Map<String, Object>> apply(State state, RunnableConfig config) {

            return actions.stream()
                    .map(action -> evalNodeAction(action, state, config) )
                    .reduce(
                        completedFuture(new ConcurrentHashMap<>()),
                        ( futureResult, futureActionResult ) ->
                            futureResult.thenCombine( futureActionResult, (result, actionResult) ->
                                AgentState.updateState( result, actionResult, channels) ) /* ,
                        (f1, f2) ->
                                f1.thenCombine( f2, ( r1, r2 ) ->
                                        AgentState.updateState( r1, r2, channels) )  */
                        );
        }

    }

    public ParallelNode(String id, List<AsyncNodeActionWithConfig<State>> actions, Map<String, Channel<?>> channels) {
        super(format( "%s(%s)", PARALLEL_PREFIX, id), (config ) -> new AsyncParallelNodeAction<>( actions, channels ));
    }

    @Override
    public final boolean isParallel() {
        return true;
    }

}
