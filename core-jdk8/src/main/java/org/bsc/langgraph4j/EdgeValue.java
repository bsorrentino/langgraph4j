package org.bsc.langgraph4j;

import lombok.Value;
import lombok.experimental.Accessors;
import org.bsc.langgraph4j.state.AgentState;


@Value
@Accessors(fluent = true)
class EdgeValue<State extends AgentState> {

    /**
     * The unique identifier for the edge value.
     */
    String id;

    /**
     * The condition associated with the edge value.
     */
    EdgeCondition<State> value;
}
