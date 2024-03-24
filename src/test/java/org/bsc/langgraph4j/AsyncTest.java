package org.bsc.langgraph4j;

import org.bsc.langgraph4j.async.AsyncIterator;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class AsyncTest {
    @Test
    public void asyncIteratorTest() throws Exception {

            int[] myArray = {1, 2, 3, 4, 5};

            final var it = new AsyncIterator<Integer>() {

                private int cursor = 0;
                @Override
                public CompletableFuture<Data<Integer>> next() {

                    if (cursor == myArray.length) {
                        return completedFuture(new Data<>(null, true) );
                    }

                    return completedFuture(new Data<>(myArray[cursor++], false));
                }
            };

            it.forEachAsync(System.out::println).get();

            for (var i : it) {
                System.out.println(i);
            }
        }
}
