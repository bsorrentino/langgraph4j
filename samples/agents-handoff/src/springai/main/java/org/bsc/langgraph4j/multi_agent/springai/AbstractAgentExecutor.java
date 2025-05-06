package org.bsc.langgraph4j.multi_agent.springai;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.serializer.StateSerializer;
import org.bsc.langgraph4j.spring.ai.agentexecutor.AgentExecutor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.ToolCallback;

import java.util.List;
import java.util.Map;

public abstract class AbstractAgentExecutor<B extends AbstractAgent.Builder<B>> extends AbstractAgent<AbstractAgentExecutor.Request,String,B> {

    public record Request( String input ) {};

    public static abstract class Builder<B extends AbstractAgent.Builder<B>> extends AbstractAgent.Builder<B> {

        final AgentExecutor.Builder agentExecutorBuilder = new AgentExecutor.Builder();

        public B chatModel(ChatModel chatModel) {
            agentExecutorBuilder.chatModel(chatModel);
            return result();
        }

        public B tool(ToolCallback tool) {
            agentExecutorBuilder.tool(tool);
            return result();
        }

        public B tools(List<ToolCallback> tools) {
            agentExecutorBuilder.tools(tools);
            return result();
        }

        public B toolsFromObject(Object objectWithTools) {
            agentExecutorBuilder.toolsFromObject(objectWithTools);
            return result();
        }

        public B stateSerializer(StateSerializer<AgentExecutor.State> stateSerializer) {
            this.agentExecutorBuilder.stateSerializer(stateSerializer);
            return result();
        }

        public B defaultSystem(String systemMessage) {
            this.agentExecutorBuilder.defaultSystem(systemMessage);
            return result();
        }


    }

    final CompiledGraph<AgentExecutor.State> agentExecutor;

    protected AbstractAgentExecutor(Builder<B> builder) throws GraphStateException {
        super(builder.inputType(Request.class));

        agentExecutor = builder.agentExecutorBuilder.build().compile();
    }

    @Override
    public String apply(Request request, ToolContext toolContext) {

        var userMessage = new UserMessage( request.input() );

        var result = agentExecutor.invoke( Map.of( "messages", userMessage ) );

        return result.flatMap(AgentExecutor.State::lastMessage)
                .map(AssistantMessage.class::cast)
                .map(AssistantMessage::getText)
                .orElseThrow()
                ;

    }
}
