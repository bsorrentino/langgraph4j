package org.bsc.spring.agentexecutor;

import org.bsc.langgraph4j.spring.ai.tool.ToolService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class that uses the {@link ChatClient} and
 * the {@link ToolService} to execute an LLM request.
 *
 */
@Service
public class AgentService {
    public final ToolService toolService;
    private final ChatClient chatClient;

    public AgentService(ChatClient.Builder chatClientBuilder, List<ToolCallback> agentFunctions ) {
        this.toolService = new ToolService( agentFunctions );

        var chatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks( toolService.agentFunctionsCallback().stream().map( FunctionCallback.class::cast ).toList() )
                .internalToolExecutionEnabled(false) // Disable automatic tool execution
                .build();


        this.chatClient = chatClientBuilder
                .defaultSystem("You are a helpful AI Assistant answering questions.")
                .defaultTools(toolService.agentFunctionsCallback())
                .defaultOptions(chatOptions)
                .build();
    }

    public ChatResponse execute( List<Message> messages ) {

        return chatClient
                .prompt()
                .messages( messages )
                .call()
                .chatResponse();
    }

}