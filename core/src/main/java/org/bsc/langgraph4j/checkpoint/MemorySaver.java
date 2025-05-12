package org.bsc.langgraph4j.checkpoint;

import org.bsc.langgraph4j.RunnableConfig;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.util.Collections.unmodifiableCollection;
import static java.util.Optional.ofNullable;

public class MemorySaver implements BaseCheckpointSaver {

	final Map<String, LinkedList<Checkpoint>> _checkpointsByThread = new HashMap<>();

	private final ReentrantLock _lock = new ReentrantLock();

	public MemorySaver() {
	}

	final LinkedList<Checkpoint> getCheckpoints(RunnableConfig config) {
		_lock.lock();
		try {
			var threadId = config.threadId().orElse(THREAD_ID_DEFAULT);
			return _checkpointsByThread.computeIfAbsent(threadId, k -> new LinkedList<>());

		}
		finally {
			_lock.unlock();
		}
	}

	final Optional<Checkpoint> getLast(LinkedList<Checkpoint> checkpoints, RunnableConfig config) {
		return (checkpoints.isEmpty()) ? Optional.empty() : ofNullable(checkpoints.peek());
	}

	@Override
	public Collection<Checkpoint> list(RunnableConfig config) {

		_lock.lock();
		try {
			final LinkedList<Checkpoint> checkpoints = getCheckpoints(config);
			return unmodifiableCollection(checkpoints); // immutable checkpoints;
		}
		finally {
			_lock.unlock();
		}
	}

	@Override
	public Optional<Checkpoint> get(RunnableConfig config) {

		_lock.lock();
		try {
			final LinkedList<Checkpoint> checkpoints = getCheckpoints(config);
			if (config.checkPointId().isPresent()) {
				return config.checkPointId()
					.flatMap(
							id -> checkpoints.stream().filter(checkpoint -> checkpoint.getId().equals(id)).findFirst());
			}
			return getLast(checkpoints, config);

		}
		finally {
			_lock.unlock();
		}
	}

	@Override
	public RunnableConfig put(RunnableConfig config, Checkpoint checkpoint) throws Exception {

		_lock.lock();
		try {
			final LinkedList<Checkpoint> checkpoints = getCheckpoints(config);

			if (config.checkPointId().isPresent()) { // Replace Checkpoint
				String checkPointId = config.checkPointId().get();
				int index = IntStream.range(0, checkpoints.size())
					.filter(i -> checkpoints.get(i).getId().equals(checkPointId))
					.findFirst()
					.orElseThrow(() -> (new NoSuchElementException(
							format("Checkpoint with id %s not found!", checkPointId))));
				checkpoints.set(index, checkpoint);
				return config;
			}

			checkpoints.push(checkpoint); // Add Checkpoint

			return RunnableConfig.builder(config).checkPointId(checkpoint.getId()).build();
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

			return new Tag(threadId, _checkpointsByThread.remove(threadId));

		}
		finally {
			_lock.unlock();
		}
	}

}
