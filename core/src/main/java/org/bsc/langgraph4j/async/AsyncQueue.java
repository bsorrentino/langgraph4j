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
        Objects.requireNonNull(queue);
        queue.put(new Data<>(e, false));
    }

    public void closeExceptionally(Throwable ex) {
        Objects.requireNonNull(queue);
        exception.set(ex);
    }

    @Override
    public void close() throws Exception {
        Objects.requireNonNull(queue);
        queue.put(new Data<>(null, true));
        queue = null;
    }


    @Override
    public CompletableFuture<Data<E>> next() {
        return CompletableFuture.supplyAsync( () -> {
            try {
                var result = queue.take();
                if( exception.get()!=null ) {
                    throw new RuntimeException(exception.get());
                }
                return result;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

}
