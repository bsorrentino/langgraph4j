# Use case proposed in [issue #78](https://github.com/bsorrentino/langgraph4j/issues/93) by [tansice](https://github.com/tansice)

## There is a null pointer issue in ToolExecutionRequestSerializer.


**Initialize Logger**


```java
try( var file = new java.io.FileInputStream("./logging.properties")) {
    java.util.logging.LogManager.getLogManager().readConfiguration( file );
}

var log = org.slf4j.LoggerFactory.getLogger("default");

```


```java
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import org.bsc.langgraph4j.action.AsyncEdgeAction;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.action.EdgeAction;
import org.bsc.langgraph4j.action.NodeAction;
import org.bsc.langgraph4j.langchain4j.generators.StreamingChatGenerator;
import org.bsc.langgraph4j.langchain4j.serializer.std.ChatMesssageSerializer;
import org.bsc.langgraph4j.langchain4j.serializer.std.ToolExecutionRequestSerializer;
import org.bsc.langgraph4j.langchain4j.tool.ToolNode;
import org.bsc.langgraph4j.prebuilt.MessagesState;
import org.bsc.langgraph4j.prebuilt.MessagesStateGraph;
import org.bsc.langgraph4j.serializer.std.ObjectStreamStateSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

static class SearchTool {
    @Tool("Use to surf the web, fetch current information, check the weather, and retrieve other information.")
    String execQuery(@P("The query to use in your search.") String query) {
        return "Cold, with a low of 13 degrees";
    }
}

void testToolsStreamingChat( String modelName ) throws Exception {
    // Setup streaming model
    var model = OllamaStreamingChatModel.builder()
            .baseUrl("http://localhost:11434")
            .temperature(0.5)
            .logRequests(true)
            .logResponses(true)
            .modelName( modelName )
            //.modelName("qwen2.5:7b")
            //.modelName("llama3.1:latest")
            .build();

    // Setup tools
    var tools = ToolNode.builder()
            .specification(new SearchTool())
            .build();

    // Setup serializers
    ObjectStreamStateSerializer<MessagesState<ChatMessage>> stateSerializer = new ObjectStreamStateSerializer<>(MessagesState::new);
    stateSerializer.mapper()
            .register(dev.langchain4j.agent.tool.ToolExecutionRequest.class, new ToolExecutionRequestSerializer())
            .register(ChatMessage.class, new ChatMesssageSerializer());

    // Define graph
    NodeAction<MessagesState<ChatMessage>> callModel = state -> {
        var generator = StreamingChatGenerator.<MessagesState<ChatMessage>>builder()
                .mapResult(response -> Map.of("messages", response.aiMessage()))
                .startingNode("agent")
                .startingState(state)
                .build();

        var parameters = ChatRequestParameters.builder()
                .toolSpecifications(tools.toolSpecifications())
                .build();
        var request = ChatRequest.builder()
                .messages(state.messages())
                .parameters(parameters)
                .build();

        model.chat(request, generator.handler());

        return Map.of("_streaming_messages", generator);
    };

    EdgeAction<MessagesState<ChatMessage>> routeMessage = state -> {
        var lastMessage = state.lastMessage()
                .orElseThrow(() -> new IllegalStateException("last message not found!"));

        if (lastMessage instanceof AiMessage message) {
            if (message.hasToolExecutionRequests()) {
                return "next";
            }
        }

        return "exit";
    };

    NodeAction<MessagesState<ChatMessage>> invokeTool = state -> {
        var lastMessage = state.lastMessage()
                .orElseThrow(() -> new IllegalStateException("last message not found!"));

        if (lastMessage instanceof AiMessage lastAiMessage) {
            var result = tools.execute(lastAiMessage.toolExecutionRequests(), null)
                    .orElseThrow(() -> new IllegalStateException("no tool found!"));

            return Map.of("messages", result);
        }

        throw new IllegalStateException("invalid last message");
    };

    var workflow = new MessagesStateGraph<>(stateSerializer)
            .addNode("agent", node_async(callModel))
            .addNode("tools", node_async(invokeTool))
            .addEdge(START, "agent")
            .addConditionalEdges("agent",
                    edge_async(routeMessage),
                    Map.of("next", "tools", "exit", END))
            .addEdge("tools", "agent");

    var app = workflow.compile();

    var output = app.stream(Map.of("messages", UserMessage.from("How is the weather in New York today?")));
    for (var out : output) {
        log.info("StreamingOutput: {}", out);
    }
}


```


