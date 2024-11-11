package org.bsc.langgraph4j.langchain4j.tool;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.service.tool.DefaultToolExecutor;
import dev.langchain4j.service.tool.ToolExecutor;
import static dev.langchain4j.agent.tool.ToolSpecifications.toolSpecificationFrom;

import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public final class ToolNode {

    @Value(staticConstructor = "of")
    @Accessors( fluent = true)
    public static class Specification {
        @NonNull
        ToolSpecification value;
        @NonNull
        ToolExecutor executor;
    }

    public static class Builder {
        private final List<Specification> toolSpecifications = new ArrayList<>();

        public Builder specification(ToolSpecification spec, ToolExecutor executor) {
            return this.specification( Specification.of(spec, executor));
        }

        public Builder specification(Specification toolSpecifications) {
            this.toolSpecifications.add(toolSpecifications);
            return this;
        }

        public Builder specification( Object objectWithTool ) {
            for (Method method : objectWithTool.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(Tool.class)) {
                    final ToolExecutor toolExecutor = new DefaultToolExecutor(objectWithTool, method);
                    toolSpecifications.add(new Specification(toolSpecificationFrom(method), toolExecutor));
                }
            }
            return this;
        }

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
     * @Deprecated use {@link #builder()}
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
     * @Deprecated use {@link #builder()}
     */
    @Deprecated
    public static ToolNode of(Object ...objectsWithToolsOrSpecification) {
        return of( Arrays.asList(objectsWithToolsOrSpecification) );
    }


    private final List<Specification> entries;

    private ToolNode( @NonNull List<Specification> entries) {
        if( entries.isEmpty() ) {
            throw new IllegalArgumentException("entries cannot be empty!");
        }
        this.entries = entries;
    }

    public List<ToolSpecification> toolSpecifications() {
        return this.entries.stream()
                .map(Specification::value)
                .collect(Collectors.toList());
    }

    public Optional<ToolExecutionResultMessage> execute( @NonNull ToolExecutionRequest request, Object memoryId ) {
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

    public Optional<ToolExecutionResultMessage> execute(@NonNull Collection<ToolExecutionRequest> requests, Object memoryId ) {
        for( ToolExecutionRequest request : requests ) {

            Optional<ToolExecutionResultMessage> result = execute( request, memoryId );

            if( result.isPresent() ) {
                return result;
            }
        }
        return Optional.empty();
    }

    public Optional<ToolExecutionResultMessage> execute( ToolExecutionRequest request ) {
        return execute( request, null );
    }

    public Optional<ToolExecutionResultMessage> execute( Collection<ToolExecutionRequest> requests ) {
        return execute( requests, null );
    }

}