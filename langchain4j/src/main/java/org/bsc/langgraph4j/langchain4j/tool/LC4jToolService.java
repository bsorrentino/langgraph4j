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


    public record Specification(ToolSpecification value, ToolExecutor executor) {
        public static LC4jToolService.Specification of(ToolSpecification value, ToolExecutor executor) {
            return new LC4jToolService.Specification(
                    Objects.requireNonNull(value, "value cannot be null"),
                    Objects.requireNonNull(executor, "executor cannot be null"));
        }
    }

    /**
     * Builder for {@link LC4jToolService}
     */
    public static class Builder {
        /**
         * List of tool specification
         */
        private final List<LC4jToolService.Specification> toolSpecifications = new ArrayList<>();

        /**
         * Adds a tool specification to the node
         *
         * @param spec     the tool specification
         * @param executor the executor to use
         * @return the builder
         */
        public LC4jToolService.Builder specification(ToolSpecification spec, ToolExecutor executor) {
            return this.specification(LC4jToolService.Specification.of(spec, executor));
        }

        /**
         * Adds a tool specification to the node
         *
         * @param toolSpecifications the tool specification
         * @return the builder
         */
        public LC4jToolService.Builder specification(LC4jToolService.Specification toolSpecifications) {
            this.toolSpecifications.add(toolSpecifications);
            return this;
        }

        /**
         * Adds all the methods annotated with {@link Tool} to the node
         *
         * @param objectWithTool the object containing the tool
         * @return the builder
         */
        public LC4jToolService.Builder specification(Object objectWithTool) {
            for (Method method : objectWithTool.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(Tool.class)) {
                    final ToolExecutor toolExecutor = new DefaultToolExecutor(objectWithTool, method);
                    toolSpecifications.add(new LC4jToolService.Specification(toolSpecificationFrom(method), toolExecutor));
                }
            }
            return this;
        }

        /**
         * Builds the node
         *
         * @return the node
         */
        public LC4jToolService build() {
            return new LC4jToolService(toolSpecifications);
        }
    }

    public static LC4jToolService.Builder builder() {
        return new LC4jToolService.Builder();
    }

    private final List<LC4jToolService.Specification> entries;

    LC4jToolService(List<LC4jToolService.Specification> entries) {
        this.entries = Objects.requireNonNull(entries, "entries cannot be null");
        if (entries.isEmpty()) {
            throw new IllegalArgumentException("entries cannot be empty!");
        }
    }

    /**
     * Returns a list of {@link ToolSpecification}s that can be executed by this node
     *
     * @return a list of tool specifications
     */
    public List<ToolSpecification> toolSpecifications() {
        return this.entries.stream()
                .map(LC4jToolService.Specification::value)
                .collect(Collectors.toList());
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

        return entries.stream()
                .filter(v -> v.value().name().equals(request.name()))
                .findFirst()
                .map(e -> {
                    String value = e.executor().execute(request, memoryId);
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