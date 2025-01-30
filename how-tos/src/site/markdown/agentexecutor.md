# Agent Executor

**Initialize Logger**

```java
try( var file = new java.io.FileInputStream("./logging.properties")) {
    var lm = java.util.logging.LogManager.getLogManager();
    lm.checkAccess(); 
    lm.readConfiguration( file );
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



var chatLanguageModel = OpenAiChatModel.builder()
    .apiKey( System.getenv("OPENAI_API_KEY") )
    //.modelName( "gpt-3.5-turbo-0125" )
    .modelName( "gpt-4o-mini" )
    .logResponses(true)
    .maxRetries(2)
    .temperature(0.0)
    .maxTokens(2000)
    .build();

var stateGraph = AgentExecutor.graphBuilder()
        .chatLanguageModel(chatLanguageModel)
        .objectsWithTools(List.of(new TestTool()))
        .build();



```

### Test 1 
Update State replacing the 'input'


```java
var saver = new MemorySaver();

CompileConfig compileConfig = CompileConfig.builder()
                .checkpointSaver( saver )
                .build();

var graph = stateGraph.compile( compileConfig );

var config = RunnableConfig.builder()
                .threadId("test1")
                .build();    
                
var iterator = graph.streamSnapshots( Map.of( "input", "perform test once" ), config );  

for( var step : iterator ) {
    log.info( "STEP: {}", step );
}


```

    START 
    callAgent 
    STEP: NodeOutput{node=__START__, state={input=perform test once, intermediate_steps=[]}} 
    executeTools 
    execute: execTest 
    STEP: StateSnapshot{node=agent, state={input=perform test once, intermediate_steps=[], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_yPECPvdBFPA3GSrnVL2eUtab", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test1, checkPointId=efaea448-fa00-4b2e-a49c-12672784d89c, nextNode=action, streamMode=SNAPSHOTS)} 
    callAgent 
    STEP: StateSnapshot{node=action, state={input=perform test once, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_yPECPvdBFPA3GSrnVL2eUtab", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_yPECPvdBFPA3GSrnVL2eUtab", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test1, checkPointId=931e84ce-9b4d-4edf-85e5-01fb93641dc9, nextNode=agent, streamMode=SNAPSHOTS)} 
    STEP: StateSnapshot{node=agent, state={input=perform test once, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_yPECPvdBFPA3GSrnVL2eUtab", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The test has been executed with the message: "perform test once".}, log=The test has been executed with the message: "perform test once".]]}, config=RunnableConfig(threadId=test1, checkPointId=2d01d052-06a9-444e-9b4a-afa05c78a0c9, nextNode=__END__, streamMode=SNAPSHOTS)} 
    STEP: NodeOutput{node=__END__, state={input=perform test once, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_yPECPvdBFPA3GSrnVL2eUtab", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The test has been executed with the message: "perform test once".}, log=The test has been executed with the message: "perform test once".]]}} 



```java
var history = graph.getStateHistory(config).stream().collect( Collectors.toList() );

var state2 =  history.get(2);

var updatedConfig = graph.updateState( state2.config(), Map.of( "input", "perform test twice"), null);

var iterator = graph.streamSnapshots( null, updatedConfig );  

for( var step : iterator ) {
    log.info( "STEP:\n {}", step );
}

```

    RESUME REQUEST 
    RESUME FROM agent 
    executeTools 
    execute: execTest 
    callAgent 
    STEP:
     StateSnapshot{node=action, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_yPECPvdBFPA3GSrnVL2eUtab", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_yPECPvdBFPA3GSrnVL2eUtab", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test1, checkPointId=08ada6be-42fc-4381-acbf-a5fbc9b8e673, nextNode=agent, streamMode=SNAPSHOTS)} 
    executeTools 
    execute: execTest 
    STEP:
     StateSnapshot{node=agent, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_yPECPvdBFPA3GSrnVL2eUtab", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_rhnSgT4hfmtFzpiHkmmeFq3w", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test1, checkPointId=2a5866ea-f9e8-4b13-a268-bbe7219e84fd, nextNode=action, streamMode=SNAPSHOTS)} 
    callAgent 
    STEP:
     StateSnapshot{node=action, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_yPECPvdBFPA3GSrnVL2eUtab", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once], IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_rhnSgT4hfmtFzpiHkmmeFq3w", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], observation=test tool executed: perform test again]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_rhnSgT4hfmtFzpiHkmmeFq3w", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test1, checkPointId=766849ff-aba6-4727-b0a0-e5f5f7dc13f6, nextNode=agent, streamMode=SNAPSHOTS)} 
    STEP:
     StateSnapshot{node=agent, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_yPECPvdBFPA3GSrnVL2eUtab", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once], IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_rhnSgT4hfmtFzpiHkmmeFq3w", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], observation=test tool executed: perform test again]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The tests have been executed successfully:
    
    1. **Test 1:** "perform test once" - Result: test tool executed.
    2. **Test 2:** "perform test again" - Result: test tool executed.}, log=The tests have been executed successfully:
    
    1. **Test 1:** "perform test once" - Result: test tool executed.
    2. **Test 2:** "perform test again" - Result: test tool executed.]]}, config=RunnableConfig(threadId=test1, checkPointId=be9287f9-210d-4690-b676-8aab4da44980, nextNode=__END__, streamMode=SNAPSHOTS)} 
    STEP:
     NodeOutput{node=__END__, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_yPECPvdBFPA3GSrnVL2eUtab", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once], IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_rhnSgT4hfmtFzpiHkmmeFq3w", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], observation=test tool executed: perform test again]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The tests have been executed successfully:
    
    1. **Test 1:** "perform test once" - Result: test tool executed.
    2. **Test 2:** "perform test again" - Result: test tool executed.}, log=The tests have been executed successfully:
    
    1. **Test 1:** "perform test once" - Result: test tool executed.
    2. **Test 2:** "perform test again" - Result: test tool executed.]]}} 


