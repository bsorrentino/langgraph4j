package org.bsc.langgraph4j.langchain4j.generators;

import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.output.Response;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.bsc.async.AsyncGenerator;
import org.bsc.async.AsyncGeneratorQueue;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.streaming.StreamingOutput;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

@Slf4j
public class LLMStreamingGenerator<T, State extends AgentState> extends AsyncGenerator.WithResult<StreamingOutput<State>> {

    public static <T,State extends AgentState> Builder<T,State> builder() {
        return new Builder<>();
    }

    final StreamingResponseHandler<T> handler;

    private LLMStreamingGenerator( @NonNull BlockingQueue<AsyncGenerator.Data<StreamingOutput<State>>> queue,
                                   String startingNode,
                                   State startingState,
                                   Function<Response<T>, Map<String,Object>> mapResult)
    {
        super(new AsyncGeneratorQueue.Generator<>( queue ));

        this.handler = new StreamingResponseHandler<T>() {

        @Override
        public void onNext(String token) {
            log.trace("onNext: {}", token);
            ;
            queue.add( AsyncGenerator.Data.of( new StreamingOutput<>( token, startingNode, startingState ) ) );
        }

        @Override
        public void onComplete(Response<T> response) {
            log.trace("onComplete: {}", response);

            queue.add(AsyncGenerator.Data.done( mapResult.apply(response) ));
        }

        @Override
        public void onError(Throwable error) {
            log.trace("onError", error);
            queue.add( AsyncGenerator.Data.error(error) );
        }
    };
    }

    public StreamingResponseHandler<T> handler() {
        return handler;
    }

    public static class Builder<T,State extends AgentState> {
        private BlockingQueue<AsyncGenerator.Data<StreamingOutput<State>>> queue;
        private Function<Response<T>,  Map<String,Object>> mapResult;
        private String startingNode;
        private State startingState;

        public Builder<T,State> queue( BlockingQueue<AsyncGenerator.Data<StreamingOutput<State>>> queue ) {
            this.queue = queue;
            return this;
        }

        public Builder<T,State> mapResult( Function<Response<T>, Map<String,Object>> mapResult ) {
            this.mapResult = mapResult;
            return this;
        }

        public Builder<T,State> startingNode( String node ) {
            this.startingNode = node;
            return this;
        }

        public Builder<T,State> startingState( State state ) {
            this.startingState = state;
            return this;
        }

        public LLMStreamingGenerator<T, State> build() {
            if( queue == null )
                queue = new LinkedBlockingQueue<AsyncGenerator.Data<StreamingOutput<State>>>();
            return new LLMStreamingGenerator<>( queue, startingNode, startingState, mapResult );
        }
    }
}


