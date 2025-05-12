# Issue [#118](https://github.com/bsorrentino/langgraph4j/issues/118) by [quovadis1234](https://github.com/quovadis1234)

Verify "**How to interrupt graph while streaming node output?**" 


**Initialize Logger**


```java
try( var file = new java.io.FileInputStream("./logging.properties")) {
    java.util.logging.LogManager.getLogManager().readConfiguration( file );
}

var log = org.slf4j.LoggerFactory.getLogger("issue118");
```

## Use StreamingChatGenerator in Agent

### Define Graph State


```java
import org.bsc.langgraph4j.prebuilt.MessagesState;
import dev.langchain4j.data.message.ChatMessage;

class State extends MessagesState<ChatMessage> {
    public State( Map<String, Object> initData ) {
        super( initData );
    }
}
```

### Setup Graph


```java

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import org.bsc.langgraph4j.action.NodeAction;
import org.bsc.langgraph4j.langchain4j.generators.StreamingChatGenerator;
import org.bsc.langgraph4j.langchain4j.serializer.std.LC4jStateSerializer;
import org.bsc.langgraph4j.streaming.StreamingOutput;
import org.bsc.langgraph4j.StateGraph;
import java.util.Map;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;


var model = OllamaStreamingChatModel.builder()
        .baseUrl("http://localhost:11434")
        .temperature(0.0)
        .logRequests(true)
        .logResponses(true)
        .modelName("llama3.1:latest")
        .build();

NodeAction<State> calculationNode = state -> {

        log.trace("calculationNode: {}", state.messages());

        var generator = StreamingChatGenerator.<State>builder()
        .mapResult(response -> Map.of("messages", response.aiMessage()))
        .startingNode("calculation")
        .startingState(state)
        .build();

        var request = ChatRequest.builder()
        .messages(state.messages())
        .build();

        model.chat(request, generator.handler());

        return Map.of("_streaming_messages", generator);
};

NodeAction<State> summaryNode = state -> {
        log.trace("summaryNode: {}", state.messages());

        var lastMessage = state.lastMessage().orElseThrow();

        return Map.of();
};

var stateSerializer = new LC4jStateSerializer<State>(State::new);

// Define Graph
var workflow = new StateGraph<State>(State.SCHEMA, stateSerializer)
        .addNode("calculation", node_async(calculationNode))
        .addNode("summary", node_async(summaryNode))
        .addEdge(START, "calculation")
        .addEdge("calculation", "summary" )
        .addEdge("summary", END);



```

### Execute Graph


```java

var app = workflow.compile();

for( var out : app.stream( Map.of( "messages", UserMessage.from( "generate a UUID for me")) ) ) {
        
        if( out instanceof StreamingOutput streaming ) {
                log.info( "StreamingOutput{node={}, chunk={} }", streaming.node(), streaming.chunk() );
        }
        else {
                log.info( "{} - {}", out.node(), out.state().lastMessage().orElseThrow() );
        }
}
```

    START 
    __START__ - UserMessage { name = null contents = [TextContent { text = "generate a UUID for me" }] } 
    StreamingOutput{node=calculation, chunk=` } 
    StreamingOutput{node=calculation, chunk=4 } 
    StreamingOutput{node=calculation, chunk=c } 
    StreamingOutput{node=calculation, chunk=9 } 
    StreamingOutput{node=calculation, chunk=f } 
    StreamingOutput{node=calculation, chunk=2 } 
    StreamingOutput{node=calculation, chunk=e } 
    StreamingOutput{node=calculation, chunk=5 } 
    StreamingOutput{node=calculation, chunk=d } 
    StreamingOutput{node=calculation, chunk=- } 
    StreamingOutput{node=calculation, chunk=1 } 
    StreamingOutput{node=calculation, chunk=b } 
    StreamingOutput{node=calculation, chunk=3 } 
    StreamingOutput{node=calculation, chunk=a } 
    StreamingOutput{node=calculation, chunk=- } 
    StreamingOutput{node=calculation, chunk=4 } 
    StreamingOutput{node=calculation, chunk=c } 
    StreamingOutput{node=calculation, chunk=6 } 
    StreamingOutput{node=calculation, chunk=f } 
    StreamingOutput{node=calculation, chunk=-b } 
    StreamingOutput{node=calculation, chunk=7 } 
    StreamingOutput{node=calculation, chunk=a } 
    StreamingOutput{node=calculation, chunk=8 } 
    StreamingOutput{node=calculation, chunk=- } 
    StreamingOutput{node=calculation, chunk=0 } 
    StreamingOutput{node=calculation, chunk=e } 
    StreamingOutput{node=calculation, chunk=92 } 
    StreamingOutput{node=calculation, chunk=c } 
    StreamingOutput{node=calculation, chunk=43 } 
    StreamingOutput{node=calculation, chunk=f } 
    StreamingOutput{node=calculation, chunk=4 } 
    StreamingOutput{node=calculation, chunk=c } 
    StreamingOutput{node=calculation, chunk=55 } 
    StreamingOutput{node=calculation, chunk=` } 
    StreamingOutput{node=calculation, chunk= } 
    calculation - AiMessage { text = "`4c9f2e5d-1b3a-4c6f-b7a8-0e92c43f4c55`" toolExecutionRequests = null } 
    summary - AiMessage { text = "`4c9f2e5d-1b3a-4c6f-b7a8-0e92c43f4c55`" toolExecutionRequests = null } 
    __END__ - AiMessage { text = "`4c9f2e5d-1b3a-4c6f-b7a8-0e92c43f4c55`" toolExecutionRequests = null } 

