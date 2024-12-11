package org.bsc.langgraph4j.agentexecutor.state;

public record AgentOutcome(
    AgentAction action,
    AgentFinish finish
) {}