```java
testToolsStreamingChat("qwen2.5:7b");
```

    START 
    StreamingOutput: NodeOutput{node=__START__, state={messages=[UserMessage { name = null contents = [TextContent { text = "How is the weather in New York today?" }] }]}} 
    StreamingOutput: StreamingOutput{node=agent, state={messages=[UserMessage { name = null contents = [TextContent { text = "How is the weather in New York today?" }] }]}, chunk=} 
    ToolExecutionRequest id is null! 
    StreamingOutput: StreamingOutput{node=agent, state={messages=[UserMessage { name = null contents = [TextContent { text = "How is the weather in New York today?" }] }]}, chunk=} 
    ToolExecutionRequest id is null! 
    execute: execQuery 
    ToolExecutionRequest id is null! 
    ToolExecutionResultMessage id is null! 
    StreamingOutput: NodeOutput{node=agent, state={messages=[AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = null, name = "execQuery", arguments = "{
      "query" : "weather in New York today"
    }" }] }]}} 
    ToolExecutionRequest id is null! 
    ToolExecutionResultMessage id is null! 
    StreamingOutput: NodeOutput{node=tools, state={messages=[AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = null, name = "execQuery", arguments = "{
      "query" : "weather in New York today"
    }" }] }, ToolExecutionResultMessage { id = null toolName = "execQuery" text = "Cold, with a low of 13 degrees" }]}} 
    StreamingOutput: StreamingOutput{node=agent, state={messages=[AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = null, name = "execQuery", arguments = "{
      "query" : "weather in New York today"
    }" }] }, ToolExecutionResultMessage { id = null toolName = "execQuery" text = "Cold, with a low of 13 degrees" }]}, chunk=The current weather in New York is cold, with the temperature expected to drop to a low of 13 degrees.} 
    StreamingOutput: NodeOutput{node=agent, state={messages=[AiMessage { text = "The current weather in New York is cold, with the temperature expected to drop to a low of 13 degrees." toolExecutionRequests = null }]}} 
    StreamingOutput: NodeOutput{node=__END__, state={messages=[AiMessage { text = "The current weather in New York is cold, with the temperature expected to drop to a low of 13 degrees." toolExecutionRequests = null }]}} 



```java

testToolsStreamingChat("llama3.1:latest");
```

    START 
    StreamingOutput: NodeOutput{node=__START__, state={messages=[UserMessage { name = null contents = [TextContent { text = "How is the weather in New York today?" }] }]}} 
    StreamingOutput: StreamingOutput{node=agent, state={messages=[UserMessage { name = null contents = [TextContent { text = "How is the weather in New York today?" }] }]}, chunk=} 
    ToolExecutionRequest id is null! 
    StreamingOutput: StreamingOutput{node=agent, state={messages=[UserMessage { name = null contents = [TextContent { text = "How is the weather in New York today?" }] }]}, chunk=} 
    ToolExecutionRequest id is null! 
    execute: execQuery 
    ToolExecutionRequest id is null! 
    ToolExecutionResultMessage id is null! 
    StreamingOutput: NodeOutput{node=agent, state={messages=[AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = null, name = "execQuery", arguments = "{
      "query" : "weather in New York today"
    }" }] }]}} 
    ToolExecutionRequest id is null! 
    ToolExecutionResultMessage id is null! 
    StreamingOutput: NodeOutput{node=tools, state={messages=[AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = null, name = "execQuery", arguments = "{
      "query" : "weather in New York today"
    }" }] }, ToolExecutionResultMessage { id = null toolName = "execQuery" text = "Cold, with a low of 13 degrees" }]}} 
    StreamingOutput: StreamingOutput{node=agent, state={messages=[AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = null, name = "execQuery", arguments = "{
      "query" : "weather in New York today"
    }" }] }, ToolExecutionResultMessage { id = null toolName = "execQuery" text = "Cold, with a low of 13 degrees" }]}, chunk=} 
    ToolExecutionRequest id is null! 
    StreamingOutput: StreamingOutput{node=agent, state={messages=[AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = null, name = "execQuery", arguments = "{
      "query" : "weather in New York today"
    }" }] }, ToolExecutionResultMessage { id = null toolName = "execQuery" text = "Cold, with a low of 13 degrees" }]}, chunk=} 
    ToolExecutionRequest id is null! 
    execute: execQuery 
    ToolExecutionRequest id is null! 
    ToolExecutionResultMessage id is null! 
    StreamingOutput: NodeOutput{node=agent, state={messages=[AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = null, name = "execQuery", arguments = "{
      "query" : "New York weather forecast for the next 5 days"
    }" }] }]}} 
    ToolExecutionRequest id is null! 
    ToolExecutionResultMessage id is null! 
    StreamingOutput: NodeOutput{node=tools, state={messages=[AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = null, name = "execQuery", arguments = "{
      "query" : "New York weather forecast for the next 5 days"
    }" }] }, ToolExecutionResultMessage { id = null toolName = "execQuery" text = "Cold, with a low of 13 degrees" }]}} 
    StreamingOutput: StreamingOutput{node=agent, state={messages=[AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = null, name = "execQuery", arguments = "{
      "query" : "New York weather forecast for the next 5 days"
    }" }] }, ToolExecutionResultMessage { id = null toolName = "execQuery" text = "Cold, with a low of 13 degrees" }]}, chunk=} 
    ToolExecutionRequest id is null! 
    StreamingOutput: StreamingOutput{node=agent, state={messages=[AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = null, name = "execQuery", arguments = "{
      "query" : "New York weather forecast for the next 5 days"
    }" }] }, ToolExecutionResultMessage { id = null toolName = "execQuery" text = "Cold, with a low of 13 degrees" }]}, chunk=} 
    ToolExecutionRequest id is null! 
    execute: execQuery 
    ToolExecutionRequest id is null! 
    ToolExecutionResultMessage id is null! 
    StreamingOutput: NodeOutput{node=agent, state={messages=[AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = null, name = "execQuery", arguments = "{
      "query" : "London weather forecast for the next 5 days"
    }" }] }]}} 
    ToolExecutionRequest id is null! 
    ToolExecutionResultMessage id is null! 
    StreamingOutput: NodeOutput{node=tools, state={messages=[AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = null, name = "execQuery", arguments = "{
      "query" : "London weather forecast for the next 5 days"
    }" }] }, ToolExecutionResultMessage { id = null toolName = "execQuery" text = "Cold, with a low of 13 degrees" }]}} 
    StreamingOutput: StreamingOutput{node=agent, state={messages=[AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = null, name = "execQuery", arguments = "{
      "query" : "London weather forecast for the next 5 days"
    }" }] }, ToolExecutionResultMessage { id = null toolName = "execQuery" text = "Cold, with a low of 13 degrees" }]}, chunk=} 
    ToolExecutionRequest id is null! 
    StreamingOutput: StreamingOutput{node=agent, state={messages=[AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = null, name = "execQuery", arguments = "{
      "query" : "London weather forecast for the next 5 days"
    }" }] }, ToolExecutionResultMessage { id = null toolName = "execQuery" text = "Cold, with a low of 13 degrees" }]}, chunk=} 
    ToolExecutionRequest id is null! 
    execute: getCurrencyRate 
    java.lang.IllegalStateException: no tool found! 
    java.util.concurrent.ExecutionException: java.lang.IllegalStateException: no tool found!
    	at java.base/java.util.concurrent.CompletableFuture.reportGet(CompletableFuture.java:396)
    	at java.base/java.util.concurrent.CompletableFuture.get(CompletableFuture.java:2073)
    	at org.bsc.langgraph4j.CompiledGraph$AsyncNodeGenerator.next(CompiledGraph.java:613)
    	at org.bsc.async.AsyncGenerator$WithEmbed.next(AsyncGenerator.java:101)
    	at org.bsc.async.InternalIterator.next(AsyncGenerator.java:398)
    	at REPL.$JShell$43.testToolsStreamingChat($JShell$43.java:97)
    	at REPL.$JShell$45.do_it$($JShell$45.java:14)
    	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
    	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    	at java.base/java.lang.reflect.Method.invoke(Method.java:568)
    	at org.rapaio.jupyter.kernel.core.java.RapaioExecutionControl.lambda$execute$1(RapaioExecutionControl.java:58)
    	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
    	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
    	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
    	at java.base/java.lang.Thread.run(Thread.java:833)
    Caused by: java.lang.IllegalStateException: no tool found!
    	at REPL.$JShell$43.lambda$testToolsStreamingChat$5($JShell$43.java:77)
    	at java.base/java.util.Optional.orElseThrow(Optional.java:403)
    	at REPL.$JShell$43.lambda$testToolsStreamingChat$6($JShell$43.java:77)
    	at org.bsc.langgraph4j.action.AsyncNodeAction.lambda$node_async$0(AsyncNodeAction.java:36)
    	at org.bsc.langgraph4j.action.AsyncNodeActionWithConfig.lambda$of$1(AsyncNodeActionWithConfig.java:53)
    	at org.bsc.langgraph4j.CompiledGraph$AsyncNodeGenerator.evaluateAction(CompiledGraph.java:517)
    	... 14 more
    
    StreamingOutput: NodeOutput{node=agent, state={messages=[AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = null, name = "getCurrencyRate", arguments = "{
      "amount" : "1000",
      "from_currency" : "USD",
      "to_currency" : "EUR"
    }" }] }]}} 
    ToolExecutionRequest id is null! 
    execute: getCurrencyRate 
    java.lang.IllegalStateException: no tool found! 
    java.util.concurrent.ExecutionException: java.lang.IllegalStateException: no tool found!
    	at java.base/java.util.concurrent.CompletableFuture.reportGet(CompletableFuture.java:396)
    	at java.base/java.util.concurrent.CompletableFuture.get(CompletableFuture.java:2073)
    	at org.bsc.langgraph4j.CompiledGraph$AsyncNodeGenerator.next(CompiledGraph.java:613)
    	at org.bsc.async.AsyncGenerator$WithEmbed.next(AsyncGenerator.java:101)
    	at org.bsc.async.InternalIterator.next(AsyncGenerator.java:398)
    	at REPL.$JShell$43.testToolsStreamingChat($JShell$43.java:97)
    	at REPL.$JShell$45.do_it$($JShell$45.java:14)
    	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
    	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    	at java.base/java.lang.reflect.Method.invoke(Method.java:568)
    	at org.rapaio.jupyter.kernel.core.java.RapaioExecutionControl.lambda$execute$1(RapaioExecutionControl.java:58)
    	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
    	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
    	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
    	at java.base/java.lang.Thread.run(Thread.java:833)
    Caused by: java.lang.IllegalStateException: no tool found!
    	at REPL.$JShell$43.lambda$testToolsStreamingChat$5($JShell$43.java:77)
    	at java.base/java.util.Optional.orElseThrow(Optional.java:403)
    	at REPL.$JShell$43.lambda$testToolsStreamingChat$6($JShell$43.java:77)
    	at org.bsc.langgraph4j.action.AsyncNodeAction.lambda$node_async$0(AsyncNodeAction.java:36)
    	at org.bsc.langgraph4j.action.AsyncNodeActionWithConfig.lambda$of$1(AsyncNodeActionWithConfig.java:53)
    	at org.bsc.langgraph4j.CompiledGraph$AsyncNodeGenerator.evaluateAction(CompiledGraph.java:517)
    	... 14 more
    



    java.util.concurrent.CompletionException: java.util.concurrent.ExecutionException: java.lang.IllegalStateException: no tool found!
    

       at java.base/java.util.concurrent.CompletableFuture.reportJoin(CompletableFuture.java:413)

       at java.base/java.util.concurrent.CompletableFuture.join(CompletableFuture.java:2118)

       at org.bsc.async.InternalIterator.next(AsyncGenerator.java:400)

       at .testToolsStreamingChat(#43:84)

    |    void testToolsStreamingChat( String modelName ) throws Exception {

    |        // Setup streaming model

    |        var model = OllamaStreamingChatModel.builder()

    |                .baseUrl("http://localhost:11434")

    |                .temperature(0.5)

    |                .logRequests(true)

    |                .logResponses(true)

    |                .modelName( modelName )

    |                //.modelName("qwen2.5:7b")

    |                //.modelName("llama3.1:latest")

    |                .build();

    |    

    |        // Setup tools

    |        var tools = ToolNode.builder()

    |                .specification(new SearchTool())

    |                .build();

    |    

    |        // Setup serializers

    |        ObjectStreamStateSerializer<MessagesState<ChatMessage>> stateSerializer = new ObjectStreamStateSerializer<>(MessagesState::new);

    |        stateSerializer.mapper()

    |                .register(dev.langchain4j.agent.tool.ToolExecutionRequest.class, new ToolExecutionRequestSerializer())

    |                .register(ChatMessage.class, new ChatMesssageSerializer());

    |    

    |        // Define graph

    |        NodeAction<MessagesState<ChatMessage>> callModel = state -> {

    |            var generator = StreamingChatGenerator.<MessagesState<ChatMessage>>builder()

    |                    .mapResult(response -> Map.of("messages", response.aiMessage()))

    |                    .startingNode("agent")

    |                    .startingState(state)

    |                    .build();

    |    

    |            var parameters = ChatRequestParameters.builder()

    |                    .toolSpecifications(tools.toolSpecifications())

    |                    .build();

    |            var request = ChatRequest.builder()

    |                    .messages(state.messages())

    |                    .parameters(parameters)

    |                    .build();

    |    

    |            model.chat(request, generator.handler());

    |    

    |            return Map.of("_streaming_messages", generator);

    |        };

    |    

    |        EdgeAction<MessagesState<ChatMessage>> routeMessage = state -> {

    |            var lastMessage = state.lastMessage()

    |                    .orElseThrow(() -> new IllegalStateException("last message not found!"));

    |    

    |            if (lastMessage instanceof AiMessage message) {

    |                if (message.hasToolExecutionRequests()) {

    |                    return "next";

    |                }

    |            }

    |    

    |            return "exit";

    |        };

    |    

    |        NodeAction<MessagesState<ChatMessage>> invokeTool = state -> {

    |            var lastMessage = state.lastMessage()

    |                    .orElseThrow(() -> new IllegalStateException("last message not found!"));

    |    

    |            if (lastMessage instanceof AiMessage lastAiMessage) {

    |                var result = tools.execute(lastAiMessage.toolExecutionRequests(), null)

    |                        .orElseThrow(() -> new IllegalStateException("no tool found!"));

    |    

    |                return Map.of("messages", result);

    |            }

    |    

    |            throw new IllegalStateException("invalid last message");

    |        };

    |    

    |        var workflow = new MessagesStateGraph<>(stateSerializer)

    |                .addNode("agent", node_async(callModel))

    |                .addNode("tools", node_async(invokeTool))

    |                .addEdge(START, "agent")

    |                .addConditionalEdges("agent",

    |                        edge_async(routeMessage),

    |                        Map.of("next", "tools", "exit", END))

    |                .addEdge("tools", "agent");

    |    

    |        var app = workflow.compile();

    |    

    |        var output = app.stream(Map.of("messages", UserMessage.from("How is the weather in New York today?")));

    |-->     for (var out : output) {

    |            log.info("StreamingOutput: {}", out);

    |        }

    |    }

       at .(#45:1)

    |--> testToolsStreamingChat("llama3.1:latest");

