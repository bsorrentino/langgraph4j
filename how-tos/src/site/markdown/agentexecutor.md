# Agent Executor

**Initialize Logger**


```java
try( var file = new java.io.FileInputStream("./logging.properties")) {
    java.util.logging.LogManager.getLogManager().readConfiguration( file );
}

var log = org.slf4j.LoggerFactory.getLogger("AgentExecutor");

```

Create Tools


```java
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

import java.util.Optional;

import static java.lang.String.format;

public class TestTool {
    private String lastResult;

    Optional<String> lastResult() {
        return Optional.ofNullable(lastResult);
    }

    @Tool("tool for test AI agent executor")
    String execTest(@P("test message") String message) {

        lastResult = format( "test tool executed: %s", message);
        return lastResult;
    }
}

```


```java
import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import org.bsc.langgraph4j.CompileConfig;
import org.bsc.langgraph4j.RunnableConfig;
import org.bsc.langgraph4j.checkpoint.BaseCheckpointSaver;
import org.bsc.langgraph4j.checkpoint.MemorySaver;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.serializer.StateSerializer;

import org.bsc.langgraph4j.agentexecutor.AgentExecutor;

import dev.langchain4j.model.openai.OpenAiChatModel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



var chatModel = OpenAiChatModel.builder()
    .apiKey( System.getenv("OPENAI_API_KEY") )
    //.modelName( "gpt-3.5-turbo-0125" )
    .modelName( "gpt-4o-mini" )
    .logResponses(true)
    .maxRetries(2)
    .temperature(0.0)
    .maxTokens(2000)
    .build();

var stateGraph = AgentExecutor.builder()
        .chatModel(chatModel)
        .objectsWithTools(List.of(new TestTool()))
        .build();



```

### Test 1 
Update State replacing the 'input'


```java
import dev.langchain4j.data.message.UserMessage;

var saver = new MemorySaver();

CompileConfig compileConfig = CompileConfig.builder()
                .checkpointSaver( saver )
                .build();

var graph = stateGraph.compile( compileConfig );

var config = RunnableConfig.builder()
                .threadId("test1")
                .build();    
                
var iterator = graph.streamSnapshots( Map.of( "messages", UserMessage.from("perform test once")), config );  

for( var step : iterator ) {
    log.info( "STEP: {}", step );
}


```

    START 
    callAgent 
    STEP: StateSnapshot{node=__START__, state={messages=[UserMessage { name = null contents = [TextContent { text = "perform test once" }] }]}, config=RunnableConfig(threadId=test1, checkPointId=664c9ede-a607-4502-9dc3-c6b26a0da3df, nextNode=agent, streamMode=SNAPSHOTS)} 
    executeTools 
    execute: execTest 
    STEP: StateSnapshot{node=agent, state={messages=[UserMessage { name = null contents = [TextContent { text = "perform test once" }] }, AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = "call_906ZmZ90zK4DaPkZNK7D3WlH", name = "execTest", arguments = "{"message":"perform test once"}" }] }]}, config=RunnableConfig(threadId=test1, checkPointId=36dde250-4ae4-4ed5-84a8-054cbbeaa3db, nextNode=action, streamMode=SNAPSHOTS)} 
    callAgent 
    STEP: StateSnapshot{node=action, state={messages=[UserMessage { name = null contents = [TextContent { text = "perform test once" }] }, AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = "call_906ZmZ90zK4DaPkZNK7D3WlH", name = "execTest", arguments = "{"message":"perform test once"}" }] }, ToolExecutionResultMessage { id = "call_906ZmZ90zK4DaPkZNK7D3WlH" toolName = "execTest" text = "test tool executed: perform test once" }]}, config=RunnableConfig(threadId=test1, checkPointId=b6638fce-d14b-40ba-a761-627fa8224130, nextNode=agent, streamMode=SNAPSHOTS)} 
    STEP: StateSnapshot{node=agent, state={agent_response=The test has been executed successfully with the message: "perform test once." How can I assist you further?, messages=[UserMessage { name = null contents = [TextContent { text = "perform test once" }] }, AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = "call_906ZmZ90zK4DaPkZNK7D3WlH", name = "execTest", arguments = "{"message":"perform test once"}" }] }, ToolExecutionResultMessage { id = "call_906ZmZ90zK4DaPkZNK7D3WlH" toolName = "execTest" text = "test tool executed: perform test once" }]}, config=RunnableConfig(threadId=test1, checkPointId=5039907e-6136-435f-a98d-ebae76ef6bb6, nextNode=__END__, streamMode=SNAPSHOTS)} 
    STEP: NodeOutput{node=__END__, state={agent_response=The test has been executed successfully with the message: "perform test once." How can I assist you further?, messages=[UserMessage { name = null contents = [TextContent { text = "perform test once" }] }, AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = "call_906ZmZ90zK4DaPkZNK7D3WlH", name = "execTest", arguments = "{"message":"perform test once"}" }] }, ToolExecutionResultMessage { id = "call_906ZmZ90zK4DaPkZNK7D3WlH" toolName = "execTest" text = "test tool executed: perform test once" }]}} 



