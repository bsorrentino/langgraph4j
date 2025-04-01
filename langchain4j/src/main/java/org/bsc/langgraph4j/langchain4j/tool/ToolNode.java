package org.bsc.langgraph4j.langchain4j.tool;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.service.tool.DefaultToolExecutor;
import dev.langchain4j.service.tool.ToolExecutor;
import static dev.langchain4j.agent.tool.ToolSpecifications.toolSpecificationFrom;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;


/**
 * A node in the graph that executes a tool
 *
 * <p>This class is just a simple wrapper around a list of {@link Specification} that can be used to build a node
 * in a graph that can execute a tool with the given id.
 *
 * <p>The node will execute the first tool that has the given id.
 *
 * @see Specification
 */
public final class ToolNode {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ToolNode.class);

    public record Specification( ToolSpecification value, ToolExecutor executor)  {
        public static Specification of( ToolSpecification value, ToolExecutor executor ) {
            return new Specification(
                    Objects.requireNonNull(value,"value cannot be null"),
                    Objects.requireNonNull(executor, "executor cannot be null"));
        }
    }

    /**
     * Builder for {@link ToolNode}
     */
    public static class Builder {
        /**
         * List of tool specification
         */
        private final List<Specification> toolSpecifications = new ArrayList<>();

        /**
         * Adds a tool specification to the node
         *
         * @param spec the tool specification
         * @param executor the executor to use
         * @return the builder
         */
        public Builder specification(ToolSpecification spec, ToolExecutor executor) {
            return this.specification( Specification.of(spec, executor));
        }

        /**
         * Adds a tool specification to the node
         *
         * @param toolSpecifications the tool specification
         * @return the builder
         */
        public Builder specification(Specification toolSpecifications) {
            this.toolSpecifications.add(toolSpecifications);
            return this;
        }

        /**
         * Adds all the methods annotated with {@link Tool} to the node
         *
         * @param objectWithTool the object containing the tool
         * @return the builder
         */
        public Builder specification( Object objectWithTool ) {
            for (Method method : objectWithTool.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(Tool.class)) {
                    final ToolExecutor toolExecutor = new DefaultToolExecutor(objectWithTool, method);
                    toolSpecifications.add(new Specification(toolSpecificationFrom(method), toolExecutor));
                }
            }
            return this;
        }

        /**
         * Builds the node
         *
         * @return the node
         */
        public ToolNode build() {
            return new ToolNode(toolSpecifications);
        }
    }
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builds a ToolNode out of a collection of objects that have tools attached or a tool specification
     *
     * @param objectsWithToolsOrSpecification a list of objects with tools
     * @return a ToolNode
     * @deprecated use {@link #builder()}
     */
    @Deprecated
    public static ToolNode of( Collection<Object> objectsWithToolsOrSpecification) {

        final Builder builder = builder();

        for (Object objectWithToolOrSpecification : objectsWithToolsOrSpecification ) {

            if( objectWithToolOrSpecification instanceof Specification ) {
                builder.specification( (Specification) objectWithToolOrSpecification);
                continue;
            }
            for (Method method : objectWithToolOrSpecification.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(Tool.class)) {
                    final ToolExecutor toolExecutor = new DefaultToolExecutor(objectWithToolOrSpecification, method);
                    builder.specification( Specification.of( toolSpecificationFrom(method), toolExecutor ) );

                }
            }
        }
        return builder.build();
    }

    /**
     * Builds a ToolNode out of a array of objects that have tools attached or a tool specification
     *
     * @param objectsWithToolsOrSpecification a list of objects with tools
     * @return a ToolNode
     * @deprecated use {@link #builder()}
     */
    @Deprecated
    public static ToolNode of(Object ...objectsWithToolsOrSpecification) {
        return of( Arrays.asList(objectsWithToolsOrSpecification) );
    }


    private final List<Specification> entries;

    private ToolNode( List<Specification> entries) {
        this.entries = Objects.requireNonNull(entries, "entries cannot be null");
        if( entries.isEmpty() ) {
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
                .map(Specification::value)
                .collect(Collectors.toList());
    }

    /**
     * Executes the first matching tool
     *
     * @param request the request to execute
     * @param memoryId the memory id to pass to the tool
     * @return the result of the tool
     */
    public Optional<ToolExecutionResultMessage> execute( ToolExecutionRequest request, Object memoryId ) {
        Objects.requireNonNull( request, "request cannot be null" );

        log.trace( "execute: {}", request.name() );

        return entries.stream()
                .filter( v -> v.value().name().equals(request.name()))
                .findFirst()
                .map( e -> {
                    String value = e.executor().execute(request, memoryId);
                    return new ToolExecutionResultMessage( request.id(), request.name(), value );
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
    public Optional<ToolExecutionResultMessage> execute(Collection<ToolExecutionRequest> requests, Object memoryId ) {
        Objects.requireNonNull( requests, "requests cannot be null" );

        for( ToolExecutionRequest request : requests ) {

            Optional<ToolExecutionResultMessage> result = execute( request, memoryId );

            if( result.isPresent() ) {
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
    public Optional<ToolExecutionResultMessage> execute( ToolExecutionRequest request ) {
        return execute( request, null );
    }

    /**
     * Executes the first matching tool
     *
     * @param requests the requests to execute
     * @return the result of the tool
     */
    public Optional<ToolExecutionResultMessage> execute( Collection<ToolExecutionRequest> requests ) {
        return execute( requests, null );
    }
}