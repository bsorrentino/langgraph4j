package org.bsc.langgraph4j.agentexecutor;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.*;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.output.Response;
import lombok.Builder;
import lombok.Singular;
import org.bsc.langgraph4j.agentexecutor.state.AgentAction;
import org.bsc.langgraph4j.agentexecutor.state.IntermediateStep;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    /**
     * Prepares a list of chat messages based on the input and intermediate steps.
     *
     * @param input the input string to process.
     * @param intermediateSteps a list of intermediate steps to consider.
     * @return a list of prepared chat messages.
     */
    private List<ChatMessage> prepareMessages(String input, List<IntermediateStep> intermediateSteps) {
        var userMessageTemplate = PromptTemplate.from("{{input}}")
                .apply(Map.of("input", input));

        var messages = new ArrayList<ChatMessage>();

        messages.add(new SystemMessage("You are a helpful assistant"));
        messages.add(new UserMessage(userMessageTemplate.text()));

        if (!intermediateSteps.isEmpty()) {

            var toolRequests = intermediateSteps.stream()
                    .map(IntermediateStep::action)
                    .map(AgentAction::toolExecutionRequest)
                    .collect(Collectors.toList());

            messages.add(new AiMessage(toolRequests)); // reply with tool requests

            for (IntermediateStep step : intermediateSteps) {
                var toolRequest = step.action().toolExecutionRequest();

                messages.add(new ToolExecutionResultMessage(toolRequest.id(), toolRequest.name(), step.observation()));
            }
        }
        return messages;
    }

    /**
     * Executes the agent's action based on the input and intermediate steps, using a streaming response handler.
     *
     * @param input the input string to process.
     * @param intermediateSteps a list of intermediate steps to consider.
     * @param handler the handler for streaming responses.
     */
    public void execute(String input, List<IntermediateStep> intermediateSteps, StreamingResponseHandler<AiMessage> handler) {
        Objects.requireNonNull(streamingChatLanguageModel, "streamingChatLanguageModel is required!");

        streamingChatLanguageModel.generate(prepareMessages(input, intermediateSteps), tools, handler);
    }

    /**
     * Executes the agent's action based on the input and intermediate steps, returning a response.
     *
     * @param input the input string to process.
     * @param intermediateSteps a list of intermediate steps to consider.
     * @return a response containing the generated AI message.
     */
    public Response<AiMessage> execute(String input, List<IntermediateStep> intermediateSteps) {
        Objects.requireNonNull(chatLanguageModel, "chatLanguageModel is required!");
        return chatLanguageModel.generate(prepareMessages(input, intermediateSteps), tools);
    }
}
