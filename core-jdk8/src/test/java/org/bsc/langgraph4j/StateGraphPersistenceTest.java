package org.bsc.langgraph4j;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.bsc.langgraph4j.checkpoint.Checkpoint;
import org.bsc.langgraph4j.checkpoint.MemorySaver;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AppenderChannel;
import org.bsc.langgraph4j.state.Channel;
import org.bsc.langgraph4j.state.StateSnapshot;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Collections.emptyMap;
import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import static org.bsc.langgraph4j.utils.CollectionsUtils.mapOf;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
@Slf4j
public class StateGraphPersistenceTest
{
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

        Optional<String> lastMessage() {
                List<String> messages = messages();
            if( messages.isEmpty() ) {
                return Optional.empty();
            }
            return Optional.of(messages.get( messages.size() - 1 ));
        }

    }


    @Test
    public void testCheckpointInitialState() throws Exception {

        var workflow = new StateGraph<>(AgentState::new)
            .addEdge( START,"agent_1")
            .addNode("agent_1", node_async( state -> {
                log.info( "agent_1");
                return mapOf("agent_1:prop1", "agent_1:test");
            }))
            .addEdge( "agent_1",  END)
        ;

        var saver = new MemorySaver();

        var compileConfig = CompileConfig.builder().checkpointSaver(saver).build();

        var runnableConfig = RunnableConfig.builder().build();
        var app = workflow.compile( compileConfig );

        Map<String, Object> inputs = mapOf( "input", "test1");

        var initState = app.cloneState( app.getInitialState( inputs, runnableConfig ) );

        assertEquals( 1, initState.data().size() );
        assertTrue(  initState.value("input").isPresent() );
        assertEquals( "test1", initState.value("input").get() );

        //
        // Test checkpoint not override inputs
        //
        var newState = new AgentState( mapOf( "input", "test2") );

        saver.put( runnableConfig, Checkpoint.builder()
                .state( newState.data() )
                .nodeId(START)
                .nextNodeId("agent_1")
                .build() ) ;

        app = workflow.compile( compileConfig );
        initState = app.cloneState( app.getInitialState( inputs, runnableConfig ) );

        assertEquals( 1, initState.data().size() );
        assertTrue(  initState.value("input").isPresent() );
        assertEquals( "test1", initState.value("input").get() );

        // Test checkpoints are saved
        newState = new AgentState( mapOf( "input", "test2", "agent_1:prop1", "agent_1:test") );
        saver.put( runnableConfig, Checkpoint.builder()
                .state( newState )
                .nodeId( "agent_1")
                .nextNodeId(END)
                .build() ) ;
        app = workflow.compile( compileConfig );
        initState = app.cloneState( app.getInitialState( inputs, runnableConfig ) );

        assertEquals( 2, initState.data().size() );
        assertTrue(  initState.value("input").isPresent() );
        assertEquals( "test1", initState.value("input").get() );
        assertTrue(  initState.value("agent_1:prop1").isPresent() );
        assertEquals( "agent_1:test", initState.value("agent_1:prop1").get() );

        var checkpoints = saver.list(runnableConfig);
        assertEquals( 2, checkpoints.size() );
        var last = saver.get(runnableConfig);
        assertTrue( last.isPresent() );
        assertEquals( "agent_1", last.get().getNodeId() );
        assertNotNull( last.get().getState().get("agent_1:prop1") );
        assertEquals( "agent_1:test", last.get().getState().get("agent_1:prop1") );

    }


    private StateGraph<MessagesState> workflow01(int expectedSteps ) throws Exception {
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
    public void testCheckpointSaverResubmit() throws Exception {
        var expectedSteps = 5;

        var workflow = workflow01( expectedSteps );

        var saver = new MemorySaver();

        var compileConfig = CompileConfig.builder()
                .checkpointSaver(saver)
                .build();

        var app = workflow.compile( compileConfig );

        Map<String, Object> inputs = mapOf( "steps", 0 );

        var runnableConfig = RunnableConfig.builder()
                                    .threadId("thread_1").build();

        var state = app.invoke( inputs, runnableConfig );

        assertTrue( state.isPresent() );
        assertEquals( expectedSteps, state.get().steps() );

        var messages = state.get().messages();
        assertFalse( messages.isEmpty() );

        log.info( "{}", messages );

        assertEquals( expectedSteps, messages.size() );
        for( int i = 0; i < messages.size(); i++ ) {
            assertEquals( format("agent_1:step %d", i+1), messages.get(i) );
        }

        var snapshot = app.getState(runnableConfig);

        assertNotNull( snapshot );
        log.info( "SNAPSHOT:\n{}\n", snapshot );

        // SUBMIT NEW THREAD 2
        runnableConfig = RunnableConfig.builder()
                .threadId("thread_2").build();

        state = app.invoke( emptyMap(), runnableConfig );

        assertTrue( state.isPresent() );
        assertEquals( expectedSteps, state.get().steps() );
        messages = state.get().messages();

        log.info( "{}", messages );

        assertEquals( expectedSteps, messages.size() );
        for( int i = 0; i < messages.size(); i++ ) {
            assertEquals( format("agent_1:step %d", i+1), messages.get(i) );
        }

        // RE-SUBMIT THREAD 1
        state = app.invoke( emptyMap(), runnableConfig );

        assertTrue( state.isPresent() );
        assertEquals( expectedSteps + 1, state.get().steps() );
        messages = state.get().messages();

        log.info( "{}", messages );

        assertEquals( expectedSteps + 1, messages.size() );

    }

    @Test
    public void testViewAndUpdatePastGraphState() throws Exception {

        var workflow = new StateGraph<>(MessagesState.SCHEMA, MessagesState::new)
                .addNode("agent", node_async( state -> {
                    String lastMessage = state.lastMessage().orElseThrow( () -> new IllegalStateException("No last message!") );

                    if( lastMessage.contains( "temperature")) {
                        return mapOf("messages", "whether in Naples is sunny");
                    }
                    if( lastMessage.contains( "whether")) {
                        return mapOf("messages", "tool_calls");
                    }
                    if( lastMessage.contains( "bartolo")) {
                        return mapOf("messages", "Hi bartolo, nice to meet you too! How can I assist you today?");
                    }
                    if(state.messages().stream().anyMatch(m -> m.contains("bartolo"))) {
                        return mapOf("messages", "Hi, bartolo welcome back?");
                    }
                    throw new IllegalStateException( "unknown message!" );
                }))
                .addNode("tools", node_async( state ->
                    mapOf( "messages", "temperature in Napoli is 30 degree" )
                ))
                .addEdge(START, "agent")
                .addConditionalEdges("agent", edge_async( state ->
                    state.lastMessage().filter( m -> m.equals("tool_calls")  ).map( m -> "tools" ).orElse(END)
                ), mapOf("tools", "tools", END, END))
                .addEdge("tools", "agent");


        var saver = new MemorySaver();

        var compileConfig = CompileConfig.builder()
                .checkpointSaver(saver)
                .build();

        var app = workflow.compile( compileConfig );

        Map<String, Object> inputs = mapOf( "messages", "whether in Naples?" );

        var runnableConfig = RunnableConfig.builder()
                .streamMode( CompiledGraph.StreamMode.SNAPSHOTS )
                .threadId("thread_1")
                .build();

        var results = app.stream( inputs, runnableConfig ).stream().collect( Collectors.toList() );

        results.forEach( r -> log.info( "{}: Node: {} - {}", r.getClass().getSimpleName(), r.node(), r.state().messages() ) );

        assertEquals( 5, results.size() );
        assertInstanceOf( NodeOutput.class, results.get(0) );
        assertInstanceOf( StateSnapshot.class, results.get(1) );
        assertInstanceOf( StateSnapshot.class, results.get(2) );
        assertInstanceOf( StateSnapshot.class, results.get(3) );
        assertInstanceOf( NodeOutput.class, results.get(4) );

        var snapshot = app.getState(runnableConfig);
        assertNotNull( snapshot );
        assertEquals( END, snapshot.next() );

        log.info( "LAST SNAPSHOT:\n{}\n", snapshot );

        var stateHistory = app.getStateHistory( runnableConfig );
        stateHistory.forEach( state -> log.info( "SNAPSHOT HISTORY:\n{}\n", state ) );
        assertNotNull( stateHistory );
        assertEquals( 4, stateHistory.size() );

        for( var s : stateHistory ) {
            log.info( "SNAPSHOT HISTORY:\n{}\n", s );
        }

        results = app.stream( null, runnableConfig ).stream().collect( Collectors.toList() );

        assertNotNull( results );
        assertFalse( results.isEmpty() );
        assertEquals( 1, results.size() );
        assertTrue( results.get(0).state().lastMessage().isPresent() );
        assertEquals( "whether in Naples is sunny", results.get(0).state().lastMessage().get() );

        var firstSnapshot = stateHistory.stream().reduce( (first, second) -> second); // take the last
        assertTrue( firstSnapshot.isPresent() );
        assertTrue( firstSnapshot.get().state().lastMessage().isPresent() );
        assertEquals( "whether in Naples?", firstSnapshot.get().state().lastMessage().get() );

        var toReplay = firstSnapshot.get().config();

        toReplay = app.updateState( toReplay, mapOf( "messages", "i'm bartolo"), null );
        results = app.stream( null, toReplay ).stream().collect( Collectors.toList() );

        assertNotNull( results );
        assertFalse( results.isEmpty() );
        assertEquals( 2, results.size() );
        assertEquals( END, results.get(1).node() );
        assertTrue( results.get(1).state().lastMessage().isPresent() );
        assertEquals( "Hi bartolo, nice to meet you too! How can I assist you today?", results.get(0).state().lastMessage().get() );

    }

    @Test
    public void testPauseAndUpdatePastGraphState() throws Exception {
        var workflow = new StateGraph<>(MessagesState.SCHEMA, MessagesState::new)
                .addNode("agent", node_async( state -> {
                    String lastMessage = state.lastMessage().orElseThrow( () -> new IllegalStateException("No last message!") );

                    if( lastMessage.contains( "temperature")) {
                        return mapOf("messages", "whether in Naples is sunny");
                    }
                    if( lastMessage.contains( "whether")) {
                        return mapOf("messages", "tool_calls");
                    }
                    if( lastMessage.contains( "bartolo")) {
                        return mapOf("messages", "Hi bartolo, nice to meet you too! How can I assist you today?");
                    }
                    if(state.messages().stream().anyMatch(m -> m.contains("bartolo"))) {
                        return mapOf("messages", "Hi, bartolo welcome back?");
                    }
                    throw new IllegalStateException( "unknown message!" );
                }))
                .addNode("tools", node_async( state ->
                        mapOf( "messages", "temperature in Napoli is 30 degree" )
                ))
                .addEdge(START, "agent")
                .addConditionalEdges("agent", edge_async( state ->
                        state.lastMessage().filter( m -> m.equals("tool_calls")  ).map( m -> "tools" ).orElse(END)
                ), mapOf("tools", "tools", END, END))
                .addEdge("tools", "agent");


        var saver = new MemorySaver();

        var compileConfig = CompileConfig.builder()
                .checkpointSaver(saver)
                .interruptBefore("tools")
                .build();

        var app = workflow.compile( compileConfig );

        var runnableConfig = RunnableConfig.builder()
                .threadId("thread_1")
                .build();

        Map<String,Object> inputs = mapOf( "messages","whether in Naples?" ) ;
        var results = app.stream( inputs, runnableConfig ).stream().collect(Collectors.toList());
        results.forEach( System.out::println);
        assertNotNull( results );
        assertEquals( 2, results.size() );
        assertEquals( START, results.get(0).node() );
        assertEquals( "agent", results.get(1).node() );
        assertTrue( results.get(1).state().lastMessage().isPresent() );

        var state = app.getState(runnableConfig);

        assertNotNull( state );
        assertEquals( "tools", state.next() );

        results = app.stream( null, state.config() ).stream().collect(Collectors.toList());

        assertNotNull( results );
        assertEquals( 3, results.size() );
        assertEquals( "tools", results.get(0).node() );
        assertEquals( "agent", results.get(1).node() );
        assertEquals( END, results.get(2).node() );
        assertTrue( results.get(2).state().lastMessage().isPresent() );

        System.out.println( results.get(2).state().lastMessage().get() );
    }
}
