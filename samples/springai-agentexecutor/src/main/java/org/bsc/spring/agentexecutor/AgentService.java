package org.bsc.spring.agentexecutor;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service class that uses the {@link ChatClient} and
 * the {@link ToolService} to execute an LLM request.
 *
 */
@Service
public class AgentService {
    public final ToolService toolService;
    private final ChatClient chatClient;

    /**
     * Constructs an instance of {@link AgentService}.
     *
     * @param chatClientBuilder The builder for the {@link ChatClient} to be used.
     * @param toolService The service providing tools and functions that the agent can utilize.
     */
    public AgentService(ChatClient.Builder chatClientBuilder, ToolService toolService) {
        var chatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks( toolService.agentFunctionsCallback().stream().map( FunctionCallback.class::cast ).toList() )
                .internalToolExecutionEnabled(false) // Disable automatic tool execution
                .build();


        this.chatClient = chatClientBuilder
                .defaultSystem("You are a helpful AI Assistant answering questions.")
                .defaultTools(toolService.agentFunctionsCallback())
                .defaultOptions(chatOptions)
                .build();

        this.toolService = toolService;
    }

    /**
     * Builds a tool response message based on the given intermediate step.
     *
     * @param intermediateStep The intermediate step containing the tool call and observation.
     * @return The constructed {@link ToolResponseMessage}.
     */
    private Message buildToolResponseMessage( AgentExecutor.Step intermediateStep ) {
        var toolCall = intermediateStep.action().toolCall();
        var response = new ToolResponseMessage.ToolResponse(
                toolCall.id(),
                toolCall.name(),
                intermediateStep.observation());
        return new ToolResponseMessage(List.of(response), Map.of());
    }

    /**
     * Executes a chat request using the provided input and intermediate steps.
     *
     * @param input       The user's input string for the chat request.
     * @param intermediateSteps A list of intermediate steps to generate tool responses.
     * @return A {@link ChatResponse} object containing the response from the chat client.
     */
    public ChatResponse execute( String input, List<AgentExecutor.Step> intermediateSteps ) {
        var messages = (List<Message>)intermediateSteps.stream()
                                .map(this::buildToolResponseMessage)
                                .toList();

        return chatClient
                .prompt()
                .user(input)
                .messages( messages )
                .call()
                .chatResponse();
    }

}