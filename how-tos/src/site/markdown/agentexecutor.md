# Agent Executor


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

    [0mRepository [1m[32mlocal[0m url: [1m[32mfile:///Users/bsorrentino/.m2/repository/[0m added.
    [0mRepositories count: 4
    [0mname: [1m[32mcentral [0murl: [1m[32mhttps://repo.maven.apache.org/maven2/ [0mrelease:[32mtrue [0mupdate:[32mnever [0msnapshot:[32mfalse [0mupdate:[32mnever 
    [0m[0mname: [1m[32mjboss [0murl: [1m[32mhttps://repository.jboss.org/nexus/content/repositories/releases/ [0mrelease:[32mtrue [0mupdate:[32mnever [0msnapshot:[32mfalse [0mupdate:[32mnever 
    [0m[0mname: [1m[32matlassian [0murl: [1m[32mhttps://packages.atlassian.com/maven/public [0mrelease:[32mtrue [0mupdate:[32mnever [0msnapshot:[32mfalse [0mupdate:[32mnever 
    [0m[0mname: [1m[32mlocal [0murl: [1m[32mfile:///Users/bsorrentino/.m2/repository/ [0mrelease:[32mtrue [0mupdate:[32mnever [0msnapshot:[32mtrue [0mupdate:[32malways 
    [0m

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

    Adding dependency [0m[1m[32morg.slf4j:slf4j-jdk14:2.0.9
    [0mAdding dependency [0m[1m[32morg.bsc.langgraph4j:langgraph4j-core-jdk8:1.0-SNAPSHOT
    [0mAdding dependency [0m[1m[32morg.bsc.langgraph4j:langgraph4j-langchain4j:1.0-SNAPSHOT
    [0mAdding dependency [0m[1m[32morg.bsc.langgraph4j:langgraph4j-agent-executor:1.0-SNAPSHOT
    [0mAdding dependency [0m[1m[32mdev.langchain4j:langchain4j:0.35.0
    [0mAdding dependency [0m[1m[32mdev.langchain4j:langchain4j-open-ai:0.35.0
    [0mSolving dependencies
    Resolved artifacts count: 27
    Add to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/slf4j/slf4j-jdk14/2.0.9/slf4j-jdk14-2.0.9.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/bsc/langgraph4j/langgraph4j-core-jdk8/1.0-SNAPSHOT/langgraph4j-core-jdk8-1.0-SNAPSHOT.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/bsc/async/async-generator-jdk8/2.2.0/async-generator-jdk8-2.2.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/bsc/langgraph4j/langgraph4j-langchain4j/1.0-SNAPSHOT/langgraph4j-langchain4j-1.0-SNAPSHOT.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/bsc/langgraph4j/langgraph4j-agent-executor/1.0-SNAPSHOT/langgraph4j-agent-executor-1.0-SNAPSHOT.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/dev/langchain4j/langchain4j/0.35.0/langchain4j-0.35.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/dev/langchain4j/langchain4j-core/0.35.0/langchain4j-core-0.35.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/apache/opennlp/opennlp-tools/1.9.4/opennlp-tools-1.9.4.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/dev/langchain4j/langchain4j-open-ai/0.35.0/langchain4j-open-ai-0.35.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/dev/ai4j/openai4j/0.22.0/openai4j-0.22.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/squareup/retrofit2/retrofit/2.9.0/retrofit-2.9.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/squareup/retrofit2/converter-jackson/2.9.0/converter-jackson-2.9.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/fasterxml/jackson/core/jackson-databind/2.17.2/jackson-databind-2.17.2.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/fasterxml/jackson/core/jackson-annotations/2.17.2/jackson-annotations-2.17.2.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/fasterxml/jackson/core/jackson-core/2.17.2/jackson-core-2.17.2.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/squareup/okhttp3/okhttp/4.12.0/okhttp-4.12.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/squareup/okio/okio/3.6.0/okio-3.6.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/squareup/okio/okio-jvm/3.6.0/okio-jvm-3.6.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/squareup/okhttp3/okhttp-sse/4.12.0/okhttp-sse-4.12.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/jetbrains/kotlin/kotlin-stdlib-jdk8/1.9.10/kotlin-stdlib-jdk8-1.9.10.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/jetbrains/kotlin/kotlin-stdlib/1.9.10/kotlin-stdlib-1.9.10.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/jetbrains/kotlin/kotlin-stdlib-common/1.9.10/kotlin-stdlib-common-1.9.10.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/jetbrains/annotations/13.0/annotations-13.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/jetbrains/kotlin/kotlin-stdlib-jdk7/1.9.10/kotlin-stdlib-jdk7-1.9.10.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/knuddels/jtokkit/1.1.0/jtokkit-1.1.0.jar[0m
    [0m

Initialize Logger


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

    START 
    callAgent 
    STEP: NodeOutput{node=__START__, state={input=perform test once, intermediate_steps=[]}} 
    executeTools 
    execute: execTest 
    STEP: StateSnapshot{node=agent, state={input=perform test once, intermediate_steps=[], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_DhSzztN2kBWJkZH04I9mlo8l", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test1, checkPointId=04ccdca5-eb10-42eb-9071-ecdb9d84f16f, nextNode=action, streamMode=SNAPSHOTS)} 
    callAgent 
    STEP: StateSnapshot{node=action, state={input=perform test once, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_DhSzztN2kBWJkZH04I9mlo8l", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_DhSzztN2kBWJkZH04I9mlo8l", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test1, checkPointId=93f21409-2ee7-43b6-b26a-530ab144b0b6, nextNode=agent, streamMode=SNAPSHOTS)} 
    STEP: StateSnapshot{node=agent, state={input=perform test once, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_DhSzztN2kBWJkZH04I9mlo8l", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The test has been executed with the message: "perform test once".}, log=The test has been executed with the message: "perform test once".]]}, config=RunnableConfig(threadId=test1, checkPointId=fe98668d-6f88-45a7-921a-2b1c708584c6, nextNode=__END__, streamMode=SNAPSHOTS)} 
    STEP: NodeOutput{node=__END__, state={input=perform test once, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_DhSzztN2kBWJkZH04I9mlo8l", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The test has been executed with the message: "perform test once".}, log=The test has been executed with the message: "perform test once".]]}} 



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
     StateSnapshot{node=action, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_DhSzztN2kBWJkZH04I9mlo8l", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_DhSzztN2kBWJkZH04I9mlo8l", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test1, checkPointId=fd1611f5-e178-450b-86e8-8ed9590d8dbe, nextNode=agent, streamMode=SNAPSHOTS)} 
    executeTools 
    execute: execTest 
    STEP:
     StateSnapshot{node=agent, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_DhSzztN2kBWJkZH04I9mlo8l", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Yds7yDLx72MJmydJIJQk8EmI", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test1, checkPointId=14b846a3-6bd7-4016-a43e-7d2269404f15, nextNode=action, streamMode=SNAPSHOTS)} 
    callAgent 
    STEP:
     StateSnapshot{node=action, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_DhSzztN2kBWJkZH04I9mlo8l", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once], IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Yds7yDLx72MJmydJIJQk8EmI", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], observation=test tool executed: perform test again]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Yds7yDLx72MJmydJIJQk8EmI", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test1, checkPointId=896eb4d3-dd99-466a-bfb0-061b56cce27f, nextNode=agent, streamMode=SNAPSHOTS)} 
    STEP:
     StateSnapshot{node=agent, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_DhSzztN2kBWJkZH04I9mlo8l", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once], IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Yds7yDLx72MJmydJIJQk8EmI", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], observation=test tool executed: perform test again]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The tests have been executed successfully:
    
    1. **Test 1:** "perform test once" - Result: test tool executed.
    2. **Test 2:** "perform test again" - Result: test tool executed.}, log=The tests have been executed successfully:
    
    1. **Test 1:** "perform test once" - Result: test tool executed.
    2. **Test 2:** "perform test again" - Result: test tool executed.]]}, config=RunnableConfig(threadId=test1, checkPointId=e5f8812c-591a-40f1-9104-2e241c841cd4, nextNode=__END__, streamMode=SNAPSHOTS)} 
    STEP:
     NodeOutput{node=__END__, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_DhSzztN2kBWJkZH04I9mlo8l", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once], IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Yds7yDLx72MJmydJIJQk8EmI", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], observation=test tool executed: perform test again]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The tests have been executed successfully:
    
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
    STEP: StateSnapshot{node=agent, state={input=perform test once, intermediate_steps=[], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Su5eLkXNEj208FiNDY96bhXG", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test2, checkPointId=66ee6889-f2b9-4635-a2c7-0159f9445405, nextNode=action, streamMode=SNAPSHOTS)} 
    callAgent 
    STEP: StateSnapshot{node=action, state={input=perform test once, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Su5eLkXNEj208FiNDY96bhXG", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Su5eLkXNEj208FiNDY96bhXG", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test2, checkPointId=eb9052f7-29d4-4d98-b817-0e25c2cfd7da, nextNode=agent, streamMode=SNAPSHOTS)} 
    STEP: StateSnapshot{node=agent, state={input=perform test once, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Su5eLkXNEj208FiNDY96bhXG", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The test has been executed with the message: "perform test once".}, log=The test has been executed with the message: "perform test once".]]}, config=RunnableConfig(threadId=test2, checkPointId=219397ea-c120-49f3-a1ac-05bcaecc2b0d, nextNode=__END__, streamMode=SNAPSHOTS)} 
    STEP: NodeOutput{node=__END__, state={input=perform test once, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Su5eLkXNEj208FiNDY96bhXG", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The test has been executed with the message: "perform test once".}, log=The test has been executed with the message: "perform test once".]]}} 



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

    UPDATED CONFIG: RunnableConfig(threadId=test2, checkPointId=66ee6889-f2b9-4635-a2c7-0159f9445405, nextNode=null, streamMode=VALUES) 
    RESUME REQUEST 
    RESUME FROM agent 
    executeTools 
    execute: execTest 
    callAgent 
    STEP:
     StateSnapshot{node=action, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Su5eLkXNEj208FiNDY96bhXG", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Su5eLkXNEj208FiNDY96bhXG", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test2, checkPointId=700bd258-64c5-4572-8ce6-31eb3611b332, nextNode=agent, streamMode=SNAPSHOTS)} 
    executeTools 
    execute: execTest 
    STEP:
     StateSnapshot{node=agent, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Su5eLkXNEj208FiNDY96bhXG", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_deC78k1uUIOEaFduxRB4VSY4", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test2, checkPointId=6dd067d4-0d26-48db-b313-25ff329acf70, nextNode=action, streamMode=SNAPSHOTS)} 
    callAgent 
    STEP:
     StateSnapshot{node=action, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Su5eLkXNEj208FiNDY96bhXG", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once], IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_deC78k1uUIOEaFduxRB4VSY4", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], observation=test tool executed: perform test again]], agent_outcome=AgentOutcome[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_deC78k1uUIOEaFduxRB4VSY4", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], finish=null]}, config=RunnableConfig(threadId=test2, checkPointId=4b2de0b0-29d5-47c1-bd34-fe02271f3123, nextNode=agent, streamMode=SNAPSHOTS)} 
    STEP:
     StateSnapshot{node=agent, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Su5eLkXNEj208FiNDY96bhXG", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once], IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_deC78k1uUIOEaFduxRB4VSY4", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], observation=test tool executed: perform test again]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The tests have been executed successfully:
    
    1. **Test 1:** "perform test once" - Result: test tool executed.
    2. **Test 2:** "perform test again" - Result: test tool executed.}, log=The tests have been executed successfully:
    
    1. **Test 1:** "perform test once" - Result: test tool executed.
    2. **Test 2:** "perform test again" - Result: test tool executed.]]}, config=RunnableConfig(threadId=test2, checkPointId=be22b92e-aaa2-4517-8010-8b56801f58c5, nextNode=__END__, streamMode=SNAPSHOTS)} 
    STEP:
     NodeOutput{node=__END__, state={input=perform test twice, intermediate_steps=[IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_Su5eLkXNEj208FiNDY96bhXG", name = "execTest", arguments = "{"message":"perform test once"}" }, log=], observation=test tool executed: perform test once], IntermediateStep[action=AgentAction[toolExecutionRequest=ToolExecutionRequest { id = "call_deC78k1uUIOEaFduxRB4VSY4", name = "execTest", arguments = "{"message":"perform test again"}" }, log=], observation=test tool executed: perform test again]], agent_outcome=AgentOutcome[action=null, finish=AgentFinish[returnValues={returnValues=The tests have been executed successfully:
    
    1. **Test 1:** "perform test once" - Result: test tool executed.
    2. **Test 2:** "perform test again" - Result: test tool executed.}, log=The tests have been executed successfully:
    
    1. **Test 1:** "perform test once" - Result: test tool executed.
    2. **Test 2:** "perform test again" - Result: test tool executed.]]}} 

