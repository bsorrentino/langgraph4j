# Agent Executor


## Create Tools


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

var agentExecutor = new AgentExecutor();

var stateGraph = agentExecutor.graphBuilder()
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

    2024-10-24 17:57:22 FINEST org.bsc.langgraph4j.CompiledGraph$AsyncNodeGenerator <init> START
    2024-10-24 17:57:22 FINEST org.bsc.langgraph4j.agentexecutor.AgentExecutor callAgent callAgent
    2024-10-24 17:57:24 INFO REPL.$JShell$37 do_it$ STEP: NodeOutput{node=__START__, state={input=perform test once, intermediate_steps=[]}}
    2024-10-24 17:57:24 FINEST org.bsc.langgraph4j.agentexecutor.AgentExecutor executeTools executeTools
    2024-10-24 17:57:24 FINEST org.bsc.langgraph4j.langchain4j.tool.ToolNode execute execute: execTest
    2024-10-24 17:57:24 INFO REPL.$JShell$37 do_it$ STEP: StateSnapshot{node=agent, state={input=perform test once, intermediate_steps=[], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_33DLAvIky9boYgbo7rD38G8M", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test1, checkPointId=6a089ecc-5cd6-43e0-83d5-8ec7ad3c91ad, nextNode=action, streamMode=SNAPSHOTS)}
    2024-10-24 17:57:24 FINEST org.bsc.langgraph4j.agentexecutor.AgentExecutor callAgent callAgent
    2024-10-24 17:57:24 INFO REPL.$JShell$37 do_it$ STEP: StateSnapshot{node=action, state={input=perform test once, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_33DLAvIky9boYgbo7rD38G8M", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_33DLAvIky9boYgbo7rD38G8M", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test1, checkPointId=e01a2d6f-47c6-4e29-a034-cb5b2e2e2c23, nextNode=agent, streamMode=SNAPSHOTS)}
    2024-10-24 17:57:24 INFO REPL.$JShell$37 do_it$ STEP: StateSnapshot{node=agent, state={input=perform test once, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_33DLAvIky9boYgbo7rD38G8M", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The test has been executed with the message: "perform test once".}, log=The test has been executed with the message: "perform test once".]]}, config=RunnableConfig(threadId=test1, checkPointId=2f90f85f-9027-423e-b3fc-404adae4e0c9, nextNode=__END__, streamMode=SNAPSHOTS)}
    2024-10-24 17:57:24 INFO REPL.$JShell$37 do_it$ STEP: NodeOutput{node=__END__, state={input=perform test once, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_33DLAvIky9boYgbo7rD38G8M", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The test has been executed with the message: "perform test once".}, log=The test has been executed with the message: "perform test once".]]}}



```java
var history = graph.getStateHistory(config).stream().collect( Collectors.toList() );

var state2 =  history.get(2);

var updatedConfig = graph.updateState( state2.config(), Map.of( "input", "perform test twice"), null);

var iterator = graph.streamSnapshots( null, updatedConfig );  

for( var step : iterator ) {
    log.info( "STEP:\n {}", step );
}

```

    2024-10-24 17:57:25 FINEST org.bsc.langgraph4j.CompiledGraph$AsyncNodeGenerator <init> RESUME REQUEST
    2024-10-24 17:57:25 FINEST org.bsc.langgraph4j.CompiledGraph$AsyncNodeGenerator <init> RESUME FROM agent
    2024-10-24 17:57:25 FINEST org.bsc.langgraph4j.agentexecutor.AgentExecutor executeTools executeTools
    2024-10-24 17:57:25 FINEST org.bsc.langgraph4j.langchain4j.tool.ToolNode execute execute: execTest
    2024-10-24 17:57:25 FINEST org.bsc.langgraph4j.agentexecutor.AgentExecutor callAgent callAgent
    2024-10-24 17:57:25 INFO REPL.$JShell$41 do_it$ STEP:
     StateSnapshot{node=action, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_33DLAvIky9boYgbo7rD38G8M", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_33DLAvIky9boYgbo7rD38G8M", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test1, checkPointId=50e480bd-9e5a-4d11-a2c2-cac15b5b7182, nextNode=agent, streamMode=SNAPSHOTS)}
    2024-10-24 17:57:25 FINEST org.bsc.langgraph4j.agentexecutor.AgentExecutor executeTools executeTools
    2024-10-24 17:57:25 FINEST org.bsc.langgraph4j.langchain4j.tool.ToolNode execute execute: execTest
    2024-10-24 17:57:25 INFO REPL.$JShell$41 do_it$ STEP:
     StateSnapshot{node=agent, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_33DLAvIky9boYgbo7rD38G8M", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_2TlHcW2aZTe0ekvInKeCpmVg", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test1, checkPointId=aaf03107-4b66-4d6a-a2ff-4cdad1161f29, nextNode=action, streamMode=SNAPSHOTS)}
    2024-10-24 17:57:25 FINEST org.bsc.langgraph4j.agentexecutor.AgentExecutor callAgent callAgent
    2024-10-24 17:57:27 INFO REPL.$JShell$41 do_it$ STEP:
     StateSnapshot{node=action, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_33DLAvIky9boYgbo7rD38G8M", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once], IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_2TlHcW2aZTe0ekvInKeCpmVg", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], observation=test tool executed: perform test again]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_2TlHcW2aZTe0ekvInKeCpmVg", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test1, checkPointId=2a20847a-0167-4f40-9ccc-659b180d9207, nextNode=agent, streamMode=SNAPSHOTS)}
    2024-10-24 17:57:27 INFO REPL.$JShell$41 do_it$ STEP:
     StateSnapshot{node=agent, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_33DLAvIky9boYgbo7rD38G8M", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once], IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_2TlHcW2aZTe0ekvInKeCpmVg", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], observation=test tool executed: perform test again]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The tests have been executed successfully:
    
    1. **Test 1:** "perform test once" - Result: test tool executed.
    2. **Test 2:** "perform test again" - Result: test tool executed.}, log=The tests have been executed successfully:
    
    1. **Test 1:** "perform test once" - Result: test tool executed.
    2. **Test 2:** "perform test again" - Result: test tool executed.]]}, config=RunnableConfig(threadId=test1, checkPointId=f5ff242d-7adb-4ee4-bc45-96c5a6515d4f, nextNode=__END__, streamMode=SNAPSHOTS)}
    2024-10-24 17:57:27 INFO REPL.$JShell$41 do_it$ STEP:
     NodeOutput{node=__END__, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_33DLAvIky9boYgbo7rD38G8M", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once], IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_2TlHcW2aZTe0ekvInKeCpmVg", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], observation=test tool executed: perform test again]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The tests have been executed successfully:
    
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

    2024-10-24 17:57:27 FINEST org.bsc.langgraph4j.CompiledGraph$AsyncNodeGenerator <init> START
    2024-10-24 17:57:27 FINEST org.bsc.langgraph4j.agentexecutor.AgentExecutor callAgent callAgent
    2024-10-24 17:57:28 INFO REPL.$JShell$42 do_it$ STEP: NodeOutput{node=__START__, state={input=perform test once, intermediate_steps=[]}}
    2024-10-24 17:57:28 FINEST org.bsc.langgraph4j.agentexecutor.AgentExecutor executeTools executeTools
    2024-10-24 17:57:28 FINEST org.bsc.langgraph4j.langchain4j.tool.ToolNode execute execute: execTest
    2024-10-24 17:57:28 INFO REPL.$JShell$42 do_it$ STEP: StateSnapshot{node=agent, state={input=perform test once, intermediate_steps=[], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_bHyrttIOlJ1yWp4YvpbssIDK", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test2, checkPointId=b05cbb4e-a1e8-4b18-b9fc-bec88d729dde, nextNode=action, streamMode=SNAPSHOTS)}
    2024-10-24 17:57:28 FINEST org.bsc.langgraph4j.agentexecutor.AgentExecutor callAgent callAgent
    2024-10-24 17:57:29 INFO REPL.$JShell$42 do_it$ STEP: StateSnapshot{node=action, state={input=perform test once, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_bHyrttIOlJ1yWp4YvpbssIDK", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_bHyrttIOlJ1yWp4YvpbssIDK", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test2, checkPointId=56b95d26-f8e3-4735-9f9d-01f078088ddd, nextNode=agent, streamMode=SNAPSHOTS)}
    2024-10-24 17:57:29 INFO REPL.$JShell$42 do_it$ STEP: StateSnapshot{node=agent, state={input=perform test once, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_bHyrttIOlJ1yWp4YvpbssIDK", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The test has been executed with the message: "perform test once".}, log=The test has been executed with the message: "perform test once".]]}, config=RunnableConfig(threadId=test2, checkPointId=1ab7a2f1-50d5-4f9b-b02e-5e52ac9893bd, nextNode=__END__, streamMode=SNAPSHOTS)}
    2024-10-24 17:57:29 INFO REPL.$JShell$42 do_it$ STEP: NodeOutput{node=__END__, state={input=perform test once, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_bHyrttIOlJ1yWp4YvpbssIDK", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The test has been executed with the message: "perform test once".}, log=The test has been executed with the message: "perform test once".]]}}



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

    2024-10-24 17:57:29 INFO REPL.$JShell$46 do_it$ UPDATED CONFIG: RunnableConfig(threadId=test2, checkPointId=b05cbb4e-a1e8-4b18-b9fc-bec88d729dde, nextNode=null, streamMode=VALUES)
    2024-10-24 17:57:29 FINEST org.bsc.langgraph4j.CompiledGraph$AsyncNodeGenerator <init> RESUME REQUEST
    2024-10-24 17:57:29 FINEST org.bsc.langgraph4j.CompiledGraph$AsyncNodeGenerator <init> RESUME FROM agent
    2024-10-24 17:57:29 FINEST org.bsc.langgraph4j.agentexecutor.AgentExecutor executeTools executeTools
    2024-10-24 17:57:29 FINEST org.bsc.langgraph4j.langchain4j.tool.ToolNode execute execute: execTest
    2024-10-24 17:57:29 FINEST org.bsc.langgraph4j.agentexecutor.AgentExecutor callAgent callAgent
    2024-10-24 17:57:30 INFO REPL.$JShell$47 do_it$ STEP:
     StateSnapshot{node=action, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_bHyrttIOlJ1yWp4YvpbssIDK", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_bHyrttIOlJ1yWp4YvpbssIDK", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test2, checkPointId=d2b12020-5058-4ce1-b888-7e87d04f6691, nextNode=agent, streamMode=SNAPSHOTS)}
    2024-10-24 17:57:30 FINEST org.bsc.langgraph4j.agentexecutor.AgentExecutor executeTools executeTools
    2024-10-24 17:57:30 FINEST org.bsc.langgraph4j.langchain4j.tool.ToolNode execute execute: execTest
    2024-10-24 17:57:30 INFO REPL.$JShell$47 do_it$ STEP:
     StateSnapshot{node=agent, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_bHyrttIOlJ1yWp4YvpbssIDK", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_eP56QWrZc6DQJJtez18C8zVL", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test2, checkPointId=a18fb247-cdd6-4b58-964b-4c275ce3e31f, nextNode=action, streamMode=SNAPSHOTS)}
    2024-10-24 17:57:30 FINEST org.bsc.langgraph4j.agentexecutor.AgentExecutor callAgent callAgent
    2024-10-24 17:57:31 INFO REPL.$JShell$47 do_it$ STEP:
     StateSnapshot{node=action, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_bHyrttIOlJ1yWp4YvpbssIDK", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once], IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_eP56QWrZc6DQJJtez18C8zVL", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_eP56QWrZc6DQJJtez18C8zVL", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test2, checkPointId=6dd8e19a-8af1-4ee2-9761-0a414aae8c50, nextNode=agent, streamMode=SNAPSHOTS)}
    2024-10-24 17:57:31 INFO REPL.$JShell$47 do_it$ STEP:
     StateSnapshot{node=agent, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_bHyrttIOlJ1yWp4YvpbssIDK", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once], IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_eP56QWrZc6DQJJtez18C8zVL", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The test was executed twice successfully. Here are the results:
    
    1. **Test 1:** test tool executed: perform test once
    2. **Test 2:** test tool executed: perform test once}, log=The test was executed twice successfully. Here are the results:
    
    1. **Test 1:** test tool executed: perform test once
    2. **Test 2:** test tool executed: perform test once]]}, config=RunnableConfig(threadId=test2, checkPointId=c15e98fa-9b66-4ce3-a9f9-257cadd619b4, nextNode=__END__, streamMode=SNAPSHOTS)}
    2024-10-24 17:57:31 INFO REPL.$JShell$47 do_it$ STEP:
     NodeOutput{node=__END__, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_bHyrttIOlJ1yWp4YvpbssIDK", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once], IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_eP56QWrZc6DQJJtez18C8zVL", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The test was executed twice successfully. Here are the results:
    
    1. **Test 1:** test tool executed: perform test once
    2. **Test 2:** test tool executed: perform test once}, log=The test was executed twice successfully. Here are the results:
    
    1. **Test 1:** test tool executed: perform test once
    2. **Test 2:** test tool executed: perform test once]]}}

