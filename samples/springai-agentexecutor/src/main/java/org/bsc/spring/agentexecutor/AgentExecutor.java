package org.bsc.spring.agentexecutor;

import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.serializer.StateSerializer;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.Channel;
import org.bsc.langgraph4j.state.Channels;
import org.bsc.spring.agentexecutor.serializer.std.AgentStateSerializer;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

/**
 * Represents the core component responsible for executing agent logic.
 * It includes methods for building and managing the execution graph,
 * as well as handling agent actions and state transitions.
 */
@Service
public class AgentExecutor {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AgentExecutor.class);

    /**
     * Class responsible for building a state graph.
     */
    public class Builder {
        private StateSerializer<State> stateSerializer;

        /**
         * Sets the state serializer for the graph builder.
         *
         * @param stateSerializer the state serializer to set
         * @return the current instance of GraphBuilder for method chaining
         */
        public Builder stateSerializer(StateSerializer<State> stateSerializer) {
            this.stateSerializer = stateSerializer;
            return this;
        }

        /**
         * Builds and returns a StateGraph with the specified configuration.
         * Initializes the stateSerializer if it's null. Then, constructs a new StateGraph object using the provided schema
         * and serializer, adds an initial edge from the START node to "agent", and then proceeds to add nodes for "agent" and
         * "action". It also sets up conditional edges from the "agent" node based on whether or not to continue.
         *
         * @return A configured StateGraph object.
         * @throws GraphStateException If there is an issue with building the graph state.
         */
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

    /**
     * Returns a new instance of {@link Builder}.
     *
     * @return a new {@link Builder} object
     */
    public final Builder builder() {
        return new Builder();
    }

    /**
     * Represents the outcome of an action with a specific finish status.
     *
     * @param action  the action performed
     * @param finish  the finish status
     */
    public record Outcome(Action action, Finish finish ) {}

    /**
     * Represents a step in a process with an associated action and observation.
     */
    public record Step( Action action, String observation) { }

    /**
     * Represents an action with a tool call and associated log details.
     *
     * @param toolCall The tool call to be executed.
     * @param log      The log information related to the action.
     */
    public record Action( AssistantMessage.ToolCall toolCall, String log ) {}

    /**
     * Represents the completion of a process, encapsulating any relevant return values as key-value pairs in a map.
     *
     * @param returnValues A map containing the return values from a completed process or operation.
     */
    public record Finish ( Map<String, Object> returnValues) {}


    /**
     * Represents the state of an agent in a system.
     * This class extends {@link AgentState} and defines constants for keys related to input, agent outcome,
     * and intermediate steps. It includes a static map schema that specifies how these keys should be handled.
     */
    public static class State extends AgentState {
        public static final String INPUT = "input";
        public static final String AGENT_OUTCOME = "outcome";
        public static final String INTERMEDIATE_STEPS = "steps";

        static Map<String, Channel<?>> SCHEMA = Map.of(
                INTERMEDIATE_STEPS, Channels.appender(ArrayList::new)
        );

        /**
         * Constructs a new State object using the initial data provided in the initData map.
         *
         * @param initData the map containing the initial settings for this state
         */
        public State(Map<String, Object> initData) {
            super(initData);
        }

        /**
         * Retrieves input from the user with a specified key.
         * 
         * @return An Optional containing the input value if found; an empty Optional otherwise.
         */
        public Optional<String> input() {
            return value(INPUT);
        }
        /**
         * Returns the outcome for the agent.
         *
         * @return an {@link Optional} containing the agent's outcome, or an empty Optional if not available.
         */
        public Optional<Outcome> agentOutcome() {
            return value(AGENT_OUTCOME);
        }
        /**
         * Retrieves the list of intermediate steps.
         *
         * This method returns a <code>List</code> of <code>Step</code> objects that represent
         * the intermediate steps. If no such list is available, it creates and returns a new empty list.
         *
         * @return a non-null list of intermediate steps
         */
        public List<Step> intermediateSteps() {
            return this.<List<Step>>value(INTERMEDIATE_STEPS).orElseGet(ArrayList::new);
        }

    }

    private final AgentService agentService;

    /**
     * Initializes a new instance of the {@link AgentExecutor} class.
     *
     * @param agentService The service to be used for executing agents, must not be null.
     */
    public AgentExecutor(AgentService agentService) {
        this.agentService = agentService;
    }

    /**
     * Calls an agent with the given state.
     *
     * @param state The current state containing input and intermediate steps.
     * @return A map containing the outcome of the agent call, either an action or a finish.
     */
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
            var finish = new Finish( Map.of("returnValues", output.getText()) );

            return Map.of(State.AGENT_OUTCOME, new Outcome( null, finish ) );
        }
    }

    /**
     * Executes tools based on the provided state.
     *
     * @param state The current state containing necessary information to execute tools.
     * @return A CompletableFuture containing a map with the intermediate steps, if successful. If there is no agent outcome or the tool service execution fails, an appropriate exception will be thrown.
     */
    CompletableFuture<Map<String,Object>> executeTools(State state )  {
        log.trace( "executeTools" );

        var agentOutcome = state.agentOutcome().orElseThrow(() -> new IllegalArgumentException("no agentOutcome provided!"));

        return agentService.toolService.executeFunction( agentOutcome.action().toolCall() )
                .thenApply( result ->
                    Map.of(State.INTERMEDIATE_STEPS, new Step( agentOutcome.action(), result.responseData() ) )
                );
    }

    /**
     * Determines whether the game should continue based on the current state.
     *
     * @param state The current state of the game.
     * @return "end" if the game should end, otherwise "continue".
     */
    String shouldContinue(State state) {

        return state.agentOutcome()
                .map(Outcome::finish)
                .map( finish -> "end" )
                .orElse("continue");
    }
}