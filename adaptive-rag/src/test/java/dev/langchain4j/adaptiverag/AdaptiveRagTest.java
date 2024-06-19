package dev.langchain4j.adaptiverag;

import dev.langchain4j.DotEnvConfig;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore;
import lombok.var;
import org.bsc.langgraph4j.GraphRepresentation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.time.Duration;
import java.util.List;
import java.util.logging.LogManager;
import java.util.stream.Collectors;

import static org.bsc.langgraph4j.utils.CollectionsUtils.mapOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AdaptiveRagTest {

    @BeforeAll
    public static void beforeAll() throws Exception {
        FileInputStream configFile = new FileInputStream("logging.properties");
        LogManager.getLogManager().readConfiguration(configFile);

        DotEnvConfig.load();
    }

    String getOpenAiKey() {
        return DotEnvConfig.valueOf("OPENAI_API_KEY")
                .orElseThrow( () -> new IllegalArgumentException("no OPENAI APIKEY provided!"));
    }

    String getTavilyApiKey() {
        return DotEnvConfig.valueOf("TAVILY_API_KEY")
                .orElseThrow( () -> new IllegalArgumentException("no TAVILY APIKEY provided!"));
    }

    @Test
    public void QuestionRewriterTest() {

        String result = QuestionRewriter.of(getOpenAiKey()).apply("agent memory");
        assertEquals("What is the role of memory in an agent's functioning?", result);
    }

    @Test
    public void RetrievalGraderTest() {

        String openApiKey = DotEnvConfig.valueOf("OPENAI_API_KEY")
                .orElseThrow( () -> new IllegalArgumentException("no APIKEY provided!"));

        RetrievalGrader grader = RetrievalGrader.of(openApiKey);

        ChromaEmbeddingStore chroma = new ChromaEmbeddingStore(
                "http://localhost:8000",
                "rag-chroma",
                Duration.ofMinutes(2) );
        OpenAiEmbeddingModel embeddingModel = OpenAiEmbeddingModel.builder()
                .apiKey(openApiKey)
                .build();

        String question = "agent memory";
        Embedding queryEmbedding = embeddingModel.embed(question).content();

        EmbeddingSearchRequest query = EmbeddingSearchRequest.builder()
                .queryEmbedding( queryEmbedding )
                .maxResults( 1 )
                .minScore( 0.0 )
                .build();
        EmbeddingSearchResult<TextSegment> relevant = chroma.search( query );

        List<EmbeddingMatch<TextSegment>> matches = relevant.matches();

        assertEquals( 1, matches.size() );

        RetrievalGrader.Score answer =
                grader.apply( RetrievalGrader.Arguments.of(question, matches.get(0).embedded().text()));

        assertEquals( "no", answer.binaryScore);


    }

    @Test
    public void WebSearchTest() {

        WebSearchTool webSearchTool = WebSearchTool.of(getTavilyApiKey());
        List<Content> webSearchResults = webSearchTool.apply("agent memory");

        String webSearchResultsText = webSearchResults.stream().map( content -> content.textSegment().text() )
                .collect(Collectors.joining("\n"));

        assertNotNull( webSearchResultsText );

        System.out.println( webSearchResultsText );

    }

    @Test
    public void questionRouterTest() {

        QuestionRouter qr = QuestionRouter.of(getOpenAiKey());

        QuestionRouter.Type result = qr.apply( "What are the stock options?");

        assertEquals( QuestionRouter.Type.web_search, result );

        result = qr.apply( "agent memory?");

        assertEquals( QuestionRouter.Type.vectorstore, result );
    }


    @Test
    public void generationTest() {

        ChromaStore retriever = ChromaStore.of(getOpenAiKey());

        String question = "agent memory";
        var relevantDocs = retriever.search(question);

        List<String> docs = relevantDocs.matches().stream()
                                .map( m -> m.embedded().text() )
                                .collect(Collectors.toList());
        Generation qr = Generation.of(getOpenAiKey());

        String result = qr.apply( question, docs );

        System.out.println( result );
    }

    @Test
    public void getGraphTest() throws Exception {

        AdaptiveRag adaptiveRag = new AdaptiveRag(getOpenAiKey(), getTavilyApiKey());

        var graph = adaptiveRag.buildGraph();

        var plantUml = graph.getGraph( GraphRepresentation.Type.PLANTUML );

        System.out.println( plantUml.getContent() );
    }
}
