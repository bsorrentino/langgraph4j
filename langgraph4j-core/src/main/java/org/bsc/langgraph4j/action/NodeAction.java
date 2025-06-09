package org.bsc.langgraph4j.action;

import org.bsc.langgraph4j.state.AgentState;

import java.util.Map;

@FunctionalInterface
public interface NodeAction <T extends AgentState> {
    Map<String, Object> apply(T state) throws Exception;

}

