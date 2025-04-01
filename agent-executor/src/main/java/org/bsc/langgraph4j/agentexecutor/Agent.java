package org.bsc.langgraph4j.agentexecutor;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.*;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


/**
 * Represents an agent that can process chat messages and execute actions using specified tools.
 */
public class Agent {

    static public class Builder {
        private ChatLanguageModel chatLanguageModel;
        private StreamingChatLanguageModel streamingChatLanguageModel;
        private List<ToolSpecification> tools = new ArrayList<>();

        public Builder chatLanguageModel( ChatLanguageModel chatLanguageModel ) {
            this.chatLanguageModel = chatLanguageModel;
            return this;
        }

        public Builder streamingChatLanguageModel( StreamingChatLanguageModel streamingChatLanguageModel ) {
            this.streamingChatLanguageModel = streamingChatLanguageModel;
            return this;
        }

        public Builder tool( ToolSpecification toolSpecification ) {
            this.tools.add( toolSpecification );
            return this;
        }

        public Builder tools( Collection<? extends ToolSpecification> toolSpecifications ) {
            this.tools.addAll( toolSpecifications );
            return this;
        }

        public Agent build() {
            return new Agent(
                    chatLanguageModel,
                    streamingChatLanguageModel,
                    tools
            );
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private final ChatLanguageModel chatLanguageModel;
    private final StreamingChatLanguageModel streamingChatLanguageModel;
    private final List<ToolSpecification> tools;

    /**
     * Checks if the agent is currently streaming.
     *
     * @return true if the agent is streaming, false otherwise.
     */
    public boolean isStreaming() {
        return streamingChatLanguageModel != null;
    }

    protected Agent( ChatLanguageModel chatLanguageModel,
                   StreamingChatLanguageModel streaming,
                     List<ToolSpecification> tools ) {
        this.chatLanguageModel = chatLanguageModel;
        this.streamingChatLanguageModel = streaming;
        this.tools = tools;

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
