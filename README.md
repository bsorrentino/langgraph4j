
# 🦜🕸️ LangGraph for Java

[![Javadoc](https://img.shields.io/badge/Javadoc-Documentation-blue)][javadocs] [![Static Badge](https://img.shields.io/badge/maven--snapshots-1.4--SNAPSHOT-blue)][snapshots] [![Maven Central](https://img.shields.io/maven-central/v/org.bsc.langgraph4j/langgraph4j-core.svg)][releases]

LangGraph for Java. A library for building stateful, multi-agents applications with LLMs, built for work with [langchain4j]
> It is a porting of original [LangGraph] from [LangChain AI project][langchain.ai] in Java fashion

## Features

- [x] StateGraph
- [x] Nodes
- [x] Edges
- [x] Conditional Edges
- [x] Entry Points
- [x] Conditional Entry Points
- [x] State
  - [x] Schema (_a series of Channels_)
    - [x] Reducer (_how apply  updates to the state attributes_)
    - [x] Default provider
    - [x] AppenderChannel (_values accumulator_)
        - [x] delete messages
- [x] Compiling graph    
- [x] Async support (_throught [CompletableFuture]_)
- [x] Streaming support (_throught [java-async-generator]_)
- [x] Checkpoints (_save and replay feature_)
- [x] Graph visualization
  - [x] [PlantUML]
  - [x] [Mermaid]
- [x] Playground (_Embeddable Webapp that plays with LangGraph4j_)
- [x] Threads (_checkpointing of multiple different runs_)
- [x] Update state (_interact with the state directly and update it_)
- [x] Breakpoints (_pause and resume feature_)
- [x] [Studio] (_Playground Webapp_)
  - [x] [Spring Boot]
  - [x] [Jetty]
- [X] Streaming response from LLM results
- [X] Child Graphs
- [X] Parallel Node Execution
    - _With some constraints_ 

## Releases

**Note: ‼️**
> From release 1.2.x the miminum supported Java version is the `Java 17` and
> the artifact `langgraph4j-core-jdk8` is replaced by `langgraph4j-core`

| Date         | Release        | info
|--------------|----------------| ---
| Feb 17, 2025 | `1.4.0` | official release

## How To - _(Java Notebook)_

* [How to add persistence ("memory") to your graph](how-tos/persistence.ipynb)
* [How to view and update past graph state](how-tos/time-travel.ipynb)
* [How to parallel branch](how-tos/parallel-branch.ipynb)
* [How to wait for user input](how-tos/wait-user-input.ipynb)
* **How to sub-graph**
  * [How to add a sub-grah in a node](how-tos/subgraph-as-nodeaction.ipynb)
  * [How to add a compiled sub-graph (by composition)](how-tos/subgraph-as-compiledgraph.ipynb)
  * [How to add a state sub-graph (by merging)](how-tos/subgraph-as-stategraph.ipynb)
* **Use Case**
  * [How to multi-agent supervisor](how-tos/multi-agent-supervisor.ipynb)

## Samples

| Project         | Integrated With        
|--------------|----------------| 
[Agent Executor][springai-agentexecutor] |  [SpringAI]
[Agent Executor][agent-executor] |  [Langchain4j][langchain4j]
[Image To PlantUML Diagram][image-to-diagram]   | [Langchain4j][langchain4j]
[Adaptive RAG][adaptive-rag] | [Langchain4j][langchain4j]

## Quick Start 

### Adding LangGraph dependency 

#### Last stable version

**Maven**
```xml
<dependency>
    <groupId>org.bsc.langgraph4j</groupId>
    <artifactId>langgraph4j-core</artifactId>
    <version>1.4.0</version>
</dependency>
```

#### Development Version 

**Maven**
```xml
<dependency>
    <groupId>org.bsc.langgraph4j</groupId>
    <artifactId>langgraph4j-core</artifactId>
    <version>1.4-SNAPSHOT</version>
</dependency>
```

### Define the agent state

The main type of graph in `langgraph` is the `StatefulGraph`. This graph is parameterized by a state object that it passes around to each node. 
Each node then returns operations to update that state. These operations can either SET specific attributes on the state (e.g. overwrite the existing values) or ADD to the existing attribute. 
Whether to set or add is described in the state's schema provided to the graph. The schema is a Map of Channels, each Channel represent an attribute in the state. If an attribute is described with an `AppendeChannel` it will be a List and each element referring the attribute will be automaically added by graph during processing. The State must inherit from `AgentState` base class (that essentially is a `Map` wrapper).

```java
public class AgentState {

   public AgentState( Map<String,Object> initData ) { ... }
   
   public final java.util.Map<String,Object> data() { ... }

   public final <T> Optional<T> value(String key) { ... }
   public final <T> T value(String key, T defaultValue ) { ... }
   public final <T> T value(String key, Supplier<T>  defaultProvider ) { ... }
    

}
```

### Define the nodes

We now need to define a few different nodes in our graph. In `langgraph`, a node is an async/sync function that accept an `AgentState` as argument and returns a (partial) state update. There are two main nodes we need for this:

1. **The agent**: responsible for deciding what (if any) actions to take.
1. **A function to invoke tools**: if the agent decides to take an action, this node will then execute that action.

```java

/**
 * Represents an asynchronous node action that operates on an agent state and returns state update.
 *
 * @param <S> the type of the agent state
 */
@FunctionalInterface
public interface AsyncNodeAction<S extends AgentState> extends Function<S, CompletableFuture<Map<String, Object>>> {

    CompletableFuture<Map<String, Object>> apply(S t);

    /**
     * Creates an asynchronous node action from a synchronous node action.
     */
    static <S extends AgentState> AsyncNodeAction<S> node_async(NodeAction<S> syncAction) { ... }
}

```

### Define Edges

We will also need to define some edges. Some of these edges may be conditional. The reason they are conditional is that based on the output of a node, one of several paths may be taken. The path that is taken is not known until that node is run (the LLM decides).

1. **Conditional Edge**: after the agent is called, we should either:
    * If the agent said to take an action, then the function to invoke tools should be called
    * If the agent said that it was finished, then it should finish
1. **Normal Edge**: after the tools are invoked, it should always go back to the agent to decide what to do next

```java

/**
 * Represents an asynchronous edge action that operates on an agent state and returns a new route.
 *
 * @param <S> the type of the agent state
 */
public interface AsyncEdgeAction<S extends AgentState> extends Function<S, CompletableFuture<String>> {

    CompletableFuture<String> apply(S t);

    /**
     * Creates an asynchronous edge action from a synchronous edge action.
     */
    static <S extends AgentState> AsyncEdgeAction<S> edge_async(EdgeAction<S> syncAction ) { ... }
}
```

### Define the graph

We can now put it all together and define the graph! (see example below)

## Integrate with LangChain4j

Like default use case proposed in [LangGraph blog][langgraph.blog], We have converted [AgentExecutor] implementation from [langchain] using LangGraph4j. In the [agent-executor][agent-executor] project's sample, there is the complete working code with tests. Feel free to checkout and use it as a reference.
Below you can find a piece of code of the `AgentExecutor` to give you an idea of how is has built in langgraph style.


```java

public static class State implements AgentState {

    // the state's (partial) schema 
    static Map<String, Channel<?>> SCHEMA = Map.of(
        "intermediate_steps", AppenderChannel.<IntermediateStep>of(ArrayList::new)
    );

    public State(Map<String, Object> initData) {
        super(initData);
    }

    Optional<String> input() {
        return value("input");
    }
    Optional<AgentOutcome> agentOutcome() {
        return value("agent_outcome");
    }
    List<IntermediateStep> intermediateSteps() {
        return this.<List<IntermediateStep>>value("intermediate_steps").orElseGet(emptyList());
    }
   
}

var toolInfo = ToolNode.builder()
                        .specification( objectsWithTools )
                        .build();

var agentRunnable = Agent.builder()
                        .chatLanguageModel( chatLanguageModel )
                        .tools( toolInfo.specifications() )
                        .build();

// Fluent Interface
var app = new StateGraph<>(State.SCHEMA,State::new)
                .addEdge(START,"agent")
                .addNode( "agent", node_async( state ->
                    runAgent(agentRunnable, state))
                )
                .addNode( "action", node_async( state ->
                    executeTools(toolInfoList, state))
                )
                .addConditionalEdges(
                        "agent",
                        edge_async( state -> {
                            if (state.agentOutcome().map(AgentOutcome::finish).isPresent()) {
                                return "end";
                            }
                            return "continue";
                        }),
                        Map.of("continue", "action", "end", END)
                )
                .addEdge("action", "agent")
                .compile();

return  app.stream( inputs );

```

# References

* [LangGraph - LangChain Blog][langgraph.blog]
* [AI Agent in Java with LangGraph4j - Bartolomeo Blog][article01]
* [Java Async Generator, a Java version of Javascript async generator][java-async-generator]


[javadocs]: https://bsorrentino.github.io/langgraph4j/apidocs/index.html
[springai-agentexecutor]: samples/springai-agentexecutor
[agent-executor]: agent-executor/
[adaptive-rag]: samples/adaptive-rag
[image-to-diagram]: samples/image-to-diagram/



[SpringAI]: https://spring.io/projects/spring-ai
[Studio]: https://bsorrentino.github.io/langgraph4j/studio/langgraph4j-studio/index.html
[CompletableFuture]: https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html
[article01]: https://bsorrentino.github.io/bsorrentino/ai/2024/05/20/langgraph-for-java.html
[langgraph.blog]: https://blog.langchain.dev/langgraph/
[langchain4j]: https://github.com/langchain4j/langchain4j
[langchain.ai]: https://github.com/langchain-ai
[langchain]: https://github.com/langchain-ai/langchain/
[langgraph]: https://github.com/langchain-ai/langgraph
[langchain.agents]: https://python.langchain.com/docs/modules/agents/
[AgentExecutor]: https://github.com/langchain-ai/langchain/blob/master/libs/langchain/langchain/agents/agent.py
[PlantUML]: https://plantuml.com
[java-async-generator]: https://github.com/bsorrentino/java-async-generator
[Mermaid]: https://mermaid.js.org

[releases]: https://central.sonatype.com/search?q=a%3Alanggraph4j-parent
