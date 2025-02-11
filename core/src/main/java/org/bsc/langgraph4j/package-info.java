/**
 * The {@code org.bsc.langgraph4j} package provides classes and interfaces for building
 * stateful, multi-agent applications with LLMs. It includes core components such as
 * {@link org.bsc.langgraph4j.StateGraph}, {@link org.bsc.langgraph4j.CompiledGraph}, {@link org.bsc.langgraph4j.internal.node.Node}, and {@link org.bsc.langgraph4j.internal.edge.Edge}, which
 * facilitate the creation and management of state graphs.
 *
 * <p>Key classes and interfaces:</p>
 * <ul>
 *   <li>{@link org.bsc.langgraph4j.StateGraph} - Represents a state graph with nodes and edges.</li>
 *   <li>{@link org.bsc.langgraph4j.CompiledGraph} - Represents a compiled state graph ready for execution.</li>
 *   <li>{@link org.bsc.langgraph4j.internal.node.Node} - Represents a node in the graph with a unique identifier and an associated action.</li>
 *   <li>{@link org.bsc.langgraph4j.internal.edge.Edge} - Represents an edge in the graph with a source ID and a target value.</li>
 * </ul>
 *
 * <p>Utility classes:</p>
 * <ul>
 *   <li>{@link org.bsc.langgraph4j.utils.CollectionsUtils} - Provides utility methods for creating collections.</li>
 * </ul>
 *
 * <p>Exception classes:</p>
 * <ul>
 *   <li>{@link org.bsc.langgraph4j.GraphStateException} - Exception thrown when there is an error related to the state of a graph.</li>
 * </ul>
 */
package org.bsc.langgraph4j;