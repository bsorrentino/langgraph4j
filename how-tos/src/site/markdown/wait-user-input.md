# Wait for User Input


```java
String userHomeDir = System.getProperty("user.home");
String localRespoUrl = "file://" + userHomeDir + "/.m2/repository/";
String langchain4jVersion = "0.35.0"
```


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


```java
%dependency /add org.bsc.langgraph4j:langgraph4j-core-jdk8:1.0-SNAPSHOT
%dependency /add org.bsc.langgraph4j:langgraph4j-langchain4j:1.0-SNAPSHOT
%dependency /add dev.langchain4j:langchain4j:\{langchain4jVersion}
%dependency /add dev.langchain4j:langchain4j-open-ai:\{langchain4jVersion}
%dependency /add net.sourceforge.plantuml:plantuml-mit:1.2024.6
%dependency /list-dependencies
%dependency /resolve
```

    Adding dependency [0m[1m[32morg.bsc.langgraph4j:langgraph4j-core-jdk8:1.0-SNAPSHOT
    [0mAdding dependency [0m[1m[32morg.bsc.langgraph4j:langgraph4j-langchain4j:1.0-SNAPSHOT
    [0mAdding dependency [0m[1m[32mdev.langchain4j:langchain4j:0.35.0
    [0mAdding dependency [0m[1m[32mdev.langchain4j:langchain4j-open-ai:0.35.0
    [0mAdding dependency [0m[1m[32mnet.sourceforge.plantuml:plantuml-mit:1.2024.6
    [0mProposed dependencies count: 5
    [0m - [1m[32morg.bsc.langgraph4j:langgraph4j-core-jdk8:jar:1.0-SNAPSHOT (runtime)
    [0m[0m - [1m[32morg.bsc.langgraph4j:langgraph4j-langchain4j:jar:1.0-SNAPSHOT (runtime)
    [0m[0m - [1m[32mdev.langchain4j:langchain4j:jar:0.35.0 (runtime)
    [0m[0m - [1m[32mdev.langchain4j:langchain4j-open-ai:jar:0.35.0 (runtime)
    [0m[0m - [1m[32mnet.sourceforge.plantuml:plantuml-mit:jar:1.2024.6 (runtime)
    [0mResolved dependencies count: 0
    Solving dependencies
    Resolved artifacts count: 26
    Add to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/bsc/langgraph4j/langgraph4j-core-jdk8/1.0-SNAPSHOT/langgraph4j-core-jdk8-1.0-SNAPSHOT.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/bsc/async/async-generator-jdk8/2.2.0/async-generator-jdk8-2.2.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/bsc/langgraph4j/langgraph4j-langchain4j/1.0-SNAPSHOT/langgraph4j-langchain4j-1.0-SNAPSHOT.jar[0m
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
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/net/sourceforge/plantuml/plantuml-mit/1.2024.6/plantuml-mit-1.2024.6.jar[0m
    [0m


```java
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.FileFormat;

static java.awt.Image plantUML2PNG( String code ) throws IOException { 
    var reader = new SourceStringReader(code);

    try(var imageOutStream = new java.io.ByteArrayOutputStream()) {

        var description = reader.outputImage( imageOutStream, 0, new FileFormatOption(FileFormat.PNG));

        var imageInStream = new java.io.ByteArrayInputStream(  imageOutStream.toByteArray() );

        return javax.imageio.ImageIO.read( imageInStream );

    }
}

```


```java
import org.bsc.langgraph4j.*;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.Channel;
import org.bsc.langgraph4j.state.AppenderChannel;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import static org.bsc.langgraph4j.utils.CollectionsUtils.mapOf;
import org.bsc.langgraph4j.checkpoint.MemorySaver;
import org.bsc.langgraph4j.CompileConfig;
import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;

public class State extends AgentState {

    static Map<String, Channel<?>> SCHEMA = Map.of(
            "messages", AppenderChannel.<AiMessage>of(ArrayList::new)
    );

    public State(Map<String, Object> initData) {
        super( initData  );
    }

    Optional<String> input() { return value("input"); } 
    Optional<String> userFeedback() { return value("user_feedback"); } 

}

AsyncNodeAction<State> step1 = node_async(state -> {
    System.out.println( "---Step 1---" );
    return mapOf();
});

AsyncNodeAction<State> humanFeedback = node_async(state -> {
    System.out.println( "---human_feedback---" );
    return mapOf();
});

AsyncNodeAction<State> step3 = node_async(state -> {
    System.out.println( "---Step 3---" );
    return mapOf();
});

var builder = new StateGraph<>(State.SCHEMA, State::new);
builder.addNode("step_1", step1);
builder.addNode("human_feedback", humanFeedback);
builder.addNode("step_3", step3);
builder.addEdge(START, "step_1");
builder.addEdge("step_1", "human_feedback");
builder.addEdge("human_feedback", "step_3");
builder.addEdge("step_3", END);

// Set up memory
var saver = new MemorySaver();

// Add
var compileConfig = CompileConfig.builder().checkpointSaver(saver).interruptBefore("human_feedback").build();
var graph = builder.compile(compileConfig);

// View as PlantUML 
var plantuml = graph.getGraph(GraphRepresentation.Type.PLANTUML).getContent();

display( plantuml );
display( plantUML2PNG(plantuml) );
```

    SLF4J: No SLF4J providers were found.
    SLF4J: Defaulting to no-operation (NOP) logger implementation
    SLF4J: See https://www.slf4j.org/codes.html#noProviders for further details.



    @startuml unnamed.puml
    skinparam usecaseFontSize 14
    skinparam usecaseStereotypeFontSize 12
    skinparam hexagonFontSize 14
    skinparam hexagonStereotypeFontSize 12
    title "Graph Diagram"
    footer
    
    powered by langgraph4j
    end footer
    circle start<<input>>
    circle stop as __END__
    usecase "step_1"<<Node>>
    usecase "human_feedback"<<Node>>
    usecase "step_3"<<Node>>
    start -down-> "step_1"
    "step_1" -down-> "human_feedback"
    "human_feedback" -down-> "step_3"
    "step_3" -down-> "__END__"
    @enduml




    
![png](wait-user-input_files/wait-user-input_5_2.png)
    





    b91410fb-a67e-4793-aec8-1a043afc772c




```java
// Input
var initialInput = mapOf("input", (Object) "hello world");

// Thread
var invokeConfig = RunnableConfig.builder().threadId("Thread1").build();

// Run the graph until the first interruption
for (var event : graph.stream(initialInput, invokeConfig)) {
    System.out.println(event);
}

```

    ---Step 1---
    NodeOutput{node=__START__, state={input=hello world, messages=[]}}
    NodeOutput{node=step_1, state={input=hello world, messages=[]}}



```java
// Get user input
//String userInput = new Scanner(System.in).nextLine();
String userInput = "go to step 3!";
System.out.println("Tell me how you want to update the state: " + userInput);

// We now update the state as if we are the human_feedback node
//var updateConfig = graph.updateState(invokeConfig, mapOf("user_feedback", userInput), "human_feedback");
var updateConfig = graph.updateState(invokeConfig, mapOf("user_feedback", userInput), null);

// We can check the state
System.out.println("--State after update--");
System.out.println(graph.getState(invokeConfig));

// We can check the next node, showing that it is node 3 (which follows human_feedback)
System.out.println("getNext with invokeConfig: " + graph.getState(invokeConfig).getNext());
System.out.println("getNext with updateConfig: " + graph.getState(updateConfig).getNext());
```

    Tell me how you want to update the state: go to step 3!
    --State after update--
    StateSnapshot{node=step_1, state={user_feedback=go to step 3!, input=hello world, messages=[]}, config=RunnableConfig(threadId=Thread1, checkPointId=8eca3260-d011-42c4-8d4c-0f4d8a7a7dbc, nextNode=human_feedback, streamMode=VALUES)}
    getNext with invokeConfig: human_feedback
    getNext with updateConfig: human_feedback



```java
// Continue the graph execution
for (var event : graph.stream(null, updateConfig)) {
    System.out.println(event);
}
```

    ---human_feedback---
    ---Step 3---
    NodeOutput{node=human_feedback, state={user_feedback=go to step 3!, input=hello world, messages=[]}}
    NodeOutput{node=step_3, state={user_feedback=go to step 3!, input=hello world, messages=[]}}
    NodeOutput{node=__END__, state={user_feedback=go to step 3!, input=hello world, messages=[]}}



```java
graph.getState(updateConfig).getState();
```




    {user_feedback=go to step 3!, input=hello world, messages=[]}


