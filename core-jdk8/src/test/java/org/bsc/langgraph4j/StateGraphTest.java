package org.bsc.langgraph4j;

import lombok.var;
import org.bsc.langgraph4j.state.AgentState;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import static org.bsc.langgraph4j.utils.CollectionsUtils.mapOf;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
public class StateGraphTest
{
    public static <T> List<Map.Entry<String,T>> sortMap(Map<String,T> map ) {
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toList());
    }
    @Test
    void testValidation() throws Exception {

        var workflow = new StateGraph<>(AgentState::new);
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
            return mapOf("prop1", "test");
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

            return mapOf("prop2", "test");
        }));

        workflow.addEdge("agent_2", "agent_3");

        exception = assertThrows(GraphStateException.class, workflow::compile);
        System.out.println(exception.getMessage());

        exception = assertThrows(GraphStateException.class, () ->
            workflow.addConditionalEdges("agent_1", edge_async(state  -> "agent_3" ), mapOf() )
        );
        System.out.println(exception.getMessage());

    }

    @Test
    public void testRunningOneNode() throws Exception {

        var workflow = new StateGraph<>(AgentState::new);
        workflow.setEntryPoint("agent_1");

        workflow.addNode("agent_1", node_async( state -> {
            System.out.print( "agent_1");
            System.out.println( state );
            return mapOf("prop1", "test");
        }));

        workflow.addEdge( "agent_1",  END);

        var app = workflow.compile();

        var result = app.invoke( mapOf( "input", "test1") );
        assertTrue( result.isPresent() );

        var  expected = mapOf("input", "test1","prop1","test");

        assertIterableEquals( sortMap(expected), sortMap(result.get().data()) );
        //assertDictionaryOfAnyEqual( expected, result.data )

    }


}
