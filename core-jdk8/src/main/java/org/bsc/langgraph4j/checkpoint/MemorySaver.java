package org.bsc.langgraph4j.checkpoint;

import org.bsc.langgraph4j.serializer.CheckpointSerializer;

import java.util.*;

import static java.util.Collections.unmodifiableCollection;
import static java.util.Collections.unmodifiableSet;

public class MemorySaver implements BaseCheckpointSaver {

    private final Stack<Checkpoint> checkpoints = new Stack<>();


    @Override
    public Collection<Checkpoint> list() {
        return unmodifiableCollection(checkpoints); // immutable checkpoints;
    }

    @Override
    public Optional<Checkpoint> getLast() {
        if( checkpoints.isEmpty() ) {
            return Optional.empty();
        }
        return Optional.ofNullable( checkpoints.peek() );
    }

    public Optional<Checkpoint> get(String id) {
        return checkpoints.stream()
                    .filter( checkpoint -> checkpoint.getId().equals(id) )
                    .findFirst();
    }

    @Override
    public void put(Checkpoint checkpoint) throws Exception {
        checkpoints.add( CheckpointSerializer.INSTANCE.cloneObject(checkpoint) );
    }

}
