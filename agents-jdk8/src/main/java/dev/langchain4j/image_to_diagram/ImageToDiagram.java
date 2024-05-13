package dev.langchain4j.image_to_diagram;

import dev.langchain4j.data.message.*;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.Getter;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.var;
import net.sourceforge.plantuml.ErrorUmlType;
import org.bsc.async.AsyncGenerator;
import org.bsc.langgraph4j.GraphState;
import org.bsc.langgraph4j.NodeOutput;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AppendableValue;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static java.util.Collections.unmodifiableMap;
import static java.util.Optional.ofNullable;
import static org.bsc.langgraph4j.GraphState.END;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import static org.bsc.langgraph4j.utils.CollectionsUtils.mapOf;

public class ImageToDiagram {

    @Value()
    @Accessors(fluent = true)
    public static class ImageUrlOrData {
        URI url;
        String data;

        public static ImageUrlOrData of( URI url) {
            Objects.requireNonNull(url);
            return new ImageUrlOrData(url, null);
        }
        public static ImageUrlOrData of( String data) {
            Objects.requireNonNull(data);
            return new ImageUrlOrData(null, data );
        }
    }
    public enum EvaluationResult {
        OK,
        ERROR,
        UNKNOWN
    }

    public static class State extends AgentState {

        public State(Map<String, Object> initData) {
            super(initData);
        }

        public Optional<Diagram.Element> diagram() {
            return value("diagram");
        }
        public AppendableValue<String> diagramCode() {
            return appendableValue("diagramCode");
        }


        public Optional<EvaluationResult> evaluationResult() {
            return value("evaluationResult" );
        }
        public Optional<String> evaluationError() {
            return value("evaluationError" );
        }
        public Optional<ErrorUmlType> evaluationErrorType() {
            return value("evaluationErrorType" );
        }
    }

    private final ImageUrlOrData imageUrlOrData;

    public ImageToDiagram( URI image ) {
        imageUrlOrData = ImageUrlOrData.of(image);
    }

    public ImageToDiagram( String resourceName ) throws  Exception {
        var imageData = ImageLoader.loadImageAsBase64( resourceName );
        imageUrlOrData = ImageUrlOrData.of(imageData);
    }


