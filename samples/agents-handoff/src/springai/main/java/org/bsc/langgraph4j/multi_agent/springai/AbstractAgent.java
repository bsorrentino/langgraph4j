package org.bsc.langgraph4j.multi_agent.springai;


import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.execution.ToolCallResultConverter;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.ai.tool.metadata.ToolMetadata;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.util.Assert;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

import static java.lang.String.format;

public abstract class AbstractAgent<I, O, B extends AbstractAgent.Builder<B>> implements BiFunction<I,ToolContext, O> {

    public static abstract class Builder<B extends Builder<B>> {

        String name;
        String description;
        String parameterDescription;
        ToolMetadata toolMetadata;
        ToolCallResultConverter toolCallResultConverter;
        Type inputType;

        @SuppressWarnings("unchecked")
        protected B result() {
            return (B) this;
        }

        public B name( String name ) {
            this.name = name;
            return result();
        }

        public B description(String description) {
            this.description = description;
            return result();
        }
        public B parameterDescription(String parameterDescription) {
            this.parameterDescription = parameterDescription;
            return result();
        }

        public B inputType( Type type) {
            this.inputType = type;
            return result();
        }

        public B toolMetadata(ToolMetadata toolMetadata) {
            this.toolMetadata = toolMetadata;
            return result();
        }

        public B toolCallResultConverter(ToolCallResultConverter toolCallResultConverter) {
            this.toolCallResultConverter = toolCallResultConverter;
            return result();
        }

        public B inputType(ParameterizedTypeReference<?> inputType) {
            Assert.notNull(inputType, "inputType cannot be null");
            this.inputType = inputType.getType();
            return result();
        }

    }

    private final ToolCallback  toolCallback;

    public ToolCallback asTool() {
        return toolCallback;
    }

    public AbstractAgent( Builder<B> builder ) {


        this.toolCallback = FunctionToolCallback.builder(
                Objects.requireNonNull( builder.name, "name cannot be null!" ),
                this)
                .inputSchema(format("""
                        {
                          "$schema": "https://json-schema.org/draft/2020-12/schema",
                          "type": "object",
                          "properties": {
                            "input": {
                              "type": "string",
                              "description": "%s"
                            }
                          },
                          "required": [ "input" ]
                        }
                        """,
                        Objects.requireNonNull(builder.parameterDescription,"parameterDescription cannot be null!")))
                .inputType( builder.inputType )
                .description( builder.description )
                .toolMetadata( builder.toolMetadata )
                .toolCallResultConverter( builder.toolCallResultConverter )
                .build();
    }

}
