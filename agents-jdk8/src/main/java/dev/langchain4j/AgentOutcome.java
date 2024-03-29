package dev.langchain4j;

import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors( fluent = true)
class AgentOutcome {
    AgentAction action;
    AgentFinish finish;
}
