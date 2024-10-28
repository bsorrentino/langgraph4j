package org.bsc.langgraph4j.jetty;

import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.EdgeAction;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.studio.jetty.LangGraphStreamingServerJetty;

import java.util.Map;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

public class LangGraphStreamingServerTest {


    public static void main(String[] args) throws Exception {
        EdgeAction<AgentState> conditionalAge  = new EdgeAction<>() {
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


        StateGraph<AgentState> workflow = new StateGraph<>(AgentState::new)
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

        var server = LangGraphStreamingServerJetty.builder()
                                                .port(8080)
                                                .title("LANGGRAPH4j STUDIO - DEMO")
                                                .addInputStringArg("input")
                                                .stateGraph(workflow)
                                                .build();

        server.start().join();

    }

}
