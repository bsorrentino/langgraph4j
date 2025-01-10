package org.bsc.langgraph4j;

import org.bsc.async.AsyncGenerator;
import org.bsc.langgraph4j.action.AsyncNodeActionWithConfig;
import org.bsc.langgraph4j.state.AgentState;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.bsc.langgraph4j.utils.CollectionsUtils.mapOf;

class SubgraphNodeAction<State extends AgentState> implements AsyncNodeActionWithConfig<State> {

    final CompiledGraph<State> subGraph;

    SubgraphNodeAction(StateGraph<State> subGraph, CompileConfig config ) throws GraphStateException {
        this.subGraph = subGraph.compile(config);
    }

    @Override
    public CompletableFuture<Map<String, Object>> apply(State state, RunnableConfig config)  {
        CompletableFuture<Map<String, Object>> future = new CompletableFuture<>();

        try {
            final Map<String,Object> input = ( subGraph.compileConfig.checkpointSaver().isPresent() ) ?
                                            Map.of() :
                                            state.data() ;

            var generator = subGraph.stream( input, config );

            future.complete( mapOf( "_subgraph", generator ) );

        } catch (Exception e) {

            future.completeExceptionally(e);
        }

        return future;
    }
}
