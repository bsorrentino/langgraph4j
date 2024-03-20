package org.bsc.langgraph4j.state;

import java.util.Optional;

public interface AgentState {

    java.util.Map<String,Object> getData();

    default <T> Optional<T> getValue(String key) {
        return Optional.ofNullable((T) getData().get(key));
    };
}
