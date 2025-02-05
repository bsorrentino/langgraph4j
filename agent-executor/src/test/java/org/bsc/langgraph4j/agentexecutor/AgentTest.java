package org.bsc.langgraph4j.agentexecutor;

import org.bsc.langgraph4j.DotEnvConfig;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.FinishReason;
import org.bsc.langgraph4j.langchain4j.tool.ToolNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;

@Disabled
public class AgentTest {

    @BeforeAll
    public static void loadEnv() {
        DotEnvConfig.load();
    }

    @Test
    public void runAgentTest() throws Exception  {

        assertTrue(DotEnvConfig.valueOf("OPENAI_API_KEY").isPresent());

        var chatLanguageModel = OpenAiChatModel.builder()
                .apiKey( DotEnvConfig.valueOf("OPENAI_API_KEY").get() )
                .modelName( "gpt-4o-mini" )
                .logResponses(true)
                .maxRetries(2)
                .temperature(0.0)
                .maxTokens(2000)
                .build();

        var tool = new TestTool();
        var agent = Agent.builder()
                .chatLanguageModel(chatLanguageModel)
                .tools( ToolNode.of(tool).toolSpecifications() )
                .build();

        var msg = "hello world";
        var response = agent.execute( format("this is an AI test with message: '%s'", msg), emptyList() );

        assertNotNull(response);
        assertEquals(response.finishReason(), FinishReason.TOOL_EXECUTION );
        var content = response.content();
        assertNotNull(content);
        assertNull( content.text());
        assertTrue(content.hasToolExecutionRequests());
        var toolExecutionRequests = content.toolExecutionRequests();
        assertEquals(1, toolExecutionRequests.size());
        var toolExecutionRequest = toolExecutionRequests.get(0);
        assertEquals("execTest", toolExecutionRequest.name());
        assertEquals("{\"arg0\":\"hello world\"}", toolExecutionRequest.arguments().replaceAll("\n",""));

    }
}
