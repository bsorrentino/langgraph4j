package org.bsc.langgraph4j.serializer.plain_text.gson;

import com.google.gson.Gson;
import lombok.NonNull;
import org.bsc.langgraph4j.serializer.plain_text.PlainTextStateSerializer;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AgentStateFactory;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Base Implementation of {@link PlainTextStateSerializer} using GSON library
 * . Need to be extended from specific state implementation
 * @param <State> The type of the agent state to be serialized/deserialized.
 */
public abstract class GsonStateSerializer<State extends AgentState> extends PlainTextStateSerializer<State> {

    protected final Gson gson;

    protected GsonStateSerializer(@NonNull AgentStateFactory<State> stateFactory, Gson gson) {
        super(stateFactory);
        this.gson = gson;
    }

    protected GsonStateSerializer(@NonNull AgentStateFactory<State> stateFactory) {
        this(stateFactory, new Gson());
    }

    @Override
    public String mimeType() {
        return "application/json";
    }

    @Override
    public void write(State object, ObjectOutput out) throws IOException {
        String json = gson.toJson(object);
        out.writeUTF(json);

    }

    @Override
    public State read(ObjectInput in) throws IOException, ClassNotFoundException {
        return gson.fromJson(in.readUTF(), getStateType());
    }
}