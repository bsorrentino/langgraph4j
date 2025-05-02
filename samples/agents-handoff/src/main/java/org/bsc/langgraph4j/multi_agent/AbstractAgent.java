package org.bsc.langgraph4j.multi_agent;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.service.tool.ToolExecutor;
import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.agentexecutor.AgentExecutor;

import java.util.Map;
import java.util.Objects;

public abstract class AbstractAgent<B extends AbstractAgent.Builder<B,T>,T extends AbstractAgent<B,T>> implements ToolExecutor {

    public static abstract class Builder<B,T> {

        final AgentExecutor.Builder delegate = AgentExecutor.builder();

        private String name;
        private String description;
        private JsonObjectSchema parameters;

        @SuppressWarnings("unchecked")
        protected B result() {
            return (B) this;
        }

        public B name(String name) {
            if( this.name == null ) {
                this.name = name;
            }
            return result();
        }

        public B description(String description) {
            if( this.description == null ) {
                this.description = description;
            }
            return result();
        }

        public B parameters(JsonObjectSchema parameters) {
            if( this.parameters == null ) {
                this.parameters = parameters;
            }
            return result();
        }

        public B singleParameter(String context) {
            if (this.parameters == null) {
                this.parameters = JsonObjectSchema.builder()
                        .addStringProperty("context",
                                Objects.requireNonNull(context, "context cannot be null"))
                        .build();
            }
            return result();
        }

        public B chatLanguageModel(ChatLanguageModel model) {
            delegate.chatLanguageModel(model);
            return result();
        }

        public B tool(Map.Entry<ToolSpecification, ToolExecutor> entry) {
            delegate.tool(entry);
            return result();
        }

        public B toolFromObject( Object objectWithTools ) {
            delegate.toolsFromObject(objectWithTools);
            return result();
        }

        public B systemMessage(SystemMessage message) {
            delegate.systemMessage(message);
            return result();
        }

        abstract public T build() throws GraphStateException;
    }

    private final String name;
    private final String description;
    private final JsonObjectSchema parameters;
    final CompiledGraph<AgentExecutor.State> agentExecutor;

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public JsonObjectSchema parameters() {
        return parameters;
    }

    public Map.Entry<ToolSpecification, ToolExecutor> asTool() {
        var spec = ToolSpecification.builder()
                .name(name())
                .description(description())
                .parameters(parameters())
                .build();
        return Map.entry(spec, this);
    }

    public AbstractAgent( Builder<B,T> builder ) throws GraphStateException {

        this.name = Objects.requireNonNull( builder.name, "name cannot be null" );
        this.description = Objects.requireNonNull( builder.description, "description cannot be null" );
        this.parameters = Objects.requireNonNull( builder.parameters, "parameters cannot be null" );

        agentExecutor = builder.delegate.build().compile();
    }

    @Override
    public String execute(ToolExecutionRequest toolExecutionRequest, Object o) {

        var userMessage = UserMessage.from( toolExecutionRequest.arguments() );

        var result = agentExecutor.invoke( Map.of( "messages", userMessage ) );

        return result.flatMap(AgentExecutor.State::finalResponse).orElseThrow();
    }

}
