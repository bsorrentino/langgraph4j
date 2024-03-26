package dev.langchain4j;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.service.AiServices;
import lombok.Builder;
import lombok.Singular;

import java.lang.reflect.Method;
import java.util.*;

import static dev.langchain4j.agent.tool.ToolSpecifications.toolSpecificationFrom;

@Builder
public class Agent {

    private final  ChatLanguageModel chatLanguageModel;
    @Singular  private final List<Object> tools;


    private List<ToolSpecification> getToolSpecifications() {
        var toolSpecifications = new ArrayList<ToolSpecification>();

        for (Object tool : tools) {
            for (Method method : tool.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(Tool.class)) {
                    var toolSpecification = toolSpecificationFrom(method);
                    toolSpecifications.add(toolSpecification);
                    //context.toolExecutors.put(toolSpecification.name(), new DefaultToolExecutor(objectWithTool, method));
                }
            }
        }
        return toolSpecifications;
    }
    public Response<AiMessage> execute( Map<String,Object> inputs ) {
        var messages = new ArrayList<ChatMessage>();
        var promptTemplate = PromptTemplate.from( "USER: {{input}}" ).apply(inputs);

        messages.add(new SystemMessage("You are a helpful assistant"));

        messages.add( new UserMessage(promptTemplate.text())  );

        return chatLanguageModel.generate( messages, getToolSpecifications() );
    }
}
