package org.bsc.langgraph4j.spring.ai.agentexecutor;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.chat.client.ChatClient;

import java.util.List;
import java.util.Objects;

class DefaultChatService implements ChatService {
    final ChatClient chatClient;
    final List<ToolCallback> tools;

    public DefaultChatService(AgentExecutor.Builder builder ) {
        Objects.requireNonNull(builder.chatModel,"chatModel cannot be null!");
        this.tools = builder.tools;

        var toolOptions = ToolCallingChatOptions.builder()
                .internalToolExecutionEnabled(false) // Disable automatic tool execution
                .build();

        var chatClientBuilder = ChatClient.builder(builder.chatModel)
                .defaultOptions(toolOptions)
                .defaultSystem( builder.systemMessage != null ? builder.systemMessage :
                        "You are a helpful AI Assistant answering questions." );
        if (!builder.tools.isEmpty()) {
            chatClientBuilder.defaultTools(builder.tools);
        }
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public ChatResponse execute(List<Message> messages) {
        return chatClient
                .prompt()
                .messages( messages )
                .call()
                .chatResponse();
    }

    @Override
    public List<ToolCallback> tools() {
        return List.copyOf(tools);
    }

}
