package org.bsc.langgraph4j.action;

import org.bsc.langgraph4j.state.AgentState;

@FunctionalInterface
public interface EdgeAction<S extends AgentState> {

    String apply(S t) throws Exception;
}
