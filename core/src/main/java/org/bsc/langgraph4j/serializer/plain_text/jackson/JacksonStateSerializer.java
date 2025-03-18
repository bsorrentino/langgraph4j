package org.bsc.langgraph4j.serializer.plain_text.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.NonNull;
import org.bsc.langgraph4j.serializer.plain_text.PlainTextStateSerializer;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AgentStateFactory;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;
import java.util.Map;

/**
 * Base Implementation of {@link PlainTextStateSerializer} using Jackson library.
 * Need to be extended from specific state implementation
 *
 * @param <State> The type of the agent state to be serialized/deserialized.
 */
public abstract class JacksonStateSerializer <State extends AgentState> extends PlainTextStateSerializer<State> {
    protected final ObjectMapper objectMapper;

    protected TypeMapper typeMapper = new TypeMapper();

    protected JacksonStateSerializer( AgentStateFactory<State> stateFactory ) {
        this( stateFactory, new ObjectMapper() );
        this.objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

    }

    protected JacksonStateSerializer(  @NonNull AgentStateFactory<State> stateFactory,  @NonNull ObjectMapper objectMapper) {
        super(stateFactory);
        this.objectMapper = objectMapper;

        var module = new SimpleModule();
        module.addDeserializer( Map.class, new GenericMapDeserializer(typeMapper) );
        module.addDeserializer( List.class, new GenericListDeserializer(typeMapper) );

        this.objectMapper.registerModule( module );

    }

    public TypeMapper typeMapper() {
        return typeMapper;
    }
    public ObjectMapper objectMapper() {
        return objectMapper;
    }

    @Override
    public String mimeType() {
        return "application/json";
    }

    @Override
    public void write(State object, ObjectOutput out) throws IOException {
        String json = objectMapper.writeValueAsString(object.data());
        out.writeUTF(json);
    }

    @Override
    public State read(ObjectInput in) throws IOException, ClassNotFoundException {
        String json = in.readUTF();
        var data = objectMapper.readValue(json, new TypeReference<Map<String,Object>>() {});
        return stateFactory().apply(data);
    }

}
