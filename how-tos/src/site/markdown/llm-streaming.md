# Langchain4j LLM streaming


**Initialize Logger**


```java
try( var file = new java.io.FileInputStream("./logging.properties")) {
    var lm = java.util.logging.LogManager.getLogManager();
    lm.checkAccess(); 
    lm.readConfiguration( file );
}

var log = org.slf4j.LoggerFactory.getLogger("llm-streaming");

```

## How to use LLMStreamingGenerator


```java
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.output.Response;
import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;
import org.bsc.langgraph4j.langchain4j.generators.LLMStreamingGenerator;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.streaming.StreamingOutput;

var generator = LLMStreamingGenerator.<AiMessage,AgentState>builder()
                        .mapResult( r -> Map.of( "content", r.content() ) )
                        .build();

StreamingChatLanguageModel model = OpenAiStreamingChatModel.builder()
    .apiKey(System.getenv("OPENAI_API_KEY"))
    .modelName(GPT_4_O_MINI)
    .build();

String userMessage = "Tell me a joke";

model.generate(userMessage, generator.handler() );

for( var r : generator ) {
    log.info( "{}", r);
}
  
log.info( "RESULT: {}", generator.resultValue().orElse(null) );
  
//Thread.sleep( 1000 );
```

## Use LLMStreamGenerator in Agent

### Define State


```java
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.Channel;
import org.bsc.langgraph4j.state.AppenderChannel;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;

public class MessageState extends AgentState {

    static Map<String, Channel<?>> SCHEMA = Map.of(
            "messages", AppenderChannel.<ChatMessage>of(ArrayList::new)
    );

    public MessageState(Map<String, Object> initData) {
        super( initData  );
    }

    List<ChatMessage> messages() {
        return this.<List<ChatMessage>>value( "messages" )
                .orElseThrow( () -> new RuntimeException( "messages not found" ) );
    }

    // utility method to quick access to last message
    Optional<ChatMessage> lastMessage() {
        List<ChatMessage> messages = messages();
        return ( messages.isEmpty() ) ? 
            Optional.empty() :
            Optional.of(messages.get( messages.size() - 1 ));
    }
}
```

### Define Serializers


```java
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import org.bsc.langgraph4j.serializer.std.ObjectStreamStateSerializer;
import org.bsc.langgraph4j.langchain4j.serializer.std.ChatMesssageSerializer;
import org.bsc.langgraph4j.langchain4j.serializer.std.ToolExecutionRequestSerializer;
import org.bsc.langgraph4j.state.AgentStateFactory;

var stateSerializer = new ObjectStreamStateSerializer<MessageState>( MessageState::new );
stateSerializer.mapper()
    // Setup custom serializer for Langchain4j ToolExecutionRequest
    .register(ToolExecutionRequest.class, new ToolExecutionRequestSerializer() )
    // Setup custom serializer for Langchain4j AiMessage
    .register(ChatMessage.class, new ChatMesssageSerializer() );

```

## Set up the tools

Using [langchain4j], We will first define the tools we want to use. For this simple example, we will
use create a placeholder search engine. However, it is really easy to create
your own tools - see documentation
[here][tools] on how to do
that.

[langchain4j]: https://docs.langchain4j.dev
[tools]: https://docs.langchain4j.dev/tutorials/tools


```java
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

import java.util.Optional;

import static java.lang.String.format;

public class SearchTool {

    @Tool("Use to surf the web, fetch current information, check the weather, and retrieve other information.")
    String execQuery(@P("The query to use in your search.") String query) {

        // This is a placeholder for the actual implementation
        return "Cold, with a low of 13 degrees";
    }
}
```


```java
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.StateGraph.END;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.EdgeAction;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import org.bsc.langgraph4j.action.NodeAction;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import dev.langchain4j.service.tool.DefaultToolExecutor;
import org.bsc.langgraph4j.langchain4j.tool.ToolNode;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;


// setup streaming model
var model = OpenAiStreamingChatModel.builder()
  .apiKey( System.getenv("OPENAI_API_KEY") )
  .modelName( "gpt-4o-mini" )
  .logResponses(true)
  .temperature(0.0)
  .maxTokens(2000)
  .build();

// setup tools 
var tools = ToolNode.builder()
              .specification( new SearchTool() ) 
              .build(); 

NodeAction<MessageState> callModel = state -> {
  log.info("CallModel:\n{}", state.messages());

  var generator = LLMStreamingGenerator.<AiMessage, MessageState>builder()
          .mapResult(response -> {
              log.info("MapResult: {}", response);
              return Map.of("messages", response.content());
          })
          .startingNode("agent")
          .startingState(state)
          .build();

    model.generate(
            state.messages(),
            tools.toolSpecifications(),
            generator.handler());

    return Map.of("messages", generator);
};
            
// Route Message
EdgeAction<MessageState> routeMessage = state -> {
    log.info("routeMessage:\n{}", state.messages());

    var lastMessage = state.lastMessage()
            .orElseThrow(() -> (new IllegalStateException("last message not found!")));

    if (lastMessage instanceof AiMessage message) {
        // If tools should be called
        if (message.hasToolExecutionRequests()) return "next";
    }

    // If no tools are called, we can finish (respond to the user)
    return "exit";
};
            
// Invoke Tool
NodeAction<MessageState> invokeTool = state -> {
    log.info("invokeTool:\n{}", state.messages());

    var lastMessage = state.lastMessage()
            .orElseThrow(() -> (new IllegalStateException("last message not found!")));


    if (lastMessage instanceof AiMessage lastAiMessage) {

        var result = tools.execute(lastAiMessage.toolExecutionRequests(), null)
                .orElseThrow(() -> (new IllegalStateException("no tool found!")));

        return Map.of("messages", result);

    }

    throw new IllegalStateException("invalid last message");
};
            
// Define Graph
var workflow = new StateGraph<MessageState>(MessageState.SCHEMA, stateSerializer)
        .addNode("agent", node_async(callModel))
        .addNode("tools", node_async(invokeTool))
        .addEdge(START, "agent")
        .addConditionalEdges("agent",
                edge_async(routeMessage),
                Map.of("next", "tools", "exit", END))
        .addEdge("tools", "agent");
            

```


