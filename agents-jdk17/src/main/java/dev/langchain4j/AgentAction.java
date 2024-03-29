package dev.langchain4j;

import dev.langchain4j.agent.tool.ToolExecutionRequest;

import java.util.Objects;

record AgentAction (ToolExecutionRequest toolExecutionRequest, String log ) {
    public AgentAction {
        Objects.requireNonNull(toolExecutionRequest);
    }
}