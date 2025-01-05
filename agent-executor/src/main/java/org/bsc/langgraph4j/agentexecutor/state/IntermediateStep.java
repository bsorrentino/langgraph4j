package org.bsc.langgraph4j.agentexecutor.state;

/**
 * Represents an intermediate step in a process, encapsulating an action taken by an agent
 * and the corresponding observation made.
 *
 * @param action      The action performed by the agent in this step.
 * @param observation The observation or feedback recorded after the action is performed.
 */
public record IntermediateStep(
    AgentAction action,
    String observation
) {}
