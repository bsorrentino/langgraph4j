package dev.langchain4j.image_to_diagram;

import dev.langchain4j.data.message.*;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.Getter;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import org.bsc.async.AsyncGenerator;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.NodeOutput;

import java.net.URI;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static java.util.Optional.ofNullable;
import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
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
        String imageData = ImageLoader.loadImageAsBase64( resourceName );
        imageUrlOrData = ImageUrlOrData.of(imageData);
    }



    private  String routeDiagramTranslation( State state)  {
        return state.diagram()
                .filter(d -> d.type.equalsIgnoreCase("sequence"))
                .map(d -> "sequence")
                .orElse("generic");
    };

    private Map<String,Object> describeDiagramImage(ChatLanguageModel visionModel, ImageUrlOrData imageUrlOrData, State state) throws Exception {

        dev.langchain4j.model.input.Prompt systemPrompt = loadPromptTemplate( "describe_diagram_image.txt" )
                                .apply( mapOf() );

        ImageContent imageContent = (imageUrlOrData.url()!=null) ?
                ImageContent.from(imageUrlOrData.url(), ImageContent.DetailLevel.AUTO) :
                ImageContent.from(imageUrlOrData.data(), "image/png", ImageContent.DetailLevel.AUTO);
        TextContent textContent = new TextContent(systemPrompt.text());
        UserMessage message = UserMessage.from(textContent, imageContent);

        dev.langchain4j.model.output.Response<AiMessage> response = visionModel.generate( message );

        DiagramOutputParser outputParser = new DiagramOutputParser();

        Diagram.Element result = outputParser.parse( response.content().text() );

        return mapOf( "diagram",result );
    }

    private Map<String,Object> translateGenericDiagramDescriptionToPlantUML( State state) throws Exception {

        Diagram.Element diagram = state.diagram()
                .orElseThrow(() -> new IllegalArgumentException("no diagram provided!"));

        dev.langchain4j.model.input.Prompt systemPrompt = loadPromptTemplate( "convert_generic_diagram_to_plantuml.txt" )
                .apply( mapOf( "diagram_description", diagram));

        dev.langchain4j.model.output.Response<AiMessage> response = getLLM().generate( new SystemMessage(systemPrompt.text()) );

        String result = response.content().text();

        return mapOf("diagramCode", result );
    }
    private Map<String,Object> translateSequenceDiagramDescriptionToPlantUML( State state) throws Exception {

        Diagram.Element diagram = state.diagram()
                .orElseThrow(() -> new IllegalArgumentException("no diagram provided!"));

        dev.langchain4j.model.input.Prompt systemPrompt = loadPromptTemplate( "convert_sequence_diagram_to_plantuml.txt" )
                .apply( mapOf( "diagram_description", diagram));

        dev.langchain4j.model.output.Response<AiMessage> response = getLLM().generate( new SystemMessage(systemPrompt.text()) );

        String result = response.content().text();

        return mapOf("diagramCode", result );
    }


    @Getter(lazy = true)
    private final OpenAiChatModel LLM = newLLM();

    private OpenAiChatModel newLLM( ) {
        String openApiKey = ofNullable( System.getProperty("OPENAI_API_KEY") )
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

        DiagramCorrectionProcess diagramCorrectionProcess = new DiagramCorrectionProcess();

        ArrayList<NodeOutput<State>> list = new ArrayList<NodeOutput<State>>();
        try {
            return diagramCorrectionProcess.execute( state.data() )
                    .collectAsync(list, v -> log.info( v.toString() ) )
                    .thenApply( v -> {
                        if( list.isEmpty() ) {
                            throw new RuntimeException("no results");
                        }
                        NodeOutput<State> last = list.get( list.size() - 1 );
                        return last.state().data();
                    });
        } catch (Exception e) {
            result.completeExceptionally(e);
        }
        return result;
    }

    public AsyncGenerator<NodeOutput<State>> execute(  Map<String, Object> inputs ) throws Exception {

        String openApiKey = ofNullable( System.getProperty("OPENAI_API_KEY") )
                .orElseThrow( () -> new IllegalArgumentException("no OPENAI_API_KEY provided!") );

        OpenAiChatModel llmVision = OpenAiChatModel.builder()
                .apiKey( openApiKey )
                .modelName( "gpt-4-vision-preview" )
                .timeout(Duration.ofMinutes(2))
                .logRequests(true)
                .logResponses(true)
                .maxRetries(2)
                .temperature(0.0)
                .maxTokens(2000)
                .build();

        org.bsc.langgraph4j.CompiledGraph<State> app = new StateGraph<>(State::new)
            .addNode("agent_describer", node_async( state ->
                    describeDiagramImage( llmVision, imageUrlOrData, state )) )
            .addNode("agent_sequence_plantuml",
                    node_async(this::translateSequenceDiagramDescriptionToPlantUML) )
            .addNode("agent_generic_plantuml",
                    node_async(this::translateGenericDiagramDescriptionToPlantUML) )
            .addConditionalEdges(
                    "agent_describer",
                    edge_async(this::routeDiagramTranslation),
                    mapOf( "sequence", "agent_sequence_plantuml",
                        "generic", "agent_generic_plantuml" )
            )
            .addNode( "evaluate_result", this::evaluateResult)
            .addEdge("agent_sequence_plantuml", "evaluate_result")
            .addEdge("agent_generic_plantuml", "evaluate_result")
            .addEdge( START,"agent_describer")
            .addEdge("evaluate_result", END)
            .compile();

        return app.stream( inputs );
    }
}
