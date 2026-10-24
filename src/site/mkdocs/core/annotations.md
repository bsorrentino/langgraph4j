# LangGraph4j - Annotated Node Actions and Edge Conditions

Available from **LangGraph4j 1.9.6**, `@NodeAction` and `@EdgeCondition` let you define graph behavior as annotated Java methods. They are provided by `langgraph4j-core` in the `org.bsc.langgraph4j.annotation` package. See [Issue #440](https://github.com/langgraph4j/langgraph4j/issues/440) for the feature proposal.

## Action Catalogs

An **Action Catalog** is an ordinary Java class that implements the `ActionCatalog` marker interface. Group related node actions and edge conditions in one catalog, or organize them into several catalogs with their own repositories.

`ActionCatalogRepository.of(catalog)` discovers the catalog's public annotated methods, including inherited public methods:

- `@NodeAction` adapts a method into a graph node action.
- `@EdgeCondition` adapts a method into a conditional-edge action.

The annotations do not build the graph automatically. Register the catalog, then explicitly add nodes, edges, and conditional routing to a `StateGraph`.

Both annotations accept these attributes:

| Attribute | Default | Meaning |
| --- | --- | --- |
| `name` | `""` | Repository lookup name. If empty or blank, the Java method name is used. |
| `description` | `""` | Optional descriptive text; it does not change execution or routing. |

Use distinct names within each repository's node actions and within its edge conditions. Give overloaded annotated methods explicit, distinct names. A node action and an edge condition can share a name because they have separate lookup maps.

The repository invokes methods on the catalog instance you supply, so you can inject dependencies through its constructor. If the instance is shared by concurrent graph runs, ensure its mutable fields and dependencies are safe for concurrent use.

## Adding Catalog Actions to a Graph

Catalog actions can be added as complete graph elements using the new `StateGraph.add(...)` overloads, or as actions supplied to the classic `addNode(...)` and `addConditionalEdges(...)` methods.

| Graph element | Add a complete element | Supply only the action |
| --- | --- | --- |
| Node | `add(repository.node(nodeId))` uses `add(Node<State> node)` | `addNode(nodeId, repository.nodeAction(actionId))` |
| Conditional edge | `add(repository.edge(sourceId, actionId, mappings))` uses `add(Edge<State> edge)` | `addConditionalEdges(sourceId, repository.edgeAction(actionId), mappings)`; see the availability note below |

`repository.node(nodeId)` returns a `Node<State>` whose ID and catalog action lookup name are both `nodeId`. `repository.edge(sourceId, actionId, mappings)` returns an `Edge<State>` containing the source node ID, the named catalog condition, and its routing mappings. Both can be passed directly to `StateGraph.add(...)`.

The following graph construction uses the `ReviewCatalog` defined in the complete example below:

```java
// Resolve complete nodes and edges from the catalog, including their graph identifiers.
var repository = ActionCatalogRepository.of(new AnnotationExample.ReviewCatalog());
var graph = new StateGraph<>(AgentState::new)
        .add(repository.node("process"))
        .add(repository.node("review"))
        .addEdge(START, "process")
        .add(repository.edge("process", "route", Map.of(
                "reviewRequired", "review",
                "done", END)))
        .addEdge("review", END)
        .compile();
```

With the classic methods, retrieve only the action and supply the graph identifiers separately. `repository.nodeAction(actionId)` returns a `Node.ActionFactory<State>`, allowing the same catalog action to be reused under different graph node IDs. The corresponding edge accessor, `repository.edgeAction(actionId)`, returns an `AsyncCommandAction<State>` for `addConditionalEdges(...)`.

**Accessor availability:** this source checkout exposes `nodeAction(actionId)` and the `edgeActions()` map, but does not yet define the singular `edgeAction(actionId)` accessor. The following form requires that accessor to be available:

```java
// Retrieve actions separately; graph node "processInput" uses catalog action "process".
var repository = ActionCatalogRepository.of(new AnnotationExample.ReviewCatalog());
var graph = new StateGraph<>(AgentState::new)
        .addNode("processInput", repository.nodeAction("process"))
        .addNode("review", repository.nodeAction("review"))
        .addEdge(START, "processInput")
        .addConditionalEdges("processInput", repository.edgeAction("route"), Map.of(
                "reviewRequired", "review",
                "done", END))
        .addEdge("review", END)
        .compile();
```

Both construction styles use the same annotated methods and routing mappings. In the classic example, `process` runs as node `processInput`, so `config.nodeId()` returns `"processInput"`. The edge's `sourceId` and mapping destinations always refer to graph node IDs, while `actionId` refers to the catalog's registered action name.

## Node Action Signatures

For execution-time methods, `State` below means `AgentState` or your graph's `AgentState` subclass.

| Return type | Parameters | Behavior |
| --- | --- | --- |
| `Map<String, Object>` | `State state, RunnableConfig config` | Returns updates to merge into the graph state. |
| `CompletableFuture<Map<String, Object>>` | `State state, RunnableConfig config` | Completes with updates to merge before routing to the next node. |

`RunnableConfig` is optional: both forms also support `(State state)`. Parameters are matched by type, so `(RunnableConfig config, State state)` is supported too. Methods needing no state can accept only `RunnableConfig` or no arguments.

- `state` is the current graph state.
- `config` is the current execution configuration.

### Compile-Time Node Actions

A node method can instead take a single `CompileConfig` parameter. It is invoked when the node action is created during graph compilation, and returns the action to execute at runtime.

| Annotated method return type | Parameters |
| --- | --- |
| `NodeActionWithConfig<State>` | `CompileConfig compileConfig` |
| `AsyncNodeActionWithConfig<State>` | `CompileConfig compileConfig` |

These action interfaces are in `org.bsc.langgraph4j.action`.

**Factory distinction:** `repository.nodeAction(name)` returns a `Node.ActionFactory<State>`. The annotated `(CompileConfig)` method itself must return `NodeActionWithConfig<State>` or `AsyncNodeActionWithConfig<State>`, not `Node.ActionFactory<State>`. The repository supplies the factory wrapper; returning a factory directly is rejected during compilation.

```java
/** Stateless catalog whose node action is created during graph compilation. */
public class CompileTimeCatalog implements ActionCatalog {
    /**
     * Creates an action that records its runtime node ID without changing the catalog.
     * @param compileConfig compilation settings supplied by the graph
     * @return a reusable action producing a nodeId state update on each invocation
     */
    @NodeAction(name = "recordNode")
    public NodeActionWithConfig<AgentState> recordNode(CompileConfig compileConfig) {
        // Read the runtime configuration when executing, rather than capturing a node ID at compilation.
        return (state, config) -> Map.of("nodeId", config.nodeId());
    }
}
```

Register it with `ActionCatalogRepository.of(new CompileTimeCatalog())` and add `repository.node("recordNode")` to the graph. Each execution writes `"recordNode"` into `nodeId`. If you reuse the factory under a different node ID with `addNode`, that runtime node ID is written instead.

## Edge Condition Signatures

| Return type | Parameters | Behavior |
| --- | --- | --- |
| `String` | `State state, RunnableConfig config` | Selects a routing key from the conditional edge's mappings. |
| `CompletableFuture<String>` | `State state, RunnableConfig config` | Completes with the selected routing key. |
| `Command` | `State state, RunnableConfig config` | Selects a routing key and can also supply state updates. |
| `CompletableFuture<Command>` | `State state, RunnableConfig config` | Completes with the routing key and optional state updates. |

As with node actions, `RunnableConfig` may be omitted, parameter order is flexible, and methods may take only the configuration or no arguments. `state` contains the source node's merged updates; `config` is the current execution configuration. Edge conditions do not accept `CompileConfig`.

A string result is adapted to `new Command(result)`. The `Command.gotoNode` value is also a **routing key**: it must be present in the mappings passed to `repository.edge(sourceId, actionId, mappings)`.

## Complete Example: Conditional Review

This example has two node actions and one edge condition in the same catalog. It uses a `Command` to record the routing decision as well as choose the next node. Save it as `AnnotationExample.java`; `main` declares `throws Exception` because graph construction and execution can fail.

```java
/** Standalone in-memory example; its catalog is stateless and reusable across runs. */
public class AnnotationExample {
    /** Supplies review actions and routing; graph topology is defined in main. */
    public static class ReviewCatalog implements ActionCatalog {
        /**
         * Records which node processed the input, leaving needsReview unchanged.
         * @param state current graph state, including the caller's input
         * @param config execution settings containing the current node ID
         * @return a processedBy update merged before evaluating the outgoing condition
         */
        @NodeAction(name = "process", description = "Record input processing")
        public Map<String, Object> process(AgentState state, RunnableConfig config) {
            return Map.of("processedBy", config.nodeId());
        }

        /**
         * Chooses review or completion and records the chosen routing key.
         * @param state current state; absent needsReview defaults to false
         * @return a command with a mapped routing key and a decision state update
         */
        @EdgeCondition(description = "Review only when requested")
        public Command route(AgentState state) {
            // Treat omitted input as a request to finish without review.
            boolean needsReview = state.<Boolean>value("needsReview").orElse(false);
            String destination = needsReview ? "reviewRequired" : "done";
            return new Command(destination, Map.of("decision", destination));
        }

        /**
         * Marks the input as reviewed without changing other state keys.
         * @param state current graph state, including the condition's decision update
         * @return an update setting reviewed to true
         */
        @NodeAction
        public Map<String, Object> review(AgentState state) {
            return Map.of("reviewed", true);
        }
    }

    /**
     * Builds and runs a graph that reviews the supplied input.
     * @param args unused command-line arguments
     * @throws Exception if graph construction or execution fails
     */
    public static void main(String[] args) throws Exception {
        // Register the catalog before resolving its node and edge action names.
        var repository = ActionCatalogRepository.of(new ReviewCatalog());

        // Map condition results to graph destinations; "done" terminates execution.
        var graph = new StateGraph<>(AgentState::new)
                .add(repository.node("process"))
                .add(repository.node("review"))
                .addEdge(START, "process")
                .add(repository.edge("process", "route", Map.of(
                        "reviewRequired", "review",
                        "done", END)))
                .addEdge("review", END)
                .compile();

        // Invoke only after compilation; edge updates reach the destination node.
        var result = graph.invoke(
                GraphInput.args(Map.of("needsReview", true)),
                RunnableConfig.empty());
        // Print the final state so the routing decision and review update are visible.
        System.out.println(result.orElseThrow().data());
    }
}
```

## Asynchronous Variants

The repository waits for returned `CompletableFuture` values before applying updates or routing. These methods can be placed in an `ActionCatalog`; the completed futures below demonstrate the return types without requiring an external service.

```java
/** Stateless asynchronous catalog; completing a future does not start a new thread. */
public class AsyncCatalog implements ActionCatalog {
    /**
     * Supplies a completed node result; a service future could be returned here instead.
     * @param state current graph state
     * @return a future containing a status update, with no background work scheduled
     */
    @NodeAction
    public CompletableFuture<Map<String, Object>> load(AgentState state) {
        return CompletableFuture.completedFuture(Map.of("status", "loaded"));
    }

    /**
     * Chooses the done routing key without adding state updates.
     * @param state state after the source node's updates have been merged
     * @return a future containing done, which must be mapped to a graph destination
     */
    @EdgeCondition
    public CompletableFuture<String> afterLoad(AgentState state) {
        return CompletableFuture.completedFuture("done");
    }
}
```

## Validation and Failures

- Annotated methods must be public so repository discovery can find them. Use one graph annotation per method.
- Unsupported execution-time parameter types are rejected when creating the repository. Supported types are your `AgentState` type and `RunnableConfig`; the compile-time node form accepts only a single `CompileConfig`.
- Explicit duplicate action names are rejected with `IllegalArgumentException`. Keep method-derived names unique too.
- Looking up an unknown action with `node`, `nodeAction`, or `edge` throws `IllegalArgumentException`.
- Unsupported results fail when the action is invoked (or during compilation for compile-time node methods). Node execution results must be maps; conditional-edge results must be strings or commands, including when wrapped in a future.
- Exceptions thrown by execution-time catalog methods and exceptional future completions propagate through graph execution. The adapter unwraps reflective invocation exceptions to expose the underlying cause.

The core tests in `langgraph4j-core/src/test/java/org/bsc/langgraph4j/annotation/LG4JAnnotationTest.java` also demonstrate metadata access, channel-based updates, compile-time actions, routing, and exception propagation.
