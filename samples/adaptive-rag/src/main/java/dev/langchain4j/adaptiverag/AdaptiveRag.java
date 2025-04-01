package dev.langchain4j.adaptiverag;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;

import org.bsc.langgraph4j.CompiledGraph;
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
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

/**
 * AdaptiveRag
 */
public class AdaptiveRag {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger("AdaptiveRag");

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
            return this.<List<String>>value("documents").orElse(emptyList());
        }

    }

    private final String openApiKey;
    private final String tavilyApiKey;
    private ChromaStore chroma;

    public ChromaStore getChroma() {
        if( chroma == null ) {
            chroma = openChroma();
        }
        return chroma;
    }

    /**
     * Constructor for the AdaptiveRag class.
     *
     * @param openApiKey  The API key for OpenAI, used for accessing its services.
     * @param tavilyApiKey  The API key for Tavily, used for web search functionality.
     *
     * This constructor initializes the AdaptiveRag instance by validating the provided API keys.
     * Both keys are required for the functionality of this class. If either key is null,
     * an exception is thrown. This ensures that the AdaptiveRag instance is always properly configured.
     */
    public AdaptiveRag(String openApiKey, String tavilyApiKey) {
        this.openApiKey     = Objects.requireNonNull(openApiKey, "no OPENAI APIKEY provided!");
        this.tavilyApiKey   = Objects.requireNonNull(tavilyApiKey, "no TAVILY APIKEY provided!");
        // The ChromaStore instance is lazily initialized when accessed via the getChroma() method.
    }
    private ChromaStore openChroma() {
        return ChromaStore.of(openApiKey);
    }

    /**
     * Node: Retrieve documents
     * @param state The current graph state
     * @return New key added to state, documents, that contains retrieved documents
     */
    private Map<String,Object> retrieve( State state ) {
        log.debug("---RETRIEVE---");

        String question = state.question();

        EmbeddingSearchResult<TextSegment> relevant = this.getChroma().search( question );

        List<String> documents = relevant.matches().stream()
                .map( m -> m.embedded().text() )
                .toList();

        return Map.of( "documents", documents , "question", question );
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

        String generation = (new Generation(openApiKey)).apply(question, documents); // service

        return Map.of("generation", generation);
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

        final RetrievalGrader grader = new RetrievalGrader( openApiKey );

        List<String> filteredDocs =  documents.stream()
                .filter( d -> {
                    RetrievalGrader.Score score = grader.apply( new RetrievalGrader.Arguments(question, d ));
                    boolean relevant = score.binaryScore.equals("yes");
                    if( relevant ) {
                        log.debug("---GRADE: DOCUMENT RELEVANT---");
                    }
                    else {
                        log.debug("---GRADE: DOCUMENT NOT RELEVANT---");
                    }
                    return relevant;
                })
                .toList();

        return Map.of( "documents", filteredDocs);
    }

    /**
     * Node: Transform the query to produce a better question.
     * @param state  The current graph state
     * @return Updates question key with a re-phrased question
     */
    private Map<String,Object> transformQuery( State state ) {
        log.debug("---TRANSFORM QUERY---");

        String question = state.question();

        String betterQuestion = (new QuestionRewriter( openApiKey )).apply( question );

        return Map.of( "question", betterQuestion );
    }

    /**
     * Node: Web search based on the re-phrased question.
     * @param state  The current graph state
     * @return Updates documents key with appended web results
     */
    private Map<String,Object> webSearch( State state ) {
        log.debug("---WEB SEARCH---");

        String question = state.question();

        List<dev.langchain4j.rag.content.Content> result = (new WebSearchTool( tavilyApiKey )).apply(question);

        String webResult = result.stream()
                            .map( content -> content.textSegment().text() )
                            .collect(Collectors.joining("\n"));

        return Map.of( "documents", List.of( webResult ) );
    }

    /**
     * Edge: Route question to web search or RAG.
     * @param state The current graph state
     * @return Next node to call
     */
    private String routeQuestion( State state  ) {
        log.debug("---ROUTE QUESTION---");

        String question = state.question();

        QuestionRouter.Type source = (new QuestionRouter( openApiKey )).apply( question );
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


        HallucinationGrader.Score score = (new HallucinationGrader( openApiKey ))
                                            .apply( new HallucinationGrader.Arguments(documents, generation));

        if(Objects.equals(score.binaryScore, "yes")) {
            log.debug( "---DECISION: GENERATION IS GROUNDED IN DOCUMENTS---" );
            log.debug("---GRADE GENERATION vs QUESTION---");
            AnswerGrader.Score score2 = (new AnswerGrader( openApiKey ))
                                            .apply( new AnswerGrader.Arguments(question, generation) );
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

    public StateGraph<State> buildGraph() throws Exception {
        return new StateGraph<>(State::new)
            // Define the nodes
            .addNode("web_search", node_async(this::webSearch) )  // web search
            .addNode("retrieve", node_async(this::retrieve) )  // retrieve
            .addNode("grade_documents",  node_async(this::gradeDocuments) )  // grade documents
            .addNode("generate", node_async(this::generate) )  // generatae
            .addNode("transform_query", node_async(this::transformQuery))  // transform_query
            // Build graph
            .addConditionalEdges(START,
                    edge_async(this::routeQuestion),
                    Map.of(
                        "web_search", "web_search",
                        "vectorstore", "retrieve"
                    ))

            .addEdge("web_search", "generate")
            .addEdge("retrieve", "grade_documents")
            .addConditionalEdges(
                    "grade_documents",
                    edge_async(this::decideToGenerate),
                    Map.of(
                        "transform_query","transform_query",
                        "generate", "generate"
                    ))
            .addEdge("transform_query", "retrieve")
            .addConditionalEdges(
                    "generate",
                    edge_async(this::gradeGeneration_v_documentsAndQuestion),
                    Map.of(
                            "not supported", "generate",
                            "useful", END,
                            "not useful", "transform_query"
                    ))
             ;
    }

    public static void main( String[] args ) throws Exception {
        try(FileInputStream configFile = new FileInputStream("logging.properties")) {
            LogManager.getLogManager().readConfiguration(configFile);
        };

        AdaptiveRag adaptiveRagTest = new AdaptiveRag( System.getenv("OPENAI_API_KEY"), System.getenv("TAVILY_API_KEY"));

        CompiledGraph<State> graph = adaptiveRagTest.buildGraph().compile();

        org.bsc.async.AsyncGenerator<org.bsc.langgraph4j.NodeOutput<State>> result = graph.stream( Map.of( "question", "What player at the Bears expected to draft first in the 2024 NFL draft?" ) );
        // var result = graph.stream( Map.of( "question", "What kind the agent memory do iu know?" ) );

        String generation = "";
        for( org.bsc.langgraph4j.NodeOutput<State> r : result ) {
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

