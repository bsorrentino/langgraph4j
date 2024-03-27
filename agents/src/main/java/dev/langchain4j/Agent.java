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

@Builder
public class Agent {

    private final  ChatLanguageModel chatLanguageModel;
    @Singular  private final List<ToolSpecification> tools;


    public Response<AiMessage> execute( Map<String,Object> inputs ) {
        var messages = new ArrayList<ChatMessage>();
        var promptTemplate = PromptTemplate.from( "USER: {{input}}" ).apply(inputs);

        messages.add(new SystemMessage("You are a helpful assistant"));

        messages.add( new UserMessage(promptTemplate.text())  );

        return chatLanguageModel.generate( messages, tools );
    }

    private PromptTemplate getToolResponseTemplate( ) {
        var TEMPLATE_TOOL_RESPONSE = new StringBuilder()
                .append("TOOL RESPONSE:").append('\n')
                .append("---------------------").append('\n')
                .append("{{observation}}").append('\n')
                .append( "--------------------" ).append('\n')
                .append('\n')
                .toString();
        return PromptTemplate.from(TEMPLATE_TOOL_RESPONSE);
    }

    public Response<AiMessage> execute( String input, List<AgentExecutor.IntermediateStep> intermediateSteps ) {
        var agentScratchpadTemplate = getToolResponseTemplate();
        var userMessageTemplate = PromptTemplate.from( "USER'S INPUT: {{input}}" ).apply( Map.of( "input", input));

        var messages = new ArrayList<ChatMessage>();

        messages.add(new SystemMessage("You are a helpful assistant"));

        if( intermediateSteps.isEmpty()) {
            messages.add(new UserMessage(userMessageTemplate.text()));
        }

        for( AgentExecutor.IntermediateStep step: intermediateSteps ) {
            var agentScratchpad = agentScratchpadTemplate.apply( Map.of("observation", step.observation()) );
            messages.add(new UserMessage(agentScratchpad.text()));
                            ;
        }

        return chatLanguageModel.generate( messages, tools );
    }
}
