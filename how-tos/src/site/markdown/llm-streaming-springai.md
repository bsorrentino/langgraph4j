# How to integrate Spring AI LLM streaming in Langgraph4j

**Initialize Logger**


```java
try( var file = new java.io.FileInputStream("./logging.properties")) {
    java.util.logging.LogManager.getLogManager().readConfiguration( file );
}

var log = org.slf4j.LoggerFactory.getLogger("llm-streaming");
```

## How to use StreamingChatGenerator


```java
import org.bsc.async.AsyncGenerator;
import org.bsc.async.FlowGenerator;
import org.bsc.langgraph4j.NodeOutput;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.action.EdgeAction;
import org.bsc.langgraph4j.action.NodeAction;
import org.bsc.langgraph4j.prebuilt.MessagesState;
import org.bsc.langgraph4j.serializer.std.ObjectStreamStateSerializer;
import org.bsc.langgraph4j.spring.ai.generators.StreamingChatGenerator;
import org.bsc.langgraph4j.spring.ai.serializer.std.SpringAIStateSerializer;
import org.bsc.langgraph4j.spring.ai.tool.SpringAIToolService;
import org.bsc.langgraph4j.streaming.StreamingOutput;
import org.bsc.langgraph4j.utils.EdgeMappings;
import org.reactivestreams.FlowAdapters;
import org.reactivestreams.Publisher;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.tool.function.FunctionToolCallback;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static java.util.Optional.ofNullable;
import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

enum AiModel {

        OPENAI_GPT_4O_MINI(
                OpenAiChatModel.builder()
                        .openAiApi(OpenAiApi.builder()
                                .baseUrl("https://api.openai.com")
                                .apiKey(System.getenv("OPENAI_API_KEY"))
                                .build())
                        .defaultOptions(OpenAiChatOptions.builder()
                                .model("gpt-4o-mini")
                                .logprobs(false)
                                .temperature(0.1)
                                .build())
                        .build()),        
        OLLAMA_QWEN2_5_7B(
            OllamaChatModel.builder()
                    .ollamaApi( OllamaApi.builder().baseUrl("http://localhost:11434").build() )
                    .defaultOptions(OllamaOptions.builder()
                            .model("qwen2.5:7b")
                            .temperature(0.1)
                            .build())
                    .build());
    ;

    public final ChatModel model;

    AiModel(  ChatModel model ) {
        this.model = model;
    }
}

var chatClient = ChatClient.builder(AiModel.OLLAMA_QWEN2_5_7B.model)
        .defaultOptions(ToolCallingChatOptions.builder()
                .internalToolExecutionEnabled(false) // Disable automatic tool execution
                .build())
        .defaultSystem("You are a helpful AI Assistant answering questions." )
        .build();

var flux = chatClient.prompt()
        .messages( new UserMessage("tell me a joke"))
        .stream()
        .chatResponse()
        ;

var generator  = StreamingChatGenerator.builder()
        .startingNode("agent")
        .mapResult( response -> Map.of( "messages", response.getResult().getOutput()))
        .build(flux);        

for( var item : generator ) {
    System.out.println("Received: " + item );
}

```

    Received: StreamingOutput{node=agent, state=null, chunk=Sure}
    Received: StreamingOutput{node=agent, state=null, chunk=,}
    Received: StreamingOutput{node=agent, state=null, chunk= here}
    Received: StreamingOutput{node=agent, state=null, chunk='s}
    Received: StreamingOutput{node=agent, state=null, chunk= a}
    Received: StreamingOutput{node=agent, state=null, chunk= light}
    Received: StreamingOutput{node=agent, state=null, chunk= joke}
    Received: StreamingOutput{node=agent, state=null, chunk= for}
    Received: StreamingOutput{node=agent, state=null, chunk= you}
    Received: StreamingOutput{node=agent, state=null, chunk=:
    
    }
    Received: StreamingOutput{node=agent, state=null, chunk=Why}
    Received: StreamingOutput{node=agent, state=null, chunk= don}
    Received: StreamingOutput{node=agent, state=null, chunk='t}
    Received: StreamingOutput{node=agent, state=null, chunk= scientists}
    Received: StreamingOutput{node=agent, state=null, chunk= trust}
    Received: StreamingOutput{node=agent, state=null, chunk= atoms}
    Received: StreamingOutput{node=agent, state=null, chunk=?
    
    }
    Received: StreamingOutput{node=agent, state=null, chunk=Because}
    Received: StreamingOutput{node=agent, state=null, chunk= they}
    Received: StreamingOutput{node=agent, state=null, chunk= make}
    Received: StreamingOutput{node=agent, state=null, chunk= up}
    Received: StreamingOutput{node=agent, state=null, chunk= everything}
    Received: StreamingOutput{node=agent, state=null, chunk=!}
    Received: StreamingOutput{node=agent, state=null, chunk=}


## Use StreamingChatGenerator in Agent Executor

## Set up the agent's tools



```java
public class WeatherTool {

    @Tool( description = "Get the weather in location")
    public String execQuery(@ToolParam( description = "The query to use in your search.") String query) {
        // This is a placeholder for the actual implementation
        return "Cold, with a low of 13 degrees";
    }
}
```

## Create Agent executor 


```java
import org.bsc.langgraph4j.spring.ai.agentexecutor.AgentExecutor;
import org.bsc.langgraph4j.NodeOutput;
import org.bsc.langgraph4j.streaming.StreamingOutput;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;


var agent = AgentExecutor.builder()
                .streamingChatModel(AiModel.OPENAI_GPT_4O_MINI.model)
                .toolsFromObject( new WeatherTool() )
                .build()
                .compile();

var result = agent.stream( Map.of( "messages", new UserMessage("Weather in Napoli ?") ));

var state = result.stream()
        .peek( s -> {
                if( s instanceof StreamingOutput<?> sout ) {
                        System.out.printf( "%s: (%s)\n", sout.node(), sout.chunk());
                }
                else {
                        System.out.println(s.node());
                }
        })
        .reduce((a, b) -> b)
        .map( NodeOutput::state)
        .orElseThrow();

log.info( "result: {}", state.lastMessage()
                                .map(AssistantMessage.class::cast)
                                .map(AssistantMessage::getText)
                                .orElseThrow() );
```

    START 
    callAgent 


    __START__
    agent: ()


    executeTools 


    agent


    callAgent 


    action
    agent: ()
    agent: (The)
    agent: ( weather)
    agent: ( in)
    agent: ( Napoli)
    agent: ( is)
    agent: ( currently)
    agent: ( cold)
    agent: (,)
    agent: ( with)
    agent: ( a)
    agent: ( low)
    agent: ( of)
    agent: ( )
    agent: (13)
    agent: ( degrees)
    agent: ( Celsius)
    agent: (.)
    agent: (null)
    agent
    __END__


    result: The weather in Napoli is currently cold, with a low of 13 degrees Celsius. 

