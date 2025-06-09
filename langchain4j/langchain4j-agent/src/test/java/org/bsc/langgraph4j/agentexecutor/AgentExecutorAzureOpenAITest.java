package org.bsc.langgraph4j.agentexecutor;

import dev.langchain4j.model.azure.AzureOpenAiChatModel;
import org.bsc.langgraph4j.StateGraph;

public class AgentExecutorAzureOpenAITest extends AbstractAgentExecutorTest {

    @Override
    protected StateGraph<AgentExecutor.State> newGraph() throws Exception {

        var chatLanguageModel = AzureOpenAiChatModel.builder()
                .apiKey(System.getenv( "AZURE_OPENAI_API_KEY"))
                .deploymentName( System.getenv("AZURE_OPENAI_DEPLOYMENT_NAME") )
                .endpoint( System.getenv("AZURE_OPENAI_ENDPOINT") )
                .logRequestsAndResponses(true)
                .maxRetries(2)
                .temperature(0.0)
                .maxTokens(2000)
                .build();

        return AgentExecutor.builder()
                .chatModel(chatLanguageModel)
                .toolsFromObject(new TestTool())
                .build();
    }
}
