package org.bsc.langgraph4j.action;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents the outcome of a {@link CommandAction} within a LangGraph4j graph.
 * A {@code Command} encapsulates instructions for the graph's next step, including
 * a target node to transition to and a map of updates to be applied
 * to the {@link org.bsc.langgraph4j.state.AgentState}.
 *
 * @param gotoNode containing the name of the next node to execute.
 * @param update   A {@link Map} containing key-value pairs representing updates
 *                 to be merged into the current agent state. An empty map indicates
 *                 no state updates.
 */
public record Command(String gotoNode, Map<String,Object> update) {

    /**
     * check for null values
     */
    public Command {
        Objects.requireNonNull(gotoNode, "gotoNode cannot be null");
        Objects.requireNonNull(update, "update cannot be null");
    }

    /**
     * Constructs a {@code Command} that specifies only the next node to transition to,
     * with no state updates.
     * If {@code gotoNode} is null, it will be treated as an empty {@link Optional}.
     *
     * @param gotoNode The name of the next node to transition to. Can be null.
     */
    public Command( String gotoNode ) {
        this( gotoNode, Map.of() );
    }

}