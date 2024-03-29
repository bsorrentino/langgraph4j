package org.bsc.langgraph4j;

import lombok.Value;
import lombok.experimental.Accessors;
import org.bsc.langgraph4j.state.AgentState;

import java.util.Map;

@Value
@Accessors(fluent = true)
class BaseAgentState implements AgentState {
    Map<String,Object> data;


}
