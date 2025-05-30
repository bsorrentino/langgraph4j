package org.bsc.langgraph4j;

import org.bsc.async.AsyncGenerator;
import org.bsc.langgraph4j.checkpoint.BaseCheckpointSaver;
import org.bsc.langgraph4j.checkpoint.Checkpoint;
import org.bsc.langgraph4j.checkpoint.FileSystemSaver;
import org.bsc.langgraph4j.prebuilt.MessagesState;
import org.bsc.langgraph4j.state.StateSnapshot;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Collections.emptyMap;
import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
public class StateGraphFileSystemSaverTest
{
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(StateGraphFileSystemSaverTest.class);
    static class State extends MessagesState<String> {

        public State(Map<String, Object> initData) {
            super( initData  );
        }

        int steps() {
            return this.<Integer>value("steps").orElse(0);
        }

    }

    final String rootPath = Paths.get( "target", "checkpoint" ).toString();

    @Test
    public void testCheckpointSaverResubmit() throws Exception {
        int expectedSteps = 5;

        StateGraph<State> workflow = new StateGraph<>(State.SCHEMA, State::new)
                .addEdge(START, "agent_1")
                .addNode("agent_1", node_async( state -> {
                    int steps = state.steps() + 1;
                    log.info( "agent_1: step: {}", steps );
                    return Map.of("steps", steps, "messages", format( "agent_1:step %d", steps ));
                }))
                .addConditionalEdges( "agent_1", edge_async( state -> {
                    int steps = state.steps();
                    if( steps >= expectedSteps) {
                        return "exit";
                    }
                    return "next";
                }), Map.of( "next", "agent_1", "exit", END) );

        var saver = new FileSystemSaver( Paths.get( rootPath, "testCheckpointSaverResubmit" ),
                                                        workflow.getStateSerializer() );

        CompileConfig compileConfig = CompileConfig.builder()
                .checkpointSaver(saver)
                .build();

        CompiledGraph<State> app = workflow.compile( compileConfig );

        RunnableConfig runnableConfig_1 = RunnableConfig.builder()
                                    .threadId("thread_1")
                                    .build();

        RunnableConfig runnableConfig_2 = RunnableConfig.builder()
                                            .threadId("thread_2")
                                            .build();

        try {

            for (int execution = 0; execution < 2; execution++) {

                Optional<State> state = app.invoke( Map.of(), runnableConfig_1);

                assertTrue(state.isPresent());
                assertEquals(expectedSteps + (execution * 2), state.get().steps());

                List<String> messages = state.get().messages();
                assertFalse(messages.isEmpty());

                log.info("thread_1: execution: {} messages:\n{}\n", execution, messages);

                assertEquals(expectedSteps + execution * 2, messages.size());
                for (int i = 0; i < messages.size(); i++) {
                    assertEquals(format("agent_1:step %d", (i + 1)), messages.get(i));
                }

                StateSnapshot<State> snapshot = app.getState(runnableConfig_1);

                assertNotNull(snapshot);
                log.info("SNAPSHOT:\n{}\n", snapshot);

                // SUBMIT NEW THREAD 2

                state = app.invoke(emptyMap(), runnableConfig_2);

                assertTrue(state.isPresent());
                assertEquals(expectedSteps + execution, state.get().steps());
                messages = state.get().messages();

                log.info("thread_2: execution: {} messages:\n{}\n", execution, messages);

                assertEquals(expectedSteps + execution, messages.size());

                // RE-SUBMIT THREAD 1
                state = app.invoke(Map.of(), runnableConfig_1);

                assertTrue(state.isPresent());
                assertEquals(expectedSteps + 1 + execution * 2, state.get().steps());
                messages = state.get().messages();

                log.info("thread_1: execution: {} messages:\n{}\n", execution, messages);

                assertEquals(expectedSteps + 1 +  execution * 2, messages.size());

            }
        }
        finally {

//            saver.clear(runnableConfig_1);
//            saver.clear(runnableConfig_2);
        }
    }

