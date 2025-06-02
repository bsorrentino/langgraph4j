package org.bsc.langgraph4j.action;

import org.bsc.langgraph4j.RunnableConfig;
import org.bsc.langgraph4j.state.AgentState;

@FunctionalInterface
public interface CommandAction<S extends AgentState> {
    Command apply(S t, RunnableConfig config) throws Exception;
}
