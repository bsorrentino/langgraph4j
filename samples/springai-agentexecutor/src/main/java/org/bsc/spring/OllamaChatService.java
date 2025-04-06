package org.bsc.spring;

import org.bsc.langgraph4j.spring.ai.tool.SpringAIToolService;
import org.bsc.spring.agentexecutor.ChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class that uses the {@link ChatClient} and
 * the {@link SpringAIToolService} to execute an LLM request.
 *
 */
@Service("ollama")
public class OllamaChatService implements ChatService {
    public final List<ToolCallback> tools;
    private final ChatClient chatClient;

    public OllamaChatService( List<ToolCallback> agentFunctions ) {
        this.tools = agentFunctions;

        this.chatClient = chatClientBuilder()
                .defaultSystem("You are a helpful AI Assistant answering questions.")
                .defaultTools(agentFunctions)
                .build();
    }


    public List<ToolCallback> tools() {
        return tools;
    }

    public ChatResponse execute( List<Message> messages ) {

        return chatClient
                .prompt()
                .messages( messages )
                .call()
                .chatResponse();
    }

    private ChatClient.Builder chatClientBuilder() {

        OllamaApi api = new OllamaApi( );

        var chatOptions = OllamaOptions.builder()
                .model("qwen2.5:7b")
                .temperature(0.1)
                .build();

        var chatModel = OllamaChatModel.builder()
                .ollamaApi( api )
                .defaultOptions(chatOptions)
                .build();

        var toolOptions = ToolCallingChatOptions.builder()
                .internalToolExecutionEnabled(false) // Disable automatic tool execution
                .build();

        return ChatClient.builder(chatModel)
                .defaultOptions(toolOptions);
    }


}