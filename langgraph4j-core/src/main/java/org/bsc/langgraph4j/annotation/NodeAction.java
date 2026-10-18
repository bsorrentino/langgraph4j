package org.bsc.langgraph4j.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method of an {@link ActionCatalog} as a graph node action.
 * <p>
 * Annotated methods are discovered by {@link ActionCatalogRepository#of(ActionCatalog)}.
 * A method may accept an {@link org.bsc.langgraph4j.state.AgentState} and/or a
 * {@link org.bsc.langgraph4j.RunnableConfig} (in any order) and return a {@code Map},
 * a {@code CompletableFuture<Map>} or {@code null}; alternatively it may accept a single
 * {@link org.bsc.langgraph4j.CompileConfig} and return a node action.
 *
 * @since 1.9.6
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NodeAction {

    /**
     * The node action name.
     *
     * @return the action name, or the method name when blank (default)
     */
    String name() default "";

    /**
     * An optional description of the node action.
     *
     * @return the description, empty by default
     */
    String description() default "";
}
