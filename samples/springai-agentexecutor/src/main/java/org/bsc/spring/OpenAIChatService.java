package org.bsc.spring;

import org.bsc.spring.agentexecutor.ChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("openai")
public class OpenAIChatService implements ChatService {
    public final List<ToolCallback> tools;
    private final ChatClient chatClient;

    public OpenAIChatService( List<ToolCallback> agentFunctions ) {
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

        var api = OpenAiApi.builder()
                .baseUrl("https://api.openai.com")
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .build();

        var chatOptions = OpenAiChatOptions.builder()
                .model("gpt-4o-mini")
                .logprobs(false)
                .temperature(0.1)
                .build();

        var chatModel = OpenAiChatModel.builder()
                .openAiApi(api)
                .defaultOptions(chatOptions)
                .build();

        var toolOptions = ToolCallingChatOptions.builder()
                .internalToolExecutionEnabled(false) // Disable automatic tool execution
                .build();

        return ChatClient.builder(chatModel)
                .defaultOptions(toolOptions);
    }


}