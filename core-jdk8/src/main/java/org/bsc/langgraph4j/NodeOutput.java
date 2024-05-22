package org.bsc.langgraph4j;

import lombok.Value;
import lombok.experimental.Accessors;
import org.bsc.langgraph4j.state.AgentState;

/**
 * Represents the output of a node in a graph.
 *
 * @param <State> the type of the state associated with the node output
 */
@Value
@Accessors(fluent = true)
public class NodeOutput<State extends AgentState> {

    /**
     * The identifier of the node.
     */
    String node;

    /**
     * The state associated with the node.
     */
    State state;
}
