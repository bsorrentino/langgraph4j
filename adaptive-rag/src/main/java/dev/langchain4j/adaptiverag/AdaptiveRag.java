package dev.langchain4j.adaptiverag;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.structured.StructuredPrompt;
import dev.langchain4j.model.input.structured.StructuredPromptProcessor;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore;
import lombok.Value;
import lombok.var;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AppendableValue;
import org.bsc.langgraph4j.utils.CollectionsUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.bsc.langgraph4j.utils.CollectionsUtils.listOf;
import static org.bsc.langgraph4j.utils.CollectionsUtils.mapOf;

public class AdaptiveRag {

    /**
     * Represents the state of our graph.
     *     Attributes:
     *         question: question
     *         generation: LLM generation
     *         documents: list of documents
     */
    public static class State extends AgentState {

        public State(Map<String, Object> initData) {
            super(initData);
        }

        public Optional<String> question() {
            return value("question");
        }
        public Optional<String> generation() {
            return value("generation");
        }
        public List<String> documents() {
            return (List<String>) value("documents").orElse(emptyList());
        }

    }

    private final String openApiKey;
    private final String tavilyApiKey;
    private final ChromaEmbeddingStore chroma = new ChromaEmbeddingStore(
            "http://localhost:8000",
            "rag-chroma",
            Duration.ofMinutes(2) );
    private final OpenAiEmbeddingModel embeddingModel;

    public AdaptiveRag( String openApiKey, String tavilyApiKey ) {
        this.openApiKey = openApiKey;
        this.tavilyApiKey = tavilyApiKey;

        this.embeddingModel = OpenAiEmbeddingModel.builder()
                .apiKey(openApiKey)
                .build();

    }

    private EmbeddingSearchResult<TextSegment> retrieverSearch( String question ) {

        Embedding queryEmbedding = embeddingModel.embed(question).content();

        EmbeddingSearchRequest query = EmbeddingSearchRequest.builder()
                .queryEmbedding( queryEmbedding )
                .maxResults( 1 )
                .minScore( 0.0 )
                .build();
        return chroma.search( query );

    }

    /**
     * Retrieve documents
     * @param state
     * @return
     */
    public Map<String,Object> retrieve( State state ) {

        String question = state.question()
                .orElseThrow( () -> new IllegalStateException( "question is null!" ) );

        EmbeddingSearchResult<TextSegment> relevant = retrieverSearch( question );

        List<String> documents = relevant.matches().stream()
                .map( m -> m.embedded().text() )
                .collect(Collectors.toList());

        return mapOf( "documents", documents , "question", question );
    }

    public interface RagService {

        @UserMessage("You are an assistant for question-answering tasks. Use the following pieces of retrieved context to answer the question. If you don't know the answer, just say that you don't know. Use three sentences maximum and keep the answer concise.\n" +
                "Question: {{question}} \n" +
                "Context: {{context}} \n" +
                "Answer:")
        String invoke(@V("question") String question, @V("context") List<String> context );
    }

    /**
     * Generate answer
     *
     * @param state
     * @return
     */
    public Map<String,Object> generate( State state ) {
        String question = state.question()
                .orElseThrow( () -> new IllegalStateException( "question is null!" ) );
        List<String> documents = state.documents();

        ChatLanguageModel chatLanguageModel = OpenAiChatModel.builder()
                .apiKey( openApiKey )
                .modelName( "gpt-3.5-turbo" )
                .timeout(Duration.ofMinutes(2))
                .logRequests(true)
                .logResponses(true)
                .maxRetries(2)
                .temperature(0.0)
                .maxTokens(2000)
                .build();

        RagService service = AiServices.create(RagService.class, chatLanguageModel);

        String generation = service.invoke( question, documents ); // service

        return mapOf("generation", generation);
    }

    /**
     * Determines whether the retrieved documents are relevant to the question.
     * @param state
     * @return
     */
    public Map<String,Object> gradeDocuments( State state ) {

        String question = state.question()
                .orElseThrow( () -> new IllegalStateException( "question is null!" ) );
        List<String> documents = state.documents();

        final RetrievalGrader grader = RetrievalGrader.of( openApiKey );

        List<String> filteredDocs =  documents.stream()
                .filter( d -> {
                    var score = grader.apply( new RetrievalGrader.Arguments(question, d ));
                    return score.binaryScore.equals("yes");
                })
                .collect(Collectors.toList());

        return mapOf( "documents", filteredDocs);
    }

    /**
     * Transform the query to produce a better question.
     * @param state
     * @return
     */
    public Map<String,Object> transformQuery( State state ) {
        String question = state.question()
                .orElseThrow( () -> new IllegalStateException( "question is null!" ) );
        List<String> documents = state.documents();

        String betterQuestion = QuestionRewriter.of( openApiKey ).apply( question );

        return mapOf( "question", betterQuestion );
    }

    /**
     * Web search based on the re-phrased question.
     * @param state
     * @return
     */
    public Map<String,Object> webSearch( State state ) {
        String question = state.question()
                .orElseThrow( () -> new IllegalStateException( "question is null!" ) );

        var result = WebSearchTool.of( tavilyApiKey ).apply(question);

        var webResult = result.stream()
                            .map( content -> content.textSegment().text() )
                            .collect(Collectors.joining("\n"));

        return mapOf( "documents", listOf( webResult ) );
    }
}
