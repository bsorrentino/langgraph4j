package org.bsc.langgraph4j.agentexecutor;

import dev.langchain4j.data.message.UserMessage;
import org.bsc.langgraph4j.DotEnvConfig;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.FinishReason;
import org.bsc.langgraph4j.langchain4j.tool.ToolNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.lang.String.format;
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

        var toolNode = ToolNode.builder()
                .specification( new TestTool() )
                .build();

        var agent = Agent.builder()
                .chatLanguageModel(chatLanguageModel)
                .tools( toolNode.toolSpecifications() )
                .build();

        var msg = "hello world";
        var response = agent.execute( List.of( UserMessage.from(format("this is an AI test with message: '%s'", msg) )));

        assertNotNull(response);
        assertEquals(FinishReason.TOOL_EXECUTION, response.finishReason() );
        var content = response.aiMessage();
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
