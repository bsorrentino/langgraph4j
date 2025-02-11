package org.bsc.langgraph4j.internal.node;

import lombok.NonNull;
import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.SubGraphNode;
import org.bsc.langgraph4j.state.AgentState;

public class SubCompiledGraphNode<State extends AgentState> extends Node<State> implements SubGraphNode<State> {

    private final CompiledGraph<State> subGraph;

    public SubCompiledGraphNode(@NonNull String id, @NonNull  CompiledGraph<State> subGraph ) {
        super(id, (config ) -> new SubCompiledGraphNodeAction<>(subGraph) );
        this.subGraph = subGraph;
    }

    public StateGraph<State> subGraph() {
        return subGraph.stateGraph;
    }
}
