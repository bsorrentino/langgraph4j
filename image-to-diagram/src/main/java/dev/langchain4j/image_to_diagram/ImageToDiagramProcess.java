package dev.langchain4j.image_to_diagram;

import dev.langchain4j.data.message.*;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.Getter;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.bsc.async.AsyncGenerator;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.NodeOutput;

import java.net.URI;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static java.util.Optional.ofNullable;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import static org.bsc.langgraph4j.utils.CollectionsUtils.mapOf;

@Slf4j( topic="ImageToDiagramProcess" )
public class ImageToDiagramProcess implements ImageToDiagram {

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


    private final ImageUrlOrData imageUrlOrData;

    public ImageToDiagramProcess(URI image ) {
        imageUrlOrData = ImageUrlOrData.of(image);
    }

    public ImageToDiagramProcess(String resourceName ) throws  Exception {
        var imageData = ImageLoader.loadImageAsBase64( resourceName );
        imageUrlOrData = ImageUrlOrData.of(imageData);
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

    private Map<String,Object> translateGenericDiagramDescriptionToPlantUML( State state) throws Exception {

        var diagram = state.diagram()
                .orElseThrow(() -> new IllegalArgumentException("no diagram provided!"));

        var systemPrompt = loadPromptTemplate( "convert_generic_diagram_to_plantuml.txt" )
                .apply( mapOf( "diagram_description", diagram));

        var response = getLLM().generate( new SystemMessage(systemPrompt.text()) );

        var result = response.content().text();

        return mapOf("diagramCode", result );
    }
    private Map<String,Object> translateSequenceDiagramDescriptionToPlantUML( State state) throws Exception {

        var diagram = state.diagram()
                .orElseThrow(() -> new IllegalArgumentException("no diagram provided!"));

        var systemPrompt = loadPromptTemplate( "convert_sequence_diagram_to_plantuml.txt" )
                .apply( mapOf( "diagram_description", diagram));

        var response = getLLM().generate( new SystemMessage(systemPrompt.text()) );

        var result = response.content().text();

        return mapOf("diagramCode", result );
    }


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
    private CompletableFuture<Map<String,Object>> evaluateResult( State state ) {

        CompletableFuture<Map<String,Object>> result = new CompletableFuture<>();

        var diagramCorrectionProcess = new DiagramCorrectionProcess();

        var list = new ArrayList<NodeOutput<State>>();
        try {
            return diagramCorrectionProcess.execute( state.data() )
                    .collectAsync(list, v -> log.info( v.toString() ) )
                    .thenApply( v -> {
                        if( list.isEmpty() ) {
                            throw new RuntimeException("no results");
                        }
                        var last = list.get( list.size() - 1 );
                        return last.state().data();
                    });
        } catch (Exception e) {
            result.completeExceptionally(e);
        }
        return result;
    }

    public AsyncGenerator<NodeOutput<State>> execute(  Map<String, Object> inputs ) throws Exception {

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

        var workflow = new StateGraph<>(State::new);

        workflow.addNode("agent_describer", node_async( state ->
                describeDiagramImage( llmVision, imageUrlOrData, state )) );
        workflow.addNode("agent_sequence_plantuml",
                node_async(this::translateSequenceDiagramDescriptionToPlantUML) );
        workflow.addNode("agent_generic_plantuml",
                node_async(this::translateGenericDiagramDescriptionToPlantUML) );
        workflow.addConditionalEdges(
                "agent_describer",
                edge_async(this::routeDiagramTranslation),
                mapOf( "sequence", "agent_sequence_plantuml",
                    "generic", "agent_generic_plantuml" )
        );

        workflow.addNode( "evaluate_result", this::evaluateResult);
        workflow.addEdge("agent_sequence_plantuml", "evaluate_result");
        workflow.addEdge("agent_generic_plantuml", "evaluate_result");
        workflow.setEntryPoint("agent_describer");
        workflow.setFinishPoint("evaluate_result");


        var app = workflow.compile();

        return app.stream( inputs );
    }
}
