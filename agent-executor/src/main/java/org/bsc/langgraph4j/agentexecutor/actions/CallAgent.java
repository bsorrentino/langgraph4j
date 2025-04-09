package org.bsc.langgraph4j.agentexecutor.actions;

import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.output.FinishReason;
import org.bsc.langgraph4j.action.NodeAction;
import org.bsc.langgraph4j.agentexecutor.*;
import org.bsc.langgraph4j.langchain4j.generators.StreamingChatGenerator;

import java.util.Map;

/**
 * The CallAgent class implements the NodeAction interface for handling 
 * actions related to an AgentExecutor's state.
 */
public class CallAgent implements NodeAction<AgentExecutor.State> {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CallAgent.class);

    final Agent agent;

    /**
     * Constructs a CallAgent with the specified agent.
     *
     * @param agent the agent to be associated with this CallAgent
     */
    public CallAgent( Agent agent ) {
        this.agent = agent;
    }

    /**
     * Maps the result of the response from an AI message to a structured format.
     *
     * @param response the response containing the AI message
     * @return a map containing the agent's outcome
     * @throws IllegalStateException if the finish reason of the response is unsupported
     */
    private Map<String,Object> mapResult( ChatResponse response )  {

        var content = response.aiMessage();

        if( response.finishReason() == FinishReason.STOP ) {
            return Map.of("agent_response", content.text());
        }
        if (response.finishReason() == FinishReason.TOOL_EXECUTION || content.hasToolExecutionRequests() ) {
            return Map.of("messages", content);
        }
        if( response.finishReason() == null ) {
            return Map.of();
        }

        throw new IllegalStateException("Unsupported finish reason: " + response.finishReason() );
    }

    /**
     * Applies the action to the given state and returns the result.
     *
     * @param state the state to which the action is applied
     * @return a map containing the agent's outcome
     * @throws IllegalArgumentException if no input is provided in the state
     */
    @Override
    public Map<String,Object> apply( AgentExecutor.State state )  {
        log.trace( "callAgent" );
        var messages = state.messages();

        if( messages.isEmpty() ) {
            throw new IllegalArgumentException("no input provided!");
        }


        if( agent.isStreaming()) {

            var generator = StreamingChatGenerator.<AgentExecutor.State>builder()
                    .mapResult( this::mapResult )
                    .startingNode("agent")
                    .startingState( state )
                    .build();
            agent.execute(messages, generator.handler());

            return Map.of( "_generator", generator);


        }
        else {
            var response = agent.execute(messages);

            return mapResult(response);
        }

    }

}