### Test 2
Update State replacing the 'input' using a cloned state


```java

var config = RunnableConfig.builder()
                .threadId("test2")
                .build();    
                
var iterator = graph.streamSnapshots( Map.of( "input", "perform test once" ), config );  

for( var step : iterator ) {
    log.info( "STEP: {}", step );
}


```

    START 
    callAgent 
    STEP: NodeOutput{node=__START__, state={input=perform test once, intermediate_steps=[]}} 
    executeTools 
    execute: execTest 
    STEP: StateSnapshot{node=agent, state={input=perform test once, intermediate_steps=[], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_vgNCAcj6yMWaOQ2GxRW7EfKu", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test2, checkPointId=ca5489e0-0b54-4e93-8aaf-f7c91d539d5a, nextNode=action, streamMode=SNAPSHOTS)} 
    callAgent 
    STEP: StateSnapshot{node=action, state={input=perform test once, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_vgNCAcj6yMWaOQ2GxRW7EfKu", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_vgNCAcj6yMWaOQ2GxRW7EfKu", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test2, checkPointId=39ea80cb-8d1e-48cb-ab3d-49bc41d3dd1c, nextNode=agent, streamMode=SNAPSHOTS)} 
    STEP: StateSnapshot{node=agent, state={input=perform test once, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_vgNCAcj6yMWaOQ2GxRW7EfKu", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The test has been executed with the message: "perform test once".}, log=The test has been executed with the message: "perform test once".]]}, config=RunnableConfig(threadId=test2, checkPointId=7c9868e2-8aa7-4986-a717-a757f9574684, nextNode=__END__, streamMode=SNAPSHOTS)} 
    STEP: NodeOutput{node=__END__, state={input=perform test once, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_vgNCAcj6yMWaOQ2GxRW7EfKu", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The test has been executed with the message: "perform test once".}, log=The test has been executed with the message: "perform test once".]]}} 



```java
var history = graph.getStateHistory(config).stream().collect( Collectors.toList() );

var state2 =  history.get(2);

var updatedState = new HashMap<String,Object>();
updatedState.putAll(state2.state().data());

//System.out.println( state2.state().data() );
updatedState.put(  "input", "perform test twice"  );
//System.out.println( updatedState );

var updatedConfig = graph.updateState( state2.config(), updatedState );
log.info( "UPDATED CONFIG: {}", updatedConfig );

var iterator = graph.streamSnapshots( null, updatedConfig );  

try {
    for( var step : iterator ) {
        log.info( "STEP:\n {}", step );
    }    
}
catch( Exception e ) {
    Throwable t = e ;
    while( t.getCause() != null ) {
        t = t.getCause();
    }
    t.printStackTrace();
}

```

    UPDATED CONFIG: RunnableConfig(threadId=test2, checkPointId=ca5489e0-0b54-4e93-8aaf-f7c91d539d5a, nextNode=null, streamMode=VALUES) 
    RESUME REQUEST 
    RESUME FROM agent 
    executeTools 
    execute: execTest 
    callAgent 
    STEP:
     StateSnapshot{node=action, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_vgNCAcj6yMWaOQ2GxRW7EfKu", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_vgNCAcj6yMWaOQ2GxRW7EfKu", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test2, checkPointId=c324eeac-5109-4f9e-b4a3-d3617d7ec360, nextNode=agent, streamMode=SNAPSHOTS)} 
    executeTools 
    execute: execTest 
    STEP:
     StateSnapshot{node=agent, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_vgNCAcj6yMWaOQ2GxRW7EfKu", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_kdsuye0KybTCqRa4Ehe9cJeF", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test2, checkPointId=67e28047-b81c-4301-9a00-d0c227bac922, nextNode=action, streamMode=SNAPSHOTS)} 
    callAgent 
    STEP:
     StateSnapshot{node=action, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_vgNCAcj6yMWaOQ2GxRW7EfKu", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once], IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_kdsuye0KybTCqRa4Ehe9cJeF", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], observation=test tool executed: perform test again]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_kdsuye0KybTCqRa4Ehe9cJeF", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test2, checkPointId=329a295c-b284-4e5b-bfee-b0c6c924691c, nextNode=agent, streamMode=SNAPSHOTS)} 
    STEP:
     StateSnapshot{node=agent, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_vgNCAcj6yMWaOQ2GxRW7EfKu", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once], IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_kdsuye0KybTCqRa4Ehe9cJeF", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], observation=test tool executed: perform test again]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The tests have been executed successfully:
    
    1. **Test 1:** "perform test once" - Result: test tool executed.
    2. **Test 2:** "perform test again" - Result: test tool executed.}, log=The tests have been executed successfully:
    
    1. **Test 1:** "perform test once" - Result: test tool executed.
    2. **Test 2:** "perform test again" - Result: test tool executed.]]}, config=RunnableConfig(threadId=test2, checkPointId=f496b8dd-c822-4ab6-b5ea-5408d7abf4eb, nextNode=__END__, streamMode=SNAPSHOTS)} 
    STEP:
     NodeOutput{node=__END__, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_vgNCAcj6yMWaOQ2GxRW7EfKu", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once], IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_kdsuye0KybTCqRa4Ehe9cJeF", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], observation=test tool executed: perform test again]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The tests have been executed successfully:
    
    1. **Test 1:** "perform test once" - Result: test tool executed.
    2. **Test 2:** "perform test again" - Result: test tool executed.}, log=The tests have been executed successfully:
    
    1. **Test 1:** "perform test once" - Result: test tool executed.
    2. **Test 2:** "perform test again" - Result: test tool executed.]]}} 

