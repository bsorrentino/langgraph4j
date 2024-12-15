package org.bsc.langgraph4j.agentexecutor.state;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import lombok.NonNull;

/**
 * Represents result of an action performed by an agent.
 * 
 * @param toolExecutionRequest the request for tool execution, must not be null
 * @param log a string representing the log of the action
 */
public record AgentAction(
    @NonNull
    ToolExecutionRequest toolExecutionRequest,
    String log ) {

}
