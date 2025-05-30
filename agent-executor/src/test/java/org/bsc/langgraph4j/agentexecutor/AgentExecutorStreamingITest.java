package org.bsc.langgraph4j.agentexecutor;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import org.bsc.async.AsyncGenerator;
import org.bsc.langgraph4j.*;
import org.bsc.langgraph4j.checkpoint.BaseCheckpointSaver;
import org.bsc.langgraph4j.checkpoint.MemorySaver;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.streaming.StreamingOutput;
import org.bsc.langgraph4j.utils.CollectionsUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import static org.junit.jupiter.api.Assertions.*;

public class AgentExecutorStreamingITest {

    private StateGraph<AgentExecutor.State> newGraph()  throws Exception {

        var chatLanguageModel = OpenAiStreamingChatModel.builder()
                .apiKey( System.getenv( "OPENAI_API_KEY") )
                .modelName( "gpt-4o-mini" )
                .logResponses(true)
                .temperature(0.0)
                .maxTokens(2000)
                .build();

        return AgentExecutor.builder()
                .chatModel(chatLanguageModel)
                .toolsFromObject(new TestTool())
                .build();
    }

    private List<AgentExecutor.State> executeAgent( String prompt )  throws Exception {

        return toStateList( newGraph().compile().stream( Map.of( "messages", UserMessage.from(prompt) ) ) );
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

        return toStateList(  graph.stream(  Map.of( "messages", UserMessage.from(prompt) ), config ) );
    }

    private List<AgentExecutor.State> toStateList(AsyncGenerator<NodeOutput<AgentExecutor.State>> generator ) {

        return generator.stream()
                .filter( s -> {
                    if( s instanceof StreamingOutput<AgentExecutor.State> streamingOutput) {
                        System.out.printf( "%s '%s'\n", streamingOutput.node(), streamingOutput.chunk() );
                        return false;
                    }
                    return true;
                })
                .peek( s -> System.out.printf( "NODE: %s\n", s.node() ) )
                .map( NodeOutput::state)
                .collect(Collectors.toList());
    }

    @Test
    void executeAgentWithSingleToolInvocation() throws Exception {

        var states = executeAgent("what is the result of test with messages: 'MY FIRST TEST'");
        assertEquals( 5, states.size() );
        var state = CollectionsUtils.last(states).orElse(null);
        assertNotNull(state);
        assertTrue(state.finalResponse().isPresent());
        System.out.println(state.finalResponse().get());


    }

    @Test
    void executeAgentWithDoubleToolInvocation() throws Exception {

        var states = executeAgent("what is the result of test with messages: 'MY FIRST TEST' and the result of test with message: 'MY SECOND TEST'");
        assertEquals( 5, states.size() );
        var state = CollectionsUtils.last(states).orElse(null);
        assertNotNull(state);
        assertTrue(state.finalResponse().isPresent());
        System.out.println(state.finalResponse().get());

    }

    @Test
    void executeAgentWithDoubleToolInvocationWithCheckpoint() throws Exception {

        var saver = new MemorySaver();
        var states = executeAgent(
                "what is the result of test with messages: 'MY FIRST TEST' and the result of test with message: 'MY SECOND TEST'",
                "thread_1",
                saver
                );
        assertEquals( 5, states.size() );
        var state = CollectionsUtils.last(states).orElse(null);
        assertNotNull(state);
        assertTrue(state.finalResponse().isPresent());
        System.out.println(state.finalResponse().get());

        states = executeAgent(
                "what is the result of test with messages: 'MY FIRST TEST' and the result of test with message: 'MY SECOND TEST'",
                "thread_1",
                saver
        );
        assertEquals( 3, states.size() );
        state = CollectionsUtils.last(states).orElse(null);
        assertNotNull(state);
        assertTrue(state.finalResponse().isPresent());
        System.out.println(state.finalResponse().get());


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

        var plantUml = app.getGraph( GraphRepresentation.Type.PLANTUML, "Agent Executor" );

        System.out.println( plantUml.content() );

        var mermaid = app.getGraph( GraphRepresentation.Type.MERMAID, "Agent Executor" );

        System.out.println( mermaid.content() );
    }
}
