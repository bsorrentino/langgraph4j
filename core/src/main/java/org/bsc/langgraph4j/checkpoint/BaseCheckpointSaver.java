package org.bsc.langgraph4j.checkpoint;

import org.bsc.langgraph4j.RunnableConfig;

import java.util.Collection;
import java.util.Optional;

public interface BaseCheckpointSaver {

    Collection<Checkpoint> list( RunnableConfig config );
    Optional<Checkpoint> get( RunnableConfig config );
    RunnableConfig put( RunnableConfig config, Checkpoint checkpoint ) throws Exception;
}
