# Langgraph4j - Agent Executor

The "<u>Agent Executor</u>" flow involves a sequence of steps where the agent receives a query, decides on necessary actions, invokes tools, processes responses, iteratively performs tasks if needed, and finally returns a synthesized response to the user. 

This flow ensures that the agent can handle complex tasks efficiently by leveraging the capabilities of various integrated tools and the decision-making power of the language model.

## Mermaid Diagram

```mermaid
---
title: Agent Executor
---
flowchart TD
	__START__((start))
	__END__((stop))
	agent("agent")
	action("action")
	%%	condition1{"check state"}
	__START__:::__START__ --> agent:::agent
	%%	agent:::agent --> condition1:::condition1
	%%	condition1:::condition1 -->|end| __END__:::__END__
	agent:::agent -->|end| __END__:::__END__
	%%	condition1:::condition1 -->|continue| action:::action
	agent:::agent -->|continue| action:::action
	action:::action --> agent:::agent
```

## PlantUML Diagram
![diagram][agentexecutor]

***

> Go to [code](src/main/java/org/bsc/langgraph4j/agentexecutor)


[agentexecutor]: agentexecutor.puml.png



