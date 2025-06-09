# Streaming

LangGraph4j is built with first class support for streaming. it uses [java-async-generator] library to help with this. Below there are the different ways to stream back outputs from a graph run

## Streaming graph outputs <span style="font-weight: normal; font-style: normal;">(_.stream()_)</span>

`.stream()` is an method for streaming back outputs from a graph run. It returns an [AsyncGenerator] on which you must iterate to fetch  the sequence of performed steps as instance of a [NodeOutput] class that bascally report the executed **node name** and the resulted **state**.

### Streaming of Streaming <span style="font-weight: normal; font-style: normal;">(embed and composition)</span>

[AsyncGenerator] supports embed (i.e. is composable), it can pause main iteration to perform a nested [AsyncGenerator] after that it resume main iteration.
Relies on this feature we can return from Node action an [AsyncGenerator] that will be embed in main one  of the graph, which result will be fetched from the same iterator given from `.stream()` making sub-streaming a seamlessy experience.

### Streaming LLM tokens <span style="font-weight: normal; font-style: normal;">(using Langchain4j)</span>

So to achieve streaming LLM tokens from an AI call using [Langchain4j] we use [StreamingChatLanguageModel], below an example:

```java
StreamingChatLanguageModel model = OpenAiStreamingChatModel.builder()
    .apiKey(System.getenv("OPENAI_API_KEY"))
    .modelName(GPT_4_O_MINI)
    .build();

model.generate("Tell me a joke";, new StreamingResponseHandler<AiMessage>() {
        public void onNext(String token) { ... }

        public void onComplete(Response<T> response) { ... }

        public void onError(Throwable error) { ... }
}  );

```

### LLMStreamingGenerator

**Langgraph4j** provides an utility class  [LLMStreamingGenerator] that convert the [StreamingResponseHandler] in an [AsyncGenerator]. Below a code snippet, a working example is in the notebook [llm-streaming])

```java
var generator = LLMStreamingGenerator.builder()
                        .mapResult( r -> Map.of( "content", r.content() ) )
                        .build();

StreamingChatLanguageModel model = OpenAiStreamingChatModel.builder()
    .apiKey(System.getenv("OPENAI_API_KEY"))
    .modelName(GPT_4_O_MINI)
    .build();

model.generate("Tell me a joke", generator.handler() );

for( var r : generator ) {
    log.info( "{}", r);
}
  
log.info( "RESULT: {}", generator.resultValue().orElse(null) );
```

When we build [LLMStreamingGenerator] we must provide a mapping function `Function<CompletionResult, Map<String,Object>>`  that will be invoked on stream completion to convert completion result in a `Map` that represent a **Partial state result** that is what **Langgrap4j** expects  as result.

### Put all together in Node Action

Now we are ready to implement a **Langgraph4j Node Action**, below a represenattiove code snippet, for a complete implementation take a look to [AgentExecutor] module. 

```java
Map<String,Object> callAgent( State state )  {

    // Mapping function
    final Function<Response<AiMessage>, Map<String,Object>> mapResult = response -> {

        if (response.finishReason() == FinishReason.TOOL_EXECUTION) {

            var toolExecutionRequests = response.content().toolExecutionRequests();
            var action = new AgentAction(toolExecutionRequests.get(0), "");

            return Map.of("agent_outcome", new AgentOutcome(action, null));

        } else {
            var result = response.content().text();
            var finish = new AgentFinish(Map.of("returnValues", result), result);

            return Map.of("agent_outcome", new AgentOutcome(null, finish));
        }
    };

    var generator = LLMStreamingGenerator.<AiMessage, State>builder()
            .mapResult(mapResult)
            .startingNode("agent") // optional: the node that require streaming 
            .startingState( state ) // optional: the state of node before streaming 
            .build();

    // call LLM in streaming mode
    streamingChatLanguageModel.generate( messages, tools, generator.handler() );        

    // return the "embed" generator
    return Map.of( "agent_outcome", generator);

}
```


[java-async-generator]: https://github.com/bsorrentino/java-async-generator
[AsyncGenerator]: https://bsorrentino.github.io/java-async-generator/apidocs/org/bsc/async/AsyncGenerator.html
[Langchain4j]: https://github.com/langchain4j/langchain4j
[StreamingChatLanguageModel]: https://docs.langchain4j.dev/apidocs/dev/langchain4j/model/chat/StreamingChatLanguageModel.html
[StreamingResponseHandler]: https://docs.langchain4j.dev/apidocs/dev/langchain4j/model/StreamingResponseHandler.html
[LLMStreamingGenerator]: /langgraph4j/apidocs/org/bsc/langgraph4j/langchain4j/generators/LLMStreamingGenerator.html
[llm-streaming]: /how-tos/llm-streaming.ipynb
[AgentExecutor]: https://github.com/bsorrentino/langgraph4j/tree/main/langchain4j/langchain4j-agent