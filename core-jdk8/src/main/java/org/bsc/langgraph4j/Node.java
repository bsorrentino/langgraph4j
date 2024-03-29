package org.bsc.langgraph4j;

import lombok.Value;
import lombok.experimental.Accessors;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.state.AgentState;

import java.util.Objects;

@Value
@Accessors(fluent = true)
class Node<State extends AgentState> {

    String id;
    AsyncNodeAction<State> action;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<?> node = (Node<?>) o;
        return Objects.equals(id, node.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
