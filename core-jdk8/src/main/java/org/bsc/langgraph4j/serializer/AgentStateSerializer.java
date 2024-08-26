package org.bsc.langgraph4j.serializer;

import org.bsc.langgraph4j.state.AgentState;

import java.io.*;
import java.util.Map;

public class AgentStateSerializer implements Serializer<AgentState> {
    public static final AgentStateSerializer INSTANCE = new AgentStateSerializer();
    private AgentStateSerializer() {}

    @Override
    public void write(AgentState object, ObjectOutput out) throws IOException {
        MapSerializer.INSTANCE.write( object.data(), out );
    }

    @Override
    public AgentState read(ObjectInput in) throws IOException, ClassNotFoundException {
        Map<String, Object> data =  MapSerializer.INSTANCE.read( in );
        return new AgentState(data);
    }
}
