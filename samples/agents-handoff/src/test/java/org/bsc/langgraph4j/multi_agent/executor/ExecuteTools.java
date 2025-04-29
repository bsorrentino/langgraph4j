package org.bsc.langgraph4j.multi_agent.executor;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessageType;
import org.bsc.langgraph4j.action.NodeAction;
import org.bsc.langgraph4j.langchain4j.tool.LC4jToolService;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * The ExecuteTools class implements the NodeAction interface for handling 
 * actions related to executing tools within an agent's context.
 */
public class ExecuteTools implements NodeAction<AgentExecutor.State> {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ExecuteTools.class);
    /**
     * The tool node that will be executed.
     */
    final LC4jToolService toolNode;

    /**
     * Constructs an ExecuteTools instance with the specified agent and tool node.
     *
     * @param toolNode the tool node to be executed, must not be null
     */
    public ExecuteTools( LC4jToolService toolNode) {
        this.toolNode = Objects.requireNonNull(toolNode, "toolNode cannot be null");
    }

    /**
     * Applies the tool execution logic based on the provided agent state.
     *
     * @param state the current state of the agent executor
     * @return a map containing the intermediate steps of the execution
     * @throws IllegalArgumentException if no agent outcome is provided
     * @throws IllegalStateException if no action or tool is found for execution
     */
    @Override
    public Map<String,Object> apply(AgentExecutor.State state )  {
        log.trace( "executeTools" );

        var toolExecutionRequests = state.lastMessage()
                .filter( m -> ChatMessageType.AI==m.type() )
                .map( m -> (AiMessage)m )
                .filter(AiMessage::hasToolExecutionRequests)
                .map(AiMessage::toolExecutionRequests);
                //.orElseThrow(() -> new IllegalArgumentException("no tool execution request found!"));

        if( toolExecutionRequests.isEmpty() ) {
            return Map.of("agent_response", "no tool execution request found!" );
        }

        var result = toolExecutionRequests.get().stream()
                        .map(toolNode::execute)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList();

        return Map.of("messages", result );

    }

}
