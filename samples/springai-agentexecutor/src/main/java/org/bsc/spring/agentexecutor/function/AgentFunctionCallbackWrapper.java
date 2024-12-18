package org.bsc.spring.agentexecutor.function;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.model.function.*;
import org.springframework.util.Assert;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A wrapper for a {@link FunctionCallback}.
 *
 * @param <I> the type of the input
 * @param <O> the type of the output
 */
@EqualsAndHashCode
public class AgentFunctionCallbackWrapper<I, O> implements BiFunction<I, ToolContext, O>, FunctionCallback {
    @Getter
    private final String name;
    @Getter
    private final String description;
    @Getter
    private final String inputTypeSchema;
    private final Class<I> inputType;
    private final ObjectMapper objectMapper;
    private final Function<O, String> responseConverter;
    private final BiFunction<I, ToolContext, O> biFunction;

    /**
     * Constructs a new instance of the AgentFunctionCallbackWrapper class with the specified parameters.
     *
     * @param name the name of the agent function callback wrapper, must not be null
     * @param description a description of the agent function callback wrapper, must not be null
     * @param inputTypeSchema the schema of the input type of the agent function, must not be null
     * @param inputType the input class type of the agent function, must not be null
     * @param responseConverter a function to convert the response object to a string, must not be null
     * @param objectMapper an instance of ObjectMapper for JSON processing, must not be null
     * @param function a bi-function that executes the agent function with the input and returns a result, must not be null
     */
    protected AgentFunctionCallbackWrapper(String name, String description, String inputTypeSchema, Class<I> inputType, Function<O, String> responseConverter, ObjectMapper objectMapper, BiFunction<I, ToolContext, O> function) {
        Assert.notNull(name, "Name must not be null");
        Assert.notNull(description, "Description must not be null");
        Assert.notNull(inputType, "InputType must not be null");
        Assert.notNull(inputTypeSchema, "InputTypeSchema must not be null");
        Assert.notNull(responseConverter, "ResponseConverter must not be null");
        Assert.notNull(objectMapper, "ObjectMapper must not be null");
        Assert.notNull(function, "Function must not be null");
        this.name = name;
        this.description = description;
        this.inputType = inputType;
        this.inputTypeSchema = inputTypeSchema;
        this.responseConverter = responseConverter;
        this.objectMapper = objectMapper;
        this.biFunction = function;
    }

