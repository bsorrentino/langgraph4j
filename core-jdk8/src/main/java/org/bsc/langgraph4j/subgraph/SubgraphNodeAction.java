package org.bsc.langgraph4j.subgraph;

import lombok.NonNull;
import org.bsc.async.AsyncGenerator;
import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.NodeOutput;
import org.bsc.langgraph4j.RunnableConfig;
import org.bsc.langgraph4j.action.AsyncNodeActionWithConfig;
import org.bsc.langgraph4j.action.NodeActionWithConfig;
import org.bsc.langgraph4j.state.AgentState;

import java.util.Map;

import static org.bsc.langgraph4j.utils.CollectionsUtils.mapOf;

public class SubgraphNodeAction<State extends AgentState> implements NodeActionWithConfig<State> {

    public static <State extends AgentState> AsyncNodeActionWithConfig<State> of( @NonNull  CompiledGraph<State> subGraph ) {
        return AsyncNodeActionWithConfig.node_async( new SubgraphNodeAction<State>(subGraph) );
    }

    final CompiledGraph<State> subGraph;

    protected SubgraphNodeAction(CompiledGraph<State> subGraph ) {
        this.subGraph = subGraph;
    }

    @Override
    public Map<String, Object> apply(State state, RunnableConfig config) throws Exception {

        AsyncGenerator<NodeOutput<State>> generator = subGraph.stream( state.data(), config );

        return mapOf( "_subgraph", generator );
    }
}