```java
import org.bsc.langgraph4j.state.RemoveByHash;

var history = graph.getStateHistory(config).stream().collect( Collectors.toList() );

for( var snapshot : history ) {
    log.info( "{}", snapshot );
}


// var state2 =  history.get(2);

// var updatedConfig = graph.updateState( state2.config(), 
//     Map.of( "messages", UserMessage.from("perform test twice")), null);

// var iterator = graph.streamSnapshots( null, updatedConfig );  


// for( var step : iterator ) {
//     log.info( "STEP:\n {}", step );
// }

```

    StateSnapshot{node=agent, state={agent_response=The test has been executed successfully with the message: "perform test once." How can I assist you further?, messages=[UserMessage { name = null contents = [TextContent { text = "perform test once" }] }, AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = "call_906ZmZ90zK4DaPkZNK7D3WlH", name = "execTest", arguments = "{"message":"perform test once"}" }] }, ToolExecutionResultMessage { id = "call_906ZmZ90zK4DaPkZNK7D3WlH" toolName = "execTest" text = "test tool executed: perform test once" }]}, config=RunnableConfig(threadId=test1, checkPointId=5039907e-6136-435f-a98d-ebae76ef6bb6, nextNode=__END__, streamMode=VALUES)} 
    StateSnapshot{node=action, state={messages=[UserMessage { name = null contents = [TextContent { text = "perform test once" }] }, AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = "call_906ZmZ90zK4DaPkZNK7D3WlH", name = "execTest", arguments = "{"message":"perform test once"}" }] }, ToolExecutionResultMessage { id = "call_906ZmZ90zK4DaPkZNK7D3WlH" toolName = "execTest" text = "test tool executed: perform test once" }]}, config=RunnableConfig(threadId=test1, checkPointId=b6638fce-d14b-40ba-a761-627fa8224130, nextNode=agent, streamMode=VALUES)} 
    StateSnapshot{node=agent, state={messages=[UserMessage { name = null contents = [TextContent { text = "perform test once" }] }, AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = "call_906ZmZ90zK4DaPkZNK7D3WlH", name = "execTest", arguments = "{"message":"perform test once"}" }] }]}, config=RunnableConfig(threadId=test1, checkPointId=36dde250-4ae4-4ed5-84a8-054cbbeaa3db, nextNode=action, streamMode=VALUES)} 
    StateSnapshot{node=__START__, state={messages=[UserMessage { name = null contents = [TextContent { text = "perform test once" }] }]}, config=RunnableConfig(threadId=test1, checkPointId=664c9ede-a607-4502-9dc3-c6b26a0da3df, nextNode=agent, streamMode=VALUES)} 


### Test 2
Update State replacing the 'input' using a cloned state


