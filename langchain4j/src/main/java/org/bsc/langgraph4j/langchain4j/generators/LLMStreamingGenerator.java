package org.bsc.langgraph4j.langchain4j.generators;

import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.output.Response;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.bsc.async.AsyncGenerator;
import org.bsc.async.AsyncGeneratorQueue;
import org.bsc.langgraph4j.NodeOutput;
import org.bsc.langgraph4j.state.AgentState;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

@Slf4j
public class LLMStreamingGenerator<T, State extends AgentState> extends AsyncGenerator.WithResult<NodeOutput<State>> {

    public static <T,State extends AgentState> Builder<T,State> builder() {
        return new Builder<>();
    }

    final BlockingQueue<AsyncGenerator.Data<NodeOutput<State>>> queue;
    final Function<Response<T>, Map<String,Object>> mapResult;

    private LLMStreamingGenerator( @NonNull  BlockingQueue<AsyncGenerator.Data<NodeOutput<State>>> queue,
                                   Function<Response<T>, Map<String,Object>> mapResult)
    {
        super(new AsyncGeneratorQueue.Generator<>( queue ));
        this.mapResult = mapResult;
        this.queue = queue;
    }

    public StreamingResponseHandler<T> handler() {
        return new StreamingResponseHandler<T>() {

            @Override
            public void onNext(String token) {
                log.trace("onNext: {}", token);
                ;
                queue.add( AsyncGenerator.Data.of(NodeOutput.of( token, null )) );
            }

            @Override
            public void onComplete(Response<T> response) {
                log.trace("onComplete: {}", response);

                queue.add(AsyncGenerator.Data.done( mapResult.apply(response) ));
            }

            @Override
            public void onError(Throwable error) {
                log.trace("onError", error);
                CompletableFuture<NodeOutput<State>> future = new CompletableFuture<>();
                future.completeExceptionally(error);
                queue.add( AsyncGenerator.Data.of(future) );
            }
        };
    }

    public static class Builder<T,State extends AgentState> {
        private BlockingQueue<AsyncGenerator.Data<NodeOutput<State>>> queue;
        private Function<Response<T>,  Map<String,Object>> mapResult;

        public Builder<T,State> queue( BlockingQueue<AsyncGenerator.Data<NodeOutput<State>>> queue ) {
            this.queue = queue;
            return this;
        }

        public Builder<T,State> mapResult( Function<Response<T>, Map<String,Object>> mapResult ) {
            this.mapResult = mapResult;
            return this;
        }

        public LLMStreamingGenerator<T, State> build() {
            if( queue == null )
                queue = new LinkedBlockingQueue<AsyncGenerator.Data<NodeOutput<State>>>();
            return new LLMStreamingGenerator<>( queue, mapResult );
        }
    }
}


