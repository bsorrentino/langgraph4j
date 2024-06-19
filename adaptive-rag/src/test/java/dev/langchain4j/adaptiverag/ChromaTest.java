package dev.langchain4j.adaptiverag;

import dev.langchain4j.DotEnvConfig;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import org.junit.jupiter.api.BeforeEach;
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore;

import java.io.FileInputStream;
import java.time.Duration;
import java.util.List;
import java.util.logging.LogManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChromaTest {

    @BeforeEach
    public void init() throws Exception {
        FileInputStream configFile = new FileInputStream("logging.properties");
        LogManager.getLogManager().readConfiguration(configFile);

        DotEnvConfig.load();
    }

    //@Test
    public void connect() throws Exception {
        String openApiKey = DotEnvConfig.valueOf("OPENAI_API_KEY")
                .orElseThrow( () -> new IllegalArgumentException("no APIKEY provided!"));

        ChromaEmbeddingStore chroma = new ChromaEmbeddingStore(
                "http://localhost:8000",
                "rag-chroma",
                Duration.ofMinutes(2) );

        OpenAiEmbeddingModel embeddingModel = OpenAiEmbeddingModel.builder()
                .apiKey(openApiKey)
                .build();
        Embedding queryEmbedding = embeddingModel.embed( "What are the types of agent memory?" ).content();

        EmbeddingSearchRequest query = EmbeddingSearchRequest.builder()
                .queryEmbedding( queryEmbedding )
                .maxResults( 3 )
                .minScore( 0.0 )
                .build();
        EmbeddingSearchResult<TextSegment> relevant = chroma.search( query );

        List<EmbeddingMatch<TextSegment>> matches = relevant.matches();

        assertEquals( 3, matches.size() );
        System.out.println( matches );
    }
}
