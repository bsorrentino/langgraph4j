package org.bsc.langgraph4j;

import org.bsc.langgraph4j.state.AgentState;

import java.util.Objects;

import static java.lang.String.format;
import static org.bsc.langgraph4j.StateGraph.END;

/**
 * Represents the output of a node in a graph.
 *
 * @param <State> the type of the state associated with the node output
 */
public class NodeOutput<State extends AgentState> {

    public static <State extends AgentState> NodeOutput<State> of( String node, State state ) {
        return new NodeOutput<>(node, state);
    }

    /**
     * The identifier of the node.
     */
    private final String node;

    /**
     * The state associated with the node.
     */
    private final State state;

    /**
     * If the output is from a subgraph.
     */
    private boolean subGraph = false;

    protected void setSubGraph( boolean subgraph ) {
        this.subGraph = subgraph;
    }

    /**
     * @return boolean if the output is from a subgraph
     */
    public boolean isSubGraph() {
        return subGraph;
    }

    /**
     * Returns the node name.
     *
     * @return the node name
     */
    public String node() {
        return node;
    }

    public State state() {
        return state;
    }

    /**
     * Checks if the current node refers to the end of the graph.
     * useful to understand if the workflow has been interrupted.
     *
     * @return {@code true} if the current node refers to the end of the graph
     */
    public boolean isEND() {
        return Objects.equals(node(),END);
    }

    protected NodeOutput( String node, State state ) {
        this.node = node;
        this.state = state;
    }

    @Override
    public String toString() {
        return format("NodeOutput{node=%s, state=%s}", node(), state());
    }

}
