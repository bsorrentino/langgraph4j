package org.bsc.langgraph4j.agentexecutor;

import dev.langchain4j.model.azure.AzureOpenAiChatModel;
import org.bsc.langgraph4j.DotEnvConfig;
import org.bsc.langgraph4j.StateGraph;
import org.junit.jupiter.api.Disabled;

@Disabled
public class AgentExecutorAzureOpenAITest extends AbstractAgentExecutorTest {

    @Override
    protected StateGraph<AgentExecutor.State> newGraph() throws Exception {
        var openApiKey = DotEnvConfig.valueOf("AZURE_OPENAI_API_KEY")
                .orElseThrow( () -> new IllegalArgumentException("no AZURE_OPENAI_API_KEY provided!"));
        var endpoint = DotEnvConfig.valueOf("AZURE_OPENAI_ENDPOINT")
                .orElseThrow( () -> new IllegalArgumentException("no AZURE_OPENAI_ENDPOINT provided!"));
        var deployment = DotEnvConfig.valueOf("AZURE_OPENAI_DEPLOYMENT_NAME")
                .orElseThrow(() -> new IllegalArgumentException("no AZURE_OPENAI_DEPLOYMENT_NAME provided!") );

        var chatLanguageModel = AzureOpenAiChatModel.builder()
                .apiKey(openApiKey)
                .deploymentName( deployment )
                .endpoint( endpoint )
                .logRequestsAndResponses(true)
                .maxRetries(2)
                .temperature(0.0)
                .maxTokens(2000)
                .build();

        return AgentExecutor.builder()
                .chatLanguageModel(chatLanguageModel)
                .toolSpecification(new TestTool())
                .build();
    }
}
