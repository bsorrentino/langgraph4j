package org.bsc.langgraph4j.checkpoint;

import org.bsc.langgraph4j.RunnableConfig;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

public class VersionedMemorySaver implements BaseCheckpointSaver, HasVersions {

    final Map<String, TreeMap<Integer,Tag>> _checkpointsHistoryByThread = new HashMap<>();
    final MemorySaver noVersionSaver = new MemorySaver();

    private final ReentrantLock _lock = new ReentrantLock();

    public VersionedMemorySaver() {
    }

    private TreeMap<Integer,Tag> getCheckpointHistoryByThread( String threadId ) {
        return ofNullable( _checkpointsHistoryByThread.get( threadId ) )
                    .orElseThrow( () -> new IllegalArgumentException( format("Thread %s not found", threadId )) );
    }

    final Optional<Tag> getTagByVersion( TreeMap<Integer,Tag> checkpointsHistory, int threadVersion ) {
        _lock.lock();
        try {
            return ofNullable(checkpointsHistory.get(threadVersion));

        } finally {
            _lock.unlock();
        }

    }


    final  Collection<Checkpoint> getCheckpointsByVersion( String threadId, int threadVersion  ) {

        _lock.lock();
        try {
            var checkpointsHistory = getCheckpointHistoryByThread(threadId);

            var checkpointsByVersion =  ofNullable(checkpointsHistory.get(threadVersion));

            return checkpointsByVersion
                    .map( Tag::checkpoints )
                    .orElseThrow( () -> new IllegalArgumentException( format("Version %s for thread %s not found", threadVersion, threadId )) );

        } finally {
            _lock.unlock();
        }
    }

    @Override
    public Collection<Integer> versionsByThreadId( String threadId ) {
        return getCheckpointHistoryByThread( ofNullable(threadId).orElse( THREAD_ID_DEFAULT ) )
                .keySet();
    }

    @Override
    public Optional<Integer> lastVersionByThreadId( String threadId ) {
        var checkpointsHistory = getCheckpointHistoryByThread( ofNullable(threadId).orElse( THREAD_ID_DEFAULT ) );
        return ofNullable(checkpointsHistory.lastEntry()).map(Map.Entry::getKey);
    }

    @Override
    public Collection<Checkpoint> list( RunnableConfig config ) {
        _lock.lock();
        try {
            return noVersionSaver.list(config);
        } finally {
            _lock.unlock();
        }
    }

    @Override
    public Optional<Checkpoint> get(RunnableConfig config) {

        _lock.lock();
        try {

            return noVersionSaver.get(config);

        } finally {
            _lock.unlock();
        }
    }

    @Override
    public RunnableConfig put(RunnableConfig config, Checkpoint checkpoint) throws Exception {

        _lock.lock();
        try {
            return noVersionSaver.put(config, checkpoint);
        }
        finally {
            _lock.unlock();
        }
    }

    @Override
    public Tag release(RunnableConfig config) throws Exception {

        _lock.lock();
        try {

            var threadId = config.threadId().orElse(THREAD_ID_DEFAULT);

            var tag = noVersionSaver.release(config);

            var checkpointsHistory = _checkpointsHistoryByThread
                                        .computeIfAbsent( threadId, k -> new TreeMap<>() );

            var threadVersion = ofNullable(checkpointsHistory.lastEntry()).map(Map.Entry::getKey).orElse(0);

            checkpointsHistory.put( threadVersion + 1, tag );

            return tag;

        }
        finally {
            _lock.unlock();
        }
    }
}
