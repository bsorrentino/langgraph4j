package org.bsc.langgraph4j.agentexecutor.actions;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.Response;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.bsc.langgraph4j.agentexecutor.*;
import org.bsc.langgraph4j.agentexecutor.state.AgentAction;
import org.bsc.langgraph4j.agentexecutor.state.AgentFinish;
import org.bsc.langgraph4j.agentexecutor.state.AgentOutcome;
import org.bsc.langgraph4j.langchain4j.generators.LLMStreamingGenerator;

import java.util.Map;

/**
 * The CallAgent class implements the NodeAction interface for handling 
 * actions related to an AgentExecutor's state.
 */
@Slf4j
public class CallAgent implements NodeAction<AgentExecutor.State> {

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
    private Map<String,Object> mapResult( Response<AiMessage> response )  {

        var content = response.content();

        if( response.finishReason() == FinishReason.STOP ) {
            var result = content.text();
            var finish = new AgentFinish(Map.of("returnValues", result), result);
            return Map.of("agent_outcome", new AgentOutcome(null, finish));
        }

        if (response.finishReason() == FinishReason.TOOL_EXECUTION || response.content().hasToolExecutionRequests() ) {

            var toolExecutionRequests = response.content().toolExecutionRequests();
            var action = new AgentAction(toolExecutionRequests.get(0), "");

            return Map.of("agent_outcome", new AgentOutcome(action, null));

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
        var input = state.input()
                .orElseThrow(() -> new IllegalArgumentException("no input provided!"));

        var intermediateSteps = state.intermediateSteps();

        if( agent.isStreaming()) {

            var generator = LLMStreamingGenerator.<AiMessage, AgentExecutor.State>builder()
                    .mapResult( this::mapResult )
                    .startingNode("agent")
                    .startingState( state )
                    .build();
            agent.execute(input, intermediateSteps, generator.handler());

            return Map.of( "agent_outcome", generator);
        }
        else {
            var response = agent.execute(input, intermediateSteps);

            return mapResult(response);
        }

    }

}
