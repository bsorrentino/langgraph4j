package org.bsc.langgraph4j.agentexecutor;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.*;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import lombok.Builder;
import lombok.Singular;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * Represents an agent that can process chat messages and execute actions using specified tools.
 */
@Builder
public class Agent {

    private final ChatLanguageModel chatLanguageModel;
    private final StreamingChatLanguageModel streamingChatLanguageModel;
    @Singular private final List<ToolSpecification> tools;

    /**
     * Checks if the agent is currently streaming.
     *
     * @return true if the agent is streaming, false otherwise.
     */
    public boolean isStreaming() {
        return streamingChatLanguageModel != null;
    }

    private ChatRequest prepareRequest(List<ChatMessage> messages ) {

//        var text =  CollectionsUtils.last(messages).map( m ->
//            switch( m.type() ) {
//                case AI -> ((AiMessage)m).text() ;
//                case USER -> ((UserMessage)m).singleText();
//                case SYSTEM -> ((SystemMessage)m).text();
//                case TOOL_EXECUTION_RESULT -> ((ToolExecutionResultMessage)m).text();
//                case CUSTOM -> ((CustomMessage)m).text();
//            }
//        ).orElseThrow();

        var reqMessages = new ArrayList<ChatMessage>( messages );
        reqMessages.add(SystemMessage.from("You are a helpful assistant"));


        var parameters = ChatRequestParameters.builder()
                .toolSpecifications(tools)
                .build();
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
