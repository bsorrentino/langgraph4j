package org.bsc.langgraph4j.agentexecutor;

import dev.langchain4j.model.openai.OpenAiChatModel;
import org.bsc.langgraph4j.StateGraph;


public class AgentExecutorITest extends AbstractAgentExecutorTest {


    @Override
    protected StateGraph<AgentExecutor.State> newGraph() throws Exception {

        var chatModel = OpenAiChatModel.builder()
                .apiKey( System.getenv("OPENAI_API_KEY") )
                .modelName( "gpt-4o-mini" )
                .logResponses(true)
                .maxRetries(2)
                .temperature(0.0)
                .maxTokens(2000)
                .build();

        return AgentExecutor.builder()
                .chatModel(chatModel)
                .toolsFromObject(new TestTool())
                .build();

    }
}
