package org.bsc.langgraph4j.agentexecutor.state;

import java.util.Map;

public record AgentFinish (
    Map<String, Object> returnValues,
    String log
) {}
