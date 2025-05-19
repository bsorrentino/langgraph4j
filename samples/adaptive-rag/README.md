# Langgraph4j - Adaptive RAG

‼️ **PROJECT HAS BEEN RELOCATED TO [langgraph4j/langgraph4j-examples](https://github.com/langgraph4j/langgraph4j-examples)** ‼️
----

> Java implementation of [Adaptive Rag]
## Mermaid diagram

```mermaid
---
title: Adaptive RAG
---
flowchart TD
	start((start))
	stop((stop))
	web_search("web_search")
	retrieve("retrieve")
	grade_documents("grade_documents")
	generate("generate")
	transform_query("transform_query")
	condition1{"check state"}
	condition2{"check state"}
	startcondition{"check state"}
	start --> startcondition
	startcondition -->|web_search| web_search
	startcondition -->|vectorstore| retrieve
	web_search --> generate
	retrieve --> grade_documents
	grade_documents --> condition1
	condition1 -->|transform_query| transform_query
	condition1 -->|generate| generate
	transform_query --> retrieve
	generate --> condition2
	condition2 -->|not supported| generate
	condition2 -->|not useful| transform_query
	condition2 -->|useful| stop
```

## PlantUML diagram
![diagram](AdaptiveRag.png)



[Adaptive Rag]:https://github.com/langchain-ai/langgraph/blob/main/examples/rag/langgraph_adaptive_rag.ipynb


