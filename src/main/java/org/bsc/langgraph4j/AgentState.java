package org.bsc.langgraph4j;

import java.util.Optional;

public interface AgentState {

    java.util.Map<String,Object> data();

    default <T> Optional<T> getValue(String key) {
        return Optional.ofNullable((T) data().get(key));
    };
}
