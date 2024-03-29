package org.bsc.langgraph4j.async;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.concurrent.CompletableFuture.completedFuture;

public interface AsyncIterator<T> extends Iterable<T> {

    record Data<T>(T data, boolean done) {}

    CompletableFuture<Data<T>> next();

    default CompletableFuture<Void> forEachAsync(  final AsyncFunction<T,Void> consumer) {

        return next().thenCompose(data -> {
                    if (data.done) {
                        return completedFuture(null);
                    }
                    return consumer.apply(data.data)
                            .thenCompose( v -> forEachAsync(consumer) );
                });
    }

    @Override
    default Iterator<T> iterator() {
        return new Iterator<>() {

            private final AtomicReference<Data<T>> currentFetchedData = new AtomicReference<>();
            @Override
            public boolean hasNext() {
                if (currentFetchedData.get() != null) {
                    return false;
                }
                return !currentFetchedData.updateAndGet( (v) -> AsyncIterator.this.next().join() ).done();
            }

            @Override
            public T next() {
                if (currentFetchedData.get() == null) {
                    if( !hasNext() ) {
                        throw new NoSuchElementException("no more elements into iterator");
                    }
                }
                if (currentFetchedData.get().done()) {
                    throw new NoSuchElementException("no more elements into iterator");
                }
                return currentFetchedData.getAndUpdate((v) -> null).data();
            }
        };
    }

    }


