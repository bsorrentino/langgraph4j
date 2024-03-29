package org.bsc.langgraph4j;

import org.bsc.langgraph4j.state.AgentState;

public record NodeOutput<State extends AgentState>(String node, State state) {

}
