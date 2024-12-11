package org.bsc.langgraph4j.agentexecutor;

import dev.langchain4j.model.openai.OpenAiChatModel;
import org.bsc.langgraph4j.DotEnvConfig;
import org.bsc.langgraph4j.StateGraph;
import org.junit.jupiter.api.Disabled;

@Disabled
public class AgentExecutorTest  extends AbstractAgentExecutorTest {


    @Override
    protected StateGraph<AgentExecutor.State> newGraph() throws Exception {
        var openApiKey = DotEnvConfig.valueOf("OPENAI_API_KEY")
                .orElseThrow( () -> new IllegalArgumentException("no APIKEY provided!"));

        var chatLanguageModel = OpenAiChatModel.builder()
                .apiKey( openApiKey )
                .modelName( "gpt-4o-mini" )
                .logResponses(true)
                .maxRetries(2)
                .temperature(0.0)
                .maxTokens(2000)
                .build();

        return AgentExecutor.graphBuilder()
                .chatLanguageModel(chatLanguageModel)
                .toolSpecification(new TestTool())
                .build();

    }
}
