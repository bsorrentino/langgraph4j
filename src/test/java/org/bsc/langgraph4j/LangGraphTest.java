package org.bsc.langgraph4j;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.bsc.langgraph4j.GraphState.END;
import static org.bsc.langgraph4j.NodeAction.async;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
public class LangGraphTest
{
    record BaseAgentState( Map<String,Object> data ) implements AgentState {}

    @Test
    void testValidation() throws Exception {

        var workflow = new GraphState<BaseAgentState>(BaseAgentState::new);
        var exception = assertThrows(GraphStateException.class, workflow::compile);
        System.out.println(exception.getMessage());
        assertEquals( "missing Entry Point", exception.getMessage());

        workflow.setEntryPoint("agent_1");

        exception = assertThrows(GraphStateException.class, workflow::compile);
        System.out.println(exception.getMessage());
        assertEquals( "entryPoint: agent_1 doesn't exist!", exception.getMessage());

        workflow.addNode("agent_1", async(( state ) -> {
            System.out.print("agent_1 ");
            System.out.println(state);
            return Map.of("prop1", "test");
        }) ) ;

        assertNotNull(workflow.compile());

        workflow.addEdge( "agent_1",  END);

        assertNotNull(workflow.compile());

        exception = assertThrows(GraphStateException.class, () ->
            workflow.addEdge(END, "agent_1") );
        System.out.println(exception.getMessage());

        exception = assertThrows(GraphStateException.class, () ->
                workflow.addEdge("agent_1", "agent_2") );
        System.out.println(exception.getMessage());

        workflow.addNode("agent_2", ( state ) -> {

            System.out.print( "agent_2: ");
            System.out.println( state );

            return completedFuture(Map.of("prop2", "test"));
        });

        workflow.addEdge("agent_2", "agent_3");

        exception = assertThrows(GraphStateException.class, workflow::compile);
        System.out.println(exception.getMessage());

        exception = assertThrows(GraphStateException.class, () ->
            workflow.addConditionalEdge("agent_1", ( state ) -> "agent_3" , Map.of() )
        );
        System.out.println(exception.getMessage());

    }

}
