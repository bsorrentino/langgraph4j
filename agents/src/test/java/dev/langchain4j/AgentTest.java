package dev.langchain4j;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.model.output.FinishReason;
import org.junit.jupiter.api.BeforeAll;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

public class AgentTest {

    @BeforeAll
    static void init() {
        DotEnvConfig.load();
    }

    public static void main( String[] args) throws Exception  {
        DotEnvConfig.load();

        assertTrue(DotEnvConfig.valueOf("OPENAI_API_KEY").isPresent());

        var chatLanguageModel = OpenAiChatModel.builder()
                .apiKey( DotEnvConfig.valueOf("OPENAI_API_KEY").get() )
                .modelName( "gpt-3.5-turbo-0613" )
                .logResponses(true)
                .maxRetries(2)
                .temperature(0.0)
                .maxTokens(2000)
                .build();

        var tool = new TestTool();
        var agent = Agent.builder()
                .chatLanguageModel(chatLanguageModel)
                .tools( ToolInfo.of(tool).stream().map(ToolInfo::specification).toList() )
                .build();

        var msg = "hello world";
        var response = agent.execute( format("this is an AI test with message: '%s'", msg), List.of() );

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
        assertEquals("{  \"arg0\": \"hello world\"}", toolExecutionRequest.arguments().replaceAll("\n",""));

        System.out.println( response );

    }
}
