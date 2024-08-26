package org.bsc.langgraph4j.serializer;

import org.bsc.langgraph4j.checkpoint.Checkpoint;
import org.bsc.langgraph4j.state.AgentState;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class CheckpointSerializer implements Serializer<Checkpoint> {

    public static final CheckpointSerializer INSTANCE = new CheckpointSerializer();

    private CheckpointSerializer() {}

    public void write( Checkpoint object, ObjectOutput out) throws IOException {
        out.writeUTF(object.getId());
        AgentStateSerializer.INSTANCE.write( object.getState(), out );
        out.writeUTF( object.getNodeId() );
        out.writeUTF( object.getNextNodeId() );
    }

    public Checkpoint read(ObjectInput in) throws IOException, ClassNotFoundException {
        return Checkpoint.builder()
                .id(in.readUTF())
                .state(AgentStateSerializer.INSTANCE.read( in ))
                .nodeId(in.readUTF())
                .nextNodeId(in.readUTF())
                .build();
    }

}
