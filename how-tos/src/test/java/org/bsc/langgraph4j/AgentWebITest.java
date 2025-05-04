package org.bsc.langgraph4j;

import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.rag.content.retriever.WebSearchContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.web.search.tavily.TavilyWebSearchEngine;
import org.junit.jupiter.api.Test;

public class AgentWebITest {

    public interface Assistant {

        String answer(String query);
    }

    @Test
    public void webSearchTest() throws Exception {

        // Let's create our web search content retriever.
        var webSearchEngine = TavilyWebSearchEngine.builder()
                .apiKey(System.getenv("TAVILY_API_KEY")) // get a free key: https://app.tavily.com/sign-in
                .build();

        var webSearchContentRetriever = WebSearchContentRetriever.builder()
                .webSearchEngine(webSearchEngine)
                .maxResults(3)
                .build();

        final var model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .temperature(0.0)
                .logRequests(true)
                .logResponses(true)
                .modelName("qwen2.5-coder:latest")
                .build();

        var agent = AiServices.builder(Assistant.class)
                .chatModel(model)
                .contentRetriever(webSearchContentRetriever)
                .build();

        var result = agent.answer( "dammi la lista delle provincie della Campania (Italia) con la popolazione e la superficie");

        System.out.println( result );
    }

}
