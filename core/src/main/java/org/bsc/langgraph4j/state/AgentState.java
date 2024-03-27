package org.bsc.langgraph4j.state;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public interface AgentState {

    java.util.Map<String,Object> data();

    default <T> Optional<T> value(String key) {
        return ofNullable((T) data().get(key));
    };

    default <T> Optional<List<T>> appendableValue(String key ) {
        return ofNullable( ((AppendableValue<T>)data().get(key)))
                    .map( ( v ) -> v.values );

    }
}
