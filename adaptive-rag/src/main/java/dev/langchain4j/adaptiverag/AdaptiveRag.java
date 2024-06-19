package dev.langchain4j.adaptiverag;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import lombok.var;
import org.bsc.langgraph4j.state.AgentState;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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

        public String question() {
            Optional<String> result = value("question");
            return result.orElseThrow( () -> new IllegalStateException( "question is not set!" ) );
        }
        public String generation() {
            Optional<String> result = value("generation");
            return result.orElseThrow( () -> new IllegalStateException( "generation is not set!" ) );

        }
        public List<String> documents() {
            Optional<List<String>> result =  value("documents");
            return result.orElse(emptyList());
        }

    }

    private final String openApiKey;
    private final String tavilyApiKey;
    private final ChromaStore chroma;

    public AdaptiveRag( String openApiKey, String tavilyApiKey ) {
        this.openApiKey = openApiKey;
        this.tavilyApiKey = tavilyApiKey;
        this.chroma = ChromaStore.of(openApiKey);

    }

    /**
     * Node: Retrieve documents
     * @param state The current graph state
     * @return New key added to state, documents, that contains retrieved documents
     */
    public Map<String,Object> retrieve( State state ) {

        String question = state.question();

        EmbeddingSearchResult<TextSegment> relevant = this.chroma.search( question );

        List<String> documents = relevant.matches().stream()
                .map( m -> m.embedded().text() )
                .collect(Collectors.toList());

        return mapOf( "documents", documents , "question", question );
    }

    /**
     * Node: Generate answer
     *
     * @param state The current graph state
     * @return New key added to state, generation, that contains LLM generation
     */
    public Map<String,Object> generate( State state ) {
        String question = state.question();
        List<String> documents = state.documents();

        String generation = Generation.of(openApiKey).apply(question, documents); // service

        return mapOf("generation", generation);
    }

    /**
     * Node: Determines whether the retrieved documents are relevant to the question.
     * @param state  The current graph state
     * @return Updates documents key with only filtered relevant documents
     */
    public Map<String,Object> gradeDocuments( State state ) {

        String question = state.question();

        List<String> documents = state.documents();

        final RetrievalGrader grader = RetrievalGrader.of( openApiKey );

        List<String> filteredDocs =  documents.stream()
                .filter( d -> {
                    var score = grader.apply( RetrievalGrader.Arguments.of(question, d ));
                    return score.binaryScore.equals("yes");
                })
                .collect(Collectors.toList());

        return mapOf( "documents", filteredDocs);
    }

    /**
     * Node: Transform the query to produce a better question.
     * @param state  The current graph state
     * @return Updates question key with a re-phrased question
     */
    public Map<String,Object> transformQuery( State state ) {
        String question = state.question();

        String betterQuestion = QuestionRewriter.of( openApiKey ).apply( question );

        return mapOf( "question", betterQuestion );
    }

    /**
     * Node: Web search based on the re-phrased question.
     * @param state  The current graph state
     * @return Updates documents key with appended web results
     */
    public Map<String,Object> webSearch( State state ) {
        String question = state.question();

        var result = WebSearchTool.of( tavilyApiKey ).apply(question);

        var webResult = result.stream()
                            .map( content -> content.textSegment().text() )
                            .collect(Collectors.joining("\n"));

        return mapOf( "documents", listOf( webResult ) );
    }

    /**
     * Edge: Route question to web search or RAG.
     * @param state The current graph state
     * @return Next node to call
     */
    public String routeQuestion( State state  ) {
        String question = state.question();

        var source = QuestionRouter.of( openApiKey ).apply( question );

        return source.name();
    }

    /**
     * Edge: Determines whether to generate an answer, or re-generate a question.
     * @param state The current graph state
     * @return Binary decision for next node to call
     */
    public String decideToGenerate( State state  ) {
        List<String> documents = state.documents();

        if(documents.isEmpty()) {
            return "transform_query";
        }
        return "generate";
    }

    /**
     * Edge: Determines whether the generation is grounded in the document and answers question.
     * @param state The current graph state
     * @return Decision for next node to call
     */
    public String gradeGeneration_v_DocumentsAndQuestion( State state ) {
        String question = state.question();
        List<String> documents = state.documents();
        String generation = state.generation();

        HallucinationGrader.Score score = HallucinationGrader.of( openApiKey )
                                            .apply( HallucinationGrader.Arguments.of(documents, generation));

        if(Objects.equals(score.binaryScore, "yes")) {

            AnswerGrader.Score score2 = AnswerGrader.of( openApiKey )
                                            .apply( AnswerGrader.Arguments.of(question, generation) );
            if( Objects.equals( score2.binaryScore, "yes") ) {
                return "useful";
            }

            return "not useful";
        }

        return "not supported";
    }
}
