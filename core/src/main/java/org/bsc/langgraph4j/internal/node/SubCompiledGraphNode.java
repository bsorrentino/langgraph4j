package org.bsc.langgraph4j.internal.node;

import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.SubGraphNode;
import org.bsc.langgraph4j.action.AsyncCommandAction;
import org.bsc.langgraph4j.state.AgentState;

import java.util.Objects;

public class SubCompiledGraphNode<State extends AgentState> extends Node<State> implements SubGraphNode<State> {

    private final CompiledGraph<State> subGraph;

    public SubCompiledGraphNode(String id, CompiledGraph<State> subGraph ) {
        super(  Objects.requireNonNull(id, "id cannot be null"),
                (config ) -> new SubCompiledGraphNodeAction<>(subGraph) );
        this.subGraph = Objects.requireNonNull(subGraph, "subGraph cannot be null");
    }

    public StateGraph<State> subGraph() {
        return subGraph.stateGraph;
    }
}
