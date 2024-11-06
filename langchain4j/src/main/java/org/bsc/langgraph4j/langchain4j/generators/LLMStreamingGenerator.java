package org.bsc.langgraph4j.langchain4j.generators;

import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.output.Response;
import org.bsc.async.AsyncGenerator;
import org.bsc.async.AsyncGeneratorQueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class LLMStreamingGenerator<T> implements AsyncGenerator<String> {
    private final AsyncGeneratorQueue.Generator<String> generator ;

    public LLMStreamingGenerator( BlockingQueue<AsyncGenerator.Data<String>> queue ) {
        this.generator = new AsyncGeneratorQueue.Generator<>( queue );
    }

    public LLMStreamingGenerator() {
        this( new LinkedBlockingQueue<>());
    }

    @Override
    public Data<String> next() {
        return generator.next();
    }

    public AsyncGenerator<String> generator( ) {
        return generator;
    }

    public StreamingResponseHandler<T> handler() {
        return new StreamingResponseHandler<T>() {

            @Override
            public void onNext(String token) {
                generator.queue().add( AsyncGenerator.Data.of(completedFuture(token)) );
            }

            @Override
            public void onComplete(Response<T> response) {
                generator.queue().add(AsyncGenerator.Data.done());
            }

            @Override
            public void onError(Throwable error) {
                CompletableFuture<String> future = new CompletableFuture<>();
                future.completeExceptionally(error);
                generator.queue().add( AsyncGenerator.Data.of(future) );
            }
        };
    }

}
