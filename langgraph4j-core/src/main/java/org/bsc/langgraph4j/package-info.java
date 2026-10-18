/**
 * The {@code org.bsc.langgraph4j} package provides classes and interfaces for building
 * stateful, multi-agent applications with LLMs. It includes core components such as
 * {@link org.bsc.langgraph4j.StateGraph} and {@link org.bsc.langgraph4j.CompiledGraph}, which
 * facilitate the creation and management of state graphs.
 *
 * <p>Key classes and interfaces:</p>
 * <ul>
 *   <li>{@link org.bsc.langgraph4j.StateGraph} - Represents a state graph with nodes and edges.</li>
 *   <li>{@link org.bsc.langgraph4j.CompiledGraph} - Represents a compiled state graph ready for execution.</li>
 *   <li>{@link org.bsc.langgraph4j.CompileConfig} - Configures how a state graph is compiled (checkpointing, interrupts, ...).</li>
 *   <li>{@link org.bsc.langgraph4j.RunnableConfig} - Configures a single graph execution (thread, checkpoint, ...).</li>
 *   <li>{@link org.bsc.langgraph4j.NodeOutput} - The output produced by a node during streaming execution.</li>
 * </ul>
 *
 * <p>Related packages:</p>
 * <ul>
 *   <li>{@link org.bsc.langgraph4j.annotation} - Annotation-based definition of node and edge actions
 *       ({@link org.bsc.langgraph4j.annotation.NodeAction}, {@link EdgeCondition}).</li>
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

import org.bsc.langgraph4j.annotation.EdgeCondition;
