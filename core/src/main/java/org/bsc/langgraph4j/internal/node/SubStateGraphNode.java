package org.bsc.langgraph4j.internal.node;

import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.SubGraphNode;
import org.bsc.langgraph4j.state.AgentState;

import java.util.Objects;

public class SubStateGraphNode<State extends AgentState> extends Node<State> implements SubGraphNode<State> {

    private final StateGraph<State> subGraph;

    public SubStateGraphNode( String id, StateGraph<State> subGraph ) {
        super( Objects.requireNonNull(id, "id cannot be null"));
        this.subGraph = Objects.requireNonNull(subGraph, "subGraph cannot be null");
    }

    public StateGraph<State> subGraph() {
        return subGraph;
    }

    public String formatId(String nodeId ) {
        return SubGraphNode.formatId( id(), nodeId );
    }

}