    private PromptTemplate loadPromptTemplate(String resourceName ) throws Exception {
        final ClassLoader classLoader = getClass().getClassLoader();
        final InputStream inputStream = classLoader.getResourceAsStream(resourceName);
        if (inputStream == null) {
            throw new IllegalArgumentException("File not found: " + resourceName);
        }
        try( final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream), 4*1024) ) {
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append('\n');
            }
            return PromptTemplate.from( result.toString() );
        }
    }

    private  String routeDiagramTranslation( State state)  {
        return state.diagram()
                .filter(d -> d.type.equalsIgnoreCase("sequence"))
                .map(d -> "sequence")
                .orElse("generic");
    };

    private Map<String,Object> describeDiagramImage(ChatLanguageModel visionModel, ImageUrlOrData imageUrlOrData, State state) throws Exception {

        var systemPrompt = loadPromptTemplate( "describe_diagram_image.txt" )
                                .apply( mapOf() );

        var imageContent = (imageUrlOrData.url()!=null) ?
                ImageContent.from(imageUrlOrData.url(), ImageContent.DetailLevel.AUTO) :
                ImageContent.from(imageUrlOrData.data(), "image/png", ImageContent.DetailLevel.AUTO);
        var textContent = new TextContent(systemPrompt.text());
        var message = UserMessage.from(textContent, imageContent);

        var response = visionModel.generate( message );

        var outputParser = new DiagramOutputParser();

        var result = outputParser.parse( response.content().text() );

        return mapOf( "diagram",result );
    }

    private Map<String,Object> translateGenericDiagramDescriptionToPlantUML(ChatLanguageModel chatLanguageModel, State state) throws Exception {

        var diagram = state.diagram()
                .orElseThrow(() -> new IllegalArgumentException("no diagram provided!"));

        var systemPrompt = loadPromptTemplate( "convert_generic_diagram_to_plantuml.txt" )
                .apply( mapOf( "diagram_description", diagram));

        var response = chatLanguageModel.generate( new SystemMessage(systemPrompt.text()) );

        var result = response.content().text();

        return mapOf("diagramCode", result );
    }
    private Map<String,Object> translateSequenceDiagramDescriptionToPlantUML(ChatLanguageModel chatLanguageModel, State state) throws Exception {

        var diagram = state.diagram()
                .orElseThrow(() -> new IllegalArgumentException("no diagram provided!"));

        var systemPrompt = loadPromptTemplate( "convert_sequence_diagram_to_plantuml.txt" )
                .apply( mapOf( "diagram_description", diagram));

        var response = chatLanguageModel.generate( new SystemMessage(systemPrompt.text()) );

        var result = response.content().text();

        return mapOf("diagramCode", result );
    }

    CompletableFuture<Map<String,Object>> reviewResult(ChatLanguageModel chatLanguageModel, State state)  {
        CompletableFuture<Map<String,Object>> future = new CompletableFuture<>();
        try {

            var diagramCode = state.diagramCode().last()
                    .orElseThrow(() -> new IllegalArgumentException("no diagram code provided!"));

            var error = state.evaluationError()
                    .orElseThrow(() -> new IllegalArgumentException("no evaluation error provided!"));

            Prompt systemPrompt = loadPromptTemplate( "review_diagram.txt" )
                        .apply( mapOf( "evaluationError", error, "diagramCode", diagramCode));
            var response = chatLanguageModel.generate( new SystemMessage(systemPrompt.text()) );

            var result = response.content().text();

            future.complete(mapOf("diagramCode", result ) );

        } catch (Exception e) {
            future.completeExceptionally(e);
        }

        return future;
    }

    private CompletableFuture<Map<String,Object>> evaluateResult(State state) {

        var diagramCode = state.diagramCode().last()
                .orElseThrow(() -> new IllegalArgumentException("no diagram code provided!"));

        return PlantUMLAction.validate( diagramCode )
                .thenApply( v -> mapOf( "evaluationResult", (Object)EvaluationResult.OK.name() ) )
                .exceptionally( e -> {
                    if( e instanceof PlantUMLAction.Error ) {
                        return mapOf("evaluationResult", EvaluationResult.ERROR.name(),
                            "evaluationError",  e.getCause().getMessage(),
                            "evaluationErrorType", ((PlantUMLAction.Error)e).getType());
                    }
                    throw new RuntimeException(e);
                });

    }

    private  String routeEvaluationResult( State state )  {
        if( state.evaluationErrorType().map( type -> type == ErrorUmlType.EXECUTION_ERROR ).orElse(false) ) {
            return EvaluationResult.UNKNOWN.name();
        }
        return state.evaluationResult()
                .map(EvaluationResult::name)
                .orElse(EvaluationResult.UNKNOWN.name());
    };

    @Getter(lazy = true)
    private final OpenAiChatModel LLM = newLLM();

    private OpenAiChatModel newLLM( ) {
        var openApiKey = ofNullable( System.getProperty("OPENAI_API_KEY") )
                .orElseThrow( () -> new IllegalArgumentException("no OPENAI_API_KEY provided!") );

        return OpenAiChatModel.builder()
                .apiKey( openApiKey )
                .modelName( "gpt-3.5-turbo" )
                .logRequests(true)
                .logResponses(true)
                .maxRetries(2)
                .temperature(0.0)
                .maxTokens(2000)
                .build();

    }
    public AsyncGenerator<NodeOutput<State>> execute(  Map<String, Object> inputs ) throws Exception {

        var llm = getLLM();

        var openApiKey = ofNullable( System.getProperty("OPENAI_API_KEY") )
                .orElseThrow( () -> new IllegalArgumentException("no OPENAI_API_KEY provided!") );

        var llmVision = OpenAiChatModel.builder()
                .apiKey( openApiKey )
                .modelName( "gpt-4-vision-preview" )
                .timeout(Duration.ofMinutes(2))
                .logRequests(true)
                .logResponses(true)
                .maxRetries(2)
                .temperature(0.0)
                .maxTokens(2000)
                .build();

        var workflow = new GraphState<>(State::new);

        workflow.addNode("agent_describer", node_async( state ->
                describeDiagramImage( llmVision, imageUrlOrData, state )) );
        workflow.addNode("agent_sequence_plantuml", node_async( state ->
                translateSequenceDiagramDescriptionToPlantUML( llm, state )) );
        workflow.addNode("agent_generic_plantuml", node_async( state ->
                translateGenericDiagramDescriptionToPlantUML( llm, state )) );
        workflow.addNode( "agent_review", this::evaluateResult);
        workflow.addNode( "evaluate_result", this::evaluateResult);
        workflow.addEdge("agent_sequence_plantuml", "evaluate_result");
        workflow.addConditionalEdges(
                "agent_describer",
                edge_async(this::routeDiagramTranslation),
                mapOf( "sequence", "agent_sequence_plantuml",
                    "generic", "agent_generic_plantuml" )
        );
        workflow.addConditionalEdges(
                "evaluate_result",
                edge_async(this::routeEvaluationResult),
                mapOf(  "OK", END,
                        "ERROR", "agent_review",
                        "UNKNOWN", END )
        );
        workflow.addEdge( "agent_review", "evaluate_result" );
        workflow.setEntryPoint("agent_describer");

        var app = workflow.compile();

        return app.stream( inputs );
    }
}
