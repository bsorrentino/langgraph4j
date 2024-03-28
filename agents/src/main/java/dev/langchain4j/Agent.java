package dev.langchain4j;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.output.Response;
import lombok.Builder;
import lombok.Singular;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

@Builder
public class Agent {

    private final  ChatLanguageModel chatLanguageModel;
    @Singular  private final List<ToolSpecification> tools;


    public Response<AiMessage> execute( String input, List<AgentExecutor.IntermediateStep> intermediateSteps ) {
        var userMessageTemplate = PromptTemplate.from( "{{input}}" )
                                                    .apply( Map.of( "input", input));

        var messages = new ArrayList<ChatMessage>();

        messages.add(new SystemMessage("You are a helpful assistant"));
        messages.add(new UserMessage(userMessageTemplate.text()));

        if (!intermediateSteps.isEmpty()) {

            var toolRequests = intermediateSteps.stream()
                    .map(AgentExecutor.IntermediateStep::action)
                    .map(AgentExecutor.AgentAction::toolExecutionRequest)
                    .toList();

            messages.add(new AiMessage(toolRequests)); // reply with tool requests

            for (AgentExecutor.IntermediateStep step : intermediateSteps) {
                var toolRequest = step.action().toolExecutionRequest();

                messages.add(new ToolExecutionResultMessage(toolRequest.id(), toolRequest.name(), step.observation()));
            }
        }
        return chatLanguageModel.generate( messages, tools );
    }
}
