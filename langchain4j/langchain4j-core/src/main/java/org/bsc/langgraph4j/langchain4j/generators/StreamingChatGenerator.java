package org.bsc.langgraph4j.langchain4j.generators;

import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import org.bsc.async.AsyncGenerator;
import org.bsc.async.AsyncGeneratorQueue;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.streaming.StreamingOutput;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;


public class StreamingChatGenerator<State extends AgentState> extends AsyncGenerator.WithResult<StreamingOutput<State>> {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(StreamingChatGenerator.class);

    /**
     * Creates a new Builder instance for LLMStreamingGenerator.
     *
     * @param <State> the type of the state extending AgentState
     * @return a new Builder instance
     */
    public static <State extends AgentState> StreamingChatGenerator.Builder<State> builder() {
        return new StreamingChatGenerator.Builder<>();
    }

    final StreamingChatResponseHandler handler;

    /**
     * Constructs an LLMStreamingGenerator with the specified parameters.
     *
     * @param queue the blocking queue for async generator data
     * @param startingNode the starting node for streaming
     * @param startingState the initial state
     * @param mapResult a function to map the response to a Map (ie. Partial State )
     */
    private StreamingChatGenerator( BlockingQueue<Data<StreamingOutput<State>>> queue,
                                   String startingNode,
                                   State startingState,
                                   Function<ChatResponse, Map<String,Object>> mapResult)
    {
        super(new AsyncGeneratorQueue.Generator<>( Objects.requireNonNull(queue, "queue cannot be null" )  ));

        this.handler = new StreamingChatResponseHandler() {

            @Override
            public void onPartialResponse(String token) {
                log.trace("onNext: {}", token);
                queue.add( AsyncGenerator.Data.of( new StreamingOutput<>( token, startingNode, startingState ) ) );

            }

            @Override
            public void onCompleteResponse(ChatResponse chatResponse) {
                log.trace("onComplete: {}", chatResponse);
                queue.add(AsyncGenerator.Data.done( mapResult.apply(chatResponse) ));

            }

            @Override
            public void onError(Throwable error) {
                log.trace("onError", error);
                queue.add( AsyncGenerator.Data.error(error) );
            }
        };
    }

    /**
     * Returns the StreamingResponseHandler associated with this generator.
     *
     * @return the handler for streaming responses
     */
    public StreamingChatResponseHandler handler() {
        return handler;
    }

    /**
     * Builder class for constructing instances of LLMStreamingGenerator.
     *
     * @param <State> the type of the state extending AgentState
     */
    public static class Builder<State extends AgentState> {
        private BlockingQueue<AsyncGenerator.Data<StreamingOutput<State>>> queue;
        private Function<ChatResponse,  Map<String,Object>> mapResult;
        private String startingNode;
        private State startingState;

        /**
         * Sets the queue for the builder.
         *
         * @param queue the blocking queue for async generator data
         * @return the builder instance
         */
        public Builder<State> queue(BlockingQueue<AsyncGenerator.Data<StreamingOutput<State>>> queue ) {
            this.queue = queue;
            return this;
        }

        /**
         * Sets the mapping function for the builder.
         *
         * @param mapResult a function to map the response to a result
         * @return the builder instance
         */
        public Builder<State> mapResult(Function<ChatResponse, Map<String,Object>> mapResult ) {
            this.mapResult = mapResult;
            return this;
        }

        /**
         * Sets the starting node for the builder.
         *
         * @param node the starting node
         * @return the builder instance
         */
        public Builder<State> startingNode(String node ) {
            this.startingNode = node;
            return this;
        }

        /**
         * Sets the starting state for the builder.
         *
         * @param state the initial state
         * @return the builder instance
         */
        public Builder<State> startingState(State state ) {
            this.startingState = state;
            return this;
        }

        /**
         * Builds and returns an instance of LLMStreamingGenerator.
         *
         * @return a new instance of LLMStreamingGenerator
         */
        public StreamingChatGenerator<State> build() {
            if( queue == null )
                queue = new LinkedBlockingQueue<>();
            return new StreamingChatGenerator<>( queue, startingNode, startingState, mapResult );
        }
    }
}
