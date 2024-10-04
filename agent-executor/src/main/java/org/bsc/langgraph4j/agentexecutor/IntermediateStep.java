package dev.langchain4j.agentexecutor;

public record IntermediateStep(
    AgentAction action,
    String observation
) {}

