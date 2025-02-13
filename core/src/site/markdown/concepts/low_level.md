# Low Level Conceptual Guide

## Graphs

At its core, LangGraph4j models agent workflows as graphs. You define the behavior of your agents using three key components:

1. [State](#State): A shared data structure that represents the current snapshot of your application. It is represented by an [AgentState] object.

2. [Nodes](#Nodes): A **Functional Interface** ([AsyncNodeAction])  that encode the logic of your agents. They receive the current `State` as input, perform some computation or side-effect, and return an updated `State`.

3. [Edges](#Edges): A **Functional Interface**  ([AsyncEdgeAction]) that determine which `Node` to execute next based on the current `State`. They can be conditional branches or fixed transitions.

By composing `Nodes` and `Edges`, you can create complex, looping workflows that evolve the `State` over time. The real power, though, comes from how LangGraph4j manages that `State`. 
To emphasize: `Nodes` and `Edges` are like functions - they can contain an LLM or just Java code.

In short: _nodes do the work. edges tell what to do next_.

<!-- 
LangGraph4j's underlying graph algorithm uses [message passing](https://en.wikipedia.org/wiki/Message_passing) to define a general program. When a Node completes its operation, it sends messages along one or more edges to other node(s). These recipient nodes then execute their functions, pass the resulting messages to the next set of nodes, and the process continues. Inspired by Google's [Pregel](https://research.google/pubs/pregel-a-system-for-large-scale-graph-processing/) system, the program proceeds in discrete "super-steps." 
-->
<!-- 
A super-step can be considered a single iteration over the graph nodes. Nodes that run in parallel are part of the same super-step, while nodes that run sequentially belong to separate super-steps. At the start of graph execution, all nodes begin in an `inactive` state. A node becomes `active` when it receives a new message (state) on any of its incoming edges (or "channels"). The active node then runs its function and responds with updates. At the end of each super-step, nodes with no incoming messages vote to `halt` by marking themselves as `inactive`. The graph execution terminates when all nodes are `inactive` and no messages are in transit.
 -->
### StateGraph

The [StateGraph] class is the main graph class to uses. This is parameterized by a user defined `State` object. 

<!-- 
### MessageGraph

The `MessageGraph` class is a special type of graph. The `State` of a `MessageGraph` is ONLY an array of messages. This class is rarely used except for chatbots, as most applications require the `State` to be more complex than an array of messages.
 -->
 <a id="compiling-your-graph"></a>
### Compiling your graph

To build your graph, you first define the [state](#State), you then add [nodes](#Nodes) and [edges](#Edges), and then you compile it. What exactly is compiling your graph and why is it needed?

Compiling is a pretty simple step. It provides a few basic checks on the structure of your graph (no orphaned nodes, etc). It is also where you can specify runtime args like [checkpointers](#Checkpointer) and [breakpoints](#Breakpoints). You compile your graph by just calling the `.compile` method:

```java
// compile your graph
var graph = graphBuilder.compile(...);
```

You **MUST** compile your graph before you can use it.

## State

The first thing you do when you define a graph is define the `State` of the graph. The `State` consists of the [schema of the graph](#Schema) as well as [reducer](#Reducers) functions which specify how to apply updates to the state. The schema of the `State` will be the input schema to all `Nodes` and `Edges` in the graph, and should be defined using a map of  [`Channel`] object. All `Nodes` will emit updates to the `State` which are then applied using the specified `reducer` function.

### Schema

The way to specify the schema of a graph is by defining map of [Channel] objects where each key is an item in the state.
If no [Channel] is specified for an item then it is assumed that all updates to that item should override it.

### Reducers

[Reducers][reducer] are key to understanding how updates from nodes are applied to the `State`. Each key in the `State` has its own independent reducer function. If no reducer function is explicitly specified then it is assumed that all updates to that key should override it. Let's take a look at a few examples to understand them better.

**Example A:**

```java
static class MessagesState extends AgentState {

    static Map<String, Channel<?>> SCHEMA = Map.of(
            "messages", AppenderChannel.<String>of(ArrayList::new)
    );
}

var graphBuilder = new StateGraph<>( MessagesState.SCHEMA, MessagesState::new)

```

### AppenderChannel

In the example above we specify for `messages` property a built-in channel [AppenderChannel] which use a [Reducer] implementation to accumulate values.

<a id="remove-messages"></a>
#### Remove Messages

[AppenderChannel] supports the message deletion throught its nested functional interface [RemoveIdentifier]. Inheriting such interface you can create a particular value that when will be put inside a State's property, with [AppenderChannel] schema, instrucs the [Reducer] to remove the  element that match the specified conditions in [RemoveIdentifier] throught `compareTo( element, index )` method. 

Langgraph4j provides a Built in [RemoveIdentifier] named [RemoveByHash] that allow to remove messages comparing their `hashCode`, below an example of its usage:

```java
class MessagesState extends AgentState {

    static Map<String, Channel<?>> SCHEMA = Map.of(
            "messages", AppenderChannel.<String>of(ArrayList::new)
    );

    public MessagesState(Map<String, Object> initData) {
        super(initData);
    }

    List<String> messages() {
        return this.<List<String>>value("messages").orElse( List.of() )
    }
}

var workflow = new StateGraph<>(MessagesState.SCHEMA, MessagesState::new)
        .addNode("agent_1", node_async(state -> Map.of("messages", "message1")))
        .addNode("agent_2", node_async(state -> Map.of("messages", List.of("message2", "message2.1"))))
        .addNode("agent_3", node_async(state -> 
            Map.of("messages", RemoveByHash.of("message2.1")) // this remove "message2.1" from messages values
        ))
        .addEdge("agent_1", "agent_2")
        .addEdge("agent_2", "agent_3")
        .addEdge(START, "agent_1")
        .addEdge("agent_3", END);

```

### Custom Reducer

You can also specify a custom reducer for a particular state property

**Example B:**

```java
static class MyState extends AgentState {

    static Map<String, Channel<?>> SCHEMA = Map.of(
            "property", Channel.<String>of( ( oldValue, newValue ) -> newValue.toUpperCase() )
    );
}

var graphBuilder = new StateGraph<>( MessagesState.SCHEMA, MyState::new)

```

### Serializer 

During graph execution the state needs to be serialized (mostly for cloning purpose) also for providing ability to persist the state across different executions. To do this we have provided a new streighforward implementation based on [Serializer] interface.

#### Why create a new Serialization framework ?

1. Doesn't rely on unsafe standard serialization framework.
1. Allow to implement serialization also to third-party (non serializable) classes
1. Avoid as much as possible class loading problem
1. Manage nullable value in serialization process

#### Features 

- [x] Allow to serialize using the java built-in standard binary serialization technique
- [x] Allow to plug also different serialization techniques

Currently the main class for state's serialization using built-in java stream is [ObjectStreamStateSerializer]. It is also available an abstraction allowing to plug serialization techniques text based like `JSON` and/or `YAML` that is [PlainTextStateSerializer].

<a id="seriliazer-out-of-box"></a>
#### Out of the Box

There are several provided Serializers out-of-the-box:

 class | description 
 ----- | -----
`ListSerializer` | built-in `List<Object>` serializer
`MapSerializer` | built-in `Map<String,Object>` serializer
&nbsp; |  &nbsp; 
`AiMessageSerializer` | langchain4j `AiMessage` Serializer
`ChatMesssageSerializer` | langchain4j `ChatMesssage` Serializer
`SystemMessageSerializer` | langchain4j `SystemMessage` Serializer
`UserMessageSerializer` | langchain4j `UserMessage` Serializer
`ToolExecutionRequestSerializer` | langchain4j `ToolExecutionRequest` Serializer
`ToolExecutionResultMessageSerializer` | langchain4j `ToolExecutionResultMessage` Serializer


## Nodes

<!--
In LangGraph4j, nodes are typically a **Functional Interface** ([AsyncNodeAction])  where the argument is the [state](#tate), and (optionally), the **second** positional argument is a "config", containing optional [configurable parameters](#configuration) (such as a `thread_id`).
-->
In LangGraph4j, nodes are typically a **Functional Interface** ([AsyncNodeAction])  where the argument is the [state](#State), you add these nodes to a graph using the [addNode] method:

```java
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

public class State extends AgentState {

  public State(Map<String, Object> initData) {
    super( initData  );
  }

  Optional<String> input() { return value("input"); } 
  Optional<String> results() { return value("results"); } 
 
}

AsyncNodeAction<State> myNode = node_async(state -> {
    System.out.println( "In myNode: " );
    return Map.of( results: "Hello " + state.input().orElse( "" ) );  
});

AsyncNodeAction<State> myOtherNode = node_async(state -> state);

var builder = new StateGraph( State::new )
  .addNode("myNode", myNode)
  .addNode("myOtherNode", myOtherNode)

```

Since [AsyncNodeAction] is designed to work with [CompletableFuture], you can use `node_async` static method that adapt it to a simpler syncronous scenario. 

<a id="start-node"></a>
### `START` Node

The `START` Node is a special node that represents the node sends user input to the graph. The main purpose for referencing this node is to determine which nodes should be called first.

```java
import static org.bsc.langgraph4j.StateGraph.START;

graph.addEdge(START, "nodeA");
```
<a id="end-node"></a>
### `END` Node

The `END` Node is a special node that represents a terminal node. This node is referenced when you want to denote which edges have no actions after they are done.

```java
import static org.bsc.langgraph4j.StateGraph.END;

graph.addEdge("nodeA", END);
```

## Edges

Edges define how the logic is routed and how the graph decides to stop. This is a big part of how your agents work and how different nodes communicate with each other. There are a few key types of edges:

- **Normal Edges**: 
  > Go directly from one node to the next.
- **Conditional Edges**: 
  > Call a function to determine which node(s) to go to next.
- **Entry Point**: 
  > Which node to call first when user input arrives.
- **Conditional Entry Point**: 
  > Call a function to determine which node(s) to call first when user input arrives.

<!-- 👉 PARALLEL
 A node can have MULTIPLE outgoing edges. If a node has multiple out-going edges, **all** of those destination nodes will be executed in parallel as a part of the next superstep. -->

<a id="normal-edges"></a>
### Normal Edges

If you **always** want to go from node A to node B, you can use the [addEdge](/langgraphjs/reference/classes/langgraph.StateGraph.html#addEdge) method directly.

```java
// add a normal edge
graph.addEdge("nodeA", "nodeB");
```

<a id="conditional-edges"></a>
### Conditional Edges

If you want to **optionally** route to 1 or more edges (or optionally terminate), you can use the [addConditionalEdges] method. This method accepts the name of a node and a **Functional Interface** ([AsyncEdgeAction]) that will be used as " routing function" to call after that node is executed:

```java
graph.addConditionalEdges("nodeA", routingFunction, Map.of( "first": "nodeB", "second": "nodeC" ) );
```

Similar to nodes, the `routingFunction` accept the current `state` of the graph and return a string value.

<!-- By default, the return value `routingFunction` is used as the name of the node (or an array of nodes) to send the state to next. All those nodes will be run in parallel as a part of the next superstep. -->

You must provide an object that maps the `routingFunction`'s output to the name of the next node.

<a id="entry-point"></a>
### Entry Point

The entry point is the first node(s) that are run when the graph starts. You can use the [addEdge] method from the virtual `START` node to the first node to execute to specify where to enter the graph.

```java
import static org.bsc.langgraph4j.StateGraph.START;

graph.addEdge(START, "nodeA");
```
<a id="conditional-entry-point"></a>
### Conditional Entry Point

A conditional entry point lets you start at different nodes depending on custom logic. You can use [addConditionalEdges] from the virtual `START` node to accomplish this.

```java
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.utils.CollectionsUtils.mapOf;

graph.addConditionalEdges(START, routingFunction, Map.of( "first": "nodeB", "second": "nodeC" ) );
```

You must provide an object that maps the `routingFunction`'s output to the name of the next node.

<!-- 
## `Send`

By default, `Nodes` and `Edges` are defined ahead of time and operate on the same shared state. However, there can be cases where the exact edges are not known ahead of time and/or you may want different versions of `State` to exist at the same time. A common of example of this is with `map-reduce` design patterns. In this design pattern, a first node may generate an array of objects, and you may want to apply some other node to all those objects. The number of objects may be unknown ahead of time (meaning the number of edges may not be known) and the input `State` to the downstream `Node` should be different (one for each generated object).

To support this design pattern, LangGraph4j supports returning [Send](/langgraphjs/reference/classes/langgraph.Send.html) objects from conditional edges. `Send` takes two arguments: first is the name of the node, and second is the state to pass to that node.

```typescript
const continueToJokes = (state: { subjects: string[] }) => {
  return state.subjects.map((subject) => new Send("generate_joke", { subject }));
}

graph.addConditionalEdges("nodeA", continueToJokes);
``` -->

## Checkpointer

LangGraph4j has a built-in persistence layer, implemented through [Checkpointers]. When you use a checkpointer with a graph, you can interact with the state of that graph. When you use a checkpointer with a graph, you can interact with and manage the graph's state. The checkpointer saves a _checkpoint_ of the graph state at every step, enabling several powerful capabilities:

First, checkpointers facilitate [human-in-the-loop workflows](agentic_concepts.md#human-in-the-loop) workflows by allowing humans to inspect, interrupt, and approve steps. Checkpointers are needed for these workflows as the human has to be able to view the state of a graph at any point in time, and the graph has to be to resume execution after the human has made any updates to the state.

Second, it allows for ["memory"](agentic_concepts.md#memory) between interactions. You can use checkpointers to create threads and save the state of a thread after a graph executes. In the case of repeated human interactions (like conversations) any follow up messages can be sent to that checkpoint, which will retain its memory of previous ones.

See [this guide](/langgraph4j/how-tos/langgraph4j-howtos/persistence.html) for how to add a checkpointer to your graph.

## Threads

Threads enable the checkpointing of multiple different runs, making them essential for multi-tenant chat applications and other scenarios where maintaining separate states is necessary. A thread is a unique ID assigned to a series of checkpoints saved by a checkpointer. When using a checkpointer, you must specify a `thread_id` when running the graph.

`thread_id` is simply the ID of a thread. This is always required

You must pass these when invoking the graph as part of the configurable part of the config.

```java

var config = RunnableConfig.builder()
                                  .threadId("a")
                                  .build();
graph.invoke(inputs, config);
```

See [this guide](/langgraph4j/how-tos/langgraph4j-howtos/persistence.html) for how to use threads.

<a id="checkpointer-state"></a>
## Checkpointer state

When interacting with the checkpointer state, you must specify a [thread identifier](#Threads). Each checkpoint saved by the checkpointer has two properties:

- **state**: This is the value of the state at this point in time.
- **nextNodeId**: This is the Idenfier of the node to execute next in the graph.


<a id="get-state"></a>
### Get state

You can get the state of a checkpointer by calling [graph.getState(config)]. The config should contain `thread_id`, and the state will be fetched for that thread.

<a id="get-state-history"></a>
### Get state history

You can also call [graph.getStateHistory(config)] to get a list of the history of the graph. The config should contain `thread_id`, and the state history will be fetched for that thread.

<a id="Update-state"></a>
### Update state

You can also interact with the state directly and update it using [graph.updateState(config,values,asNode)].  This takes three different components:

- `config`
- `values`
- `asNode`

**`config`**

The config should contain `thread_id` specifying which thread to update.

**`values`**

These are the values that will be used to update the state. Note that this update is treated exactly as any update from a node is treated. This means that these values will be passed to the [reducer](#Reducers) functions that are part of the state. So this does NOT automatically overwrite the state. 

**`asNode`**

The final thing you specify when calling `updateState` is `asNode`. This update will be applied as if it came from node `asNode`. If `asNode` is null, it will be set to the last node that updated the state.

<!-- 👉 AMBIGUITY  
The final thing you specify when calling `updateState` is `asNode`. This update will be applied as if it came from node `asNode`. If `asNode` is null, it will be set to the last node that updated the state, if not ambiguous.

The reason this matters is that the next steps in the graph to execute depend on the last node to have given an update, so this can be used to control which node executes next. -->

<!-- 
## Configuration

When creating a graph, you can also mark that certain parts of the graph are configurable. This is commonly done to enable easily switching between models or system prompts. This allows you to create a single "cognitive architecture" (the graph) but have multiple different instance of it.

You can then pass this configuration into the graph using the `configurable` config field.

```typescript
const config = { configurable: { llm: "anthropic" }};

await graph.invoke(inputs, config);
```

You can then access and use this configuration inside a node:

```typescript
const nodeA = (state, config) => {
  const llmType = config?.configurable?.llm;
  let llm: BaseChatModel;
  if (llmType) {
    const llm = getLlm(llmType);
  }
  ...
};
    
```

See [this guide](/langgraph4j/how-tos/langgraph4j-howtos/configuration.html) for a full breakdown on configuration 
-->

## Breakpoints

It can often be useful to set breakpoints before or after certain nodes execute. This can be used to wait for human approval before continuing. These can be set when you ["compile" a graph](#compiling-your-graph). You can set breakpoints either _before_ a node executes (using `interruptBefore`) or after a node executes (using `interruptAfter`.)

You **MUST** use a [checkpoiner](#Checkpointer) when using breakpoints. This is because your graph needs to be able to resume execution.

In order to resume execution, you can just invoke your graph with `null` as the input.

```java
// Initial run of graph
graph.invoke(inputs, config);

// Let's assume it hit a breakpoint somewhere, you can then resume by passing in None
graph.invoke(null, config);
```

See [this guide](/langgraph4j/how-tos/langgraph4j-howtos/breakpoints.html) for a full walkthrough of how to add breakpoints.

## Visualization

It's often nice to be able to visualize graphs, especially as they get more complex. LangGraph4j comes with several built-in ways to visualize graphs using diagram-as-code tools such as [PlantUML] and [Mermaid] through the [graph.getGraph] method. 

```java
// for PlantUML
GraphRepresentation result = app.getGraph(GraphRepresentation.Type.PLANTUML);

System.out.println(result.getContent());

// for Mermaid
GraphRepresentation result = app.getGraph(GraphRepresentation.Type.MERMAID);
System.out.println(result.getContent());

```

<!-- 
There are several different streaming modes that LangGraph4j supports:

- ["values"](/langgraph4j/how-tos/langgraph4j-howtos/stream-values.html): This streams the full value of the state after each step of the graph.
- ["updates](/langgraph4j/how-tos/langgraph4j-howtos/stream-updates.html): This streams the updates to the state after each step of the graph. If multiple updates are made in the same step (e.g. multiple nodes are run) then those updates are streamed separately.

In addition, you can use the [streamEvents](https://v02.api.js.langchain.com/classes/langchain_core_runnables.Runnable.html#streamEvents) method to stream back events that happen _inside_ nodes. This is useful for [streaming tokens of LLM calls](/langgraph4j/how-tos/langgraph4j-howtos/streaming-tokens-without-langchain.html). -->

[PlainTextStateSerializer]: /langgraph4j/apidocs/org/bsc/langgraph4j/serializer/plain_text/PlainTextStateSerializer.html
[ObjectStreamStateSerializer]: /langgraph4j/apidocs/org/bsc/langgraph4j/serializer/std/ObjectStreamStateSerializer.html
[RemoveByHash]: /langgraph4j/apidocs/org/bsc/langgraph4j/state/RemoveByHash.html
[RemoveIdentifier]: /langgraph4j/apidocs/org/bsc/langgraph4j/state/AppenderChannel.RemoveIdentifier.html
[Serializer]: /langgraph4j/apidocs/org/bsc/langgraph4j/serializer/Serializer.html
[Reducer]: /langgraph4j/apidocs/org/bsc/langgraph4j/state/Reducer.html
[AgentState]: /langgraph4j/apidocs/org/bsc/langgraph4j/state/AgentState.html
[StateGraph]: /langgraph4j/apidocs/org/bsc/langgraph4j/StateGraph.html
[Channel]: /langgraph4j/apidocs/org/bsc/langgraph4j/state/Channel.html
[AsyncNodeAction]: /langgraph4j/apidocs/org/bsc/langgraph4j/action/AsyncNodeAction.html
[AsyncEdgeAction]: /langgraph4j/apidocs/org/bsc/langgraph4j/action/AsyncEdgeAction.html
[AppenderChannel]: /langgraph4j/apidocs/org/bsc/langgraph4j/state/AppenderChannel.html
[addNode]: /langgraph4j/apidocs/org/bsc/langgraph4j/StateGraph.html#addNode-java.lang.String-org.bsc.langgraph4j.action.AsyncNodeAction-
[addEdge]: /langgraph4j/apidocs/org/bsc/langgraph4j/StateGraph.html#addEdge-java.lang.String-java.lang.String-
[addConditionalEdges]: /langgraph4j/apidocs/org/bsc/langgraph4j/StateGraph.html#addConditionalEdges-java.lang.String-org.bsc.langgraph4j.action.AsyncEdgeAction-java.util.Map-
[CompletableFuture]: https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html
[Checkpointers]: /langgraph4j/apidocs/org/bsc/langgraph4j/checkpoint/BaseCheckpointSaver.html
[graph.updateState(config,values,asNode)]: /langgraph4j/apidocs/org/bsc/langgraph4j/CompiledGraph.html#updateState-org.bsc.langgraph4j.RunnableConfig-java.util.Map-java.lang.String-
[graph.getStateHistory(config)]: /langgraph4j/apidocs/org/bsc/langgraph4j/CompiledGraph.html#getStateHistory-org.bsc.langgraph4j.RunnableConfig-
[graph.getState(config)]: /langgraph4j/apidocs/org/bsc/langgraph4j/CompiledGraph.html#getState-org.bsc.langgraph4j.RunnableConfig-
[PlantUML]: https://plantuml.com
[java-async-generator]: https://github.com/bsorrentino/java-async-generator
[Mermaid]: https://mermaid.js.org
[graph.getGraph]: /langgraph4j/apidocs/org/bsc/langgraph4j/CompiledGraph.html#getGraph-org.bsc.langgraph4j.GraphRepresentation.Type-java.lang.String-