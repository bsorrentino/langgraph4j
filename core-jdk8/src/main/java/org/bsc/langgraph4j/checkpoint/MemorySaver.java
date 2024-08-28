package org.bsc.langgraph4j.checkpoint;

import org.bsc.langgraph4j.RunnableConfig;
import org.bsc.langgraph4j.serializer.CheckpointSerializer;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.util.Collections.unmodifiableCollection;
import static java.util.Optional.ofNullable;

public class MemorySaver implements BaseCheckpointSaver {
    private final Map<String, LinkedList<Checkpoint>> _checkpointsByThread = new HashMap<>();
    private final LinkedList<Checkpoint> _defaultCheckpoints = new LinkedList<>();
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();

    public MemorySaver() {
    }

    private LinkedList<Checkpoint> getCheckpoints( RunnableConfig config ) {
        return config.threadId()
                    .map( threadId -> _checkpointsByThread.computeIfAbsent(threadId, k -> new LinkedList<>()) )
                    .orElse( _defaultCheckpoints );
    }

    @Override
    public Collection<Checkpoint> list( RunnableConfig config ) {
        final LinkedList<Checkpoint> checkpoints = getCheckpoints(config);
        r.lock();
        try {
            return unmodifiableCollection(checkpoints); // immutable checkpoints;
        } finally {
            r.unlock();
        }
    }

    private Optional<Checkpoint> getLast( LinkedList<Checkpoint> checkpoints, RunnableConfig config ) {
        return (checkpoints.isEmpty() ) ? Optional.empty() : ofNullable(checkpoints.peek());
    }

    @Override
    public Optional<Checkpoint> get(RunnableConfig config) {
        final LinkedList<Checkpoint> checkpoints = getCheckpoints(config);
        r.lock();
        try {
            if( config.checkPointId().isPresent() ) {
                return config.checkPointId()
                        .flatMap( id -> checkpoints.stream()
                                .filter( checkpoint -> checkpoint.getId().equals(id) )
                                .findFirst());
            }
            return getLast(checkpoints,config);
        }   finally {
            r.unlock();
        }
    }

    @Override
    public RunnableConfig put(RunnableConfig config, Checkpoint checkpoint) throws Exception {
        final LinkedList<Checkpoint> checkpoints = getCheckpoints(config);

        w.lock();
        try {
            final Checkpoint clonedCheckpoint = CheckpointSerializer.INSTANCE.cloneObject(checkpoint);

            if (config.checkPointId().isPresent()) { // Replace Checkpoint
                String checkPointId = config.checkPointId().get();
                int index = IntStream.range(0, checkpoints.size())
                        .filter(i -> checkpoints.get(i).getId().equals(checkPointId))
                        .findFirst()
                        .orElseThrow(() -> (new NoSuchElementException(format("Checkpoint with id %s not found!", checkPointId))));
                checkpoints.set(index, clonedCheckpoint);
                return config;
            }

            checkpoints.push(clonedCheckpoint); // Add Checkpoint

            return RunnableConfig.builder(config)
                    .checkPointId(clonedCheckpoint.getId())
                    .build();
        }
        finally {
            w.unlock();
        }
    }

}
