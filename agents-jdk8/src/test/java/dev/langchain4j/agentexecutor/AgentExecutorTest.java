package dev.langchain4j.agentexecutor;

import dev.langchain4j.DotEnvConfig;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.var;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.bsc.langgraph4j.utils.CollectionsUtils.listOf;
import static org.bsc.langgraph4j.utils.CollectionsUtils.mapOf;
import static org.junit.jupiter.api.Assertions.*;

public class AgentExecutorTest {

    @BeforeAll
    public static void loadEnv() {
        DotEnvConfig.load();
    }

    private AgentExecutor.State executeAgent(String prompt )  throws Exception {

        var openApiKey = DotEnvConfig.valueOf("OPENAI_API_KEY")
                .orElseThrow( () -> new IllegalArgumentException("no APIKEY provided!"));

        var chatLanguageModel = OpenAiChatModel.builder()
                .apiKey( openApiKey )
                .modelName( "gpt-3.5-turbo-0125" )
                .logResponses(true)
                .maxRetries(2)
                .temperature(0.0)
                .maxTokens(2000)
                .build();


        var agentExecutor = new AgentExecutor();

        var iterator = agentExecutor.execute(
                chatLanguageModel,
                mapOf( "input", prompt ),
                listOf(new TestTool()) );

       AgentExecutor.State state = null;

        for( var i : iterator ) {
            state = i.state();
            System.out.println(i.node());
        }

        return state;

    }

    @Test
    void executeAgentWithSingleToolInvocation() throws Exception {

        var state = executeAgent("what is the result of test with messages: 'MY FIRST TEST'");

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

        var state = executeAgent("what is the result of test with messages: 'MY FIRST TEST' and the result of test with message: 'MY SECOND TEST'");

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
}
