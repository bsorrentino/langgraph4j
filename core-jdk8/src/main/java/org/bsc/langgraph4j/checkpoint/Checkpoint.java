package org.bsc.langgraph4j.checkpoint;

import lombok.Data;
import org.bsc.langgraph4j.state.AgentState;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.*;


/**
 * Represents a checkpoint of an agent state.
 *
 * The checkpoint is an immutable object that holds an {@link AgentState}
 * and a {@code String} that represents the next state.
 *
 * The checkpoint is serializable and can be persisted and restored.
 *
 * @see AgentState
 * @see Externalizable
 */
public class Checkpoint {

    @lombok.Value(staticConstructor="of")
    public static class Value {
        AgentState state;
        String next;
    }

    String id;
    Value value;

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
