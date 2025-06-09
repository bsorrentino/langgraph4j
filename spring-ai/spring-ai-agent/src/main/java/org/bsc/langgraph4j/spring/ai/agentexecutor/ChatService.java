package org.bsc.langgraph4j.spring.ai.agentexecutor;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.chat.client.ChatClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;

class ChatService {
    final ChatClient chatClient;
    final List<ToolCallback> tools;
    final boolean streaming;

    public ChatService(AgentExecutorBuilder<?,?> builder ) {
        Objects.requireNonNull(builder.chatModel,"chatModel cannot be null!");
        this.tools = builder.tools;
        this.streaming = builder.streaming;
        var toolOptions = ToolCallingChatOptions.builder()
                .internalToolExecutionEnabled(false) // Disable automatic tool execution
                .build();

        var chatClientBuilder = ChatClient.builder(builder.chatModel)
                .defaultOptions(toolOptions)
                .defaultSystem( builder.systemMessage != null ?
                        builder.systemMessage :
                        "You are a helpful AI Assistant answering questions." );
                        
        if (!builder.tools.isEmpty()) {
            chatClientBuilder.defaultToolCallbacks(builder.tools);
        }

        this.chatClient = chatClientBuilder.build();
    }

    public ChatResponse execute(List<Message> messages) {
        return chatClient
                .prompt()
                .messages( messages )
                .call()
                .chatResponse();
    }

    public Flux<ChatResponse> streamingExecute(List<Message> messages) {
        return chatClient
                .prompt()
                .messages( messages )
                .stream()
                .chatResponse();
    }

    public List<ToolCallback> tools() {
        return List.copyOf(tools);
    }

}
