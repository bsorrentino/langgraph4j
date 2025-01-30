# Sub-graph sample

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


```java
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;


AsyncNodeAction<State> childStep1 = node_async(state -> Map.of( "messages", "child:step1") );

AsyncNodeAction<State> childStep2 = node_async(state -> Map.of( "messages", "child:step2") );

AsyncNodeAction<State> childStep3 = node_async(state -> Map.of( "messages", "child:step3") );


var workflowChild = new StateGraph<>(State.SCHEMA, State::new)        
                    .addNode("child:step_1", childStep1)
                    .addNode("child:step_2", childStep2)
                    .addNode("child:step_3", childStep3)
                    .addEdge(START, "child:step_1")
                    .addEdge("child:step_1", "child:step_2")
                    .addEdge("child:step_2", "child:step_3")
                    .addEdge("child:step_3", END)
                    .compile();

for( var step : workflowChild.stream( Map.of() ) ) {
    System.out.println( step );
}
```

    NodeOutput{node=__START__, state={messages=[]}}
    NodeOutput{node=child:step_1, state={messages=[child:step1]}}
    NodeOutput{node=child:step_2, state={messages=[child:step1, child:step2]}}
    NodeOutput{node=child:step_3, state={messages=[child:step1, child:step2, child:step3]}}
    NodeOutput{node=__END__, state={messages=[child:step1, child:step2, child:step3]}}



```java

AsyncNodeAction<State> step1 = node_async(state -> Map.of( "messages", "step1") );

AsyncNodeAction<State> step2 = node_async(state -> Map.of( "messages", "step2") );

AsyncNodeAction<State> step3 = node_async(state -> Map.of( "messages", "step3") );

var workflowParent = new StateGraph<>(State.SCHEMA, State::new)        
                    .addNode("step_1", step1)
                    .addNode("step_2", step2)
                    .addNode("step_3", step3)
                    .addSubgraph( "subgraph", workflowChild )
                    .addEdge(START, "step_1")
                    .addEdge("step_1", "step_2")
                    .addEdge("step_2", "subgraph")
                    .addEdge("subgraph", "step_3")
                    .addEdge("step_3", END)
                    .compile();

for( var step : workflowParent.stream( Map.of() )) {
    System.out.println( step );
}
```

    NodeOutput{node=__START__, state={messages=[]}}
    NodeOutput{node=step_1, state={messages=[step1]}}
    NodeOutput{node=step_2, state={messages=[step1, step2]}}
    NodeOutput{node=__START__, state={messages=[step1, step2]}}
    NodeOutput{node=child:step_1, state={messages=[step1, step2, child:step1]}}
    NodeOutput{node=child:step_2, state={messages=[step1, step2, child:step1, child:step2]}}
    NodeOutput{node=child:step_3, state={messages=[step1, step2, child:step1, child:step2, child:step3]}}
    NodeOutput{node=__END__, state={messages=[step1, step2, child:step1, child:step2, child:step3]}}
    NodeOutput{node=subgraph, state={messages=[step1, step2, child:step1, child:step2, child:step3]}}
    NodeOutput{node=step_3, state={messages=[step1, step2, child:step1, child:step2, child:step3, step3]}}
    NodeOutput{node=__END__, state={messages=[step1, step2, child:step1, child:step2, child:step3, step3]}}



```java
import org.bsc.langgraph4j.GraphRepresentation;

var representation = workflowParent.getGraph( GraphRepresentation.Type.PLANTUML, "sub graph",false );

representation.getContent()

```




    @startuml sub_graph
    skinparam usecaseFontSize 14
    skinparam usecaseStereotypeFontSize 12
    skinparam hexagonFontSize 14
    skinparam hexagonStereotypeFontSize 12
    title "sub graph"
    footer
    
    powered by langgraph4j
    end footer
    circle start<<input>> as __START__
    circle stop as __END__
    usecase "step_1"<<Node>>
    usecase "step_2"<<Node>>
    usecase "step_3"<<Node>>
    rectangle subgraph [ {{
    title "subgraph"
    circle " " as __START__
    circle exit as __END__
    usecase "child:step_1"<<Node>>
    usecase "child:step_2"<<Node>>
    usecase "child:step_3"<<Node>>
    "__START__" -down-> "child:step_1"
    "child:step_1" -down-> "child:step_2"
    "child:step_2" -down-> "child:step_3"
    "child:step_3" -down-> "__END__"
    
    }} ]
    "__START__" -down-> "step_1"
    "step_1" -down-> "step_2"
    "step_2" -down-> "subgraph"
    "subgraph" -down-> "step_3"
    "step_3" -down-> "__END__"
    @enduml





```java
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.FileFormat;

java.awt.Image plantUML2PNG( String code ) throws IOException { 
    var reader = new SourceStringReader(code);

    try(var imageOutStream = new java.io.ByteArrayOutputStream()) {

        var description = reader.outputImage( imageOutStream, 0, new FileFormatOption(FileFormat.PNG));

        var imageInStream = new java.io.ByteArrayInputStream(  imageOutStream.toByteArray() );

        return javax.imageio.ImageIO.read( imageInStream );

    }
}

display( plantUML2PNG( representation.getContent() ) )
```


    
![png](subgraph_files/subgraph_8_0.png)
    





    cf5b3b68-c6d9-4fdf-a123-6a133b46b164


