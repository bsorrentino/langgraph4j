package org.bsc.langgraph4j;

import org.bsc.langgraph4j.state.AgentState;

import java.util.Objects;

record Edge<State extends AgentState>(String sourceId, EdgeValue<State> target) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge<?> node = (Edge<?>) o;
        return Objects.equals(sourceId, node.sourceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceId);
    }

}
