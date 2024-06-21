package dev.langchain4j.agentexecutor;

import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors( fluent = true)
public class IntermediateStep {
    AgentAction action;
    String observation;
}
