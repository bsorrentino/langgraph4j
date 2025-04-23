package org.bsc.langgraph4j.checkpoint;

import org.bsc.langgraph4j.RunnableConfig;

import java.util.Collection;
import java.util.Optional;

public interface HasVersions {

    Collection<Integer> versionsByThreadId(String threadId );

    default Collection<Integer> versionsByThreadId( RunnableConfig config ) {
        return versionsByThreadId( config.threadId().orElse(null) );
    }

    Optional<Integer> lastVersionByThreadId(String threadId );

    default Optional<Integer> lastVersionByThreadId( RunnableConfig config ) {
        return lastVersionByThreadId( config.threadId().orElse(null) );
    }

}
