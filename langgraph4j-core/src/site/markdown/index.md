# 🦜🕸️ LangGraph4j Core

This is the core library for LangGraph4j.

## Concepts

Below the main concepts behind build agentic and multi-agent systems with LangGraph4j. Threse are core for understanding how to build your own agentic systems.

<!-- 
LangGraph4j for Agentic Applications

- [What does it mean to be agentic?](high_level.md#what-does-it-mean-to-be-agentic)
- [Why LangGraph4j](high_level.md#why-langgraph)
- [Deployment](high_level.md#deployment) 
-->

- [Graphs](concepts/low_level.md#Graphs)
    - [StateGraph](concepts/low_level.md#StateGraph)
    <!-- - [MessageGraph](concepts/low_level.md#messagegraph) -->
    - [Compiling Your Graph](concepts/low_level.md#compiling-your-graph)
- [State](concepts/low_level.md#State)
    - [Schema](concepts/low_level.md#Schema)
    - [Reducers](concepts/low_level.md#Reducers)
    - [AppenderChannel](concepts/low_level.md#AppenderChannel)
        - [Remove Messages](concepts/low_level.md#remove-messages)
    - [Serializer](concepts/low_level.md#Serializer)
        - [Out of the Box](concepts/low_level.md#seriliazer-out-of-box)
- [Nodes](concepts/low_level.md#Nodes)
    - [`START` node](concepts/low_level.md#start-node)
    - [`END` node](concepts/low_level.md#end-node)
- [Edges](concepts/low_level.md#Edges)
    - [Normal Edges](concepts/low_level.md#normal-edges)
    - [Conditional Edges](concepts/low_level.md#conditional-edges)
    - [Entry Point](concepts/low_level.md#entry-point)
    - [Conditional Entry Point](concepts/low_level.md#conditional-entry-point)
- [Checkpointer](concepts/low_level.md#Checkpointer)
- [Threads](concepts/low_level.md#Threads)
- [Checkpointer states](concepts/low_level.md#checkpointer-state)
    - [Get state](concepts/low_level.md#get-state)
    - [Get state history](concepts/low_level.md#get-state-history)
    - [Update state](concepts/low_level.md#update-state)
- [Breakpoints](concepts/low_level.md#Breakpoints)
- [Visualization](concepts/low_level.md#Visualization)
- [Streaming](concepts/streaming.md)
- [Subgraph](concepts/subgraph.md)

<!-- 
Common Agentic Patterns

- [Structured output](agentic_concepts.md#structured-output)
- [Tool calling](agentic_concepts.md#tool-calling)
- [Memory](agentic_concepts.md#memory)
- [Human in the loop](agentic_concepts.md#human-in-the-loop)
    - [Approval](agentic_concepts.md#approval)
    - [Wait for input](agentic_concepts.md#wait-for-input)
    - [Edit agent actions](agentic_concepts.md#edit-agent-actions)
    - [Time travel](agentic_concepts.md#time-travel)
- [Multi-agent](agentic_concepts.md#multi-agent)
- [Planning](agentic_concepts.md#planning)
- [Reflection](agentic_concepts.md#reflection)
- [Off-the-shelf ReAct Agent](agentic_concepts.md#react-agent) 
-->

***

## References

* [Langgraph.js Conceptual Guides](https://langchain-ai.github.io/langgraphjs/concepts/)