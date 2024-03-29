package org.bsc.langgraph4j;

import lombok.Value;
import lombok.experimental.Accessors;
import org.bsc.langgraph4j.state.AgentState;

@Value
@Accessors(fluent = true)
public class NodeOutput<State extends AgentState> {
    String node;
    State state;
}
