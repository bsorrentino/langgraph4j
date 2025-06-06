This guide introduces LangGraph4j’s key features in a beginner-friendly way.
These are the mainly features for LangGraph4j:

## StateGraph: Workflow as a Graph

LangGraph4j lets you define your AI workflow as a graph with:

  -	**Nodes**: Each node represents a unit of logic (e.g., calling an LLM, fetching data).
  - **Edges**: Control the flow between nodes.
  -	**Normal edges**: Move to the next step.
  -	**Conditional edges**: Choose the next step based on conditions (like an if/else).
  -	**Entry Points**: Where the graph starts.
  -	**State Schema**: Defines what data is passed and updated through the graph.

📌 Think of it like drawing a flowchart for your AI application’s logic.

----

## Async & Streaming Support

LangGraph4j uses:

  -	**CompletableFuture**: Allows non-blocking, asynchronous operations.
  -	**Java async generators**: Enable streaming responses from LLMs and other sources.

✅ Your app stays responsive and handles large outputs smoothly.

----

## Checkpoints and Breakpoints

  -	**Checkpoints**: Save the state of the graph so you can resume or replay later.
  - **Breakpoints**: Pause execution at certain points and resume when ready.

🛠️ Great for debugging or long-running workflows that need manual review or delay.

----

## Embedded Playground Web App (Studio)

LangGraph4j comes with an embeddable web UI ([Studio]) that allows you to visualize, run, and interact with your graphs in real-time. This is excellent for development and debugging.

👉 LangGraph4j [Studio]

  -	**Visualize and test your graph**
  -	**Simulate inputs**
  -	**Step through execution**

🎨 No need to write code to understand how the graph works!

----

## Graph Visualization

LangGraph4j supports two popular diagram tools:

  - [PlantUML]
  - [Mermaid]

You can export and view your workflow as a diagram.

🧭 Easily share and review your application’s logic visually.

----

## Multi-Agent & Parallel Execution

LangGraph4j supports:

  -	**Multiple agents**: Each with its own logic or behavior
  -	**Threads and sub-graphs**: Reuse workflows inside others
  -	**Parallel node execution**: Run parts of your graph at the same time

🤖 Useful for building advanced systems where multiple AI agents collaborate.

----

## Framework Integrations

LangGraph4j works well with popular Java AI tools:

  -	[Langchain4j]: Core LLM and AI integration.
  -	[Spring AI]: Build AI apps using Spring Boot.

🔌 Smooth integration into your existing Java tech stack.

----

## Visual Builder Tool

Build your LangGraph4j applications visually using the official tool:
👉 LangGraph4j Builder

🧱 No need to hand-code everything — click and build!

These are 2 examples of workflow generate from [LangGraph4j Builder]:

### Banking agent workflow

An AI Agentic workflow that provide a Banking Assistant that include the Human-in-the-Loop to confirm, if any, required payments:

![Banking agent](images/builder_diagram1.png)

### Supervisor agent workflow

A classical ReACT agent plus RAG integration developed using a controllable and  well defined workflow

![Supervisor agent](images/builder_diagram2.png)

[PlantUML]: https://plantuml.com
[Mermaid]: https://mermaid.js.org

[Jetty]: https://jetty.org
[Spring Boot]: https://spring.io/projects/spring-boot
[Quarkus]: https://quarkus.io
[Spring AI]: https://spring.io/projects/spring-ai
[Studio]: /studio
[Langchain4j]: https://github.com/langchain4j/langchain4j
[LangGraph4j Builder]: https://github.com/langgraph4j/langgraph4j-builder
