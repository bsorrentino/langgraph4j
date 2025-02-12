# Subgraph as compiled graph sample

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

**Create a subgraph as compiled graph**


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
var compiledWorkflowChild = workflowChild.compile();

```

**Create graph with a sub graph as compiled graph**
> The subgraph will be executed independently to the parent sharing its state but not `CompileConfig`


```java

var workflow = new StateGraph<>(State.SCHEMA, State::new)        
                    .addNode("step_1",  makeNode("step1"))
                    .addNode("step_2",  makeNode("step2"))
                    .addNode("step_3",  makeNode("step3"))
                    .addSubgraph( "subgraph", compiledWorkflowChild )
                    .addEdge(START, "step_1")
                    .addEdge("step_1", "step_2")
                    .addEdge("step_2", "subgraph")
                    .addEdge("subgraph", "step_3")
                    .addEdge("step_3", END)
                    ;

var compiledWorkflow = workflow.compile();

for( var step : compiledWorkflow.stream( Map.of() )) {
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


**Display StateGraph** as you defined


```java
var representation = compiledWorkflow.getGraph( GraphRepresentation.Type.PLANTUML, "sub graph", false );

displayDiagram( representation );
```


    
![png](subgraph-as-compiledgraph_files/subgraph-as-compiledgraph_15_0.png)
    


**Display Compiled Graph result as graph processing**
> in such case the result will be the same of previous one because no merge will be done


```java
var representation = compiledWorkflow.getGraph( GraphRepresentation.Type.PLANTUML, "merged sub graph", false );

displayDiagram( representation );
```


    
![png](subgraph-as-compiledgraph_files/subgraph-as-compiledgraph_17_0.png)
    

