package dev.langchain4j.adaptiverag;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.WebSearchContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.web.search.WebSearchEngine;
import dev.langchain4j.web.search.tavily.TavilyWebSearchEngine;
import dev.langchain4j.rag.content.Content;
import java.util.List;
import java.util.function.Function;

/**
* The `WebSearchTool` class provides a solution for performing web searches based on user queries.
* It implements the {@literal Function<String, List<Content>> } interface to define how queries are processed and results are returned.
* The class utilizes an API key from Tavily to authenticate requests to their search engine.
* 
* @since 1.0
* @author Your Name (if applicable)
*/
public record WebSearchTool( String tavilyApiKey ) implements Function<String, List<Content>> {

    /**
     * Applies the query to a search engine and retrieves up to 3 results.
     *
     * @param query The search query to be executed.
     * @return A list of content items retrieved from the searchengine.
     */
    @Override
    public List<Content> apply(String query) {
        WebSearchEngine webSearchEngine = TavilyWebSearchEngine.builder()
                .apiKey(tavilyApiKey) // get a free key: https://app.tavily.com/sign-in
                .build();

        ContentRetriever webSearchContentRetriever = WebSearchContentRetriever.builder()
                .webSearchEngine(webSearchEngine)
                .maxResults(3)
                .build();

        return webSearchContentRetriever.retrieve( new Query( query ) );
    }
}