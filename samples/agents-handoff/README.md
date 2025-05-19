# Langgraph4j - Multi-Agent handoff

‼️ **PROJECT HAS BEEN RELOCATED TO [langgraph4j/langgraph4j-examples](https://github.com/langgraph4j/langgraph4j-examples)** ‼️
----

##  Understanding Multi-Agent Architecture 

Multi-agent systems consist of multiple interacting agents, each designed to perform specific tasks. These agents can be homogeneous or heterogeneous, functioning independently or collaboratively to achieve common goals. The essence of multi-agent architecture lies in the coordination and communication among agents, ensuring seamless execution of complex processes. 

## Defining Agents Handoff 

Agents Handoff refers to the mechanism where control and data ( context ) are transferred from one agent to another, enabling continuous and efficient task execution. This concept is pivotal in scenarios where tasks require diverse expertise or when tasks need to be distributed among multiple agents to optimize performance. 

##
How to implement Agents Handoff ?

Before to evaluate possibly solutions take a more deeper look to "function calling" feature and their role in AI model

### The Role of Function Calls in AI Models 

Function calls in AI models serve as the backbone fo agents allowing them to invoke specific functions, share data, and execute tasks collaboratively. By leveraging function calls, developers can design agents that interact dynamically, responding to changes in the environment and adapting to new information in real-time. 

The diagram below show the architecture behind a ReAct Agent

```mermaid
---
title: ReACT Agent - Function calling Anatomy
---
flowchart TD
	__START__((start))
	__END__((stop))
    subgraph AGENT
	LLM("LLM
    reasoning") 
	actions("actions dispatching
    and 
    result gathering")
	LLM e1@-->|actions execution plan| actions
	end
    action1("action 1") 
    action2("action 2") 
    actionN("action N") 
	__START__ --> LLM
	LLM -->|end| __END__
    actions -.-> LLM
    actions e2@-->|invoke| action1
    actions -->|invoke| action2
    actions -->|invoke| actionN

    action1 -.-> actions
    action2 -.-> actions
    actionN -.-> actions

e1@{ animate: true }     
e2@{ animate: true }     
```

It is interesting to note that the LLM reasoning process creates an **actions plan**, while the agent platform handles **dispatching and gathering results**.
Now, since the LLM (tools enabled) produce a well defined  **actions invocation plan** based on its input to solve the problem he is dealing with, **what about behind the action we have another Agent ?**


```mermaid
---
title: Action as Agent
---
flowchart TD
	__START__((start))
	__END__((stop))
    action1("action 1") 
    action2("action 2") 
    actionN("action N") 
	__START__ --> LLM

    subgraph action1
        A@{ shape: brace-r, label: "Action as Agent" }
        LLM_1("LLM
        reasoning")
    	actions_1("actions dispatching
        and 
        result gathering")
	    LLM_1 e1_1@-->|actions execution plan| actions_1
    end

    actions_1 -.-> LLM_1
    actions_1 -->|invoke| action1_1
    actions_1 -->|invoke| action2_1
    actions_1 e2_1@-->|invoke| actionN_1
    action1_1 -.-> actions_1
    action2_1 -.-> actions_1
    actionN_1 -.-> actions_1

    subgraph AGENT
        LLM("LLM
        reasoning") 
        actions("actions dispatching
        and 
        result gathering")
        LLM e1@-->|actions execution plan| actions
  	end

    actions -->|invoke| action2
    actions -->|invoke| actionN    
    action2 -.-> actions
    actionN -.-> actions
    actions -.-> LLM

    LLM -->|end| __END__
    
    actions e2@-->|invoke| LLM_1

    LLM_1 -.-> actions

e1@{ animate: true }     
e2@{ animate: true }     
e1_1@{ animate: true }     
e2_1@{ animate: true }     

```
and iteratively we can continue to add new agents making complex multi agents scenarios

```mermaid
---
title: Multiple Actions as Agents
---
flowchart TD
	__START__((start))
	__END__((stop))
    action1("action 1") 
    action2("action 2") 
    actionN("action N") 
	__START__ --> LLM

    subgraph action1
        A@{ shape: brace-r, label: "Action as Agent" }
        LLM_1("LLM
        reasoning")
    	actions_1("actions dispatching
        and 
        result gathering")
	    LLM_1 e1_1@-->|actions invocation plan| actions_1
    end

    actions_1 -.-> LLM_1
    actions_1 -->|invoke| action1_1
    actions_1 -->|invoke| action2_1
    actions_1 e2_1@-->|invoke| actionN_1
    action1_1 -.-> actions_1
    action2_1 -.-> actions_1
    actionN_1 -.-> actions_1

    subgraph action2
        B@{ shape: brace-r, label: "Action2 as Agent" }
        LLM_2("LLM
        reasoning")
    	actions_2("actions dispatching
        and 
        result gathering")
	    LLM_2 e1_2@-->|actions invocation plan| actions_2

    end

    actions_2 -.-> LLM_2
    actions_2 e3_2@-->|invoke| action1_2
    actions_2 -->|invoke| action2_2
    actions_2 e2_2@-->|invoke| actionN_2
    action1_2 -.-> actions_2
    action2_2 -.-> actions_2
    actionN_2 -.-> actions_2

    subgraph AGENT
        LLM("LLM
        reasoning") 
        actions("actions dispatching
        and 
        result gathering")
        LLM e1@-->|actions invocation plan| actions
        
  	end
    
    actions -->|invoke| actionN
    actionN -.-> actions
    actions -.-> LLM

    LLM -->|end| __END__
    
    actions e2@-->|invoke| LLM_1
    actions e3@-->|invoke| LLM_2

    LLM_1 -.-> actions
    LLM_2 -.-> actions

e1@{ animate: true }     
e2@{ animate: true }     
e3@{ animate: true }     
e1_1@{ animate: true }     
e2_1@{ animate: true }     
e1_2@{ animate: true }     
e2_2@{ animate: true }     
e3_2@{ animate: true }     
```

## Define Agent Roles and Capabilities using function calls 

In this scenario we can consider to defining :

* **Function description**
  > It will become the Agent role and capabilities. This is crucial to feed the LLM in order to produce a "Actions execution plan" that fit for purpose
* **Function parameters**
  > It will be the context on which the agent will operate as input for its LLM.
  > By default it will be just one parameter named 'context'