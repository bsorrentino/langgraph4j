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
        Checkpoint.Value value = object.getValue();
        AgentStateSerializer.INSTANCE.write( value.getState(), out );
        out.writeUTF( value.getNodeId() );
    }

    public Checkpoint read(ObjectInput in) throws IOException, ClassNotFoundException {
        String id = in.readUTF();
        AgentState state =  AgentStateSerializer.INSTANCE.read( in );
        String nodeId = in.readUTF();
        Checkpoint.Value value  = Checkpoint.Value.of( state, nodeId );
        return new Checkpoint(id, value);
    }

}
