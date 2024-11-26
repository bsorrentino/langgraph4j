package org.bsc.langgraph4j.agentexecutor.state;

public record IntermediateStep(
    AgentAction action,
    String observation
) {}

