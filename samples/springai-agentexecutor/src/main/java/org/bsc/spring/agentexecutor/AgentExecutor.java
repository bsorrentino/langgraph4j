package org.bsc.spring.agentexecutor;

import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.serializer.StateSerializer;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AppenderChannel;
import org.bsc.langgraph4j.state.Channel;
import org.bsc.spring.agentexecutor.serializer.std.AgentStateSerializer;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

@Slf4j
@Service
public class AgentExecutor {
    public class GraphBuilder {
        private StateSerializer<State> stateSerializer;

        public GraphBuilder stateSerializer( StateSerializer<State> stateSerializer) {
            this.stateSerializer = stateSerializer;
            return this;
        }

        public StateGraph<State> build() throws GraphStateException {
            if( stateSerializer == null ) {
                stateSerializer = new AgentStateSerializer();
            }

            return new StateGraph<>(State.SCHEMA, stateSerializer)
                    .addEdge(START,"agent")
                    .addNode( "agent", node_async(AgentExecutor.this::callAgent) )
                    .addNode( "action", AgentExecutor.this::executeTools )
                    .addConditionalEdges(
                            "agent",
                            edge_async(AgentExecutor.this::shouldContinue),
                            Map.of("continue", "action", "end", END)
                    )
                    .addEdge("action", "agent")
                    ;

        }
    }

    public final GraphBuilder graphBuilder() {
        return new GraphBuilder();
    }

    public record Outcome(Action action, Finish finish ) {}

    public record Step( Action action, String observation) { }

    public record Action( AssistantMessage.ToolCall toolCall, String log ) {}

    public record Finish ( Map<String, Object> returnValues) {}


    public static class State extends AgentState {
        public static final String INPUT = "input";
        public static final String AGENT_OUTCOME = "outcome";
        public static final String INTERMEDIATE_STEPS = "steps";

        static Map<String, Channel<?>> SCHEMA = Map.of(
                INTERMEDIATE_STEPS, AppenderChannel.<Step>of(ArrayList::new)
        );

        public State(Map<String, Object> initData) {
            super(initData);
        }

        public Optional<String> input() {
            return value(INPUT);
        }
        public Optional<Outcome> agentOutcome() {
            return value(AGENT_OUTCOME);
        }
        public List<Step> intermediateSteps() {
            return this.<List<Step>>value(INTERMEDIATE_STEPS).orElseGet(ArrayList::new);
        }

    }

    private final AgentService agentService;

    public AgentExecutor(AgentService agentService) {
        this.agentService = agentService;
    }

    Map<String,Object> callAgent( State state )  {
        log.info( "callAgent" );

        var input = state.input()
                .orElseThrow(() -> new IllegalArgumentException("no input provided!"));

        var intermediateSteps = state.intermediateSteps();

        var response = agentService.execute( input, intermediateSteps  );

        var output = response.getResult().getOutput();

        if( output.hasToolCalls() ) {

            var action = new Action( output.getToolCalls().get(0), "");

            return Map.of(State.AGENT_OUTCOME, new Outcome( action, null ) );

        }
        else  {
            var finish = new Finish( Map.of("returnValues", output.getContent()) );

            return Map.of(State.AGENT_OUTCOME, new Outcome( null, finish ) );
        }
    }

    CompletableFuture<Map<String,Object>> executeTools(State state )  {
        log.trace( "executeTools" );

        var agentOutcome = state.agentOutcome().orElseThrow(() -> new IllegalArgumentException("no agentOutcome provided!"));

        return agentService.toolService.executeFunction( agentOutcome.action().toolCall() )
                .thenApply( result ->
                    Map.of("intermediate_steps", new Step( agentOutcome.action(), result.responseData() ) )
                );
    }

    String shouldContinue(State state) {

        return state.agentOutcome()
                .map(Outcome::finish)
                .map( finish -> "end" )
                .orElse("continue");
    }
}
