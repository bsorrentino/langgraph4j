package org.bsc.langgraph4j.multi_agent.executor;

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
import org.bsc.langgraph4j.langchain4j.tool.LC4jToolService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Optional.ofNullable;


/**
 * Represents an agent that can process chat messages and execute actions using specified tools.
 */
public class Agent {

    static public abstract class Builder {
        final LC4jToolService.Builder toolServiceBuilder = LC4jToolService.builder();
        ChatLanguageModel chatLanguageModel;
        StreamingChatLanguageModel streamingChatLanguageModel;
        SystemMessage systemMessage;
        ResponseFormat responseFormat;

        public Builder chatLanguageModel( ChatLanguageModel chatLanguageModel ) {
            this.chatLanguageModel = chatLanguageModel;
            return this;
        }

        public Builder streamingChatLanguageModel( StreamingChatLanguageModel streamingChatLanguageModel ) {
            this.streamingChatLanguageModel = streamingChatLanguageModel;
            return this;
        }

        public Builder systemMessage( SystemMessage systemMessage ) {
            this.systemMessage = systemMessage;
            return this;
        }

        public Builder responseFormat( ResponseFormat responseFormat ) {
            this.responseFormat = responseFormat;
            return this;
        }
        /**
         * Sets the tool specification for the graph builder.
         *
         * @param objectsWithTool the tool specification
         * @return the updated GraphBuilder instance
         */
        public Builder toolSpecification(Object objectsWithTool) {
            toolServiceBuilder.specification(objectsWithTool);
            return this;
        }

        /**
         * Sets the tool specification with executor for the graph builder.
         *
         * @param spec    the tool specification
         * @param executor the tool executor
         * @return the updated GraphBuilder instance
         */
        public Builder toolSpecification(ToolSpecification spec, ToolExecutor executor) {
            toolServiceBuilder.specification(spec, executor);
            return this;
        }

        /**
         * Sets the tool specification for the graph builder.
         *
         * @param toolSpecification the tool specifications
         * @return the updated GraphBuilder instance
         */
        public Builder toolSpecification(LC4jToolService.Specification toolSpecification) {
            toolServiceBuilder.specification(toolSpecification);
            return this;
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

        var toolService = builder.toolServiceBuilder.build();

        var parametersBuilder = ChatRequestParameters.builder()
                .toolSpecifications( toolService.toolSpecifications() );

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
