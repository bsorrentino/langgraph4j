package org.bsc.langgraph4j;

import org.bsc.langgraph4j.action.AsyncEdgeAction;
import org.bsc.langgraph4j.state.AgentState;

import java.util.Map;

record EdgeCondition<S extends AgentState>(AsyncEdgeAction<S> action, Map<String,String> mappings) {

}
