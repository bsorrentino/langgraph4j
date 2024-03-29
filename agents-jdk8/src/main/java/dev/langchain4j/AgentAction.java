package dev.langchain4j;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

import java.util.Objects;

@Value
@Accessors( fluent = true)
class AgentAction  {
    @NonNull
    ToolExecutionRequest toolExecutionRequest;
    String log;
}