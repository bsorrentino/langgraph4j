/**
 * Defines the core interfaces and classes for actions that constitute the building blocks of a LangGraph4j graph.
 * Actions in this context represent the executable logic associated with graph nodes and the conditional
 * logic that determines transitions along graph edges. They operate on an {@link org.bsc.langgraph4j.state.AgentState}
 * and can optionally interact with a {@link org.bsc.langgraph4j.RunnableConfig}.
 *
 * <p>This package provides a clear separation between synchronous and asynchronous operations,
 * allowing for flexible graph construction that can accommodate various execution models.</p>
 *
 * <h2>Key Components:</h2>
 * <ul>
 *   <li>{@link org.bsc.langgraph4j.action.NodeAction}: Represents a synchronous operation
 *       performed at a node. It takes the current agent state and returns a {@link java.util.Map}
 *       representing updates to the state.</li>
 *   <li>{@link org.bsc.langgraph4j.action.NodeActionWithConfig}: Similar to {@code NodeAction},
 *       but also accepts a {@link org.bsc.langgraph4j.RunnableConfig} parameter, allowing for
 *       more configurable node behavior at runtime.</li>
 *   <li>{@link org.bsc.langgraph4j.action.EdgeAction}: Defines synchronous conditional logic
 *       for an edge. It evaluates the current agent state and returns a {@link java.lang.String}
 *       indicating the name of the next node to transition to, or a special value to end the graph.</li>
 *   <li>{@link org.bsc.langgraph4j.action.CommandAction}: Represents a synchronous action that
 *       evaluates the agent state and a {@link org.bsc.langgraph4j.RunnableConfig} to produce a
 *       {@link org.bsc.langgraph4j.action.Command}. This command encapsulates both potential state
 *       updates and the next node to transition to, offering a structured way to define node outcomes.</li>
 *   <li>{@link org.bsc.langgraph4j.action.AsyncNodeAction}: An asynchronous counterpart to
 *       {@code NodeAction}. It allows for non-blocking operations at nodes, returning a
 *       {@link java.util.concurrent.CompletableFuture} that will eventually provide the {@link java.util.Map}
 *       of state updates. This is suitable for I/O-bound tasks or long-running computations.</li>
 *   <li>{@link org.bsc.langgraph4j.action.AsyncEdgeAction}: The asynchronous version of
 *       {@code EdgeAction}. It provides a non-blocking way to determine the next path in the
 *       graph, returning a {@link java.util.concurrent.CompletableFuture} with the name of the
 *       next node or an indication to end.</li>
 * </ul>
 *
 * <p>Implementations of these interfaces are fundamental to defining the behavior and flow
 * of control within a stateful agent graph constructed using LangGraph4j.</p>
 *
 */
package org.bsc.langgraph4j.action;