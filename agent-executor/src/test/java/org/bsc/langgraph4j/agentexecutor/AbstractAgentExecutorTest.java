package org.bsc.langgraph4j.agentexecutor;

import dev.langchain4j.model.azure.AzureOpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.bsc.langgraph4j.*;
import org.bsc.langgraph4j.checkpoint.BaseCheckpointSaver;
import org.bsc.langgraph4j.checkpoint.MemorySaver;
import org.bsc.langgraph4j.state.AgentState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractAgentExecutorTest {

    @BeforeAll
    public static void loadEnv() {
        DotEnvConfig.load();
    }

    protected abstract  StateGraph<AgentExecutor.State> newGraph()  throws Exception ;


    private List<AgentExecutor.State> executeAgent( String prompt )  throws Exception {

        var iterator = newGraph().compile().stream( Map.of( "input", prompt ) );

        return iterator.stream()
                .peek( s -> System.out.println( s.node() ) )
                .map( NodeOutput::state)
                .collect(Collectors.toList());
    }

    private List<AgentExecutor.State> executeAgent( String prompt,
                                                    String threadId,
                                                    BaseCheckpointSaver saver)  throws Exception
    {

        CompileConfig compileConfig = CompileConfig.builder()
                .checkpointSaver( saver )
                .build();

        var config = RunnableConfig.builder().threadId(threadId).build();

        var graph = newGraph().compile( compileConfig );

        var iterator = graph.stream( Map.of( "input", prompt ), config );

        return iterator.stream()
                .peek( s -> System.out.println( s.node() ) )
                .map( NodeOutput::state)
                .collect(Collectors.toList());
    }

    @Test
    void executeAgentWithSingleToolInvocation() throws Exception {

        var states = executeAgent("what is the result of test with messages: 'MY FIRST TEST'");
        var state = states.get( states.size() - 1 );
        assertNotNull(state);
        assertFalse(state.intermediateSteps().isEmpty());
        assertEquals( 1, state.intermediateSteps().size());
        assertTrue(state.agentOutcome().isPresent());
        assertNotNull(state.agentOutcome().get().finish());
        assertTrue( state.agentOutcome().get().finish().returnValues().containsKey("returnValues"));
        var returnValues = state.agentOutcome().get().finish().returnValues().get("returnValues").toString();
        assertTrue( returnValues.contains( "MY FIRST TEST") );
        System.out.println(returnValues);
    }

    @Test
    void executeAgentWithDoubleToolInvocation() throws Exception {

        var states = executeAgent("what is the result of test with messages: 'MY FIRST TEST' and the result of test with message: 'MY SECOND TEST'");
        var state = states.get( states.size() - 1 );
        assertNotNull(state);
        assertFalse(state.intermediateSteps().isEmpty());
        assertEquals( 2, state.intermediateSteps().size());
        assertTrue(state.agentOutcome().isPresent());
        assertNotNull(state.agentOutcome().get().finish());
        assertTrue( state.agentOutcome().get().finish().returnValues().containsKey("returnValues"));
        var returnValues = state.agentOutcome().get().finish().returnValues().get("returnValues").toString();
        assertTrue( returnValues.contains( "MY FIRST TEST") );
        assertTrue( returnValues.contains( "MY SECOND TEST") );
        System.out.println(returnValues);

    }

    @Test
    void executeAgentWithDoubleToolInvocationWithCheckpoint() throws Exception {

        var saver = new MemorySaver();
        var states = executeAgent(
                "what is the result of test with messages: 'MY FIRST TEST' and the result of test with message: 'MY SECOND TEST'",
                "thread_1",
                saver
                );
        assertEquals( 7, states.size() ); // iterations
        var state = states.get( states.size() - 1 );
        assertNotNull(state);
        assertFalse(state.intermediateSteps().isEmpty());
        assertEquals( 2, state.intermediateSteps().size());
        assertTrue(state.agentOutcome().isPresent());
        assertNotNull(state.agentOutcome().get().finish());
        assertTrue( state.agentOutcome().get().finish().returnValues().containsKey("returnValues"));
        var returnValues = state.agentOutcome().get().finish().returnValues().get("returnValues").toString();
        assertTrue( returnValues.contains( "MY FIRST TEST") );
        assertTrue( returnValues.contains( "MY SECOND TEST") );
        System.out.println(returnValues);

        states = executeAgent(
                "what is the result of test with messages: 'MY FIRST TEST' and the result of test with message: 'MY SECOND TEST'",
                "thread_1",
                saver
        );
        assertEquals( 3, states.size() ); // iterations
        state = states.get( states.size() - 1 );
        assertNotNull(state);
        assertTrue(state.agentOutcome().isPresent());
        assertNotNull(state.agentOutcome().get().finish());
        assertTrue( state.agentOutcome().get().finish().returnValues().containsKey("returnValues"));
        returnValues = state.agentOutcome().get().finish().returnValues().get("returnValues").toString();
        assertTrue( returnValues.contains( "MY FIRST TEST") );
        assertTrue( returnValues.contains( "MY SECOND TEST") );
        System.out.println(returnValues);
    }

    @Test
    public void getGraphTest() throws Exception {

        var app = new StateGraph<>(AgentState::new)
            .addEdge(START,"agent")
            .addNode( "agent", node_async( state -> Map.of() ))
            .addNode( "action", node_async( state -> Map.of() ))
            .addConditionalEdges(
                    "agent",
                    edge_async(state -> ""),
                    Map.of("continue", "action", "end", END)
            )
            .addEdge("action", "agent")
            .compile();

        var printConditionalEdge = false;

        var plantUml = app.getGraph( GraphRepresentation.Type.PLANTUML, "Agent Executor", printConditionalEdge );

        System.out.println( plantUml.getContent() );

        var mermaid = app.getGraph( GraphRepresentation.Type.MERMAID, "Agent Executor", printConditionalEdge );

        System.out.println( mermaid.getContent() );
    }
}
