package org.bsc.langgraph4j.state;

import lombok.Value;

import java.util.*;

public class Checkpoint {

    @lombok.Value(staticConstructor="of")
    public static class Value {
        AgentState state;
        String next;
    }

    private final String id;
    private final Value value;

    public final String getId() {
        return id;
    }
    public final Value getValue() {
        return value;
    }

    public Checkpoint( Value value ) {
        this(UUID.randomUUID().toString(), value );
    }
    public Checkpoint(String id, Value value) {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(value, "value cannot be null");
        this.id = id;
        this.value = value;
    }

}
