# Subgraph as state graph sample


```java
var userHomeDir = System.getProperty("user.home");
var localRespoUrl = "file://" + userHomeDir + "/.m2/repository/";
var langchain4jVersion = "0.36.2";
var langgraph4jVersion = "1.4-SNAPSHOT";
```


```bash
%%bash 
rm -rf \{userHomeDir}/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/bsc/langgraph4j
```


```java
%dependency /add-repo local \{localRespoUrl} release|never snapshot|always
// %dependency /list-repos
%dependency /add org.slf4j:slf4j-jdk14:2.0.9
%dependency /add org.bsc.langgraph4j:langgraph4j-core:\{langgraph4jVersion}
%dependency /add org.bsc.langgraph4j:langgraph4j-langchain4j:\{langgraph4jVersion}
%dependency /add dev.langchain4j:langchain4j:\{langchain4jVersion}
%dependency /add dev.langchain4j:langchain4j-open-ai:\{langchain4jVersion}
%dependency /add net.sourceforge.plantuml:plantuml-mit:1.2024.8

%dependency /resolve
```

    [0mRepository [1m[32mlocal[0m url: [1m[32mfile:///Users/bsorrentino/.m2/repository/[0m added.
    [0mAdding dependency [0m[1m[32morg.slf4j:slf4j-jdk14:2.0.9
    [0mAdding dependency [0m[1m[32morg.bsc.langgraph4j:langgraph4j-core:1.4-SNAPSHOT
    [0mAdding dependency [0m[1m[32morg.bsc.langgraph4j:langgraph4j-langchain4j:1.4-SNAPSHOT
    [0mAdding dependency [0m[1m[32mdev.langchain4j:langchain4j:0.36.2
    [0mAdding dependency [0m[1m[32mdev.langchain4j:langchain4j-open-ai:0.36.2
    [0mAdding dependency [0m[1m[32mnet.sourceforge.plantuml:plantuml-mit:1.2024.8
    [0mSolving dependencies
    Resolved artifacts count: 27
    Add to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/slf4j/slf4j-jdk14/2.0.9/slf4j-jdk14-2.0.9.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/bsc/langgraph4j/langgraph4j-core/1.4-SNAPSHOT/langgraph4j-core-1.4-SNAPSHOT.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/bsc/async/async-generator/3.0.0/async-generator-3.0.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/bsc/langgraph4j/langgraph4j-langchain4j/1.4-SNAPSHOT/langgraph4j-langchain4j-1.4-SNAPSHOT.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/dev/langchain4j/langchain4j/0.36.2/langchain4j-0.36.2.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/dev/langchain4j/langchain4j-core/0.36.2/langchain4j-core-0.36.2.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/apache/opennlp/opennlp-tools/1.9.4/opennlp-tools-1.9.4.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/dev/langchain4j/langchain4j-open-ai/0.36.2/langchain4j-open-ai-0.36.2.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/dev/ai4j/openai4j/0.23.0/openai4j-0.23.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/squareup/retrofit2/retrofit/2.9.0/retrofit-2.9.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/squareup/retrofit2/converter-jackson/2.9.0/converter-jackson-2.9.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/fasterxml/jackson/core/jackson-databind/2.17.2/jackson-databind-2.17.2.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/fasterxml/jackson/core/jackson-annotations/2.17.2/jackson-annotations-2.17.2.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/fasterxml/jackson/core/jackson-core/2.17.2/jackson-core-2.17.2.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/squareup/okhttp3/okhttp/4.12.0/okhttp-4.12.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/squareup/okio/okio/3.6.0/okio-3.6.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/squareup/okio/okio-jvm/3.6.0/okio-jvm-3.6.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/jetbrains/kotlin/kotlin-stdlib-common/1.9.10/kotlin-stdlib-common-1.9.10.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/squareup/okhttp3/okhttp-sse/4.12.0/okhttp-sse-4.12.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/jetbrains/kotlin/kotlin-stdlib-jdk8/1.9.25/kotlin-stdlib-jdk8-1.9.25.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/jetbrains/kotlin/kotlin-stdlib/1.9.25/kotlin-stdlib-1.9.25.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/jetbrains/annotations/13.0/annotations-13.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/jetbrains/kotlin/kotlin-stdlib-jdk7/1.9.25/kotlin-stdlib-jdk7-1.9.25.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/knuddels/jtokkit/1.1.0/jtokkit-1.1.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/net/sourceforge/plantuml/plantuml-mit/1.2024.8/plantuml-mit-1.2024.8.jar[0m
    [0m

**utility to render graph respresentation in PlantUML**


```java
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.FileFormat;
import org.bsc.langgraph4j.GraphRepresentation;

void displayDiagram( GraphRepresentation representation ) throws IOException { 
    
    var reader = new SourceStringReader(representation.getContent());

    try(var imageOutStream = new java.io.ByteArrayOutputStream()) {

        var description = reader.outputImage( imageOutStream, 0, new FileFormatOption(FileFormat.PNG));

        var imageInStream = new java.io.ByteArrayInputStream(  imageOutStream.toByteArray() );

        var image = javax.imageio.ImageIO.read( imageInStream );

        display(  image );

    }
}
```

**Graph State**


```java
import org.bsc.langgraph4j.prebuilt.MessagesState;
import org.bsc.langgraph4j.state.Channel;
import org.bsc.langgraph4j.state.AppenderChannel;

public class State extends MessagesState<String> {

    public State(Map<String, Object> initData) {
        super( initData  );
    }
}
```

**Utility action to simplify node creation**


```java
import org.bsc.langgraph4j.action.AsyncNodeAction;

AsyncNodeAction<State> makeNode( String id ) {
    return node_async(state ->
            Map.of("messages", id)
    );
}
```

**Create a subgraph as state graph**


```java
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;

var workflowChild = new StateGraph<>(State.SCHEMA, State::new)        
                    .addNode("child:step_1", makeNode("child:step1") )
                    .addNode("child:step_2", makeNode("child:step2"))
                    .addNode("child:step_3", makeNode("child:step3"))
                    .addEdge(START, "child:step_1")
                    .addEdge("child:step_1", "child:step_2")
                    .addConditionalEdges(  "child:step_2",
                                edge_async(state -> "continue"),
                                Map.of( END, END, "continue", "child:step_3") )
                    .addEdge("child:step_3", END)
                    ;
```

**Create graph with a sub graph as state graph**
> The subgraph will be merged to the parent and executed as part of it, sharing its state and also `CompileConfig`


```java

var workflow = new StateGraph<>(State.SCHEMA, State::new)        
                    .addNode("step_1",  makeNode("step1"))
                    .addNode("step_2",  makeNode("step2"))
                    .addNode("step_3",  makeNode("step3"))
                    .addSubgraph( "subgraph", workflowChild )
                    .addEdge(START, "step_1")
                    .addEdge("step_1", "step_2")
                    .addEdge("step_2", "subgraph")
                    .addEdge("subgraph", "step_3")
                    .addEdge("step_3", END)
                    ;
var compiledWorkflow =  workflow.compile();

for( var step : compiledWorkflow.stream( Map.of() )) {
    System.out.println( step );
}
```

    NodeOutput{node=__START__, state={messages=[]}}
    NodeOutput{node=step_1, state={messages=[step1]}}
    NodeOutput{node=step_2, state={messages=[step1, step2]}}
    NodeOutput{node=(subgraph)child:step_1, state={messages=[step1, step2, child:step1]}}
    NodeOutput{node=(subgraph)child:step_2, state={messages=[step1, step2, child:step1, child:step2]}}
    NodeOutput{node=(subgraph)child:step_3, state={messages=[step1, step2, child:step1, child:step2, child:step3]}}
    NodeOutput{node=step_3, state={messages=[step1, step2, child:step1, child:step2, child:step3, step3]}}
    NodeOutput{node=__END__, state={messages=[step1, step2, child:step1, child:step2, child:step3, step3]}}


**Display StateGraph** as you defined


```java
var representation = workflow.getGraph( GraphRepresentation.Type.PLANTUML, "sub graph", false );

displayDiagram( representation );
```


    
![png](subgraph-as-stategraph_files/subgraph-as-stategraph_15_0.png)
    


**Display Compiled Graph** result as merged process


```java
var representation = compiledWorkflow.getGraph( GraphRepresentation.Type.PLANTUML, "merged sub graph", false );

displayDiagram( representation );
```


    
![png](subgraph-as-stategraph_files/subgraph-as-stategraph_17_0.png)
    

