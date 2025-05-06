package org.bsc.langgraph4j.multi_agent.lc4j;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.service.tool.ToolExecutor;

import java.util.Map;
import java.util.Objects;

public abstract class AbstractAgent<B extends AbstractAgent.Builder<B>> implements ToolExecutor {

    public static abstract class Builder<B extends Builder<B>> {

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

    }

    private final String name;
    private final String description;
    private final JsonObjectSchema parameters;

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

    public AbstractAgent(Builder<B> builder ) {

        this.name = Objects.requireNonNull( builder.name, "name cannot be null" );
        this.description = Objects.requireNonNull( builder.description, "description cannot be null" );
        this.parameters = Objects.requireNonNull( builder.parameters, "parameters cannot be null" );
    }

}