```java

var config = RunnableConfig.builder()
                .threadId("test2")
                .build();    
                
                
var iterator = graph.streamSnapshots( Map.of( "messages", UserMessage.from("perform test once") ), config );  

for( var step : iterator ) {
    log.info( "STEP: {}", step );
}


```

    START 
    callAgent 
    STEP: StateSnapshot{node=__START__, state={messages=[UserMessage { name = null contents = [TextContent { text = "perform test once" }] }]}, config=RunnableConfig(threadId=test2, checkPointId=703c11db-598f-4236-a4b2-a6fe47bc0a7d, nextNode=agent, streamMode=SNAPSHOTS)} 
    executeTools 
    execute: execTest 
    STEP: StateSnapshot{node=agent, state={messages=[UserMessage { name = null contents = [TextContent { text = "perform test once" }] }, AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = "call_3VzeynG2L5EVdwTvS6UC4POA", name = "execTest", arguments = "{"message":"perform test once"}" }] }]}, config=RunnableConfig(threadId=test2, checkPointId=93f88e19-8b2a-4173-bdbe-be1a7c4d09d0, nextNode=action, streamMode=SNAPSHOTS)} 
    callAgent 
    STEP: StateSnapshot{node=action, state={messages=[UserMessage { name = null contents = [TextContent { text = "perform test once" }] }, AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = "call_3VzeynG2L5EVdwTvS6UC4POA", name = "execTest", arguments = "{"message":"perform test once"}" }] }, ToolExecutionResultMessage { id = "call_3VzeynG2L5EVdwTvS6UC4POA" toolName = "execTest" text = "test tool executed: perform test once" }]}, config=RunnableConfig(threadId=test2, checkPointId=a38a473f-c27e-455e-a2cf-c4457b887343, nextNode=agent, streamMode=SNAPSHOTS)} 
    STEP: StateSnapshot{node=agent, state={agent_response=The test has been successfully executed with the message: "perform test once." How can I assist you further?, messages=[UserMessage { name = null contents = [TextContent { text = "perform test once" }] }, AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = "call_3VzeynG2L5EVdwTvS6UC4POA", name = "execTest", arguments = "{"message":"perform test once"}" }] }, ToolExecutionResultMessage { id = "call_3VzeynG2L5EVdwTvS6UC4POA" toolName = "execTest" text = "test tool executed: perform test once" }]}, config=RunnableConfig(threadId=test2, checkPointId=eede344c-a2dc-4c0d-9805-b53c4ec6cea2, nextNode=__END__, streamMode=SNAPSHOTS)} 
    STEP: NodeOutput{node=__END__, state={agent_response=The test has been successfully executed with the message: "perform test once." How can I assist you further?, messages=[UserMessage { name = null contents = [TextContent { text = "perform test once" }] }, AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = "call_3VzeynG2L5EVdwTvS6UC4POA", name = "execTest", arguments = "{"message":"perform test once"}" }] }, ToolExecutionResultMessage { id = "call_3VzeynG2L5EVdwTvS6UC4POA" toolName = "execTest" text = "test tool executed: perform test once" }]}} 



```java
var history = graph.getStateHistory(config).stream().collect( Collectors.toList() );

var state2 =  history.get(2);

var updatedState = new HashMap<String,Object>();
updatedState.putAll(state2.state().data());

updatedState.put(  "messages", UserMessage.from("perform test twice")  );

// var updatedConfig = graph.updateState( state2.config(), updatedState );
// log.info( "UPDATED CONFIG: {}", updatedConfig );

// var iterator = graph.streamSnapshots( null, updatedConfig );  

// for( var step : iterator ) {
//     log.info( "STEP:\n {}", step );
// }    

```




    [UserMessage { name = null contents = [TextContent { text = "perform test once" }] }, AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = "call_3VzeynG2L5EVdwTvS6UC4POA", name = "execTest", arguments = "{"message":"perform test once"}" }] }]


