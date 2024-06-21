package dev.langchain4j.agentexecutor;

import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors( fluent = true)
class AgentOutcome {
    AgentAction action;
    AgentFinish finish;
}
