package org.bsc.langgraph4j.async;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.concurrent.CompletableFuture.completedFuture;

public interface AsyncIterator<T> extends AsyncIteratorData<T>, Iterable<T> {

    CompletableFuture<Data<T>> next();

    default CompletableFuture<Void> forEachAsync(  final AsyncFunction<T,Void> consumer) {

        return next().thenCompose(data -> {
                    if (data.done()) {
                        return completedFuture(null);
                    }
                    return consumer.apply(data.data())
                            .thenCompose( v -> forEachAsync(consumer) );
                });
    }

    default Stream<T> stream() {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(iterator(), Spliterator.ORDERED),
                false);
    }
    @Override
    default Iterator<T> iterator() {
        return new Iterator<T>() {

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


