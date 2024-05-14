# LangGraph for Java
🚀 LangGraph for Java. A library for building stateful, multi-actor applications with LLMs, built for work with [langchain4j]
> It is a porting of original [LangGraph] from [LangChain AI project][langchain.ai] in Java fashion


## Quick Start 

### Adding LangGraph dependency 

> 👉 Currently are available only the developer SNAPSHOTs

**Maven**

**JDK8 compliant**
```xml
<dependency>
    <groupId>org.bsc.langgraph4j</groupId>
    <artifactId>langgraph4j-jdk8</artifactId>
    <version>1.0-SNAPSHOT</version>
<dependency>
```

**JDK17 compliant**
> _work in progress_


### Define the agent state

The main type of graph in `langgraph` is the `StatefulGraph`. This graph is parameterized by a state object that it passes around to each node. 
Each node then returns operations to update that state. These operations can either SET specific attributes on the state (e.g. overwrite the existing values) or ADD to the existing attribute. 
Whether to set or add is denoted by initialize the property with a `AppendableValue`. The State must inherit from `AgentState` base class (that essentially is a `Map` wrapper).

```java
public class AgentState {

   public AgentState( Map<String,Object> initData );
   
   public final java.util.Map<String,Object> data();

   public final <T> Optional<T> value(String key);

   public final <T> AppendableValue<T> appendableValue(String key );

}
```

### Define the nodes

We now need to define a few different nodes in our graph. In `langgraph`, a node is a function that accept an `AgentState` as argument. There are two main nodes we need for this:

1. **The agent**: responsible for deciding what (if any) actions to take.
1. **A function to invoke tools**: if the agent decides to take an action, this node will then execute that action.

### Define Edges

We will also need to define some edges. Some of these edges may be conditional. The reason they are conditional is that based on the output of a node, one of several paths may be taken. The path that is taken is not known until that node is run (the LLM decides).

1. **Conditional Edge**: after the agent is called, we should either:
    * If the agent said to take an action, then the function to invoke tools should be called
    * If the agent said that it was finished, then it should finish
1. **Normal Edge**: after the tools are invoked, it should always go back to the agent to decide what to do next

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

var workflow = new GraphState<>(State::new);

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
## Samples

* [Agent Executor](agents-jdk8/src/main/java/dev/langchain4j/agentexecutor)
* [Image To PlantUML Diagram](agents-jdk8/src/main/java/dev/langchain4j/image_to_diagram)

# References

* [LangGraph - LangChain Blog][langgraph.blog]

[langgraph.blog]: https://blog.langchain.dev/langgraph/
[langchain4j]: https://github.com/langchain4j/langchain4j
[langchain.ai]: https://github.com/langchain-ai
[langchain]: https://github.com/langchain-ai/langchain/
[langgraph]: https://github.com/langchain-ai/langgraph
[langchain.agents]: https://python.langchain.com/docs/modules/agents/
[AgentExecutor]: https://github.com/langchain-ai/langchain/blob/master/libs/langchain/langchain/agents/agent.py
