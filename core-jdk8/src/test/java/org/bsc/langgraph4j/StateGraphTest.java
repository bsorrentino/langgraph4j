package org.bsc.langgraph4j;

import lombok.var;
import org.bsc.langgraph4j.checkpoint.Checkpoint;
import org.bsc.langgraph4j.checkpoint.MemorySaver;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AppendableValue;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Collections.emptyMap;
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

    @Test
    public void testCheckpointInitialState() throws Exception {

        var workflow = new StateGraph<>(AgentState::new);
        workflow.setEntryPoint("agent_1");

        workflow.addNode("agent_1", node_async( state -> {
            System.out.print( "agent_1");
            return mapOf("agent_1:prop1", "agent_1:test");
        }));

        workflow.addEdge( "agent_1",  END);

        var saver = new MemorySaver();

        var compileConfig = CompileConfig.builder().checkpointSaver(saver).build();

        var app = workflow.compile( compileConfig );

        Map<String, Object> inputs = mapOf( "input", "test1");

        var initState = app.getInitialState( inputs );

        assertEquals( 1, initState.data().size() );
        assertTrue(  initState.value("input").isPresent() );
        assertEquals( "test1", initState.value("input").get() );

        //
        // Test checkpoint not override inputs
        //
        var newState = new AgentState( mapOf( "input", "test2") );
        saver.put( new Checkpoint( Checkpoint.Value.of( newState, "start" ) ) );

        app = workflow.compile( compileConfig );
        initState = app.getInitialState( inputs );

        assertEquals( 1, initState.data().size() );
        assertTrue(  initState.value("input").isPresent() );
        assertEquals( "test1", initState.value("input").get() );

        // Test checkpoints are saved
        newState = new AgentState( mapOf( "input", "test2", "agent_1:prop1", "agent_1:test") );
        saver.put( new Checkpoint( Checkpoint.Value.of( newState, "agent_1" ) ) );

        app = workflow.compile( compileConfig );
        initState = app.getInitialState( inputs );

        assertEquals( 2, initState.data().size() );
        assertTrue(  initState.value("input").isPresent() );
        assertEquals( "test1", initState.value("input").get() );
        assertTrue(  initState.value("agent_1:prop1").isPresent() );
        assertEquals( "agent_1:test", initState.value("agent_1:prop1").get() );

        var checkpoints = saver.list();
        assertEquals( 2, checkpoints.size() );
        var last = saver.getLast();
        assertTrue( last.isPresent() );
        assertEquals( "agent_1", last.get().getValue().getNodeId() );
        assertTrue( last.get().getValue().getState().value("agent_1:prop1").isPresent() );
        assertEquals( "agent_1:test", last.get().getValue().getState().value("agent_1:prop1").get() );

    }

    static class MessagesState extends AgentState {

        public MessagesState(Map<String, Object> initData) {
            super( initData  );
            appendableValue("messages"); // tip: initialize messages
        }

        int steps() {
            return value("steps").map(Integer.class::cast).orElse(0);
        }

        AppendableValue<String> messages() {
            return appendableValue("messages");
        }

    }

    @Test
    public void testCheckpointSaver() throws Exception {
        var STEPS_COUNT = 5;

        var workflow = new StateGraph<>(MessagesState::new);
        workflow.setEntryPoint("agent_1");

        workflow.addNode("agent_1", node_async( state -> {

            System.out.println( "agent_1");
            var steps = state.steps() + 1;
            return mapOf("steps", steps, "messages", format( "agent_1:step %d", steps ));
        }));
        workflow.addConditionalEdges( "agent_1", edge_async( state -> {
            var steps = state.steps();
            if( steps >= STEPS_COUNT) {
                return "exit";
            }
            return "next";
        }), mapOf( "next", "agent_1", "exit", END) );

        var saver = new MemorySaver();

        var compileConfig = CompileConfig.builder()
                .checkpointSaver(saver)
                .build();

        var app = workflow.compile( compileConfig );

        Map<String, Object> inputs = mapOf( "steps", 0 );

        var invokeConfig = InvokeConfig.builder().checkpointThreadId("thread_1").build();

        var state = app.invoke( inputs, invokeConfig );

        assertTrue( state.isPresent() );
        assertEquals( STEPS_COUNT, state.get().steps() );
        var messages = state.get().appendableValue("messages");
        assertFalse( messages.isEmpty() );

        System.out.println( messages.values() );

        assertEquals( STEPS_COUNT, messages.size() );
        for( int i = 0; i < messages.size(); i++ ) {
            assertEquals( format("agent_1:step %d", i+1), messages.values().get(i) );
        }

        state = app.invoke( emptyMap(), invokeConfig );

        assertTrue( state.isPresent() );
        assertEquals( STEPS_COUNT + 1, state.get().steps() );
        messages = state.get().appendableValue("messages");
        assertEquals( STEPS_COUNT + 1, messages.size() );
        for( int i = 0; i < messages.size(); i++ ) {
            assertEquals( format("agent_1:step %d", i+1), messages.values().get(i) );
        }
    }

}
