package dev.langchain4j.agentexecutor;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors( fluent = true)
class AgentAction  {
    @NonNull
    ToolExecutionRequest toolExecutionRequest;
    String log;
}