    /**
     * Converts a {@link ToolResponseMessage.ToolResponse} object to the specified output type.
     *
     * @param toolResponse The response message from an external tool. Must not be null.
     * @return An instance of the target class populated with data from the tool response.
     * @throws RuntimeException if there is an error processing the JSON data.
     */
    public O convertResponse( ToolResponseMessage.ToolResponse toolResponse ) {
        try {
            Class<O> targetClass = resolveOutputType(biFunction);
            return objectMapper.readValue(toolResponse.responseData(), targetClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Executes a given function with the provided input and tool context.
     *
     * @param functionInput The input string in JSON format representing the function to be called. 
     * @param toolContext  The context object containing tools and utilities required for executing the function.
     * @return A string representing the result of the function execution, converted to a format suitable for output.
     */
    public String call(String functionInput, ToolContext toolContext) {
        I request = fromJson(functionInput, inputType);
        O response = apply(request, toolContext);
        return this.responseConverter.apply(response);
    }

    /**
     * Calls a specified function with the provided arguments.
     *
     * @param functionArguments The arguments for the function as a JSON string.
     * @return A string representing the response after processing.
     */
    public String call(String functionArguments) {
        I request = fromJson(functionArguments, inputType);
        return andThen(responseConverter).apply(request, null);
    }

    /**
     * Converts a JSON string to an instance of the specified class using Jackson's ObjectMapper.
     *
     * @param json The JSON string to be converted.
     * @param targetClass The class type of the resulting object.
     * @return An instance of the specified class populated with data from the JSON string.
     * @throws RuntimeException if an error occurs during the conversion process, wrapping any underlying {@link JsonProcessingException}.
     */
    private <T> T fromJson(String json, Class<T> targetClass) {
        try {
            return objectMapper.readValue(json, targetClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Applies a function to the given input and context.
     *
     * @param input  the input to apply
     * @param context the context in which to apply the input
     * @return the result of applying the input and context
     */
    public O apply(I input, ToolContext context) {
        return biFunction.apply(input, context);
    }

    /**
     * Resolves the output type of a given {@link BiFunction}.
     *
     * @param <I>   the type of the first input argument to the function
     * @param <O>   the type of the second input argument and return type of the function
     * @param biFunction the {@link BiFunction} to resolve the output type for
     * @return the class representing the output type of the {@code biFunction}
     */
    @SuppressWarnings("unchecked")
    private static <I, O> Class<O> resolveOutputType(BiFunction<I, ToolContext, O> biFunction) {
        return (Class<O>) TypeResolverHelper.getBiFunctionArgumentClass((Class<? extends BiFunction<?, ?, ?>>) biFunction.getClass(), 2);
    }

    /**
     * Creates a builder for an agent function callback.
     *
     * @param biFunction The bi-function that will be used to create the agent function.
     * @param <I>        The type of input to the bi-function.
     * @param <O>        The type of output from the bi-function.
     * @return A new builder instance.
     */
    public static <I, O> Builder<I, O> builder(BiFunction<I, ToolContext, O> biFunction) {
        return new AgentFunctionCallbackWrapper.Builder<>(biFunction);
    }

    /**
     * Creates a builder for an {@code AgentFunctionCallbackWrapper}.
     * The builder takes a {@link Function} that defines the mapping from input type {@code I} to output type {@code O}.
     *
     * @param <I> the input type of the function
     * @param <O> the output type of the function
     * @return a new {@code Builder} instance that can be used to configure and build an {@code AgentFunctionCallbackWrapper}
     */
    public static <I, O> Builder<I, O> builder(Function<I, O> function) {
        return new AgentFunctionCallbackWrapper.Builder<>(function);
    }

    /**
     * A class that serves as a builder for creating instances of {@link AgentFunctionCallbackWrapper}.
     * This builder allows for the specification of various properties such as name, description, input type,
     * and response converter, providing a structured way to construct complex objects.
     *
     * @param <I> The input type for the function or bi-function being built.
     * @param <O> The output type for the function or bi-function being built.
     */
    public static class Builder<I, O> {
        private String name;
        private String description;
        private Class<I> inputType;
        private final BiFunction<I, ToolContext, O> biFunction;
        private final Function<I, O> function;
        private FunctionCallbackContext.SchemaType schemaType;
        private Function<O, String> responseConverter;
        private String inputTypeSchema;
        private ObjectMapper objectMapper;

        /**
         * Initializes a new instance of the Builder class with the specified BiFunction.
         *
         * @param biFunction The {@link BiFunction} that the builder will use to handle operations, which cannot be null.
         */
        public Builder(BiFunction<I, ToolContext, O> biFunction) {
            this.schemaType = FunctionCallbackContext.SchemaType.JSON_SCHEMA;
            this.responseConverter = ModelOptionsUtils::toJsonString;
            this.objectMapper = (new ObjectMapper()).disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).disable(SerializationFeature.FAIL_ON_EMPTY_BEANS).registerModule(new JavaTimeModule());
            Assert.notNull(biFunction, "Function must not be null");
            this.biFunction = biFunction;
            this.function = null;
        }

        /**
         * Constructs a new instance of the Builder class.
         *
         * @param function The function to be used for processing input and producing output.
         * @throws IllegalArgumentException if the function is null.
         */
        public Builder(Function<I, O> function) {
            this.schemaType = FunctionCallbackContext.SchemaType.JSON_SCHEMA;
            this.responseConverter = ModelOptionsUtils::toJsonString;
            this.objectMapper = (new ObjectMapper()).disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).disable(SerializationFeature.FAIL_ON_EMPTY_BEANS).registerModule(new JavaTimeModule());
            Assert.notNull(function, "Function must not be null");
            this.biFunction = null;
            this.function = function;
        }

        /**
         * Sets the name for the current builder.
         *
         * @param name The name to be set. Must not be empty.
         * @return This builder instance for method chaining.
         */
        public Builder<I, O> withName(String name) {
            Assert.hasText(name, "Name must not be empty");
            this.name = name;
            return this;
        }

        /**
         * Sets the description for the builder.
         *
         * @param description the description to set
         * @return the current instance of Builder, allowing method chaining
         */
        public Builder<I, O> withDescription(String description) {
            Assert.hasText(description, "Description must not be empty");
            this.description = description;
            return this;
        }

        /**
         * Sets the input type for this builder.
         *
         * @param inputType the class representing the input type
         * @return the builder instance for method chaining
         */
        public Builder<I, O> withInputType(Class<I> inputType) {
            this.inputType = inputType;
            return this;
        }

        /**
         * Sets the response converter for the builder.
         *
         * @param responseConverter a function that converts the response object to a string
         * @return this builder instance for method chaining
         * @throws IllegalArgumentException if the {@code responseConverter} is null
         */
        public Builder<I, O> withResponseConverter(Function<O, String> responseConverter) {
            Assert.notNull(responseConverter, "ResponseConverter must not be null");
            this.responseConverter = responseConverter;
            return this;
        }

        /**
         * Sets the input type schema for the builder.
         *
         * @param inputTypeSchema the input type schema to set, cannot be empty
         * @return a reference to this Builder instance
         * @throws IllegalArgumentException if inputTypeSchema is empty
         */
        public Builder<I, O> withInputTypeSchema(String inputTypeSchema) {
            Assert.hasText(inputTypeSchema, "InputTypeSchema must not be empty");
            this.inputTypeSchema = inputTypeSchema;
            return this;
        }

        /**
         * Sets the objectMapper for the builder.
         *
         * @param objectMapper the ObjectMapper to use
         * @return this builder instance
         */
        public Builder<I, O> withObjectMapper(ObjectMapper objectMapper) {
            Assert.notNull(objectMapper, "ObjectMapper must not be null");
            this.objectMapper = objectMapper;
            return this;
        }

        /**
         * Sets the schema type for the builder.
         *
         * @param schemaType the schema type to set, must not be null
         * @return this builder instance for method chaining
         */
        public Builder<I, O> withSchemaType(FunctionCallbackContext.SchemaType schemaType) {
            Assert.notNull(schemaType, "SchemaType must not be null");
            this.schemaType = schemaType;
            return this;
        }

        /**
         * Builds an instance of {@link AgentFunctionCallbackWrapper}.
         *
         * <p>Asserts that the name and description are not empty, and that the responseConverter and objectMapper are not null. If the inputType is null,
         * it resolves the input type based on the function or biFunction provided. If the inputTypeSchema is null, it generates a JSON schema for the
         * input type based on whether the schemaType is OPEN_API_SCHEMA. It creates a finalBiFunction using the provided biFunction or falls back to using
         * the function with a default implementation.
         *
         * @return a new instance of {@link AgentFunctionCallbackWrapper}
         */
        public AgentFunctionCallbackWrapper<I, O> build() {
            Assert.hasText(name, "Name must not be empty");
            Assert.hasText(description, "Description must not be empty");
            Assert.notNull(responseConverter, "ResponseConverter must not be null");
            Assert.notNull(objectMapper, "ObjectMapper must not be null");
            if (inputType == null) {
                if (function != null) {
                    inputType = resolveInputType(function);
                } else {
                    inputType = resolveInputType(biFunction);
                }
            }

            if (inputTypeSchema == null) {
                boolean upperCaseTypeValues = schemaType == FunctionCallbackContext.SchemaType.OPEN_API_SCHEMA;
                inputTypeSchema = ModelOptionsUtils.getJsonSchema(inputType, upperCaseTypeValues);
            }

            BiFunction<I, ToolContext, O> finalBiFunction = biFunction != null ?
                    biFunction :
                    (request, context) -> function.apply(request);

            return new AgentFunctionCallbackWrapper<>(name, description, inputTypeSchema, inputType, responseConverter, objectMapper, finalBiFunction);
        }

        /**
         * Retrieves the input class type parameter of a {@link BiFunction}.
         *
         * @param <I>   the type of the input argument to the specified function object
         * @param <O>   the type of the result of the specified function object
         * @param biFunction the {@link BiFunction} whose input type parameter is to be retrieved
         * @return the input class type parameter of the specified {@code BiFunction}
         */
        @SuppressWarnings("unchecked")
        private static <I, O> Class<I> resolveInputType(BiFunction<I, ToolContext, O> biFunction) {
            return (Class<I>) TypeResolverHelper.getBiFunctionInputClass((Class<? extends BiFunction<?, ?, ?>>) biFunction.getClass());
        }

        /**
         * Resolves the input type of a given function.
         *
         * @param <I>   the input type parameter bound
         * @param <O>   the output type parameter bound
         * @param function the function whose input type is to be resolved
         * @return the input type of the provided function
         */
        @SuppressWarnings("unchecked")
        private static <I, O> Class<I> resolveInputType(Function<I, O> function) {
            return (Class<I>) TypeResolverHelper.getFunctionInputClass((Class<? extends Function<?, ?>>) function.getClass());
        }
    }

}