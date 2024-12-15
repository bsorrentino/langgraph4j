package org.bsc.langgraph4j.agentexecutor.state;

/**
 * Represents an intermediate step in a process, encapsulating an action taken by an agent
 * and the corresponding observation made.
 */
public record IntermediateStep(
    AgentAction action,
    String observation
) {}
