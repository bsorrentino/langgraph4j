package org.bsc.langgraph4j;

import org.bsc.langgraph4j.state.AgentState;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.bsc.langgraph4j.GraphState.END;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
public class LangGraphTest
{
    <T> List<Map.Entry<String,T>> sort(Map<String,T> map ) {
        return map.entrySet().stream().sorted(Map.Entry.comparingByKey()).toList();
    }

    record BaseAgentState( Map<String,Object> data ) implements AgentState {}

    @Test
    void testValidation() throws Exception {

        var workflow = new GraphState<>(BaseAgentState::new);
        var exception = assertThrows(GraphStateException.class, workflow::compile);
        System.out.println(exception.getMessage());
        assertEquals( "missing Entry Point", exception.getMessage());

        workflow.setEntryPoint("agent_1");

        exception = assertThrows(GraphStateException.class, workflow::compile);
        System.out.println(exception.getMessage());
        assertEquals( "entryPoint: agent_1 doesn't exist!", exception.getMessage());

        workflow.addNode("agent_1", node_async((state ) -> {
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

        workflow.addNode("agent_2", node_async( state  -> {

            System.out.print( "agent_2: ");
            System.out.println( state );

            return Map.of("prop2", "test");
        }));

        workflow.addEdge("agent_2", "agent_3");

        exception = assertThrows(GraphStateException.class, workflow::compile);
        System.out.println(exception.getMessage());

        exception = assertThrows(GraphStateException.class, () ->
            workflow.addConditionalEdge("agent_1", edge_async( state  -> "agent_3" ), Map.of() )
        );
        System.out.println(exception.getMessage());

    }

    @Test
    public void testRunningOneNode() throws Exception {

        var workflow = new GraphState<>(BaseAgentState::new);
        workflow.setEntryPoint("agent_1");

        workflow.addNode("agent_1", node_async( state -> {
            System.out.print( "agent_1");
            System.out.println( state );
            return Map.of("prop1", "test");
        }));

        workflow.addEdge( "agent_1",  END);

        var app = workflow.compile();

        var result = app.invoke( Map.of( "input", "test1") );
        assertTrue( result.isPresent() );

        var  expected = Map.of("input", "test1","prop1","test");

        assertIterableEquals( sort(expected), sort(result.get().data()) );
        //assertDictionaryOfAnyEqual( expected, result.data )

    }
}
