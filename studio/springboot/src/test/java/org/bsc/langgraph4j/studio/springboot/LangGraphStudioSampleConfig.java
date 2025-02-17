package org.bsc.langgraph4j.studio.springboot;

import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.action.EdgeAction;
import org.bsc.langgraph4j.prebuilt.MessagesState;
import org.bsc.langgraph4j.prebuilt.MessagesStateGraph;
import org.bsc.langgraph4j.state.AgentState;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

@Configuration
public class LangGraphStudioSampleConfig extends AbstractLangGraphStudioConfig {

    final LangGraphFlow flow;

    public LangGraphStudioSampleConfig() throws GraphStateException {
        // this.flow = sampleFlow();
        // this.flow = addStateSubgraphSample();
        this.flow = withCompiledSubgraphSample();
    }

    private AsyncNodeAction<MessagesState<String>> _makeNode(String id ) {
        return node_async(state ->
                Map.of("messages", id)
        );
    }

    private LangGraphFlow sampleFlow() throws GraphStateException {

        final EdgeAction<AgentState> conditionalAge  = new EdgeAction<>() {
            int steps= 0;
            @Override
            public String apply(AgentState state) {
                if( ++steps == 2 ) {
                    steps = 0;
                    return "end";
                }
                return "next";
            }
        };

        var workflow = new StateGraph<>(AgentState::new)
                .addNode("agent", node_async((state ) -> {
                    System.out.println("agent ");
                    System.out.println(state);
                    if( state.value( "action_response").isPresent() ) {
                        return Map.of("agent_summary", "This is just a DEMO summary");
                    }
                    return Map.of("agent_response", "This is an Agent DEMO response");
                }) )
                .addNode("action", node_async( state  -> {
                    System.out.print( "action: ");
                    System.out.println( state );
                    return Map.of("action_response", "This is an Action DEMO response");
                }))
                .addEdge(START, "agent")
                .addEdge("action", "agent" )
                .addConditionalEdges("agent",
                        edge_async(conditionalAge), Map.of( "next", "action", "end", END ) )
                ;

        return  LangGraphFlow.builder()
                .title("LangGraph Studio (Sample)")
                .stateGraph( workflow )
                .build();

    }

    private LangGraphFlow withStateSubgraphSample() throws GraphStateException {
        var workflowChild = new MessagesStateGraph<String>()
                .addNode("B1", _makeNode("B1") )
                .addNode("B2", _makeNode( "B2" ) )
                .addNode("C", _makeNode( "subgraph(C)" ) )
                .addEdge(START, "B1")
                .addEdge("B1", "B2")
                .addConditionalEdges( "B2",
                        edge_async(state -> "c"),
                        Map.of( END, END, "c", "C") )
                .addEdge("C", END)
                ;

        var workflowParent = new MessagesStateGraph<String>()
                .addNode("A", _makeNode("A") )
                .addSubgraph("B",  workflowChild )
                .addNode("C", _makeNode("C") )
                .addConditionalEdges(START,
                        edge_async(state -> "a"),
                        Map.of( "a", "A", "b", "B") )
                .addEdge("A", "B")
                .addEdge("B", "C")
                .addEdge("C", END)
                //.compile(compileConfig)
                ;

        return  LangGraphFlow.builder()
                .title("LangGraph Studio (Merged Subgraph)")
                .stateGraph( workflowParent )
                .build();

    }

    private LangGraphFlow withCompiledSubgraphSample() throws GraphStateException {
        var workflowChild = new MessagesStateGraph<String>()
                .addNode("B1", _makeNode("B1") )
                .addNode("B2", _makeNode( "B2" ) )
                .addNode("C", _makeNode( "subgraph(C)" ) )
                .addEdge(START, "B1")
                .addEdge("B1", "B2")
                .addConditionalEdges( "B2",
                        edge_async(state -> "c"),
                        Map.of( END, END, "c", "C") )
                .addEdge("C", END)
                .compile()
                ;

        var workflowParent = new MessagesStateGraph<String>()
                .addNode("A", _makeNode("A") )
                .addSubgraph("B",  workflowChild )
                .addNode("C", _makeNode("C") )
                .addConditionalEdges(START,
                        edge_async(state -> "a"),
                        Map.of( "a", "A", "b", "B") )
                .addEdge("A", "B")
                .addEdge("B", "C")
                .addEdge("C", END)
                //.compile(compileConfig)
                ;

        return  LangGraphFlow.builder()
                .title("LangGraph Studio (Compiled Subgraph)")
                .stateGraph( workflowParent )
                .build();

    }

    @Override
    public LangGraphFlow getFlow() {
        return this.flow;
    }
}
