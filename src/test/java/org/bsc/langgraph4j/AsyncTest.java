package org.bsc.langgraph4j;

import org.bsc.langgraph4j.async.AsyncIterator;
import org.bsc.langgraph4j.async.AsyncQueue;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.ForkJoinPool.commonPool;
import static org.bsc.langgraph4j.async.AsyncFunction.*;

import static org.junit.jupiter.api.Assertions.*;
public class AsyncTest {
    @Test
    public void asyncIteratorTest() throws Exception {

            String[] myArray = { "e1", "e2", "e3", "e4", "e5"};

            final var it = new AsyncIterator<String>() {

                private int cursor = 0;
                @Override
                public CompletableFuture<Data<String>> next() {

                    if (cursor == myArray.length) {
                        return completedFuture(new Data<>(null, true) );
                    }

                    return completedFuture(new Data<>(myArray[cursor++], false));
                }
            };

            List<String> result = new ArrayList<>();

            it.forEachAsync( consumer_async(result::add)).thenAccept( t -> {
                System.out.println( "Finished");
            });

            for (var i : it) {
                result.add(i);
                System.out.println(i);
            }
            System.out.println( "Finished");

            assertEquals( result.size(), myArray.length );
            assertIterableEquals( result, List.of(myArray));
        }
    @Test
    public void asyncQueueTest() throws Exception {

        var result = new ArrayList<String>();

        try ( final var queue = new AsyncQueue<String>() ) {

            queue.forEachAsync( consumer_async(result::add)).thenAccept( (t) -> {
                System.out.println( "Finished");
            });

            for( int i = 0 ; i < 10 ; ++i ) {
                queue.put("e"+i );
            }

        }

        assertEquals(result.size(), 10);
        assertIterableEquals(result, List.of("e0", "e1", "e2", "e3", "e4", "e5", "e6", "e7", "e8", "e9"));

    }

    @Test
    public void asyncQueueDirectTest() throws Exception {

        // AsyncQueue initialized with a direct executor. No thread is used on next() invocation
        final var queue = new AsyncQueue<String>(Runnable::run);

        commonPool().execute( () -> {
            try(queue) {
                for( int i = 0 ; i < 10 ; ++i ) {
                        queue.put( "e"+i );
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

        List<String> result = new ArrayList<>();

        for (var i : queue) {
            result.add(i);
        }

        System.out.println("Finished");

        queue.forEachAsync( consumer_async(result::add)).thenAccept( t -> {
            System.out.println( "Finished");

        });

        assertEquals( result.size(), 10 );
        assertIterableEquals(result, List.of("e0", "e1", "e2", "e3", "e4", "e5", "e6", "e7", "e8", "e9"));

    }

}
