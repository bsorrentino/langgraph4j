package org.bsc.langgraph4j.internal;

import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.state.AgentState;

import static java.lang.String.format;

interface SubGraphNode<State extends AgentState> {
    String PREFIX_FORMAT = "(%s)%s";

    StateGraph<State> subGraph();

    static String formatId(String subGraphNodeId, String nodeId ) {
        return format(PREFIX_FORMAT, subGraphNodeId, nodeId );
    }

}

