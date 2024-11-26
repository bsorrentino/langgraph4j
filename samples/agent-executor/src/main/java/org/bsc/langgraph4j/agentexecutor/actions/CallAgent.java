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
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.completedFuture;

@Slf4j
public class CallAgent implements NodeAction<AgentExecutor.State> {

    final Agent agent;

    public CallAgent( Agent agent ) {
        this.agent = agent;
    }

    @Override
    public Map<String,Object> apply( AgentExecutor.State state )  {
        log.trace( "callAgent" );
        var input = state.input()
                .orElseThrow(() -> new IllegalArgumentException("no input provided!"));

        var intermediateSteps = state.intermediateSteps();

        final Function<Response<AiMessage>, Map<String,Object>> mapResult = response -> {

            if (response.finishReason() == FinishReason.TOOL_EXECUTION) {

                var toolExecutionRequests = response.content().toolExecutionRequests();
                var action = new AgentAction(toolExecutionRequests.get(0), "");

                return Map.of("agent_outcome", new AgentOutcome(action, null));

            } else {
                var result = response.content().text();
                var finish = new AgentFinish(Map.of("returnValues", result), result);

                return Map.of("agent_outcome", new AgentOutcome(null, finish));
            }
        };

        if( agent.isStreaming()) {

            var generator = LLMStreamingGenerator.<AiMessage, AgentExecutor.State>builder()
                    .mapResult(mapResult)
                    .startingNode("agent")
                    .startingState( state )
                    .build();
            agent.execute(input, intermediateSteps, generator.handler());

            return Map.of( "agent_outcome", generator);
        }
        else {
            var response = agent.execute(input, intermediateSteps);

            return mapResult.apply(response);
        }

    }

}
