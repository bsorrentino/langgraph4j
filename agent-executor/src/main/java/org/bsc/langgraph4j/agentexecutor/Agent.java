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

@Builder
public class Agent {

    private final  ChatLanguageModel chatLanguageModel;
    private final StreamingChatLanguageModel streamingChatLanguageModel;
    @Singular  private final List<ToolSpecification> tools;

    public boolean isStreaming() {
        return streamingChatLanguageModel != null;
    }

    private List<ChatMessage> prepareMessages(String input, List<IntermediateStep> intermediateSteps) {
        var userMessageTemplate = PromptTemplate.from( "{{input}}" )
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

    public void execute( String input, List<IntermediateStep> intermediateSteps, StreamingResponseHandler<AiMessage> handler ) {
        Objects.requireNonNull(streamingChatLanguageModel, "streamingChatLanguageModel is required!");

        streamingChatLanguageModel.generate( prepareMessages(input, intermediateSteps), tools, handler );
    }

    public Response<AiMessage> execute( String input, List<IntermediateStep> intermediateSteps ) {
        Objects.requireNonNull(chatLanguageModel, "chatLanguageModel is required!");
        return chatLanguageModel.generate( prepareMessages(input, intermediateSteps), tools );
    }
}
