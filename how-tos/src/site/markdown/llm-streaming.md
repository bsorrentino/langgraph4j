# How to integrate Langchain4j LLM streaming in Langgraph4j


**Initialize Logger**


```java
try( var file = new java.io.FileInputStream("./logging.properties")) {
    java.util.logging.LogManager.getLogManager().readConfiguration( file );
}

var log = org.slf4j.LoggerFactory.getLogger("llm-streaming");
```

## How to use StreamingChatGenerator


```java
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.output.Response;
import org.bsc.langgraph4j.langchain4j.generators.StreamingChatGenerator;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.streaming.StreamingOutput;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;

var generator = StreamingChatGenerator.<AgentState>builder()
                        .mapResult( r -> Map.of( "content", r.aiMessage().text() ) )
                        .build();

var model = OllamaStreamingChatModel.builder()
    .baseUrl( "http://localhost:11434" )
    .temperature(0.0)
    .logRequests(true)
    .logResponses(true)
    .modelName("llama3.1:latest")
    .build();

var request = ChatRequest.builder()
        .messages( UserMessage.from("Tell me a joke") )
        .build();
model.chat(request, generator.handler() );

for( var r : generator ) {
    log.info( "{}", r);
}
  
log.info( "RESULT: {}", generator.resultValue().orElse(null) );
  
//Thread.sleep( 1000 );
```

    StreamingOutput{chunk=Here} 
    StreamingOutput{chunk='s} 
    StreamingOutput{chunk= one} 
    StreamingOutput{chunk=:
    
    } 
    StreamingOutput{chunk=What} 
    StreamingOutput{chunk= do} 
    StreamingOutput{chunk= you} 
    StreamingOutput{chunk= call} 
    StreamingOutput{chunk= a} 
    StreamingOutput{chunk= fake} 
    StreamingOutput{chunk= nood} 
    StreamingOutput{chunk=le} 
    StreamingOutput{chunk=?
    
    } 
    StreamingOutput{chunk=An} 
    StreamingOutput{chunk= imp} 
    StreamingOutput{chunk=asta} 
    StreamingOutput{chunk=!
    
    } 
    StreamingOutput{chunk=Hope} 
    StreamingOutput{chunk= that} 
    StreamingOutput{chunk= made} 
    StreamingOutput{chunk= you} 
    StreamingOutput{chunk= laugh} 
    StreamingOutput{chunk=!} 
    StreamingOutput{chunk= Do} 
    StreamingOutput{chunk= you} 
    StreamingOutput{chunk= want} 
    StreamingOutput{chunk= to} 
    StreamingOutput{chunk= hear} 
    StreamingOutput{chunk= another} 
    StreamingOutput{chunk= one} 
    StreamingOutput{chunk=?} 
    StreamingOutput{chunk=} 
    RESULT: {content=Here's one:
    
    What do you call a fake noodle?
    
    An impasta!
    
    Hope that made you laugh! Do you want to hear another one?} 


## Use StreamingChatGenerator in Agent

### Define Serializers


```java
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import org.bsc.langgraph4j.serializer.std.ObjectStreamStateSerializer;
import org.bsc.langgraph4j.langchain4j.serializer.std.ChatMesssageSerializer;
import org.bsc.langgraph4j.langchain4j.serializer.std.ToolExecutionRequestSerializer;
import org.bsc.langgraph4j.state.AgentStateFactory;
import org.bsc.langgraph4j.prebuilt.MessagesState;

var stateSerializer = new ObjectStreamStateSerializer<MessagesState<ChatMessage>>( MessagesState::new );
stateSerializer.mapper()
    // Setup custom serializer for Langchain4j ToolExecutionRequest
    .register(ToolExecutionRequest.class, new ToolExecutionRequestSerializer() )
    // Setup custom serializer for Langchain4j AiMessage
    .register(ChatMessage.class, new ChatMesssageSerializer() );

```




    SerializerMapper: 
    java.util.Map
    java.util.Collection
    dev.langchain4j.agent.tool.ToolExecutionRequest
    dev.langchain4j.data.message.ChatMessage



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
import org.bsc.langgraph4j.prebuilt.MessagesStateGraph;
import org.bsc.langgraph4j.action.EdgeAction;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import org.bsc.langgraph4j.action.NodeAction;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import dev.langchain4j.service.tool.DefaultToolExecutor;
import org.bsc.langgraph4j.langchain4j.tool.ToolNode;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.request.ChatRequestParameters;

// setup streaming model
var model = OllamaStreamingChatModel.builder()
    .baseUrl( "http://localhost:11434" )
    .temperature(0.0)
    .logRequests(true)
    .logResponses(true)
    .modelName("llama3.1:latest")
    .build();

// setup tools 
var tools = ToolNode.builder()
              .specification( new SearchTool() ) 
              .build(); 

NodeAction<MessagesState<ChatMessage>> callModel = state -> {
        log.info( "CallModel" );

        var generator = StreamingChatGenerator.<MessagesState<ChatMessage>>builder()
                .mapResult( response -> Map.of("messages", response.aiMessage()) )
                .startingNode("agent")
                .startingState(state)
                .build();

        var parameters = ChatRequestParameters.builder()
                .toolSpecifications(tools.toolSpecifications())
                .build();        
        var request = ChatRequest.builder()
                .messages( state.messages() )
                .build();
        
        model.chat( request, generator.handler() );

        return Map.of("_streaming_messages", generator);
};
            
// Route Message
EdgeAction<MessagesState<ChatMessage>> routeMessage = state -> {

        var lastMessage = state.lastMessage()
            .orElseThrow(() -> (new IllegalStateException("last message not found!")));

        log.info("routeMessage:\n{}", lastMessage );
        
        if (lastMessage instanceof AiMessage message) {
                // If tools should be called
                if (message.hasToolExecutionRequests()) { 
                        return "next";
                }
        }

        // If no tools are called, we can finish (respond to the user)
        return "exit";
};
            
// Invoke Tool
NodeAction<MessagesState<ChatMessage>> invokeTool = state -> {

    var lastMessage = state.lastMessage()
            .orElseThrow(() -> (new IllegalStateException("last message not found!")));

    log.info("invokeTool:\n{}", lastMessage );

    if (lastMessage instanceof AiMessage lastAiMessage) {

        var result = tools.execute(lastAiMessage.toolExecutionRequests(), null)
                .orElseThrow(() -> (new IllegalStateException("no tool found!")));

        return Map.of("messages", result);

    }

    throw new IllegalStateException("invalid last message");
};
            
// Define Graph
var workflow = new MessagesStateGraph<ChatMessage>(stateSerializer)
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
    CallModel 
    NodeOutput{node=__START__, state={messages=[UserMessage { name = null contents = [TextContent { text = "what is the whether today?" }] }]}} 
    StreamingOutput{node=agent, chunk=However } 
    StreamingOutput{node=agent, chunk=, } 
    StreamingOutput{node=agent, chunk= I } 
    StreamingOutput{node=agent, chunk='m } 
    StreamingOutput{node=agent, chunk= a } 
    StreamingOutput{node=agent, chunk= large } 
    StreamingOutput{node=agent, chunk= language } 
    StreamingOutput{node=agent, chunk= model } 
    StreamingOutput{node=agent, chunk=, } 
    StreamingOutput{node=agent, chunk= I } 
    StreamingOutput{node=agent, chunk= don } 
    StreamingOutput{node=agent, chunk='t } 
    StreamingOutput{node=agent, chunk= have } 
    StreamingOutput{node=agent, chunk= real } 
    StreamingOutput{node=agent, chunk=-time } 
    StreamingOutput{node=agent, chunk= access } 
    StreamingOutput{node=agent, chunk= to } 
    StreamingOutput{node=agent, chunk= current } 
    StreamingOutput{node=agent, chunk= weather } 
    StreamingOutput{node=agent, chunk= conditions } 
    StreamingOutput{node=agent, chunk=. } 
    StreamingOutput{node=agent, chunk= But } 
    StreamingOutput{node=agent, chunk= I } 
    StreamingOutput{node=agent, chunk= can } 
    StreamingOutput{node=agent, chunk= suggest } 
    StreamingOutput{node=agent, chunk= some } 
    StreamingOutput{node=agent, chunk= ways } 
    StreamingOutput{node=agent, chunk= for } 
    StreamingOutput{node=agent, chunk= you } 
    StreamingOutput{node=agent, chunk= to } 
    StreamingOutput{node=agent, chunk= find } 
    StreamingOutput{node=agent, chunk= out } 
    StreamingOutput{node=agent, chunk= the } 
    StreamingOutput{node=agent, chunk= weather } 
    StreamingOutput{node=agent, chunk= in } 
    StreamingOutput{node=agent, chunk= your } 
    StreamingOutput{node=agent, chunk= area } 
    StreamingOutput{node=agent, chunk=:
    
     } 
    StreamingOutput{node=agent, chunk=1 } 
    StreamingOutput{node=agent, chunk=. } 
    StreamingOutput{node=agent, chunk= ** } 
    StreamingOutput{node=agent, chunk=Check } 
    StreamingOutput{node=agent, chunk= online } 
    StreamingOutput{node=agent, chunk= weather } 
    StreamingOutput{node=agent, chunk= websites } 
    StreamingOutput{node=agent, chunk=**: } 
    StreamingOutput{node=agent, chunk= You } 
    StreamingOutput{node=agent, chunk= can } 
    StreamingOutput{node=agent, chunk= visit } 
    StreamingOutput{node=agent, chunk= websites } 
    StreamingOutput{node=agent, chunk= like } 
    StreamingOutput{node=agent, chunk= Acc } 
    StreamingOutput{node=agent, chunk=u } 
    StreamingOutput{node=agent, chunk=Weather } 
    StreamingOutput{node=agent, chunk=, } 
    StreamingOutput{node=agent, chunk= Weather } 
    StreamingOutput{node=agent, chunk=.com } 
    StreamingOutput{node=agent, chunk=, } 
    StreamingOutput{node=agent, chunk= or } 
    StreamingOutput{node=agent, chunk= the } 
    StreamingOutput{node=agent, chunk= National } 
    StreamingOutput{node=agent, chunk= Weather } 
    StreamingOutput{node=agent, chunk= Service } 
    StreamingOutput{node=agent, chunk= ( } 
    StreamingOutput{node=agent, chunk=N } 
    StreamingOutput{node=agent, chunk=WS } 
    StreamingOutput{node=agent, chunk=) } 
    StreamingOutput{node=agent, chunk= website } 
    StreamingOutput{node=agent, chunk= to } 
    StreamingOutput{node=agent, chunk= get } 
    StreamingOutput{node=agent, chunk= the } 
    StreamingOutput{node=agent, chunk= current } 
    StreamingOutput{node=agent, chunk= weather } 
    StreamingOutput{node=agent, chunk= conditions } 
    StreamingOutput{node=agent, chunk= and } 
    StreamingOutput{node=agent, chunk= forecast } 
    StreamingOutput{node=agent, chunk=.
     } 
    StreamingOutput{node=agent, chunk=2 } 
    StreamingOutput{node=agent, chunk=. } 
    StreamingOutput{node=agent, chunk= ** } 
    StreamingOutput{node=agent, chunk=Use } 
    StreamingOutput{node=agent, chunk= a } 
    StreamingOutput{node=agent, chunk= mobile } 
    StreamingOutput{node=agent, chunk= app } 
    StreamingOutput{node=agent, chunk=**: } 
    StreamingOutput{node=agent, chunk= Download } 
    StreamingOutput{node=agent, chunk= a } 
    StreamingOutput{node=agent, chunk= weather } 
    StreamingOutput{node=agent, chunk= app } 
    StreamingOutput{node=agent, chunk= on } 
    StreamingOutput{node=agent, chunk= your } 
    StreamingOutput{node=agent, chunk= smartphone } 
    StreamingOutput{node=agent, chunk=, } 
    StreamingOutput{node=agent, chunk= such } 
    StreamingOutput{node=agent, chunk= as } 
    StreamingOutput{node=agent, chunk= Dark } 
    StreamingOutput{node=agent, chunk= Sky } 
    StreamingOutput{node=agent, chunk= or } 
    StreamingOutput{node=agent, chunk= Weather } 
    StreamingOutput{node=agent, chunk= Underground } 
    StreamingOutput{node=agent, chunk=, } 
    StreamingOutput{node=agent, chunk= which } 
    StreamingOutput{node=agent, chunk= can } 
    StreamingOutput{node=agent, chunk= provide } 
    StreamingOutput{node=agent, chunk= you } 
    StreamingOutput{node=agent, chunk= with } 
    StreamingOutput{node=agent, chunk= real } 
    StreamingOutput{node=agent, chunk=-time } 
    StreamingOutput{node=agent, chunk= weather } 
    StreamingOutput{node=agent, chunk= updates } 
    StreamingOutput{node=agent, chunk=.
     } 
    StreamingOutput{node=agent, chunk=3 } 
    StreamingOutput{node=agent, chunk=. } 
    StreamingOutput{node=agent, chunk= ** } 
    StreamingOutput{node=agent, chunk=Check } 
    StreamingOutput{node=agent, chunk= social } 
    StreamingOutput{node=agent, chunk= media } 
    StreamingOutput{node=agent, chunk=**: } 
    StreamingOutput{node=agent, chunk= Follow } 
    StreamingOutput{node=agent, chunk= local } 
    StreamingOutput{node=agent, chunk= news } 
    StreamingOutput{node=agent, chunk= outlets } 
    StreamingOutput{node=agent, chunk= or } 
    StreamingOutput{node=agent, chunk= meteor } 
    StreamingOutput{node=agent, chunk=ologists } 
    StreamingOutput{node=agent, chunk= on } 
    StreamingOutput{node=agent, chunk= social } 
    StreamingOutput{node=agent, chunk= media } 
    StreamingOutput{node=agent, chunk= platforms } 
    StreamingOutput{node=agent, chunk= like } 
    StreamingOutput{node=agent, chunk= Twitter } 
    StreamingOutput{node=agent, chunk= or } 
    StreamingOutput{node=agent, chunk= Facebook } 
    StreamingOutput{node=agent, chunk= to } 
    StreamingOutput{node=agent, chunk= get } 
    StreamingOutput{node=agent, chunk= the } 
    StreamingOutput{node=agent, chunk= latest } 
    StreamingOutput{node=agent, chunk= weather } 
    StreamingOutput{node=agent, chunk= updates } 
    StreamingOutput{node=agent, chunk=.
    
     } 
    StreamingOutput{node=agent, chunk=If } 
    StreamingOutput{node=agent, chunk= you } 
    StreamingOutput{node=agent, chunk='d } 
    StreamingOutput{node=agent, chunk= like } 
    StreamingOutput{node=agent, chunk=, } 
    StreamingOutput{node=agent, chunk= I } 
    StreamingOutput{node=agent, chunk= can } 
    StreamingOutput{node=agent, chunk= also } 
    StreamingOutput{node=agent, chunk= suggest } 
    StreamingOutput{node=agent, chunk= some } 
    StreamingOutput{node=agent, chunk= general } 
    StreamingOutput{node=agent, chunk= questions } 
    StreamingOutput{node=agent, chunk= about } 
    StreamingOutput{node=agent, chunk= the } 
    StreamingOutput{node=agent, chunk= weather } 
    StreamingOutput{node=agent, chunk= that } 
    StreamingOutput{node=agent, chunk= might } 
    StreamingOutput{node=agent, chunk= help } 
    StreamingOutput{node=agent, chunk= me } 
    StreamingOutput{node=agent, chunk= provide } 
    StreamingOutput{node=agent, chunk= more } 
    StreamingOutput{node=agent, chunk= information } 
    StreamingOutput{node=agent, chunk=:
    
     } 
    StreamingOutput{node=agent, chunk=* } 
    StreamingOutput{node=agent, chunk= What } 
    StreamingOutput{node=agent, chunk= city } 
    StreamingOutput{node=agent, chunk= or } 
    StreamingOutput{node=agent, chunk= region } 
    StreamingOutput{node=agent, chunk= are } 
    StreamingOutput{node=agent, chunk= you } 
    StreamingOutput{node=agent, chunk= interested } 
    StreamingOutput{node=agent, chunk= in } 
    StreamingOutput{node=agent, chunk=?
     } 
    StreamingOutput{node=agent, chunk=* } 
    StreamingOutput{node=agent, chunk= Are } 
    StreamingOutput{node=agent, chunk= you } 
    StreamingOutput{node=agent, chunk= looking } 
    StreamingOutput{node=agent, chunk= for } 
    StreamingOutput{node=agent, chunk= a } 
    StreamingOutput{node=agent, chunk= specific } 
    StreamingOutput{node=agent, chunk= type } 
    StreamingOutput{node=agent, chunk= of } 
    StreamingOutput{node=agent, chunk= weather } 
    StreamingOutput{node=agent, chunk= ( } 
    StreamingOutput{node=agent, chunk=e } 
    StreamingOutput{node=agent, chunk=.g } 
    StreamingOutput{node=agent, chunk=., } 
    StreamingOutput{node=agent, chunk= sunny } 
    StreamingOutput{node=agent, chunk=, } 
    StreamingOutput{node=agent, chunk= rainy } 
    StreamingOutput{node=agent, chunk=, } 
    StreamingOutput{node=agent, chunk= hot } 
    StreamingOutput{node=agent, chunk=, } 
    StreamingOutput{node=agent, chunk= cold } 
    StreamingOutput{node=agent, chunk=)?
     } 
    StreamingOutput{node=agent, chunk=* } 
    StreamingOutput{node=agent, chunk= Do } 
    StreamingOutput{node=agent, chunk= you } 
    StreamingOutput{node=agent, chunk= have } 
    StreamingOutput{node=agent, chunk= any } 
    StreamingOutput{node=agent, chunk= specific } 
    StreamingOutput{node=agent, chunk= dates } 
    StreamingOutput{node=agent, chunk= or } 
    StreamingOutput{node=agent, chunk= times } 
    StreamingOutput{node=agent, chunk= in } 
    StreamingOutput{node=agent, chunk= mind } 
    StreamingOutput{node=agent, chunk=?
    
     } 
    StreamingOutput{node=agent, chunk=Let } 
    StreamingOutput{node=agent, chunk= me } 
    StreamingOutput{node=agent, chunk= know } 
    StreamingOutput{node=agent, chunk= if } 
    StreamingOutput{node=agent, chunk= there } 
    StreamingOutput{node=agent, chunk='s } 
    StreamingOutput{node=agent, chunk= anything } 
    StreamingOutput{node=agent, chunk= else } 
    StreamingOutput{node=agent, chunk= I } 
    StreamingOutput{node=agent, chunk= can } 
    StreamingOutput{node=agent, chunk= do } 
    StreamingOutput{node=agent, chunk= to } 
    StreamingOutput{node=agent, chunk= help } 
    StreamingOutput{node=agent, chunk=! } 
    routeMessage:
    AiMessage { text = "However, I'm a large language model, I don't have real-time access to current weather conditions. But I can suggest some ways for you to find out the weather in your area:
    
    1. **Check online weather websites**: You can visit websites like AccuWeather, Weather.com, or the National Weather Service (NWS) website to get the current weather conditions and forecast.
    2. **Use a mobile app**: Download a weather app on your smartphone, such as Dark Sky or Weather Underground, which can provide you with real-time weather updates.
    3. **Check social media**: Follow local news outlets or meteorologists on social media platforms like Twitter or Facebook to get the latest weather updates.
    
    If you'd like, I can also suggest some general questions about the weather that might help me provide more information:
    
    * What city or region are you interested in?
    * Are you looking for a specific type of weather (e.g., sunny, rainy, hot, cold)?
    * Do you have any specific dates or times in mind?
    
    Let me know if there's anything else I can do to help!" toolExecutionRequests = null } 
    StreamingOutput{node=agent, chunk= } 
    NodeOutput{node=agent, state={messages=[AiMessage { text = "However, I'm a large language model, I don't have real-time access to current weather conditions. But I can suggest some ways for you to find out the weather in your area:
    
    1. **Check online weather websites**: You can visit websites like AccuWeather, Weather.com, or the National Weather Service (NWS) website to get the current weather conditions and forecast.
    2. **Use a mobile app**: Download a weather app on your smartphone, such as Dark Sky or Weather Underground, which can provide you with real-time weather updates.
    3. **Check social media**: Follow local news outlets or meteorologists on social media platforms like Twitter or Facebook to get the latest weather updates.
    
    If you'd like, I can also suggest some general questions about the weather that might help me provide more information:
    
    * What city or region are you interested in?
    * Are you looking for a specific type of weather (e.g., sunny, rainy, hot, cold)?
    * Do you have any specific dates or times in mind?
    
    Let me know if there's anything else I can do to help!" toolExecutionRequests = null }]}} 
    NodeOutput{node=__END__, state={messages=[AiMessage { text = "However, I'm a large language model, I don't have real-time access to current weather conditions. But I can suggest some ways for you to find out the weather in your area:
    
    1. **Check online weather websites**: You can visit websites like AccuWeather, Weather.com, or the National Weather Service (NWS) website to get the current weather conditions and forecast.
    2. **Use a mobile app**: Download a weather app on your smartphone, such as Dark Sky or Weather Underground, which can provide you with real-time weather updates.
    3. **Check social media**: Follow local news outlets or meteorologists on social media platforms like Twitter or Facebook to get the latest weather updates.
    
    If you'd like, I can also suggest some general questions about the weather that might help me provide more information:
    
    * What city or region are you interested in?
    * Are you looking for a specific type of weather (e.g., sunny, rainy, hot, cold)?
    * Do you have any specific dates or times in mind?
    
    Let me know if there's anything else I can do to help!" toolExecutionRequests = null }]}} 

