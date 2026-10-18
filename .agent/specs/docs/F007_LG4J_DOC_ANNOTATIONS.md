# F007 - Document the new feature: Annotations for defining action on graph nodes and its conditional Edges


## Instructions

I want document in the project the new feature  "Annotations for defining action on graph nodes and its conditional Edges" from [Issue #440](https://github.com/langgraph4j/langgraph4j/issues/440)
available from version `1.9.6` of LangGraph4j.

Take a look to implementation in module `langgraph4j-core` package [annotation](./langgraph4j-core/src/main/java/org/bsc/langgraph4j/annotation)
The main annotation classes are:
* `@NodeAction` - defines an action to be executed on a graph node.
* `@EdgeCondition` - defines a condition to be evaluated on a graph edge.

For understand how feature works take a look to [unit tests](langgraph4j-core/src/test/java/org/bsc/langgraph4j/annotation/LG4JAnnotationTest.java)

The documentation must write in the [annotation.md](./src/site/mkdocs/core/annotations.md)

I want that in documentation you explain how to use the annotations, with examples of usage and the expected behavior. Also, include any relevant information about the parameters and return values of the annotations.
It is important explains:

- All the graph related actions can be grouped in `Action Catalog(s)` that are normal classes that must inherit from `ActionCatalog` tag interface.
- the `@NodeAction` annotation converts a method within an `ActionCatalog` into an action compatible with a graph node action and in the same way `@EdgeCondition` annotation convert a ActionCatalog method into a graph edge condition.
- For the Action Catalog method annotated with `@NodeAction`, the possible signatures are:
  * `Map<String,Object> method_name( State, RunnableConfig);`
  * `CompletableFuture<Map<String,Object>> method_name( State, RunnableConfig);`
  * `Node.ActionFactory<State> method_name( CompileConfig);`
  Where `State` is the current state of the graph node and `RunnableConfig` is optional and can be omitted if not needed.
- For the Action Catalog method annotated with `@EdgeCondition`, the possible signatures are:
  * `String method_name( State, RunnableConfig);`
  * `CompletableFuture<String> method_name( State, RunnableConfig);`
  * `Command method_name( State, RunnableConfig);`
  * `CompletableFuture<Command> method_name( State, RunnableConfig);`
  Where `State` is the current state of the graph node and `RunnableConfig` is optional and can be omitted if not needed.

Highlighting that actions from ActionCatalogRepository can be added with new StateGraph methods add( Node<State> node ) and add( Edge<State> edge ) using methods node(nodeId) and edge(sourceId,actionId,mappings) or can use the classic method AddNode() and AddConditionalEdges() using the ActionCatalogRepository methods to retrieve only the action with nodeAction(nodeId), edgeAction(actionId)

Complete documentation with examples of usage and the expected behavior. Also, include any relevant information about the parameters and return values of the annotations.



