# Issue [#99](https://github.com/bsorrentino/langgraph4j/issues/99) by [zu1k](https://github.com/zu1k)

Verify "**Edge Update Logic Error During Subgraph Processing**" 

**Initialize Logger**


```java
try( var file = new java.io.FileInputStream("./logging.properties")) {
    java.util.logging.LogManager.getLogManager().readConfiguration( file );
}

var log = org.slf4j.LoggerFactory.getLogger("AdaptiveRag");

```

## State declaration


```java
import org.bsc.langgraph4j.action.NodeAction;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.StateGraph;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

import java.util.Map;
import java.util.Optional;

class State extends AgentState {

    public State(Map<String, Object> initData) {
        super(initData);
    }

    public Optional<String> intent() {
        return  value("intent");
    }
}

```

## Subgraph definitions


```java
// SubGraph1 Definition
StateGraph<State> subGraph1 = new StateGraph<>( State::new )
            .addNode("work", node_async(state -> Map.of("step", "work1")))
            .addEdge(START, "work")
            .addEdge("work", END)
            ;

// SubGraph2 Definition
StateGraph<State> subGraph2 = new StateGraph<>( State::new )
            .addNode("work", node_async(state -> Map.of("step", "work2")))
            .addEdge(START, "work")
            .addEdge("work", END)
            ;

```

## IntentRecognize node declaration


```java
class IntentRecognizeNode implements NodeAction<State> {

    String intent;

    public void setIntent( String intent ) {
        this.intent = intent;
    }

    public String getIntent() {
        return intent;
    }

    @Override
    public Map<String, Object> apply(State state) {
        return Map.of( "intent", intent );
    }

}

```

## Using Subgraph as StateGraph


```java
var intentRecognizeNode = new IntentRecognizeNode();

// MainGraph Definition
var graph = new StateGraph<>( State::new )
        .addNode("intent_recognize", node_async(intentRecognizeNode))
        .addNode("subAgent1", subGraph1 )
        .addNode("subAgent2", subGraph2 )
        .addEdge(START, "intent_recognize")
        .addConditionalEdges("intent_recognize",
                edge_async( state ->
                        state.intent().orElseThrow() ),
                Map.of("explain", "subAgent1",
                        "query", "subAgent2"
                )
        )
        .addEdge("subAgent1", END)
        .addEdge("subAgent2", END)
        ;

var workflow = graph.compile();

// System.out.println( workflow.getGraph( GraphRepresentation.Type.PLANTUML, "", false ));

// EXPLAIN
log.info( "EXPLAIN");
intentRecognizeNode.setIntent("explain");
for( var output : workflow.stream( Map.of("input", "explain me") ) ) {
       log.info( "{}", output );
}

// QUERY
log.info( "QUERY");
intentRecognizeNode.setIntent("query");
for( var output : workflow.stream( Map.of("input", "search for") ) ) {
        log.info( "{}", output );
}
         

```

    EXPLAIN 
    START 
    NodeOutput{node=__START__, state={input=explain me}} 
    NodeOutput{node=intent_recognize, state={input=explain me, intent=explain}} 
    NodeOutput{node=subAgent1-work, state={input=explain me, step=work1, intent=explain}} 
    NodeOutput{node=__END__, state={input=explain me, step=work1, intent=explain}} 
    QUERY 
    START 
    NodeOutput{node=__START__, state={input=search for}} 
    NodeOutput{node=intent_recognize, state={input=search for, intent=query}} 
    NodeOutput{node=subAgent2-work, state={input=search for, step=work2, intent=query}} 
    NodeOutput{node=__END__, state={input=search for, step=work2, intent=query}} 


## Using Subgraph as CompiledGraph


```java
var intentRecognizeNode = new IntentRecognizeNode();

// MainGraph Definition
var graph = new StateGraph<>( State::new )
        .addNode("intent_recognize", node_async(intentRecognizeNode))
        .addNode("subAgent1", subGraph1.compile() )
        .addNode("subAgent2", subGraph2.compile() )
        .addEdge(START, "intent_recognize")
        .addConditionalEdges("intent_recognize",
                edge_async( state ->
                        state.intent().orElseThrow() ),
                Map.of("explain", "subAgent1",
                        "query", "subAgent2"
                )
        )
        .addEdge("subAgent1", END)
        .addEdge("subAgent2", END)
        ;

var workflow = graph.compile();

// System.out.println( workflow.getGraph( GraphRepresentation.Type.PLANTUML, "", false ));

// EXPLAIN
log.info( "EXPLAIN");
intentRecognizeNode.setIntent("explain");
for( var output : workflow.stream( Map.of("input", "explain me") ) ) {
       log.info( "{}", output );
}

// QUERY
log.info( "QUERY");
intentRecognizeNode.setIntent("query");
for( var output : workflow.stream( Map.of("input", "search for") ) ) {
        log.info( "{}", output );
}
         

```

    EXPLAIN 
    START 
    NodeOutput{node=__START__, state={input=explain me}} 
    START 
    NodeOutput{node=intent_recognize, state={input=explain me, intent=explain}} 
    NodeOutput{node=__START__, state={input=explain me, intent=explain}} 
    NodeOutput{node=work, state={input=explain me, step=work1, intent=explain}} 
    NodeOutput{node=__END__, state={input=explain me, step=work1, intent=explain}} 
    NodeOutput{node=subAgent1, state={input=explain me, step=work1, intent=explain}} 
    NodeOutput{node=__END__, state={input=explain me, step=work1, intent=explain}} 
    QUERY 
    START 
    NodeOutput{node=__START__, state={input=search for}} 
    START 
    NodeOutput{node=intent_recognize, state={input=search for, intent=query}} 
    NodeOutput{node=__START__, state={input=search for, intent=query}} 
    NodeOutput{node=work, state={input=search for, step=work2, intent=query}} 
    NodeOutput{node=__END__, state={input=search for, step=work2, intent=query}} 
    NodeOutput{node=subAgent2, state={input=search for, step=work2, intent=query}} 
    NodeOutput{node=__END__, state={input=search for, step=work2, intent=query}} 

