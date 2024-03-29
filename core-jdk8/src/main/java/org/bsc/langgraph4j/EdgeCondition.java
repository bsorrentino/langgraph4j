package org.bsc.langgraph4j;

import lombok.Value;
import lombok.experimental.Accessors;
import org.bsc.langgraph4j.action.AsyncEdgeAction;
import org.bsc.langgraph4j.state.AgentState;

import java.util.Map;

@Value
@Accessors(fluent = true)
class EdgeCondition<S extends AgentState> {
    AsyncEdgeAction<S> action;
    Map<String,String> mappings;
}
