/**
 * The {@code org.bsc.langgraph4j.annotation} package provides an annotation-based way to
 * define graph actions. Methods of an {@link org.bsc.langgraph4j.annotation.ActionCatalog}
 * implementation are collected by {@link org.bsc.langgraph4j.annotation.ActionCatalogRepository}
 * and exposed as node and conditional-edge actions that can be used to build a
 * {@link org.bsc.langgraph4j.StateGraph}.
 *
 * <p>Key classes and interfaces:</p>
 * <ul>
 *   <li>{@link org.bsc.langgraph4j.annotation.ActionCatalog} - Marker interface for classes that declare actions.</li>
 *   <li>{@link org.bsc.langgraph4j.annotation.NodeAction} - Marks a method as a node action.</li>
 *   <li>{@link org.bsc.langgraph4j.annotation.EdgeAction} - Marks a method as a conditional-edge action.</li>
 *   <li>{@link org.bsc.langgraph4j.annotation.ActionCatalogRepository} - Builds node and edge actions from an annotated catalog.</li>
 * </ul>
 *
 * <p>Supported method signatures:</p>
 * <ul>
 *   <li>Parameters: {@link org.bsc.langgraph4j.state.AgentState} and/or {@link org.bsc.langgraph4j.RunnableConfig}, in any order.</li>
 *   <li>Node action return: {@code Map}, {@code CompletableFuture<Map>} or {@code null}.</li>
 *   <li>Edge action return: {@link org.bsc.langgraph4j.action.Command}, {@code String} (target node),
 *       {@code CompletableFuture<Command>} or {@code null}.</li>
 * </ul>
 */
package org.bsc.langgraph4j.annotation;
