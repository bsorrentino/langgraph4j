package org.bsc.langgraph4j;

import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.bsc.langgraph4j.checkpoint.MemorySaver;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AppenderChannel;
import org.bsc.langgraph4j.state.Channel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.LogManager;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class SubGraphTest {
    static class MessagesState extends AgentState {

        static Map<String, Channel<?>> SCHEMA = Map.of(
                "messages", AppenderChannel.<String>of(ArrayList::new)
        );

        public MessagesState(Map<String, Object> initData) {
            super( initData  );
        }

        int steps() {
            return this.<Integer>value("steps").orElse(0);
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


    @BeforeAll
    public static void initLogging() throws IOException {
        try( var is = StateGraphPersistenceTest.class.getResourceAsStream("/logging.properties") ) {
            LogManager.getLogManager().readConfiguration(is);
        }
    }

    @Test
    public void testCheckpointWithSubgraph() throws Exception {

        NodeAction<MessagesState> childStep1 = (state) -> Map.of("messages", "child:step1");

        NodeAction<MessagesState> childStep2 = (state) -> Map.of("messages", "child:step2");

        NodeAction<MessagesState> childStep3 = ( state) -> Map.of("messages", "child:step3");

        var compileConfig = CompileConfig.builder().checkpointSaver(new MemorySaver()).build();

        var workflowChild = new StateGraph<>(MessagesState.SCHEMA, MessagesState::new)
                .addNode("child:step_1", node_async(childStep1) )
                .addNode("child:step_2", node_async(childStep2))
                .addNode("child:step_3", node_async(childStep3))
                .addEdge(START, "child:step_1")
                .addEdge("child:step_1", "child:step_2")
                .addEdge("child:step_2", "child:step_3")
                .addEdge("child:step_3", END)
                //.compile(compileConfig)
                ;
        var step1 = node_async((MessagesState state) -> Map.of("messages", "step1"));

        var step2 = node_async((MessagesState state) -> Map.of("messages", "step2"));

        var step3 = node_async((MessagesState state) -> Map.of("messages", "step3"));

        var workflowParent = new StateGraph<>(MessagesState.SCHEMA, MessagesState::new)
                .addNode("step_1", step1)
                .addNode("step_2", step2)
                .addNode("step_3", step3)
                .addSubgraph("subgraph", workflowChild)
                .addEdge(START, "step_1")
                .addEdge("step_1", "step_2")
                .addEdge("step_2", "subgraph")
                .addEdge("subgraph", "step_3")
                .addEdge("step_3", END)
                .compile(compileConfig);


        var result = workflowParent.stream(Map.of())
                .stream()
                .peek( n -> log.info("{}", n) )
                .reduce((a, b) -> b)
                .map(NodeOutput::state);

        assertTrue(result.isPresent());
        assertIterableEquals(List.of("step1", "step2", "child:step1", "child:step2", "child:step3", "step3"), result.get().messages());

    }

}
