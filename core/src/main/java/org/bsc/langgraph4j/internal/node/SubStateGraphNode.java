package org.bsc.langgraph4j.internal.node;

import lombok.NonNull;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.SubGraphNode;
import org.bsc.langgraph4j.state.AgentState;

public class SubStateGraphNode<State extends AgentState> extends Node<State> implements SubGraphNode<State> {

    private final StateGraph<State> subGraph;

    public SubStateGraphNode(@NonNull String id, @NonNull  StateGraph<State> subGraph ) {
        super(id);
        this.subGraph = subGraph;
    }

    public StateGraph<State> subGraph() {
        return subGraph;
    }

    public String formatId(String nodeId ) {
        return SubGraphNode.formatId( id(), nodeId );
    }

}
