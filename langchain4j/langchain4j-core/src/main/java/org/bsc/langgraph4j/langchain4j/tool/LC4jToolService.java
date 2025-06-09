package org.bsc.langgraph4j.langchain4j.tool;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.service.tool.DefaultToolExecutor;
import dev.langchain4j.service.tool.ToolExecutor;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static dev.langchain4j.agent.tool.ToolSpecifications.toolSpecificationFrom;

public final  class LC4jToolService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LC4jToolService.class);

    /**
     * Tool specification data class
     * @param value
     * @param executor
     * @deprecated use {@link Map.Entry<ToolSpecification, ToolExecutor>}  instead
     */
    @Deprecated
    public record Specification(ToolSpecification value, ToolExecutor executor)  {
        public static LC4jToolService.Specification of(ToolSpecification value, ToolExecutor executor) {
            return new LC4jToolService.Specification(
                    Objects.requireNonNull(value, "value cannot be null"),
                    Objects.requireNonNull(executor, "executor cannot be null"));
        }
    }

    /**
     * Builder for {@link LC4jToolService}
     */
    public static class Builder extends LC4jToolMapBuilder<Builder> {

        /**
         * Adds a tool specification to the node
         *
         * @param spec     the tool specification
         * @param executor the executor to use
         * @return the builder
         */
        @Deprecated
        public Builder specification(ToolSpecification spec, ToolExecutor executor) {
            return super.tool(spec, executor);
        }

        /**
         * Adds a tool specification to the node
         *
         * @param toolSpecification the tool specification
         * @return the builder
         */
        @Deprecated
        public Builder specification(LC4jToolService.Specification toolSpecification) {
            return super.tool( toolSpecification.value(), toolSpecification.executor());
        }

        /**
         * Adds all the methods annotated with {@link Tool} to the node
         *
         * @param objectWithTools the object containing the tools
         * @return the builder
         */
        @Deprecated
        public Builder specification(Object objectWithTools) {
            return super.toolsFromObject( objectWithTools );
        }

        /**
         * Builds the node
         *
         * @return the node
         */
        public LC4jToolService build() {
            return new LC4jToolService(toolMap());
        }
    }

    public static LC4jToolService.Builder builder() {
        return new LC4jToolService.Builder();
    }

    private final Map<ToolSpecification, ToolExecutor> toolMap;

    public LC4jToolService(  Map<ToolSpecification, ToolExecutor> toolMap ) {
        this.toolMap = Objects.requireNonNull(toolMap, "toolMap cannot be null");
        if (toolMap.isEmpty()) {
            log.warn( "tool chain is empty!" );
            // throw new IllegalArgumentException("entries cannot be empty!");
        }
    }

    /**
     * Returns a list of {@link ToolSpecification}s that can be executed by this node
     *
     * @return a list of tool specifications
     */
    public List<ToolSpecification> toolSpecifications() {
        return this.toolMap.keySet().stream().toList();
    }

    /**
     * Executes the first matching tool
     *
     * @param request  the request to execute
     * @param memoryId the memory id to pass to the tool
     * @return the result of the tool
     */
    public Optional<ToolExecutionResultMessage> execute(ToolExecutionRequest request, Object memoryId) {
        Objects.requireNonNull(request, "request cannot be null");

        log.trace("execute: {}", request.name());

        return toolMap.entrySet().stream()
                .filter(e -> Objects.equals(e.getKey().name(),request.name()))
                .map(Map.Entry::getValue)
                .findFirst()
                .map(e -> {
                    String value = e.execute(request, memoryId);
                    return new ToolExecutionResultMessage(request.id(), request.name(), value);
                })
                ;
    }

    /**
     * Executes the first matching tool
     *
     * @param requests the requests to execute
     * @param memoryId the memory id to pass to the tool
     * @return the result of the tool
     */
    public Optional<ToolExecutionResultMessage> execute(Collection<ToolExecutionRequest> requests, Object memoryId) {
        Objects.requireNonNull(requests, "requests cannot be null");

        for (ToolExecutionRequest request : requests) {

            Optional<ToolExecutionResultMessage> result = execute(request, memoryId);

            if (result.isPresent()) {
                return result;
            }
        }
        return Optional.empty();
    }

    /**
     * Executes the first matching tool
     *
     * @param request the request to execute
     * @return the result of the tool
     */
    public Optional<ToolExecutionResultMessage> execute(ToolExecutionRequest request) {
        return execute(request, null);
    }

    /**
     * Executes the first matching tool
     *
     * @param requests the requests to execute
     * @return the result of the tool
     */
    public Optional<ToolExecutionResultMessage> execute(Collection<ToolExecutionRequest> requests) {
        return execute(requests, null);
    }

}