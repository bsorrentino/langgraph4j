package org.bsc.langgraph4j.async;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static java.util.concurrent.CompletableFuture.completedFuture;

public interface AsyncIterator<T> extends Iterable<T> {

    record Data<T>(T data, boolean done) {}

    CompletableFuture<Data<T>> next();

    default CompletableFuture<Void> fetchNext( final Consumer<T> consumer ) {
        return next().thenApply(data -> {
            if (data.done) {
                return false;
            }
            consumer.accept(data.data);
            return true;
        })
        .thenCompose( hasNext  -> {
            if (!hasNext) {
                return completedFuture(null);
            }
            return fetchNext(consumer);
        });
    }
    default CompletableFuture<Void> forEachAsync(  final Consumer<T> consumer) {
        return fetchNext(consumer);
    }

    default Iterator<T> iterator() {
        return new Iterator<>() {

            private Data<T> currentFetchedData;
            @Override
            public boolean hasNext() {
                if( currentFetchedData != null && currentFetchedData.done ) {
                    return false;
                }
                currentFetchedData = AsyncIterator.this.next().join();
                return !currentFetchedData.done;
            }

            @Override
            public T next() {
                if (currentFetchedData == null ) {
                   if( !hasNext() ) {
                       throw new NoSuchElementException("no elements into iterator");
                   }
                }
                if( currentFetchedData.done ) {
                    throw new NoSuchElementException("no more elements into iterator");
                }
                return currentFetchedData.data;
            }
        };
    }

    }


