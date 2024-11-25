package dev.langchain4j.image_to_diagram;

import dev.langchain4j.image_to_diagram.actions.DescribeDiagramImage;
import dev.langchain4j.image_to_diagram.actions.EvaluateResult;
import dev.langchain4j.image_to_diagram.actions.TranslateGenericDiagramToPlantUML;
import dev.langchain4j.image_to_diagram.actions.TranslateSequenceDiagramToPlantUML;
import dev.langchain4j.image_to_diagram.serializer.gson.JSONStateSerializer;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.extern.slf4j.Slf4j;

import org.bsc.async.AsyncGenerator;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.NodeOutput;
import org.bsc.langgraph4j.subgraph.*;

import java.net.URI;
import java.util.*;

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

    public ImageToDiagramProcess(URI image) {
        imageUrlOrData = ImageUrlOrData.of(image);
    }

    public ImageToDiagramProcess(String resourceName  ) throws  Exception {
        String imageData = ImageLoader.loadImageAsBase64( resourceName );
        imageUrlOrData = ImageUrlOrData.of(imageData);
    }

    private  String routeDiagramTranslation( State state)  {
        return state.diagram()
                .filter(d -> d.type().equalsIgnoreCase("sequence"))
                .map(d -> "sequence")
                .orElse("generic");
    };

    public AsyncGenerator<NodeOutput<State>> execute( Map<String, Object> inputs ) throws Exception {

        var stateSerializer = new JSONStateSerializer();

        var app = new StateGraph<>(State.SCHEMA,stateSerializer)
            .addNode("agent_describer", DescribeDiagramImage.of(imageUrlOrData, getVisionModel() )  )
            .addNode("agent_sequence_plantuml", TranslateSequenceDiagramToPlantUML.of(getModel()) )
            .addNode("agent_generic_plantuml", TranslateGenericDiagramToPlantUML.of(getModel()) )
            .addNode( "evaluate_result", EvaluateResult.of(getModel()))
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

    public AsyncGenerator<NodeOutput<State>> executeWithCorrection(  Map<String, Object> inputs ) throws Exception {

        var stateSerializer = new JSONStateSerializer();

        var diagramCorrectionProcess = new DiagramCorrectionProcess().workflow(stateSerializer).compile();

        var app = new StateGraph<>(State.SCHEMA,stateSerializer)
                .addNode("agent_describer", DescribeDiagramImage.of(imageUrlOrData, getVisionModel() )  )
                .addNode("agent_sequence_plantuml", TranslateSequenceDiagramToPlantUML.of(getModel()) )
                .addNode("agent_generic_plantuml", TranslateGenericDiagramToPlantUML.of(getModel()) )
                .addNode( "agent_diagram_correction", SubgraphNodeAction.of(diagramCorrectionProcess) )
                .addNode( "evaluate_result", EvaluateResult.of(getModel()))
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
