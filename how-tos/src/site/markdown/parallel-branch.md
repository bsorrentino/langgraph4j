# How to create branches for parallel node execution

LangGraph4j lets you run nodes in parallel to speed up your total graph execution.

Currently there are some limitations on parallel execution:

 
* Only the **Fork-Join** model is supported
 
  > ```
  >        ┌─┐       
  >        │A│       
  >        └─┘       
  >         |        
  >   ┌-----------┐  
  >   |     |     |  
  > ┌──┐  ┌──┐  ┌──┐ 
  > │A1│  │A2│  │A3│ 
  > └──┘  └──┘  └──┘ 
  >   |     |     |  
  >   └-----------┘  
  >         |        
  >        ┌─┐       
  >        │B│       
  >        └─┘       
  >   ```
* No **Conditional Edges** are allowed
  
Below are some examples showing how to add create branching dataflows that work for you.


```java
try( var file = new java.io.FileInputStream("./logging.properties")) {
    java.util.logging.LogManager.getLogManager().readConfiguration( file );
}
```


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
```

## Define Graph with parallel branch


```java
import org.bsc.langgraph4j.prebuilt.MessagesStateGraph;
import org.bsc.langgraph4j.prebuilt.MessagesState;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;

AsyncNodeAction<MessagesState<String>> makeNode( String message ) {
    return node_async(state -> Map.of( "messages", message ) );
}

var workflow = new MessagesStateGraph<String>()
                .addNode("A", makeNode("A"))
                .addNode("A1", makeNode("A1"))
                .addNode("A2", makeNode("A2"))
                .addNode("A3", makeNode("A3"))
                .addNode("B", makeNode("B"))
                .addNode("C", makeNode("C"))
                .addEdge("A", "A1")
                .addEdge("A", "A2")
                .addEdge("A", "A3")
                .addEdge("A1", "B")
                .addEdge("A2", "B")
                .addEdge("A3", "B")
                .addEdge("B", "C")
                .addEdge(START, "A")
                .addEdge("C", END)                   
                .compile();

```

## Print graph representation


```java
import org.bsc.langgraph4j.GraphRepresentation;

var representation = workflow.getGraph( GraphRepresentation.Type.PLANTUML, "parallel branch",false );

display( plantUML2PNG( representation.getContent() ) )
```


    
![png](parallel-branch_files/parallel-branch_9_0.png)
    





    3b2a2a5b-de5a-4273-bcbf-68e021d55e2e




```java
for( var step : workflow.stream( Map.of() ) ) {
    System.out.println( step );
}
```

    START 


    NodeOutput{node=__START__, state={messages=[]}}
    NodeOutput{node=A, state={messages=[A]}}
    NodeOutput{node=__PARALLEL__(A), state={messages=[A, A1, A2, A3]}}
    NodeOutput{node=B, state={messages=[A, A1, A2, A3, B]}}
    NodeOutput{node=C, state={messages=[A, A1, A2, A3, B, C]}}
    NodeOutput{node=__END__, state={messages=[A, A1, A2, A3, B, C]}}



```java
import org.bsc.langgraph4j.prebuilt.MessagesStateGraph;
import org.bsc.langgraph4j.prebuilt.MessagesState;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.utils.EdgeMappings;

import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;


AsyncNodeAction<MessagesState<String>> makeNode( String message ) {
    return node_async(state -> Map.of( "messages", message ) );
}

var workflow = new MessagesStateGraph<String>()
                .addNode("A", makeNode("A"))
                .addNode("A1", makeNode("A1"))
                .addNode("A2", makeNode("A2"))
                .addNode("A3", makeNode("A3"))
                .addNode("B", makeNode("B"))
                .addNode("C", makeNode("C"))
                .addEdge("A", "A1")
                .addEdge("A", "A2")
                .addEdge("A", "A3")
                .addEdge("A1", "B")
                .addEdge("A2", "B")
                .addEdge("A3", "B")
                // .addEdge("B", "C")
                .addConditionalEdges( "B", 
                    edge_async( state -> 
                        state.lastMinus(1) 
                            .filter( m -> Objects.equals(m,"A1"))
                            .map( m -> "continue" )
                            .orElse("back") ), 
                    EdgeMappings.builder()
                        .to( "A1", "back" )
                        .to( "C" , "continue")
                        .build()
                 )
                .addEdge(START, "A")
                .addEdge("C", END)                   
                .compile();

