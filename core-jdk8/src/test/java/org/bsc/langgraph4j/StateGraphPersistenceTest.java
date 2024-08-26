package org.bsc.langgraph4j;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.bsc.langgraph4j.checkpoint.Checkpoint;
import org.bsc.langgraph4j.checkpoint.MemorySaver;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AppendableValue;
import org.bsc.langgraph4j.state.AppenderChannel;
import org.bsc.langgraph4j.state.Channel;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Collections.emptyMap;
import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import static org.bsc.langgraph4j.utils.CollectionsUtils.listOf;
import static org.bsc.langgraph4j.utils.CollectionsUtils.mapOf;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
@Slf4j
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

        workflow.addEdge( START,"agent_1");

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

        var workflow = new StateGraph<>(AgentState::new)
            .addEdge( START,"agent_1")
            .addNode("agent_1", node_async( state -> {
                System.out.print( "agent_1");
                System.out.println( state );
                return mapOf("prop1", "test");
            }))
            .addEdge( "agent_1",  END)
            ;

        var app = workflow.compile();

        var result = app.invoke( mapOf( "input", "test1") );
        assertTrue( result.isPresent() );

        var  expected = mapOf("input", "test1","prop1","test");

        assertIterableEquals( sortMap(expected), sortMap(result.get().data()) );
        //assertDictionaryOfAnyEqual( expected, result.data )

    }

    @Test
    public void testCheckpointInitialState() throws Exception {

        var workflow = new StateGraph<>(AgentState::new)
            .addEdge( START,"agent_1")
            .addNode("agent_1", node_async( state -> {
                System.out.print( "agent_1");
                return mapOf("agent_1:prop1", "agent_1:test");
            }))
            .addEdge( "agent_1",  END)
        ;

        var saver = new MemorySaver();

        var compileConfig = CompileConfig.builder().checkpointSaver(saver).build();

        var runnableConfig = RunnableConfig.builder().build();
        var app = workflow.compile( compileConfig );

        Map<String, Object> inputs = mapOf( "input", "test1");

        var initState = app.getInitialState( inputs, runnableConfig );

        assertEquals( 1, initState.data().size() );
        assertTrue(  initState.value("input").isPresent() );
        assertEquals( "test1", initState.value("input").get() );

        //
        // Test checkpoint not override inputs
        //
        var newState = new AgentState( mapOf( "input", "test2") );
        saver.put( runnableConfig, new Checkpoint( Checkpoint.Value.of( newState, "start" ) ) );

        app = workflow.compile( compileConfig );
        initState = app.getInitialState( inputs, runnableConfig );

        assertEquals( 1, initState.data().size() );
        assertTrue(  initState.value("input").isPresent() );
        assertEquals( "test1", initState.value("input").get() );

        // Test checkpoints are saved
        newState = new AgentState( mapOf( "input", "test2", "agent_1:prop1", "agent_1:test") );
        saver.put( runnableConfig, new Checkpoint( Checkpoint.Value.of( newState, "agent_1" ) ) );

        app = workflow.compile( compileConfig );
        initState = app.getInitialState( inputs, runnableConfig );

        assertEquals( 2, initState.data().size() );
        assertTrue(  initState.value("input").isPresent() );
        assertEquals( "test1", initState.value("input").get() );
        assertTrue(  initState.value("agent_1:prop1").isPresent() );
        assertEquals( "agent_1:test", initState.value("agent_1:prop1").get() );

        var checkpoints = saver.list(runnableConfig);
        assertEquals( 2, checkpoints.size() );
        var last = saver.get(runnableConfig);
        assertTrue( last.isPresent() );
        assertEquals( "agent_1", last.get().getValue().getNodeId() );
        assertTrue( last.get().getValue().getState().value("agent_1:prop1").isPresent() );
        assertEquals( "agent_1:test", last.get().getValue().getState().value("agent_1:prop1").get() );

    }

    static class MessagesStateDeprecated extends AgentState {

        public MessagesStateDeprecated(Map<String, Object> initData) {
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
    void testWithAppenderDeprecated() throws Exception {

        var workflow = new StateGraph<>(MessagesStateDeprecated::new)
            .addNode("agent_1", node_async( state -> {
                System.out.println( "agent_1" );
                return mapOf("messages", "message1");
            }))
           .addNode("agent_2", node_async( state -> {
               System.out.println( "agent_2" );
                return mapOf( "messages", "message2");
            }))
           .addNode("agent_3", node_async( state -> {
               System.out.println( "agent_3" );
               var messages = state.messages();
               var steps = messages.size() +1 ;
               return mapOf("messages", "message3","steps", steps);
            }))
            .addEdge("agent_1", "agent_2")
            .addEdge( "agent_2", "agent_3")
            .addEdge( START, "agent_1")
            .addEdge( "agent_3", END);

        var app = workflow.compile();

        Optional<MessagesStateDeprecated> result = app.invoke( mapOf() );

        assertTrue( result.isPresent() );
        System.out.println( result.get().data() );
        assertEquals( 3, result.get().steps() );
        assertEquals( 3, result.get().messages().size() );
        assertIterableEquals( listOf( "message1", "message2", "message3"), result.get().messages().values() );
    }

    static class MessagesState extends AgentState {

        static Map<String, Channel<?>> SCHEMA = mapOf(
            "messages", AppenderChannel.<String>of(ArrayList::new)
        );

        public MessagesState(Map<String, Object> initData) {
            super( initData  );
        }

        int steps() {
            return value("steps", 0);
        }

        List<String> messages() {
            return this.<List<String>>value( "messages" )
                    .orElseThrow( () -> new RuntimeException( "messages not found" ) );
        }

    }

    @Test
    void testWithAppender() throws Exception {

        var workflow = new StateGraph<>( MessagesState.SCHEMA, MessagesState::new)
                .addNode("agent_1", node_async( state -> {
                    System.out.println( "agent_1" );
                    return mapOf("messages", "message1");
                }))
                .addNode("agent_2", node_async( state -> {
                    System.out.println( "agent_2" );
                    return mapOf( "messages", "message2");
                }))
                .addNode("agent_3", node_async( state -> {
                    System.out.println( "agent_3" );
                    var steps = state.messages().size() +1 ;
                    return mapOf("messages", "message3","steps", steps);
                }))
                .addEdge("agent_1", "agent_2")
                .addEdge( "agent_2", "agent_3")
                .addEdge( START, "agent_1")
                .addEdge( "agent_3", END);

        var app = workflow.compile();

        Optional<MessagesState> result = app.invoke( mapOf() );

        assertTrue( result.isPresent() );
        System.out.println( result.get().data() );
        assertEquals( 3, result.get().steps() );
        assertEquals( 3, result.get().messages().size() );
        assertIterableEquals( listOf( "message1", "message2", "message3"), result.get().messages() );
    }


    private StateGraph<MessagesState> oneNodeWorkflow( int expectedSteps ) throws Exception {
        return new StateGraph<>(MessagesState.SCHEMA, MessagesState::new)
                .addEdge(START, "agent_1")
                .addNode("agent_1", node_async( state -> {
                    var steps = state.steps() + 1;
                    log.info( "agent_1: step: {}", steps );
                    return mapOf("steps", steps, "messages", format( "agent_1:step %d", steps ));
                }))
                .addConditionalEdges( "agent_1", edge_async( state -> {
                    var steps = state.steps();
                    if( steps >= expectedSteps) {
                        return "exit";
                    }
                    return "next";
                }), mapOf( "next", "agent_1", "exit", END) );
    }

    @Test
    public void testCheckpointSaver() throws Exception {
        var STEPS_COUNT = 5;

        var workflow = oneNodeWorkflow( STEPS_COUNT );

        var saver = new MemorySaver();

        var compileConfig = CompileConfig.builder()
                .checkpointSaver(saver)
                .build();

        var app = workflow.compile( compileConfig );

        Map<String, Object> inputs = mapOf( "steps", 0 );

        var runnableConfig = RunnableConfig.builder().threadId("thread_1").build();

        var state = app.invoke( inputs, runnableConfig );

        assertTrue( state.isPresent() );
        assertEquals( STEPS_COUNT, state.get().steps() );

        var messages = state.get().messages();
        assertFalse( messages.isEmpty() );

        log.info( "{}", messages );

        assertEquals( STEPS_COUNT, messages.size() );
        for( int i = 0; i < messages.size(); i++ ) {
            assertEquals( format("agent_1:step %d", i+1), messages.get(i) );
        }

        state = app.invoke( emptyMap(), runnableConfig );

        assertTrue( state.isPresent() );
        assertEquals( STEPS_COUNT + 1, state.get().steps() );
        messages = state.get().messages();

        log.info( "{}", messages );

        assertEquals( STEPS_COUNT + 1, messages.size() );
        for( int i = 0; i < messages.size(); i++ ) {
            assertEquals( format("agent_1:step %d", i+1), messages.get(i) );
        }
    }

}
