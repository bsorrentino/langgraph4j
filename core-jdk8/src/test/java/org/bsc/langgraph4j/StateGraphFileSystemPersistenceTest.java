package org.bsc.langgraph4j;

import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.checkpoint.FileSystemSaver;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AppenderChannel;
import org.bsc.langgraph4j.state.Channel;
import org.bsc.langgraph4j.state.StateSnapshot;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
public class StateGraphFileSystemPersistenceTest
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

    final String rootPath = Paths.get( "target", "checkpoint" ).toString();

    @Test
    public void testCheckpointSaverResubmit() throws Exception {
        int expectedSteps = 5;

        StateGraph<MessagesState> workflow = new StateGraph<>(MessagesState.SCHEMA, MessagesState::new)
                .addEdge(START, "agent_1")
                .addNode("agent_1", node_async( state -> {
                    int steps = state.steps() + 1;
                    log.info( "agent_1: step: {}", steps );
                    return mapOf("steps", steps, "messages", format( "agent_1:step %d", steps ));
                }))
                .addConditionalEdges( "agent_1", edge_async( state -> {
                    int steps = state.steps();
                    if( steps >= expectedSteps) {
                        return "exit";
                    }
                    return "next";
                }), mapOf( "next", "agent_1", "exit", END) );

        FileSystemSaver saver = new FileSystemSaver(    Paths.get( rootPath, "testCheckpointSaverResubmit" ),
                                                        workflow.getStateSerializer() );

        CompileConfig compileConfig = CompileConfig.builder()
                .checkpointSaver(saver)
                .build();

        CompiledGraph<MessagesState> app = workflow.compile( compileConfig );

        RunnableConfig runnableConfig_1 = RunnableConfig.builder()
                                    .threadId("thread_1")
                                    .build();

        RunnableConfig runnableConfig_2 = RunnableConfig.builder()
                                            .threadId("thread_2")
                                            .build();

        try {

            for (int execution = 0; execution < 2; execution++) {

                Optional<MessagesState> state = app.invoke( mapOf(), runnableConfig_1);

                assertTrue(state.isPresent());
                assertEquals(expectedSteps + (execution * 2), state.get().steps());

                List<String> messages = state.get().messages();
                assertFalse(messages.isEmpty());

                log.info("thread_1: execution: {} messages:\n{}\n", execution, messages);

                assertEquals(expectedSteps + execution * 2, messages.size());
                for (int i = 0; i < messages.size(); i++) {
                    assertEquals(format("agent_1:step %d", (i + 1)), messages.get(i));
                }

                StateSnapshot<MessagesState> snapshot = app.getState(runnableConfig_1);

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
                state = app.invoke(mapOf(), runnableConfig_1);

                assertTrue(state.isPresent());
                assertEquals(expectedSteps + 1 + execution * 2, state.get().steps());
                messages = state.get().messages();

                log.info("thread_1: execution: {} messages:\n{}\n", execution, messages);

                assertEquals(expectedSteps + 1 +  execution * 2, messages.size());

            }
        }
        finally {

            saver.clear(runnableConfig_1);
            saver.clear(runnableConfig_2);
        }
    }

}