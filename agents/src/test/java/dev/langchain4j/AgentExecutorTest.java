package dev.langchain4j;

import dev.langchain4j.model.openai.OpenAiChatModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

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
                Map.of( "input", prompt ),
                List.of(new TestTool()) );

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
        assertTrue(state.intermediateSteps().isPresent());
        assertEquals( 1, state.intermediateSteps().get().size());
        assertTrue(state.agentOutcome().isPresent());
        assertNotNull(state.agentOutcome().get().finish());
        assertTrue( state.agentOutcome().get().finish().returnValues().containsKey("returnValues"));
        assertEquals("The test with the message 'MY FIRST TEST' has been executed successfully.",
                state.agentOutcome().get().finish().returnValues().get("returnValues") );
    }
    @Test
    void executeAgentWithDoubleToolInvocation() throws Exception {

        var state = executeAgent("what is the result of test with messages: 'MY FIRST TEST' and the result of test with message: 'MY SECOND TEST'");

        assertNotNull(state);
        assertTrue(state.intermediateSteps().isPresent());
        assertEquals( 2, state.intermediateSteps().get().size());
        assertTrue(state.agentOutcome().isPresent());
        assertNotNull(state.agentOutcome().get().finish());
        assertTrue( state.agentOutcome().get().finish().returnValues().containsKey("returnValues"));
        assertEquals(
                "The result of the test with the message 'MY FIRST TEST' is: test tool executed: MY FIRST TEST\n" +
                "The result of the test with the message 'MY SECOND TEST' is: test tool executed: MY SECOND TEST",
                state.agentOutcome().get().finish().returnValues().get("returnValues") );

    }
}
