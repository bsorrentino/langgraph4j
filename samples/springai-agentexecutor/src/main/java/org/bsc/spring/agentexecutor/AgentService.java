package org.bsc.spring.agentexecutor;

import org.bsc.langgraph4j.spring.ai.tool.SpringAIToolService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class that uses the {@link ChatClient} and
 * the {@link SpringAIToolService} to execute an LLM request.
 *
 */
@Service
public class AgentService {
    public final SpringAIToolService toolService;
    private final ChatClient chatClient;

    public AgentService( List<ToolCallback> agentFunctions ) {
        this.toolService = new SpringAIToolService( agentFunctions );

        this.chatClient = ollamaChatClient()
                .defaultSystem("You are a helpful AI Assistant answering questions.")
                .defaultTools(toolService.agentFunctionsCallback())
                .build();
    }

    public ChatResponse execute( List<Message> messages ) {

        return chatClient
                .prompt()
                .messages( messages )
                .call()
                .chatResponse();
    }

    private ChatClient.Builder ollamaChatClient() {

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

    private ChatClient.Builder openAIChatClient() {

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