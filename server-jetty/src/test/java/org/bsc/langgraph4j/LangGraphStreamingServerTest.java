package org.bsc.langgraph4j;

import org.bsc.langgraph4j.action.EdgeAction;
import org.bsc.langgraph4j.state.AgentState;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import static org.bsc.langgraph4j.utils.CollectionsUtils.mapOf;

public class LangGraphStreamingServerTest {


    public static void main(String[] args) throws Exception {
        StateGraph<AgentState> workflow = new StateGraph<>(AgentState::new);

        workflow.setEntryPoint("agent_1");

        workflow.addNode("agent_1", node_async((state ) -> {
            System.out.println("agent_1 ");
            System.out.println(state);
            return mapOf("prop1", "value1");
        }) ) ;

        workflow.addNode("agent_2", node_async( state  -> {

            System.out.print( "agent_2: ");
            System.out.println( state );
            return mapOf("prop2", "value2");
        }));

        workflow.addEdge("agent_2", "agent_1" );


        EdgeAction<AgentState> conditionalAge  = new EdgeAction<>() {
            int steps= 0;
            @Override
            public String apply(AgentState state) {
                if( ++steps == 2 ) {
                    steps = 0;
                    return "end";
                }
                return "a2";
            }
        };

        workflow.addConditionalEdges("agent_1",
                edge_async(conditionalAge), mapOf( "a2", "agent_2", "end", END ) );

        CompiledGraph<AgentState> app = workflow.compile();

        LangGraphStreamingServer server = LangGraphStreamingServer.builder()
                                                                    .port(8080)
                                                                    .title("LANGGRAPH4j - TEST")
                                                                    .addInputStringArg("input")
                                                                    .build(app);

        server.start().join();

    }

}
