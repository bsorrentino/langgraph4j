package dev.langchain4j.image_to_diagram;

import dev.langchain4j.image_to_diagram.actions.DescribeDiagramImage;
import dev.langchain4j.image_to_diagram.actions.EvaluateResult;
import dev.langchain4j.image_to_diagram.actions.TranslateGenericDiagramToPlantUML;
import dev.langchain4j.image_to_diagram.actions.TranslateSequenceDiagramToPlantUML;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.bsc.async.AsyncGenerator;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.NodeOutput;

import java.net.URI;
import java.time.Duration;
import java.util.*;

import static java.util.Optional.ofNullable;
import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.utils.CollectionsUtils.mapOf;

@Slf4j( topic="ImageToDiagramProcess" )
public class ImageToDiagramProcess implements ImageToDiagram {

    public record ImageUrlOrData(
        URI url,
        String data
    ) {
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

    private  String routeDiagramTranslation( State state)  {
        return state.diagram()
                .filter(d -> d.type.equalsIgnoreCase("sequence"))
                .map(d -> "sequence")
                .orElse("generic");
    };

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
            .addNode("agent_describer", DescribeDiagramImage.of(imageUrlOrData, llmVision )  )
            .addNode("agent_sequence_plantuml", TranslateSequenceDiagramToPlantUML.of(getLLM()) )
            .addNode("agent_generic_plantuml", TranslateGenericDiagramToPlantUML.of(getLLM()) )
            .addNode( "evaluate_result", EvaluateResult.of())
            .addConditionalEdges(
                    "agent_describer",
                    edge_async(this::routeDiagramTranslation),
                    mapOf( "sequence", "agent_sequence_plantuml",
                        "generic", "agent_generic_plantuml" )
            )
            .addEdge("agent_sequence_plantuml", "evaluate_result")
            .addEdge("agent_generic_plantuml", "evaluate_result")
            .addEdge( START,"agent_describer")
            .addEdge("evaluate_result", END)
            .compile();

        return app.stream( inputs );
    }
}
