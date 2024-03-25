package org.bsc.langgraph4j.async;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class AsyncQueue<E> implements AsyncIterator<E>, AutoCloseable {

    private BlockingQueue<Data<E>> queue;
    private final Executor executor;
    private final AtomicReference<Throwable> exception = new AtomicReference<>();

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
        if( exception.get() != null ) {
            throw new IllegalStateException("Queue has been closed with exception!");
        }
        queue.put(new Data<>(e, false));
    }

    public void closeExceptionally(Throwable ex) {
        exception.set(ex);
    }

    @Override
    public void close() throws Exception {
        if (exception.get() != null) {
            queue.put(new Data<>(exception.get()));
        }
        else {
            queue.put(new Data<>(null, true));
        }
    }


    @Override
    public CompletableFuture<Data<E>> next() {
        return CompletableFuture.supplyAsync( () -> {
            try {
                var result = queue.take();
                if( result.error() != null ) {
                    queue = null;
                }
                return result;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

}
