package org.bsc.langgraph4j.agentexecutor;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.service.tool.ToolExecutor;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.langchain4j.tool.LC4jToolMapBuilder;
import org.bsc.langgraph4j.langchain4j.tool.LC4jToolService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Optional.ofNullable;


/**
 * Represents an agent that can process chat messages and execute actions using specified tools.
 */
class Agent {

    static public abstract class Builder<T extends Builder<T>> extends LC4jToolMapBuilder<T> {
        ChatLanguageModel chatLanguageModel;
        StreamingChatLanguageModel streamingChatLanguageModel;
        SystemMessage systemMessage;
        ResponseFormat responseFormat;

        @SuppressWarnings( "unchecked" )
        protected T result() {
            return (T)this;
        }

        public T chatLanguageModel( ChatLanguageModel chatLanguageModel ) {
            if( this.chatLanguageModel == null ) {
                this.chatLanguageModel = chatLanguageModel;
            }
            return result();
        }

        public T chatLanguageModel( StreamingChatLanguageModel streamingChatLanguageModel ) {
            if( this.streamingChatLanguageModel == null ) {
                this.streamingChatLanguageModel = streamingChatLanguageModel;
            }
            return result();
        }

        public T systemMessage( SystemMessage systemMessage ) {
            if( this.systemMessage == null ) {
                this.systemMessage = systemMessage;
            }
            return result();
        }

        public T responseFormat( ResponseFormat responseFormat ) {
            this.responseFormat = responseFormat;
            return result();
        }

        /**
         * Sets the tool specification for the graph builder.
         *
         * @param objectsWithTools the tool specification
         * @return the updated GraphBuilder instance
         */
        @Deprecated
        public T toolSpecification(Object objectsWithTools) {
            super.toolsFromObject( objectsWithTools );
            return result();
        }

        @Deprecated
        public T toolSpecification(ToolSpecification spec, ToolExecutor executor) {
            super.tool(spec, executor);
            return result();
        }

        /**
         * Sets the tool specification for the graph builder.
         *
         * @param toolSpecification the tool specifications
         * @return the updated GraphBuilder instance
         */
        @Deprecated
        public T toolSpecification(LC4jToolService.Specification toolSpecification) {
            super.tool(toolSpecification.value(), toolSpecification.executor());
            return result();
        }

        public abstract StateGraph<AgentExecutor.State> build() throws GraphStateException;
    }

    private final ChatLanguageModel chatLanguageModel;
    private final StreamingChatLanguageModel streamingChatLanguageModel;
    private final SystemMessage systemMessage;

    final ChatRequestParameters parameters;

    /**
     * Checks if the agent is currently streaming.
     *
     * @return true if the agent is streaming, false otherwise.
     */
    public boolean isStreaming() {
        return streamingChatLanguageModel != null;
    }

    protected Agent( Builder builder ) {
        this.chatLanguageModel = builder.chatLanguageModel;
        this.streamingChatLanguageModel = builder.streamingChatLanguageModel;
        this.systemMessage = ofNullable( builder.systemMessage ).orElseGet( () -> SystemMessage.from("You are a helpful assistant") );

        var parametersBuilder = ChatRequestParameters.builder()
                .toolSpecifications( builder.toolMap().keySet().stream().toList() );

        if( builder.responseFormat != null ) {
            parametersBuilder.responseFormat(builder.responseFormat);
        }

        this.parameters =  parametersBuilder.build();
    }

    private ChatRequest prepareRequest(List<ChatMessage> messages ) {

        var reqMessages = new ArrayList<>( messages );
        reqMessages.add(systemMessage);

        return ChatRequest.builder()
                .messages( reqMessages )
                .parameters(parameters)
                .build();
    }

    /**
     * Executes the agent's action based on the input and intermediate steps, using a streaming response handler.
     *
     * @param messages the messages to process.
     * @param handler the handler for streaming responses.
     */
    public void execute(List<ChatMessage> messages, StreamingChatResponseHandler handler) {
        Objects.requireNonNull(streamingChatLanguageModel, "streamingChatLanguageModel is required!");

        streamingChatLanguageModel.chat(prepareRequest(messages), handler);

    }

    /**
     * Executes the agent's action based on the input and intermediate steps, returning a response.
     *
     * @param messages the messages to process.
     * @return a response containing the generated AI message.
     */
    public ChatResponse execute(List<ChatMessage> messages ) {
        Objects.requireNonNull(chatLanguageModel, "chatLanguageModel is required!");

       return chatLanguageModel.chat(prepareRequest(messages));
    }
}
