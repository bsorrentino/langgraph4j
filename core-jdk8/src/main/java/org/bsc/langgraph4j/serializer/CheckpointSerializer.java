package org.bsc.langgraph4j.serializer;

import org.bsc.langgraph4j.checkpoint.Checkpoint;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class CheckpointSerializer implements Serializer<Checkpoint> {

    public static CheckpointSerializer of() {
        return new CheckpointSerializer();
    }
    private final StateSerializer stateSerializer = StateSerializer.of();

    private CheckpointSerializer() {}

    public void write( Checkpoint object, ObjectOutput out) throws IOException {
        out.writeUTF(object.getId());
        stateSerializer.write( object.getState(), out );
        out.writeUTF( object.getNodeId() );
        Serializer.writeUTFNullable( object.getNextNodeId(), out );
    }

    public Checkpoint read(ObjectInput in) throws IOException, ClassNotFoundException {
        return Checkpoint.builder()
                .id(in.readUTF())
                .state(stateSerializer.read( in ))
                .nodeId(in.readUTF())
                .nextNodeId(Serializer.readUTFNullable(in).orElse(null))
                .build();
    }

}