```


```java
import org.bsc.langgraph4j.GraphRepresentation;

var representation = workflow.getGraph( GraphRepresentation.Type.PLANTUML, "parallel branch",false );

display( plantUML2PNG( representation.getContent() ) )
```


    
![png](parallel-branch_files/parallel-branch_12_0.png)
    





    abfc2bdf-024d-4376-818e-886c8509209f




```java
for( var step : workflow.stream( Map.of() ) ) {
    System.out.println( step );
}
```

    START 


    NodeOutput{node=__START__, state={messages=[]}}
    NodeOutput{node=A, state={messages=[A]}}
    NodeOutput{node=__PARALLEL__(A), state={messages=[A, A1, A2, A3]}}
    NodeOutput{node=B, state={messages=[A, A1, A2, A3, B]}}
    NodeOutput{node=A1, state={messages=[A, A1, A2, A3, B]}}
    NodeOutput{node=B, state={messages=[A, A1, A2, A3, B]}}
    NodeOutput{node=A1, state={messages=[A, A1, A2, A3, B]}}
    NodeOutput{node=B, state={messages=[A, A1, A2, A3, B]}}
    NodeOutput{node=A1, state={messages=[A, A1, A2, A3, B]}}
    NodeOutput{node=B, state={messages=[A, A1, A2, A3, B]}}
    NodeOutput{node=A1, state={messages=[A, A1, A2, A3, B]}}
    NodeOutput{node=B, state={messages=[A, A1, A2, A3, B]}}
    NodeOutput{node=A1, state={messages=[A, A1, A2, A3, B]}}
    NodeOutput{node=B, state={messages=[A, A1, A2, A3, B]}}
    NodeOutput{node=A1, state={messages=[A, A1, A2, A3, B]}}
    NodeOutput{node=B, state={messages=[A, A1, A2, A3, B]}}
    NodeOutput{node=A1, state={messages=[A, A1, A2, A3, B]}}
    NodeOutput{node=B, state={messages=[A, A1, A2, A3, B]}}
    NodeOutput{node=A1, state={messages=[A, A1, A2, A3, B]}}
    NodeOutput{node=B, state={messages=[A, A1, A2, A3, B]}}
    NodeOutput{node=A1, state={messages=[A, A1, A2, A3, B]}}
    NodeOutput{node=B, state={messages=[A, A1, A2, A3, B]}}
    NodeOutput{node=A1, state={messages=[A, A1, A2, A3, B]}}
    NodeOutput{node=B, state={messages=[A, A1, A2, A3, B]}}


    Maximum number of iterations (25) reached! 


    NodeOutput{node=A1, state={messages=[A, A1, A2, A3, B]}}


## Use compiled sub graph as parallel node

This example answer to issue **Will plan support multiple target on parallel node?** [#104](https://github.com/bsorrentino/langgraph4j/issues/104) 


```java
import org.bsc.langgraph4j.prebuilt.MessagesStateGraph;
import org.bsc.langgraph4j.prebuilt.MessagesState;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;

AsyncNodeAction<MessagesState<String>> makeNode( String message ) {
    return node_async(state -> Map.of( "messages", message ) );
}

var subgraphA3 = new MessagesStateGraph<String>()
                .addNode("A3.1", makeNode("A3.1"))
                .addNode("A3.2", makeNode("A3.2"))
                .addEdge(START, "A3.1")
                .addEdge( "A3.1", "A3.2")
                .addEdge("A3.2", END)   
                .compile(); 
var subgraphA1 = new MessagesStateGraph<String>()
                .addNode("A1.1", makeNode("A1.1"))
                .addNode("A1.2", makeNode("A1.2"))
                .addEdge(START, "A1.1")
                .addEdge( "A1.1", "A1.2")
                .addEdge("A1.2", END)   
                .compile(); 

var workflow = new MessagesStateGraph<String>()
                .addNode("A", makeNode("A"))
                .addNode("A1", subgraphA1)
                .addNode("A2", makeNode("A2"))
                .addNode("A3", subgraphA3)
                .addNode("B", makeNode("B"))
                .addEdge("A", "A1")
                .addEdge("A", "A2")
                .addEdge("A", "A3")
                .addEdge("A1", "B")
                .addEdge("A2", "B")
                .addEdge("A3", "B")
                .addEdge(START, "A")
                .addEdge("B", END)                   
                .compile();

