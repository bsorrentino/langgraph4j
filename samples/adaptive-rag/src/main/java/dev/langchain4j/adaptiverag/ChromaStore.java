package dev.langchain4j.adaptiverag;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore;
import java.time.Duration;

/**
 * The ChromaStore class provides a singleton interface for interacting with the Chroma Embedding Store.
 * This class is designed to be used in conjunction with an OpenAI API key to perform search operations on text segments.
 * It abstracts the embedding model and the storage layer, offering a simple method to retrieve relevant data based on queries.
 */
public final class ChromaStore {
    /**
     * Creates a new instance of {@link ChromaStore} using the provided OpenAI API key.
     *
     * @param openApiKey The API key to be used for authentication with OpenAI services.
     * @return A new instance of {@link ChromaStore}.
     */
    public static ChromaStore of(String openApiKey) {
        return new ChromaStore(openApiKey);
    }

    private final ChromaEmbeddingStore chroma = new ChromaEmbeddingStore(
            "http://localhost:8000",
            "rag-chroma",
            Duration.ofMinutes(2),
            true,
            true );
    private final OpenAiEmbeddingModel embeddingModel;

    /**
     * This constructor initializes a new instance of the ChromaStore with the provided OpenAI API key.
     *
     * @param openApiKey The API key for accessing OpenAI services.
     */
    private ChromaStore( String openApiKey ) {
        this.embeddingModel = OpenAiEmbeddingModel.builder()
                .apiKey(openApiKey)
                .build();
    }

    /**
     * Searches for text segments in the embedding index that match a given query.
     *
     * @param query The search query to be embedded and used for searching.
     * @return An EmbeddingSearchResult containing up to one TextSegment that best matches the query.
     */
    public EmbeddingSearchResult<TextSegment> search(String query) {

        Embedding queryEmbedding = embeddingModel.embed(query).content();

        EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding( queryEmbedding )
                .maxResults( 1 )
                .minScore( 0.0 )
                .build();
        return chroma.search( searchRequest );

    }
}