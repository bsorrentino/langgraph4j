package org.bsc.langgraph4j;

import org.bsc.langgraph4j.state.AgentState;

import static java.lang.String.format;

public interface SubGraphNode<State extends AgentState> {
    String PREFIX_FORMAT = "(%s)%s";

    StateGraph<State> subGraph();

    static String formatId(String subGraphNodeId, String nodeId ) {
        return format(PREFIX_FORMAT, subGraphNodeId, nodeId );
    }

}

