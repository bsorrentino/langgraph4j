package dev.langchain4j.adaptiverag;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.GraphRepresentation;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.state.AgentState;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.LogManager;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import static org.bsc.langgraph4j.utils.CollectionsUtils.listOf;
import static org.bsc.langgraph4j.utils.CollectionsUtils.mapOf;

@Slf4j( topic="AdaptiveRag")
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
        public Optional<String> generation() {
            return value("generation");

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
        Objects.requireNonNull(openApiKey, "no OPENAI APIKEY provided!");
        Objects.requireNonNull(tavilyApiKey, "no TAVILY APIKEY provided!");
        this.openApiKey = openApiKey;
        this.tavilyApiKey = tavilyApiKey;
        this.chroma = ChromaStore.of(openApiKey);

    }

    /**
     * Node: Retrieve documents
     * @param state The current graph state
     * @return New key added to state, documents, that contains retrieved documents
     */
    private Map<String,Object> retrieve( State state ) {
        log.debug("---RETRIEVE---");

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
    private Map<String,Object> generate( State state ) {
        log.debug("---GENERATE---");

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
    private Map<String,Object> gradeDocuments( State state ) {
        log.debug("---CHECK DOCUMENT RELEVANCE TO QUESTION---");

        String question = state.question();

        List<String> documents = state.documents();

        final RetrievalGrader grader = RetrievalGrader.of( openApiKey );

        List<String> filteredDocs =  documents.stream()
                .filter( d -> {
                    var score = grader.apply( RetrievalGrader.Arguments.of(question, d ));
                    boolean relevant = score.binaryScore.equals("yes");
                    if( relevant ) {
                        log.debug("---GRADE: DOCUMENT RELEVANT---");
                    }
                    else {
                        log.debug("---GRADE: DOCUMENT NOT RELEVANT---");
                    }
                    return relevant;
                })
                .collect(Collectors.toList());

        return mapOf( "documents", filteredDocs);
    }

    /**
     * Node: Transform the query to produce a better question.
     * @param state  The current graph state
     * @return Updates question key with a re-phrased question
     */
    private Map<String,Object> transformQuery( State state ) {
        log.debug("---TRANSFORM QUERY---");

        String question = state.question();

        String betterQuestion = QuestionRewriter.of( openApiKey ).apply( question );

        return mapOf( "question", betterQuestion );
    }

    /**
     * Node: Web search based on the re-phrased question.
     * @param state  The current graph state
     * @return Updates documents key with appended web results
     */
    private Map<String,Object> webSearch( State state ) {
        log.debug("---WEB SEARCH---");

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
    private String routeQuestion( State state  ) {
        log.debug("---ROUTE QUESTION---");

        String question = state.question();

        var source = QuestionRouter.of( openApiKey ).apply( question );
        if( source == QuestionRouter.Type.web_search ) {
            log.debug("---ROUTE QUESTION TO WEB SEARCH---");
        }
        else {
            log.debug("---ROUTE QUESTION TO RAG---");
        }
        return source.name();
    }

    /**
     * Edge: Determines whether to generate an answer, or re-generate a question.
     * @param state The current graph state
     * @return Binary decision for next node to call
     */
    private String decideToGenerate( State state  ) {
        log.debug("---ASSESS GRADED DOCUMENTS---");
        List<String> documents = state.documents();

        if(documents.isEmpty()) {
            log.debug("---DECISION: ALL DOCUMENTS ARE NOT RELEVANT TO QUESTION, TRANSFORM QUERY---");
            return "transform_query";
        }
        log.debug( "---DECISION: GENERATE---" );
        return "generate";
    }

    /**
     * Edge: Determines whether the generation is grounded in the document and answers question.
     * @param state The current graph state
     * @return Decision for next node to call
     */
    private String gradeGeneration_v_documentsAndQuestion( State state ) {
        log.debug("---CHECK HALLUCINATIONS---");

        String question = state.question();
        List<String> documents = state.documents();
        String generation = state.generation()
                .orElseThrow( () -> new IllegalStateException( "generation is not set!" ) );


        HallucinationGrader.Score score = HallucinationGrader.of( openApiKey )
                                            .apply( HallucinationGrader.Arguments.of(documents, generation));

        if(Objects.equals(score.binaryScore, "yes")) {
            log.debug( "---DECISION: GENERATION IS GROUNDED IN DOCUMENTS---" );
            log.debug("---GRADE GENERATION vs QUESTION---");
            AnswerGrader.Score score2 = AnswerGrader.of( openApiKey )
                                            .apply( AnswerGrader.Arguments.of(question, generation) );
            if( Objects.equals( score2.binaryScore, "yes") ) {
                log.debug( "---DECISION: GENERATION ADDRESSES QUESTION---" );
                return "useful";
            }

            log.debug("---DECISION: GENERATION DOES NOT ADDRESS QUESTION---");
            return "not useful";
        }

        log.debug( "---DECISION: GENERATION IS NOT GROUNDED IN DOCUMENTS, RE-TRY---" );
        return "not supported";
    }

    public CompiledGraph<State> buildGraph() throws Exception {
        var workflow = new StateGraph<>(State::new);

        // Define the nodes
        workflow.addNode("web_search", node_async(this::webSearch) );  // web search
        workflow.addNode("retrieve", node_async(this::retrieve) );  // retrieve
        workflow.addNode("grade_documents",  node_async(this::gradeDocuments) );  // grade documents
        workflow.addNode("generate", node_async(this::generate) );  // generatae
        workflow.addNode("transform_query", node_async(this::transformQuery));  // transform_query

        // Build graph
        workflow.setConditionalEntryPoint(
                edge_async(this::routeQuestion),
                mapOf(
                    "web_search", "web_search",
                    "vectorstore", "retrieve"
                ));

        workflow.addEdge("web_search", "generate");
        workflow.addEdge("retrieve", "grade_documents");
        workflow.addConditionalEdges(
                "grade_documents",
                edge_async(this::decideToGenerate),
                mapOf(
                    "transform_query","transform_query",
                    "generate", "generate"
                ));
        workflow.addEdge("transform_query", "retrieve");
        workflow.addConditionalEdges(
                "generate",
                edge_async(this::gradeGeneration_v_documentsAndQuestion),
                mapOf(
                        "not supported", "generate",
                        "useful", END,
                        "not useful", "transform_query"
                ));

        return workflow.compile();
    }

    public static void main( String[] args ) throws Exception {
        try(FileInputStream configFile = new FileInputStream("logging.properties")) {
            LogManager.getLogManager().readConfiguration(configFile);
        };

        AdaptiveRag adaptiveRagTest = new AdaptiveRag( System.getenv("OPENAI_API_KEY"), System.getenv("TAVILY_API_KEY"));

        var graph = adaptiveRagTest.buildGraph();

        var result = graph.stream( mapOf( "question", "What player at the Bears expected to draft first in the 2024 NFL draft?" ) );
        // var result = graph.stream( mapOf( "question", "What kind the agent memory do iu know?" ) );

        String generation = "";
        for( var r : result ) {
            System.out.printf( "Node: '%s':\n", r.node() );

            generation = r.state().generation().orElse( "")
            ;
        }

        System.out.println( generation );

        // generate plantuml script
        // var plantUml = graph.getGraph( GraphRepresentation.Type.PLANTUML );
        // System.out.println( plantUml );

    }

}

