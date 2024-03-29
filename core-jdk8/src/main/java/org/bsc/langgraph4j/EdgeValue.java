package org.bsc.langgraph4j;

import lombok.Value;
import lombok.experimental.Accessors;
import org.bsc.langgraph4j.state.AgentState;


@Value
@Accessors(fluent = true)
class EdgeValue<State extends AgentState> {
    String id;
    EdgeCondition<State> value;
}
