package org.bsc.langgraph4j.checkpoint;

import org.bsc.langgraph4j.RunnableConfig;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue105Test {

    // Test using the following code in Memory Saver, since the '_checkpointsByThread' is private.
    @Test
    public void concurrentExceptionTest() throws Exception {
        var memorySaver = new MemorySaver();
        ExecutorService executorService = Executors.newCachedThreadPool();
        int count = 100;
        CountDownLatch latch = new CountDownLatch(count);
        var index = new AtomicInteger(0);
        for (int i = 0; i < count; i++) {

            executorService.submit(() -> {
                try {


                    var threadName = format( "thread-%d", index.incrementAndGet() );
                    System.out.println( threadName );
                    memorySaver.list(RunnableConfig.builder().threadId(threadName).build());

                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        int size = memorySaver._checkpointsByThread.size();
        // size must be equals to count

        assertEquals( count, size, "Checkpoint Lost during concurrency" );
    }
}
