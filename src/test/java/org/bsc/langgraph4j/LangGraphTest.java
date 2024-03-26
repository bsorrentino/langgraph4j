package org.bsc.langgraph4j;

import lombok.Value;
import lombok.var;
import org.bsc.langgraph4j.state.AgentState;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.bsc.langgraph4j.GraphState.END;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
public class LangGraphTest
{
    <State extends AgentState> List<Map.Entry<String,Object>> sort(State state ) {
        return state.getData().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toList());
    }

    <K,V> Map<K,V> mapOf() {
        return Collections.emptyMap();
    }
    <K,V> Map<K,V> mapOf( K key, V value  ) {
        var result = new HashMap<K, V>();
        result.put(key, value);
        return Collections.unmodifiableMap(result);
    }
    <K,V> Map<K,V> mapOf( K key, V value, K key1, V value1 ) {
        var result = new HashMap<K, V>();
        result.put(key, value);
        result.put(key1, value1);
        return Collections.unmodifiableMap(result);
    }

    @Value
    static class BaseAgentState implements AgentState {
        Map<String,Object> data;

        @Override
        public Map<String, Object> getData() {
            return data;
        }
    }

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
            workflow.addConditionalEdge("agent_1", edge_async( state  -> "agent_3" ), mapOf() )
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
            return mapOf("prop1", "test");
        }));

        workflow.addEdge( "agent_1",  END);

        var app = workflow.compile();

        var result = app.invoke( mapOf( "input", "test1") );
        assertTrue( result.isPresent() );

        System.out.println( result.get().getData() );
        var  expected = mapOf("input", "test1","prop1","test");

        assertIterableEquals( expected.entrySet(), sort(result.get()) );
        //assertDictionaryOfAnyEqual( expected, result.data )

    }
}
