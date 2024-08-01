
# LangGraph for Java

[![Javadoc](https://img.shields.io/badge/Javadoc-Documentation-blue)][javadocs] [![Static Badge](https://img.shields.io/badge/maven--snapshots-1.0--SNAPSHOT-blue)][snapshots] [![Maven Central](https://img.shields.io/maven-central/v/org.bsc.langgraph4j-jdk8/langgraph4j-jdk8.svg)]() 

🦜🕸️LangGraph for Java. A library for building stateful, multi-agents applications with LLMs, built for work with [langchain4j]
> It is a porting of original [LangGraph] from [LangChain AI project][langchain.ai] in Java fashion


## News

| Date         | Release | info
|--------------| --- | ---
| Jul 29, 2024 | `1.0-SNAPSHOT` | upgrade to langchain4j 0.33.0. improve **Playground Webapp** 
| Jul 19, 2024 | `1.0-SNAPSHOT` | Add support of an embed **Playground Webapp** able to run Langgrap4j flow - [issue #9](https://github.com/bsorrentino/langgraph4j/issues/9)
| Jun 21, 2024 | `1.0-SNAPSHOT` | Add support of [Mermaid] diagram generation - [issue #5](https://github.com/bsorrentino/langgraph4j/issues/5)
| Jun 19, 2024 | `1.0-SNAPSHOT` | Add [adaptive rag](adaptice-rag/README.md) sample
| Jun 10, 2024 | `1.0-SNAPSHOT` | Refactoring how generate graph representation (plantuml)
| May 20, 2024 | `1.0-SNAPSHOT` | Add "[Image To PlantUML Diagram](agents-jdk8/README.md#generate-plantuml-diagram-from-image)" sample
| May 18, 2024 | `1.0-SNAPSHOT` | Add `getGraph()` method to `CompiledGraph` to return a [PlantUML] representation of your Graph


## Quick Start 

### Adding LangGraph dependency 

#### Last stable version

**Maven**
```xml
<dependency>
    <groupId>org.bsc.langgraph4j</groupId>
    <artifactId>langgraph4j-core-jdk8</artifactId>
    <version>1.0-beta1</version>
<dependency>
```

#### Development Version 

**Maven**
```xml
<dependency>
    <groupId>org.bsc.langgraph4j</groupId>
    <artifactId>langgraph4j-core-jdk8</artifactId>
    <version>1.0-SNAPSHOT</version>
<dependency>
```

### Define the agent state

The main type of graph in `langgraph` is the `StatefulGraph`. This graph is parameterized by a state object that it passes around to each node. 
Each node then returns operations to update that state. These operations can either SET specific attributes on the state (e.g. overwrite the existing values) or ADD to the existing attribute. 
Whether to set or add is denoted by initialize the property with a `AppendableValue`. The State must inherit from `AgentState` base class (that essentially is a `Map` wrapper).

```java
public class AgentState {

   public AgentState( Map<String,Object> initData ) { ... };
   
   public final java.util.Map<String,Object> data() { ... };

   public final <T> Optional<T> value(String key) { ... };

   public final <T> AppendableValue<T> appendableValue(String key ) { ... };

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

Like default use case proposed in [LangGraph blog][langgraph.blog], We have ported [AgentExecutor] implementation from [langchain] using LangGraph4j. In the [agents](agents) project's module, you can the complete working code with tests. Feel free to checkout and use it as a reference.
Below you can find a piece of code of the `AgentExecutor` to give you an idea of how is has built in langgraph style.


```java

public static class State implements AgentState {

   public State(Map<String, Object> initData) {
      super(initData);
   }

   Optional<String> input() {
      return value("input");
   }
   Optional<AgentOutcome> agentOutcome() {
      return value("agent_outcome");
   }
   AppendableValue<IntermediateStep> intermediateSteps() {
      return appendableValue("intermediate_steps");
   }
   
}

var toolInfoList = ToolInfo.fromList( objectsWithTools );

final List<ToolSpecification> toolSpecifications = toolInfoList.stream()
        .map(ToolInfo::specification)
        .toList();

var agentRunnable = Agent.builder()
                        .chatLanguageModel(chatLanguageModel)
                        .tools( toolSpecifications )
                        .build();

var workflow = new StateGraph<>(State::new);

workflow.setEntryPoint("agent");

workflow.addNode( "agent", node_async( state ->
    runAgent(agentRunnable, state)) // see implementation in the repo code
);

workflow.addNode( "action", node_async( state ->
    executeTools(toolInfoList, state)) // see implementation in the repo code
);

workflow.addConditionalEdge(
        "agent",
        edge_async( state -> {
            if (state.agentOutcome().map(AgentOutcome::finish).isPresent()) {
                return "end";
            }
            return "continue";
        }),
        Map.of("continue", "action", "end", END)
);

workflow.addEdge("action", "agent");

var app = workflow.compile();

return  app.stream( inputs );

```

# Samples

* [Agent Executor](agent-executor/README.md)
* [Image To PlantUML Diagram](image-to-diagram/README.md)
* [Adaptive RAG](adaptive-rag/README.md)

# Playground Webapp 

It is available an **embed playground webapp** able to run a Langgraph4j workflow in visual way. 

## Maven

```xml
<dependency>
    <groupId>org.bsc.langgraph4j</groupId>
    <artifactId>langgraph4j-server-jetty</artifactId>
    <version>1.0-SNAPSHOT</version>
<dependency>
```

## Sample

### Code
```java
StateGraph<AgentState> workflow = new StateGraph<>( AgentState::new );

// define your workflow   

...

// compile workflow
CompiledGraph<AgentState> app = workflow.compile();

// connect playgroud webapp to workflow
var server = LangGraphStreamingServer.builder()
                                      .port(8080)
                                      .title("LANGGRAPH4j - TEST")
                                      .addInputStringArg("input")
                                      .build(app);
// start playground
server.start().join();

```
### Demo
![result](assets/playground-demo.gif)

# References

* [LangGraph - LangChain Blog][langgraph.blog]
* [AI Agent in Java with LangGraph4j - Bartolomeo Blog][article01]
* [Java Async Generator, a Java version of Javascript async generator][java-async-generator]

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

[javadocs]: https://bsorrentino.github.io/langgraph4j/apidocs/index.html
[snapshots]: https://oss.sonatype.org/content/repositories/snapshots/org/bsc/langgraph4j/langgraph4j-jdk8/1.0-SNAPSHOT