    @Test
    public void testCheckpointSaverWithManualRelease() throws Exception {
        int expectedSteps = 5;

        StateGraph<State> workflow = new StateGraph<>(State.SCHEMA, State::new)
                .addEdge(START, "agent_1")
                .addNode("agent_1", node_async( state -> {
                    int steps = state.steps() + 1;
                    log.info( "agent_1: step: {}", steps );
                    return Map.of("steps", steps, "messages", format( "agent_1:step %d", steps ));
                }))
                .addConditionalEdges( "agent_1", edge_async( state -> {
                    int steps = state.steps();
                    if( steps >= expectedSteps) {
                        return "exit";
                    }
                    return "next";
                }), Map.of( "next", "agent_1", "exit", END) );

        var saver = new FileSystemSaver( Paths.get( rootPath, "testCheckpointSaverWithManualRelease" ),
                workflow.getStateSerializer() );

        CompileConfig compileConfig = CompileConfig.builder()
                .checkpointSaver(saver)
                .build();

        CompiledGraph<State> app = workflow.compile( compileConfig );

        RunnableConfig runnableConfig_1 = RunnableConfig.builder()
                .threadId("thread_1")
                .build();

        RunnableConfig runnableConfig_2 = RunnableConfig.builder()
                .threadId("thread_2")
                .build();

        var state = app.invoke( Map.of(), runnableConfig_1);

        assertTrue(state.isPresent());
        assertEquals(expectedSteps, state.get().steps());

        var tag = saver.release(runnableConfig_1);
        assertNotNull( tag );
        assertEquals( "thread_1", tag.threadId());

        var tagState = tag.checkpoints().stream().map(Checkpoint::getState).findFirst();
        assertTrue( tagState.isPresent() );

        assertIterableEquals( state.get().data().entrySet(), tagState.get().entrySet() );

        var messages = state.get().messages();

        assertEquals(expectedSteps, messages.size());

        for (int i = 0; i < messages.size(); i++) {
            assertEquals(format("agent_1:step %d", (i + 1)), messages.get(i));
        }

        var ex = assertThrowsExactly( IllegalStateException.class, () -> app.getState(runnableConfig_1));
        assertEquals( "Missing Checkpoint!", ex.getMessage() );

        // SUBMIT NEW THREAD 2

        state = app.invoke(emptyMap(), runnableConfig_2);

        assertTrue(state.isPresent());
        assertEquals(expectedSteps, state.get().steps());
        messages = state.get().messages();

        tag = saver.release(runnableConfig_2);
        assertNotNull( tag );
        assertEquals( "thread_2", tag.threadId());

        tagState = tag.checkpoints().stream().map(Checkpoint::getState).findFirst();
        assertTrue( tagState.isPresent() );

        assertIterableEquals( state.get().data().entrySet(), tagState.get().entrySet() );

        assertEquals(expectedSteps, messages.size());

        // RE-SUBMIT THREAD 1
        state = app.invoke(Map.of(), runnableConfig_1);

        assertTrue(state.isPresent());
        assertEquals(expectedSteps, state.get().steps());

        tag = saver.release(runnableConfig_1);
        assertNotNull( tag );
        assertEquals( "thread_1", tag.threadId());

        tagState = tag.checkpoints().stream().map(Checkpoint::getState).findFirst();
        assertTrue( tagState.isPresent() );

        assertIterableEquals( state.get().data().entrySet(), tagState.get().entrySet() );

    }

    @Test
    public void testCheckpointSaverWithAutoRelease() throws Exception {
        int expectedSteps = 5;

        StateGraph<State> workflow = new StateGraph<>(State.SCHEMA, State::new)
                .addEdge(START, "agent_1")
                .addNode("agent_1", node_async( state -> {
                    int steps = state.steps() + 1;
                    log.info( "agent_1: step: {}", steps );
                    return Map.of("steps", steps, "messages", format( "agent_1:step %d", steps ));
                }))
                .addConditionalEdges( "agent_1", edge_async( state -> {
                    int steps = state.steps();
                    if( steps >= expectedSteps) {
                        return "exit";
                    }
                    return "next";
                }), Map.of( "next", "agent_1", "exit", END) );

        var saver = new FileSystemSaver( Paths.get( rootPath, "testCheckpointSaverWithManualRelease" ),
                workflow.getStateSerializer() );

        var compileConfig = CompileConfig.builder()
                .checkpointSaver(saver)
                .releaseThread(true)
                .build();

        var app = workflow.compile( compileConfig );

        var runnableConfig_1 = RunnableConfig.builder()
                .threadId("thread_1")
                .build();

        var runnableConfig_2 = RunnableConfig.builder()
                .threadId("thread_2")
                .build();

        var state_1 = app.invoke( Map.of(), runnableConfig_1);

        assertTrue(state_1.isPresent());
        assertEquals(expectedSteps, state_1.get().steps());

        var tag = saver.release(runnableConfig_1);
        assertNotNull( tag );
        assertEquals( "thread_1", tag.threadId());

        var tagState = tag.checkpoints().stream().map(Checkpoint::getState).findFirst();
        assertTrue( tagState.isEmpty() );

        var messages = state_1.get().messages();

        assertEquals(expectedSteps, messages.size());

        for (int i = 0; i < messages.size(); i++) {
            assertEquals(format("agent_1:step %d", (i + 1)), messages.get(i));
        }

        var ex = assertThrowsExactly( IllegalStateException.class, () -> app.getState(runnableConfig_1));
        assertEquals( "Missing Checkpoint!", ex.getMessage() );

        // SUBMIT NEW THREAD 2

        var state_2 = app.invoke(emptyMap(), runnableConfig_2);

        assertTrue(state_2.isPresent());
        assertEquals(expectedSteps, state_2.get().steps());
        messages = state_2.get().messages();

        tag = saver.release(runnableConfig_2);
        assertEquals( "thread_2", tag.threadId());
        assertNotNull( tag );

        tagState = tag.checkpoints().stream().map(Checkpoint::getState).findFirst();

        assertTrue( tagState.isEmpty() );
        assertEquals(expectedSteps, messages.size());

        // RE-SUBMIT THREAD 1
        var iterator = app.stream(Map.of(), runnableConfig_1);

        state_1 = iterator.stream()
                .reduce((a, b) -> b)
                .map( NodeOutput::state);
        assertTrue( state_1.isPresent() );
        assertInstanceOf(AsyncGenerator.HasResultValue.class, iterator );

        var result = ((AsyncGenerator.HasResultValue) iterator).resultValue();

        assertTrue( result.isPresent() );
        assertInstanceOf(BaseCheckpointSaver.Tag.class, result.get() );

        tag = result.map( BaseCheckpointSaver.Tag.class::cast ).orElseThrow();
        tagState = tag.checkpoints().stream().map(Checkpoint::getState).findFirst();

        assertTrue( tagState.isPresent() );
        assertIterableEquals( state_1.get().data().entrySet(), tagState.get().entrySet() );


    }

}
