package org.bsc.langgraph4j.langchain4j.generators;

import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.output.Response;
import lombok.extern.slf4j.Slf4j;
import org.bsc.async.AsyncGenerator;
import org.bsc.async.AsyncGeneratorQueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.concurrent.CompletableFuture.completedFuture;

@Slf4j
public class LLMStreamingGenerator<T> extends AsyncGenerator.WithResult<String> {
    final BlockingQueue<AsyncGenerator.Data<String>> queue;

    public LLMStreamingGenerator( BlockingQueue<AsyncGenerator.Data<String>> queue ) {
        super(new AsyncGeneratorQueue.Generator<>( queue ));
        this.queue = queue;
    }

    public LLMStreamingGenerator() {
        this( new LinkedBlockingQueue<>());
    }

    public StreamingResponseHandler<T> handler() {
        return new StreamingResponseHandler<T>() {

            @Override
            public void onNext(String token) {
                log.trace("onNext: {}", token);
                queue.add( AsyncGenerator.Data.of(completedFuture(token)) );
            }

            @Override
            public void onComplete(Response<T> response) {
                log.trace("onComplete: {}", response);
                queue.add(AsyncGenerator.Data.done(response));
            }

            @Override
            public void onError(Throwable error) {
                log.trace("onError", error);
                CompletableFuture<String> future = new CompletableFuture<>();
                future.completeExceptionally(error);
                queue.add( AsyncGenerator.Data.of(future) );
            }
        };
    }

}
