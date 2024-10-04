package dev.langchain4j.agentexecutor;

public record AgentOutcome(
    AgentAction action,
    AgentFinish finish
) {}
