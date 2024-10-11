# Agent Executor

<!-- 
```java
String userHomeDir = System.getProperty("user.home");
String localRespoUrl = "file://" + userHomeDir + "/.m2/repository/";
String langchain4jVersion = "0.35.0"
```

add local maven repository


```java
%dependency /add-repo local \{localRespoUrl} release|never snapshot|always
%dependency /list-repos

```

Remove installed package from Jupiter cache


```bash
%%bash 
rm -rf \{userHomeDir}/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/bsc/langgraph4j
```
 

Install required maven dependencies


```java
%dependency /add org.slf4j:slf4j-jdk14:2.0.9
%dependency /add org.bsc.langgraph4j:langgraph4j-core-jdk8:1.0-SNAPSHOT
%dependency /add org.bsc.langgraph4j:langgraph4j-langchain4j:1.0-SNAPSHOT
%dependency /add org.bsc.langgraph4j:langgraph4j-agent-executor:1.0-SNAPSHOT
%dependency /add dev.langchain4j:langchain4j:\{langchain4jVersion}
%dependency /add dev.langchain4j:langchain4j-open-ai:\{langchain4jVersion}

%dependency /resolve
```

Initialize Logger


```java
try( var file = new java.io.FileInputStream("./logging.properties")) {
    var lm = java.util.logging.LogManager.getLogManager();
    lm.checkAccess(); 
    lm.readConfiguration( file );
}

var log = org.slf4j.LoggerFactory.getLogger("AgentExecutor");

```
-->


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

    2024-10-11 13:05:40 FINEST org.bsc.langgraph4j.CompiledGraph$AsyncNodeGenerator <init> START
    2024-10-11 13:05:40 FINEST org.bsc.langgraph4j.agentexecutor.AgentExecutor callAgent callAgent
    2024-10-11 13:05:42 INFO REPL.$JShell$37 do_it$ STEP: NodeOutput{node=__START__, state={input=perform test once, intermediate_steps=[]}}
    2024-10-11 13:05:42 FINEST org.bsc.langgraph4j.agentexecutor.AgentExecutor executeTools executeTools
    2024-10-11 13:05:42 FINEST org.bsc.langgraph4j.langchain4j.tool.ToolNode execute execute: execTest
    2024-10-11 13:05:42 INFO REPL.$JShell$37 do_it$ STEP: StateSnapshot{node=agent, state={input=perform test once, intermediate_steps=[], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_IWpeVQWcA0B2GJzXbQiLCQHL", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test1, checkPointId=dbe479ed-21dd-4e9b-bef8-3f4fda0abdb9, nextNode=action, streamMode=SNAPSHOTS)}
    2024-10-11 13:05:42 FINEST org.bsc.langgraph4j.agentexecutor.AgentExecutor callAgent callAgent
    2024-10-11 13:05:43 INFO REPL.$JShell$37 do_it$ STEP: StateSnapshot{node=action, state={input=perform test once, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_IWpeVQWcA0B2GJzXbQiLCQHL", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_IWpeVQWcA0B2GJzXbQiLCQHL", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test1, checkPointId=7e382592-e877-46db-947a-f6c20ed00133, nextNode=agent, streamMode=SNAPSHOTS)}
    2024-10-11 13:05:43 INFO REPL.$JShell$37 do_it$ STEP: StateSnapshot{node=agent, state={input=perform test once, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_IWpeVQWcA0B2GJzXbQiLCQHL", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The test has been executed with the message: "perform test once".}, log=The test has been executed with the message: "perform test once".]]}, config=RunnableConfig(threadId=test1, checkPointId=b450e944-ba09-4aa4-8903-92e600f3e251, nextNode=__END__, streamMode=SNAPSHOTS)}
    2024-10-11 13:05:43 INFO REPL.$JShell$37 do_it$ STEP: NodeOutput{node=__END__, state={input=perform test once, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_IWpeVQWcA0B2GJzXbQiLCQHL", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The test has been executed with the message: "perform test once".}, log=The test has been executed with the message: "perform test once".]]}}



```java
var history = graph.getStateHistory(config).stream().collect( Collectors.toList() );

var state2 =  history.get(2);

var updatedConfig = graph.updateState( state2.config(), Map.of( "input", "perform test twice"), null);

var iterator = graph.streamSnapshots( null, updatedConfig );  

for( var step : iterator ) {
    log.info( "STEP:\n {}", step );
}

```

    2024-10-11 13:05:43 FINEST org.bsc.langgraph4j.CompiledGraph$AsyncNodeGenerator <init> RESUME REQUEST
    2024-10-11 13:05:43 FINEST org.bsc.langgraph4j.CompiledGraph$AsyncNodeGenerator <init> RESUME FROM agent
    2024-10-11 13:05:43 FINEST org.bsc.langgraph4j.agentexecutor.AgentExecutor executeTools executeTools
    2024-10-11 13:05:43 FINEST org.bsc.langgraph4j.langchain4j.tool.ToolNode execute execute: execTest
    2024-10-11 13:05:43 FINEST org.bsc.langgraph4j.agentexecutor.AgentExecutor callAgent callAgent
    2024-10-11 13:05:44 INFO REPL.$JShell$41 do_it$ STEP:
     StateSnapshot{node=action, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_IWpeVQWcA0B2GJzXbQiLCQHL", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_IWpeVQWcA0B2GJzXbQiLCQHL", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test1, checkPointId=171ba325-e803-4a08-a4fc-c23a3dcbd7ef, nextNode=agent, streamMode=SNAPSHOTS)}
    2024-10-11 13:05:44 FINEST org.bsc.langgraph4j.agentexecutor.AgentExecutor executeTools executeTools
    2024-10-11 13:05:44 FINEST org.bsc.langgraph4j.langchain4j.tool.ToolNode execute execute: execTest
    2024-10-11 13:05:44 INFO REPL.$JShell$41 do_it$ STEP:
     StateSnapshot{node=agent, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_IWpeVQWcA0B2GJzXbQiLCQHL", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_ZFTeqmQgcEegRyb0Xoi2f6PY", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test1, checkPointId=89fe550d-c356-4354-b076-4fc8ad6e127a, nextNode=action, streamMode=SNAPSHOTS)}
    2024-10-11 13:05:44 FINEST org.bsc.langgraph4j.agentexecutor.AgentExecutor callAgent callAgent
    2024-10-11 13:05:46 INFO REPL.$JShell$41 do_it$ STEP:
     StateSnapshot{node=action, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_IWpeVQWcA0B2GJzXbQiLCQHL", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once], IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_ZFTeqmQgcEegRyb0Xoi2f6PY", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], observation=test tool executed: perform test again]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_ZFTeqmQgcEegRyb0Xoi2f6PY", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test1, checkPointId=9aded304-4a2a-4c84-8cdb-53622d50dda2, nextNode=agent, streamMode=SNAPSHOTS)}
    2024-10-11 13:05:46 INFO REPL.$JShell$41 do_it$ STEP:
     StateSnapshot{node=agent, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_IWpeVQWcA0B2GJzXbQiLCQHL", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once], IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_ZFTeqmQgcEegRyb0Xoi2f6PY", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], observation=test tool executed: perform test again]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The tests have been executed successfully:
    
    1. **Test 1:** "perform test once" - Result: test tool executed.
    2. **Test 2:** "perform test again" - Result: test tool executed.}, log=The tests have been executed successfully:
    
    1. **Test 1:** "perform test once" - Result: test tool executed.
    2. **Test 2:** "perform test again" - Result: test tool executed.]]}, config=RunnableConfig(threadId=test1, checkPointId=3c0170b3-c270-41f2-b59f-4cbd433e0068, nextNode=__END__, streamMode=SNAPSHOTS)}
    2024-10-11 13:05:46 INFO REPL.$JShell$41 do_it$ STEP:
     NodeOutput{node=__END__, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_IWpeVQWcA0B2GJzXbQiLCQHL", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once], IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_ZFTeqmQgcEegRyb0Xoi2f6PY", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], observation=test tool executed: perform test again]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The tests have been executed successfully:
    
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

    2024-10-11 13:05:46 FINEST org.bsc.langgraph4j.CompiledGraph$AsyncNodeGenerator <init> START
    2024-10-11 13:05:46 FINEST org.bsc.langgraph4j.agentexecutor.AgentExecutor callAgent callAgent
    2024-10-11 13:05:47 INFO REPL.$JShell$42 do_it$ STEP: NodeOutput{node=__START__, state={input=perform test once, intermediate_steps=[]}}
    2024-10-11 13:05:47 FINEST org.bsc.langgraph4j.agentexecutor.AgentExecutor executeTools executeTools
    2024-10-11 13:05:47 FINEST org.bsc.langgraph4j.langchain4j.tool.ToolNode execute execute: execTest
    2024-10-11 13:05:47 INFO REPL.$JShell$42 do_it$ STEP: StateSnapshot{node=agent, state={input=perform test once, intermediate_steps=[], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_BKVXOUlQmFPqydEJZ3hotT0H", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test2, checkPointId=c470d430-fed3-477c-a6a7-84c6e67c55e4, nextNode=action, streamMode=SNAPSHOTS)}
    2024-10-11 13:05:47 FINEST org.bsc.langgraph4j.agentexecutor.AgentExecutor callAgent callAgent
    2024-10-11 13:05:49 INFO REPL.$JShell$42 do_it$ STEP: StateSnapshot{node=action, state={input=perform test once, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_BKVXOUlQmFPqydEJZ3hotT0H", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_BKVXOUlQmFPqydEJZ3hotT0H", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test2, checkPointId=0504ac40-f4e6-4fae-be3d-6a2b9f1562e7, nextNode=agent, streamMode=SNAPSHOTS)}
    2024-10-11 13:05:49 INFO REPL.$JShell$42 do_it$ STEP: StateSnapshot{node=agent, state={input=perform test once, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_BKVXOUlQmFPqydEJZ3hotT0H", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The test has been executed with the message: "perform test once".}, log=The test has been executed with the message: "perform test once".]]}, config=RunnableConfig(threadId=test2, checkPointId=db351672-0f25-4120-ae28-09012ae2803b, nextNode=__END__, streamMode=SNAPSHOTS)}
    2024-10-11 13:05:49 INFO REPL.$JShell$42 do_it$ STEP: NodeOutput{node=__END__, state={input=perform test once, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_BKVXOUlQmFPqydEJZ3hotT0H", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The test has been executed with the message: "perform test once".}, log=The test has been executed with the message: "perform test once".]]}}



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

    2024-10-11 13:06:10 INFO REPL.$JShell$49 do_it$ UPDATED CONFIG: RunnableConfig(threadId=test2, checkPointId=c470d430-fed3-477c-a6a7-84c6e67c55e4, nextNode=null, streamMode=VALUES)
    2024-10-11 13:06:10 FINEST org.bsc.langgraph4j.CompiledGraph$AsyncNodeGenerator <init> RESUME REQUEST
    2024-10-11 13:06:10 FINEST org.bsc.langgraph4j.CompiledGraph$AsyncNodeGenerator <init> RESUME FROM agent
    2024-10-11 13:06:10 FINEST org.bsc.langgraph4j.agentexecutor.AgentExecutor executeTools executeTools
    2024-10-11 13:06:10 FINEST org.bsc.langgraph4j.langchain4j.tool.ToolNode execute execute: execTest
    2024-10-11 13:06:10 FINEST org.bsc.langgraph4j.agentexecutor.AgentExecutor callAgent callAgent
    2024-10-11 13:06:11 INFO REPL.$JShell$50 do_it$ STEP:
     StateSnapshot{node=action, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_BKVXOUlQmFPqydEJZ3hotT0H", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_BKVXOUlQmFPqydEJZ3hotT0H", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test2, checkPointId=92b00613-4f65-4f2a-b573-5302a46568d3, nextNode=agent, streamMode=SNAPSHOTS)}
    2024-10-11 13:06:11 FINEST org.bsc.langgraph4j.agentexecutor.AgentExecutor executeTools executeTools
    2024-10-11 13:06:11 FINEST org.bsc.langgraph4j.langchain4j.tool.ToolNode execute execute: execTest
    2024-10-11 13:06:11 INFO REPL.$JShell$50 do_it$ STEP:
     StateSnapshot{node=agent, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_BKVXOUlQmFPqydEJZ3hotT0H", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_eUegMsToBzjw53pntENbHK6U", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test2, checkPointId=9ac9f853-40d1-401b-a909-4c6779e8e593, nextNode=action, streamMode=SNAPSHOTS)}
    2024-10-11 13:06:11 FINEST org.bsc.langgraph4j.agentexecutor.AgentExecutor callAgent callAgent
    2024-10-11 13:06:13 INFO REPL.$JShell$50 do_it$ STEP:
     StateSnapshot{node=action, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_BKVXOUlQmFPqydEJZ3hotT0H", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once], IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_eUegMsToBzjw53pntENbHK6U", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], observation=test tool executed: perform test again]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_eUegMsToBzjw53pntENbHK6U", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test2, checkPointId=c8a9dfb0-e902-4f16-8139-904d1b4666c7, nextNode=agent, streamMode=SNAPSHOTS)}
    2024-10-11 13:06:13 INFO REPL.$JShell$50 do_it$ STEP:
     StateSnapshot{node=agent, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_BKVXOUlQmFPqydEJZ3hotT0H", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once], IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_eUegMsToBzjw53pntENbHK6U", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], observation=test tool executed: perform test again]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The tests have been executed successfully:
    
    1. **Test 1:** "perform test once" - Result: test tool executed.
    2. **Test 2:** "perform test again" - Result: test tool executed.}, log=The tests have been executed successfully:
    
    1. **Test 1:** "perform test once" - Result: test tool executed.
    2. **Test 2:** "perform test again" - Result: test tool executed.]]}, config=RunnableConfig(threadId=test2, checkPointId=74a5a0eb-8712-4f4c-afce-df8b4e4599da, nextNode=__END__, streamMode=SNAPSHOTS)}
    2024-10-11 13:06:13 INFO REPL.$JShell$50 do_it$ STEP:
     NodeOutput{node=__END__, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_BKVXOUlQmFPqydEJZ3hotT0H", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once], IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_eUegMsToBzjw53pntENbHK6U", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], observation=test tool executed: perform test again]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The tests have been executed successfully:
    
    1. **Test 1:** "perform test once" - Result: test tool executed.
    2. **Test 2:** "perform test again" - Result: test tool executed.}, log=The tests have been executed successfully:
    
    1. **Test 1:** "perform test once" - Result: test tool executed.
    2. **Test 2:** "perform test again" - Result: test tool executed.]]}}