```java
import org.bsc.langgraph4j.streaming.StreamingOutput;

var app = workflow.compile();

for( var out : app.stream( Map.of( "messages", UserMessage.from( "what is the whether today?")) ) ) {
  if( out instanceof StreamingOutput streaming ) {
    log.info( "StreamingOutput{node={}, chunk={} }", streaming.node(), streaming.chunk() );
  }
  else {
    log.info( "{}", out );
  }

}

```

    START 
    CallModel:
    [UserMessage { name = null contents = [TextContent { text = "what is the whether today?" }] }] 
    MapResult: Response { content = AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = "call_ycvEnAG6NCrreo0WqSoxS5jJ", name = "execQuery", arguments = "{"query":"current weather"}" }] }, tokenUsage = TokenUsage { inputTokenCount = 71, outputTokenCount = 15, totalTokenCount = 86 }, finishReason = TOOL_EXECUTION, metadata = {} } 
    routeMessage:
    [AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = "call_ycvEnAG6NCrreo0WqSoxS5jJ", name = "execQuery", arguments = "{"query":"current weather"}" }] }] 
    NodeOutput{node=__START__, state={messages=[UserMessage { name = null contents = [TextContent { text = "what is the whether today?" }] }]}} 
    invokeTool:
    [AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = "call_ycvEnAG6NCrreo0WqSoxS5jJ", name = "execQuery", arguments = "{"query":"current weather"}" }] }] 
    execute: execQuery 
    NodeOutput{node=agent, state={messages=[AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = "call_ycvEnAG6NCrreo0WqSoxS5jJ", name = "execQuery", arguments = "{"query":"current weather"}" }] }]}} 
    CallModel:
    [AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = "call_ycvEnAG6NCrreo0WqSoxS5jJ", name = "execQuery", arguments = "{"query":"current weather"}" }] }, ToolExecutionResultMessage { id = "call_ycvEnAG6NCrreo0WqSoxS5jJ" toolName = "execQuery" text = "Cold, with a low of 13 degrees" }] 
    NodeOutput{node=tools, state={messages=[AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = "call_ycvEnAG6NCrreo0WqSoxS5jJ", name = "execQuery", arguments = "{"query":"current weather"}" }] }, ToolExecutionResultMessage { id = "call_ycvEnAG6NCrreo0WqSoxS5jJ" toolName = "execQuery" text = "Cold, with a low of 13 degrees" }]}} 
    StreamingOutput{node=agent, chunk= } 
    StreamingOutput{node=agent, chunk=The } 
    StreamingOutput{node=agent, chunk= current } 
    StreamingOutput{node=agent, chunk= weather } 
    StreamingOutput{node=agent, chunk= is } 
    StreamingOutput{node=agent, chunk= cold } 
    StreamingOutput{node=agent, chunk=, } 
    StreamingOutput{node=agent, chunk= with } 
    StreamingOutput{node=agent, chunk= a } 
    StreamingOutput{node=agent, chunk= low } 
    StreamingOutput{node=agent, chunk= of } 
    StreamingOutput{node=agent, chunk=  } 
    StreamingOutput{node=agent, chunk=13 } 
    StreamingOutput{node=agent, chunk= degrees } 
    StreamingOutput{node=agent, chunk=. } 
    StreamingOutput{node=agent, chunk= If } 
    StreamingOutput{node=agent, chunk= you } 
    StreamingOutput{node=agent, chunk= need } 
    StreamingOutput{node=agent, chunk= more } 
    StreamingOutput{node=agent, chunk= specific } 
    StreamingOutput{node=agent, chunk= information } 
    StreamingOutput{node=agent, chunk= or } 
    StreamingOutput{node=agent, chunk= details } 
    StreamingOutput{node=agent, chunk= about } 
    StreamingOutput{node=agent, chunk= a } 
    StreamingOutput{node=agent, chunk= particular } 
    StreamingOutput{node=agent, chunk= location } 
    StreamingOutput{node=agent, chunk=, } 
    StreamingOutput{node=agent, chunk= feel } 
    StreamingOutput{node=agent, chunk= free } 
    StreamingOutput{node=agent, chunk= to } 
    StreamingOutput{node=agent, chunk= ask } 
    MapResult: Response { content = AiMessage { text = "The current weather is cold, with a low of 13 degrees. If you need more specific information or details about a particular location, feel free to ask!" toolExecutionRequests = null }, tokenUsage = TokenUsage { inputTokenCount = 93, outputTokenCount = 33, totalTokenCount = 126 }, finishReason = STOP, metadata = {} } 
    routeMessage:
    [AiMessage { text = "The current weather is cold, with a low of 13 degrees. If you need more specific information or details about a particular location, feel free to ask!" toolExecutionRequests = null }] 
    StreamingOutput{node=agent, chunk=! } 
    NodeOutput{node=agent, state={messages=[AiMessage { text = "The current weather is cold, with a low of 13 degrees. If you need more specific information or details about a particular location, feel free to ask!" toolExecutionRequests = null }]}} 
    NodeOutput{node=__END__, state={messages=[AiMessage { text = "The current weather is cold, with a low of 13 degrees. If you need more specific information or details about a particular location, feel free to ask!" toolExecutionRequests = null }]}} 

