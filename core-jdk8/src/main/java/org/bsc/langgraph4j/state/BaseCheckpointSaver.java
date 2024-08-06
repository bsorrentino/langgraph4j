package org.bsc.langgraph4j.state;

import java.util.Collection;
import java.util.Optional;

public interface BaseCheckpointSaver {

    Collection<Checkpoint> list();
    Optional<Checkpoint> get( String id );
    void put( Checkpoint checkpoint );
}
