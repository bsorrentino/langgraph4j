package org.bsc.langgraph4j.async;

import java.util.Objects;
import java.util.concurrent.*;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class AsyncQueue<E> implements AsyncIterator<E>, AutoCloseable {

    private BlockingQueue<Data<E>> queue;
    private final Executor executor;

    public AsyncQueue() {
        this(ForkJoinPool.commonPool());
    }

    public AsyncQueue(Executor executor) {
        queue = new SynchronousQueue<>();
        this.executor = executor;
    }
    /**
     * Inserts the specified element into this queue, waiting if necessary for space to become available.
     * @param e Element to be inserted
     * @throws InterruptedException if interrupted while waiting for space to become available
     */
    public void put(E e) throws InterruptedException {
        Objects.requireNonNull(queue);
        queue.put(new Data<>(e, false));
    }

    @Override
    public void close() throws Exception {
        Objects.requireNonNull(queue);
        queue.put(new Data<>(null, true));
        queue = null;
    }


    @Override
    public CompletableFuture<Data<E>> next() {
        // queue has been closed
        if( queue == null ) {
//            final var result = new CompletableFuture<Data<E>>();
//            result.completeExceptionally( new IllegalStateException("Queue has been closed"));
//            return result;
            return completedFuture(new Data<>(null, true));
        }
        return CompletableFuture.supplyAsync( () -> {
            try {
                return queue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

}