```


```java
import org.bsc.langgraph4j.GraphRepresentation;

var representation = workflow.getGraph( GraphRepresentation.Type.PLANTUML, "parallel branch",false );

display( plantUML2PNG( representation.getContent() ) )
```


    
![png](parallel-branch_files/parallel-branch_16_0.png)
    





    e885507c-a8ad-4adc-a8bc-3659e5eb0742




```java
// workflow.getGraph( GraphRepresentation.Type.MERMAID, "parallel branch",false ).content();
```


```java
for( var step : workflow.stream( Map.of() ) ) {
    System.out.println( step );
}
```

    START 


    NodeOutput{node=__START__, state={messages=[]}}


    START 
    START 


    NodeOutput{node=A, state={messages=[A]}}
    NodeOutput{node=__PARALLEL__(A), state={messages=[A, A1.1, A1.2, A2, A3.1, A3.2]}}
    NodeOutput{node=B, state={messages=[A, A1.1, A1.2, A2, A3.1, A3.2, B]}}
    NodeOutput{node=__END__, state={messages=[A, A1.1, A1.2, A2, A3.1, A3.2, B]}}



```java
import org.bsc.langgraph4j.prebuilt.MessagesStateGraph;
import org.bsc.langgraph4j.prebuilt.MessagesState;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;

AsyncNodeAction<MessagesState<String>> makeNode( String message ) {
    return node_async(state -> Map.of( "messages", message ) );
}

var subgraphA3 = new MessagesStateGraph<String>()
                .addNode("A3.1", makeNode("A3.1"))
                .addNode("A3.2", makeNode("A3.2"))
                .addEdge(START, "A3.1")
                .addEdge( "A3.1", "A3.2")
                .addEdge("A3.2", END)   
                .compile(); 

var subgraphA2 = new MessagesStateGraph<String>()
                .addNode("A2.1", makeNode("A2.1"))
                .addNode("A2.2", makeNode("A2.2"))
                .addEdge(START, "A2.1")
                .addEdge( "A2.1", "A2.2")
                .addEdge("A2.2", END)   
                .compile(); 

var subgraphA1 = new MessagesStateGraph<String>()
                .addNode("A1.1", makeNode("A1.1"))
                .addNode("A1.2", makeNode("A1.2"))
                .addEdge(START, "A1.1")
                .addEdge( "A1.1", "A1.2")
                .addEdge("A1.2", END)   
                .compile(); 

var workflow = new MessagesStateGraph<String>()
                .addNode("A", makeNode("A"))
                .addNode("A1", subgraphA1)
                .addNode("A2", subgraphA2)
                .addNode("A3", subgraphA3)
                .addNode("B", makeNode("B"))
                .addEdge("A", "A1")
                .addEdge("A", "A2")
                .addEdge("A", "A3")
                .addEdge("A1", "B")
                .addEdge("A2", "B")
                .addEdge("A3", "B")
                .addEdge(START, "A")
                .addEdge("B", END)                   
                .compile();

```


```java
import org.bsc.langgraph4j.GraphRepresentation;

var representation = workflow.getGraph( GraphRepresentation.Type.PLANTUML, "parallel branch",false );

display( plantUML2PNG( representation.getContent() ) )
```


    
![png](parallel-branch_files/parallel-branch_20_0.png)
    





    0113f2c8-6566-425a-8018-4a621097b864




```java
for( var step : workflow.stream( Map.of() ) ) {
    System.out.println( step );
}
```

    START 


    NodeOutput{node=__START__, state={messages=[]}}


    START 
    START 
    START 


    NodeOutput{node=A, state={messages=[A]}}
    NodeOutput{node=__PARALLEL__(A), state={messages=[A, A1.1, A1.2, A2.1, A2.2, A3.1, A3.2]}}
    NodeOutput{node=B, state={messages=[A, A1.1, A1.2, A2.1, A2.2, A3.1, A3.2, B]}}
    NodeOutput{node=__END__, state={messages=[A, A1.1, A1.2, A2.1, A2.2, A3.1, A3.2, B]}}

