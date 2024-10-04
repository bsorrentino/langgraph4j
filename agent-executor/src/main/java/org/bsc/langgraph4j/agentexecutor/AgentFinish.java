package dev.langchain4j.agentexecutor;

import java.util.Map;

public record AgentFinish (
    Map<String, Object> returnValues,
    String log
) {}
