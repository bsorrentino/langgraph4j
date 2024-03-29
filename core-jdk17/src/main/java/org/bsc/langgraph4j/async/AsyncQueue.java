package org.bsc.langgraph4j.async;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Stream;

import static java.lang.String.format;

public class AsyncQueue<E> implements AsyncIterator<E> {

    private BlockingQueue<Item<E>> queue;
    private final Executor executor;

    final long timeout;
    final TimeUnit timeoutUnit;

    public AsyncQueue() {
        this(ForkJoinPool.commonPool(), 60, TimeUnit.SECONDS);
    }
    public AsyncQueue(Executor executor) {
        this(executor, 60, TimeUnit.SECONDS);
    }
    public AsyncQueue(Executor executor, long timeout, TimeUnit timeoutUnit) {
        Objects.requireNonNull(executor);
        Objects.requireNonNull(timeoutUnit);

        queue = new SynchronousQueue<>();
        this.executor = executor;
        this.timeout = timeout;
        this.timeoutUnit = timeoutUnit;
    }
    /**
     * Inserts the specified element into this queue, waiting if necessary for space to become available.
     * @param e Element to be inserted
     * @throws InterruptedException if interrupted while waiting for space to become available
     */
    public final void put(E e) throws InterruptedException {
        Objects.requireNonNull(queue);
        queue.put( Item.of( new AsyncIterator.Data<>(e, false) ) );
    }

    public boolean closeExceptionally(Throwable ex) {
        if( queue == null ) {
            return false;
        }
        try {
            return queue.offer( Item.of(ex), timeout, timeoutUnit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    public final boolean close()  {
        if( queue == null ) {
            return false;
        }
        try {
            return queue.offer( Item.of(new AsyncIterator.Data<>(null, true) ), timeout, timeoutUnit);
        } catch (InterruptedException e) {
           return false;
        }
    }

    //@Override
    //public Stream<E> stream() {
    //    return queue.stream().map( i -> i.data().data() );
    //}

    @Override
    public final CompletableFuture<Data<E>> next() {
        return CompletableFuture.supplyAsync( () -> {
            try {
                Item<E> result = queue.poll(timeout, timeoutUnit);
                if( result == null ) {
                    queue = null;
                    throw new RuntimeException( format("queue exceed the poll timeout %d %s", timeout, timeoutUnit) );
                }
                if (result.isError()) {
                    queue = null;
                    throw new RuntimeException(result.error());
                }
                if( result.isEnd() ) {
                    queue = null;
                }
                return result.data();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

}
