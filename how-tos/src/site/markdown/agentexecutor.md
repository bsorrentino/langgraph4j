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
    STEP: StateSnapshot{node=agent, state={input=perform test once, intermediate_steps=[], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Clf7G6EHLEBiP4tSvIjJZyPG", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test1, checkPointId=30e7455d-d650-4c00-9967-8554d5a368fa, nextNode=action, streamMode=SNAPSHOTS)} 
    callAgent 
    STEP: StateSnapshot{node=action, state={input=perform test once, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Clf7G6EHLEBiP4tSvIjJZyPG", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Clf7G6EHLEBiP4tSvIjJZyPG", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test1, checkPointId=931a2e9c-5e4e-4fc4-9625-e726f4ce2b1b, nextNode=agent, streamMode=SNAPSHOTS)} 
    STEP: StateSnapshot{node=agent, state={input=perform test once, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Clf7G6EHLEBiP4tSvIjJZyPG", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The test has been executed with the message: "perform test once".}, log=The test has been executed with the message: "perform test once".]]}, config=RunnableConfig(threadId=test1, checkPointId=309266ac-a37c-4d7d-ab8b-92ae6107edb7, nextNode=__END__, streamMode=SNAPSHOTS)} 
    STEP: NodeOutput{node=__END__, state={input=perform test once, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Clf7G6EHLEBiP4tSvIjJZyPG", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The test has been executed with the message: "perform test once".}, log=The test has been executed with the message: "perform test once".]]}} 



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
     StateSnapshot{node=action, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Clf7G6EHLEBiP4tSvIjJZyPG", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Clf7G6EHLEBiP4tSvIjJZyPG", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test1, checkPointId=728fe35a-73c6-4ca9-b447-4a4a90abd862, nextNode=agent, streamMode=SNAPSHOTS)} 
    executeTools 
    execute: execTest 
    STEP:
     StateSnapshot{node=agent, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Clf7G6EHLEBiP4tSvIjJZyPG", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Yhu662UagKfZOCC8GgUznqWk", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test1, checkPointId=e02fff1d-5ef3-4332-88dc-e8e0156bb86b, nextNode=action, streamMode=SNAPSHOTS)} 
    callAgent 
    STEP:
     StateSnapshot{node=action, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Clf7G6EHLEBiP4tSvIjJZyPG", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once], IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Yhu662UagKfZOCC8GgUznqWk", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], observation=test tool executed: perform test again]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Yhu662UagKfZOCC8GgUznqWk", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test1, checkPointId=54c8c50f-60cf-43f8-b873-adcdf33f6ce7, nextNode=agent, streamMode=SNAPSHOTS)} 
    STEP:
     StateSnapshot{node=agent, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Clf7G6EHLEBiP4tSvIjJZyPG", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once], IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Yhu662UagKfZOCC8GgUznqWk", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], observation=test tool executed: perform test again]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The tests have been executed successfully:
    
    1. **Test 1:** "perform test once" - Result: test tool executed.
    2. **Test 2:** "perform test again" - Result: test tool executed.}, log=The tests have been executed successfully:
    
    1. **Test 1:** "perform test once" - Result: test tool executed.
    2. **Test 2:** "perform test again" - Result: test tool executed.]]}, config=RunnableConfig(threadId=test1, checkPointId=474e8ae9-3f88-4312-8609-f7e12beaf231, nextNode=__END__, streamMode=SNAPSHOTS)} 
    STEP:
     NodeOutput{node=__END__, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Clf7G6EHLEBiP4tSvIjJZyPG", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once], IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Yhu662UagKfZOCC8GgUznqWk", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], observation=test tool executed: perform test again]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The tests have been executed successfully:
    
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
    STEP: StateSnapshot{node=agent, state={input=perform test once, intermediate_steps=[], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_s005sVfqzckhCtDzuCGQCBZP", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test2, checkPointId=4a476858-5aea-486a-8db8-6cfc6d33b1d6, nextNode=action, streamMode=SNAPSHOTS)} 
    callAgent 
    STEP: StateSnapshot{node=action, state={input=perform test once, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_s005sVfqzckhCtDzuCGQCBZP", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_s005sVfqzckhCtDzuCGQCBZP", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test2, checkPointId=a8ae00e2-f4dd-4bcb-9ce3-4c271904ffbf, nextNode=agent, streamMode=SNAPSHOTS)} 
    STEP: StateSnapshot{node=agent, state={input=perform test once, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_s005sVfqzckhCtDzuCGQCBZP", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The test has been executed with the message: "perform test once".}, log=The test has been executed with the message: "perform test once".]]}, config=RunnableConfig(threadId=test2, checkPointId=2a4aa960-e06f-49fb-831a-eec895c381c6, nextNode=__END__, streamMode=SNAPSHOTS)} 
    STEP: NodeOutput{node=__END__, state={input=perform test once, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_s005sVfqzckhCtDzuCGQCBZP", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The test has been executed with the message: "perform test once".}, log=The test has been executed with the message: "perform test once".]]}} 



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

    UPDATED CONFIG: RunnableConfig(threadId=test2, checkPointId=4a476858-5aea-486a-8db8-6cfc6d33b1d6, nextNode=null, streamMode=VALUES) 
    RESUME REQUEST 
    RESUME FROM agent 
    executeTools 
    execute: execTest 
    callAgent 
    STEP:
     StateSnapshot{node=action, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_s005sVfqzckhCtDzuCGQCBZP", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_s005sVfqzckhCtDzuCGQCBZP", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test2, checkPointId=5209cd5c-c383-434e-b490-95d0dad1b152, nextNode=agent, streamMode=SNAPSHOTS)} 
    executeTools 
    execute: execTest 
    STEP:
     StateSnapshot{node=agent, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_s005sVfqzckhCtDzuCGQCBZP", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_eAbVm48rSd6n4sum7k1As5vE", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test2, checkPointId=d99cdf44-d8a0-48a2-b660-c9239d21929b, nextNode=action, streamMode=SNAPSHOTS)} 
    callAgent 
    STEP:
     StateSnapshot{node=action, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_s005sVfqzckhCtDzuCGQCBZP", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once], IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_eAbVm48rSd6n4sum7k1As5vE", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], observation=test tool executed: perform test again]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_eAbVm48rSd6n4sum7k1As5vE", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test2, checkPointId=4c7dafd9-51eb-4dec-883c-aa60d91eb820, nextNode=agent, streamMode=SNAPSHOTS)} 
    STEP:
     StateSnapshot{node=agent, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_s005sVfqzckhCtDzuCGQCBZP", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once], IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_eAbVm48rSd6n4sum7k1As5vE", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], observation=test tool executed: perform test again]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The tests have been executed successfully:
    
    1. **Test 1:** "perform test once" - Result: test tool executed.
    2. **Test 2:** "perform test again" - Result: test tool executed.}, log=The tests have been executed successfully:
    
    1. **Test 1:** "perform test once" - Result: test tool executed.
    2. **Test 2:** "perform test again" - Result: test tool executed.]]}, config=RunnableConfig(threadId=test2, checkPointId=3e2411fa-5bf3-4946-b5c2-20b3c3f994bb, nextNode=__END__, streamMode=SNAPSHOTS)} 
    STEP:
     NodeOutput{node=__END__, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_s005sVfqzckhCtDzuCGQCBZP", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once], IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_eAbVm48rSd6n4sum7k1As5vE", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], observation=test tool executed: perform test again]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The tests have been executed successfully:
    
    1. **Test 1:** "perform test once" - Result: test tool executed.
    2. **Test 2:** "perform test again" - Result: test tool executed.}, log=The tests have been executed successfully:
    
    1. **Test 1:** "perform test once" - Result: test tool executed.
    2. **Test 2:** "perform test again" - Result: test tool executed.]]}} 

