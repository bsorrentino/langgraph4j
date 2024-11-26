package dev.langchain4j.image_to_diagram;

import dev.langchain4j.image_to_diagram.actions.*;
import dev.langchain4j.image_to_diagram.serializer.gson.JSONStateSerializer;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.bsc.async.AsyncGenerator;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.NodeOutput;
import org.bsc.langgraph4j.action.AsyncEdgeAction;
import org.bsc.langgraph4j.action.AsyncNodeAction;

import java.net.URI;
import java.util.*;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
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

    final AsyncNodeAction<State> describeDiagramImage =
            node_async( new DescribeDiagramImage(getVisionModel() ) );
    final AsyncNodeAction<State> translateSequenceDiagramToPlantUML =
            node_async( new TranslateSequenceDiagramToPlantUML(getModel()) );
    final AsyncNodeAction<State> translateGenericDiagramToPlantUML =
            node_async( new TranslateGenericDiagramToPlantUML(getModel()) );
    final AsyncNodeAction<State> evaluateResult = new EvaluateResult(getModel());
    final AsyncEdgeAction<State> routeDiagramTranslation = new RouteDiagramTranslation();

//    public ImageToDiagramProcess(URI image) {
//        this(ImageUrlOrData.of(image));
//    }
//
//    public ImageToDiagramProcess(String resourceName  ) throws  Exception {
//        this( ImageUrlOrData.of(ImageLoader.loadImageAsBase64( resourceName ) ) );
//    }

    public AsyncGenerator<NodeOutput<State>> execute( @NonNull ImageUrlOrData imageData ) throws Exception {

        var stateSerializer = new JSONStateSerializer();

        var app = new StateGraph<>(State.SCHEMA,stateSerializer)
            .addNode("agent_describer", describeDiagramImage  )
            .addNode("agent_sequence_plantuml", translateSequenceDiagramToPlantUML)
            .addNode("agent_generic_plantuml", translateGenericDiagramToPlantUML )
            .addNode( "evaluate_result", evaluateResult )
            .addConditionalEdges("agent_describer",
                    routeDiagramTranslation,
                    mapOf( "sequence", "agent_sequence_plantuml",
                        "generic", "agent_generic_plantuml" )
            )
            .addEdge("agent_sequence_plantuml", "evaluate_result")
            .addEdge("agent_generic_plantuml", "evaluate_result")
            .addEdge( START,"agent_describer")
            .addEdge("evaluate_result", END)
            .compile();

        return app.stream( Map.of( "imageData", imageData ) );
    }

    public AsyncGenerator<NodeOutput<State>> executeWithCorrection( @NonNull ImageUrlOrData imageData ) throws Exception {

        var stateSerializer = new JSONStateSerializer();

        var diagramCorrectionProcess = new DiagramCorrectionProcess().workflow(stateSerializer).compile();

        var app = new StateGraph<>(State.SCHEMA,stateSerializer)
                .addNode("agent_describer", describeDiagramImage  )
                .addNode("agent_sequence_plantuml", translateSequenceDiagramToPlantUML )
                .addNode("agent_generic_plantuml", translateGenericDiagramToPlantUML )
                .addSubgraph( "agent_diagram_correction", diagramCorrectionProcess )
                .addNode( "evaluate_result", evaluateResult )
                .addConditionalEdges("agent_describer",
                        routeDiagramTranslation,
                        mapOf( "sequence", "agent_sequence_plantuml",
                                "generic", "agent_generic_plantuml" )
                )
                .addEdge("agent_sequence_plantuml", "evaluate_result")
                .addEdge("agent_generic_plantuml", "evaluate_result")
                .addEdge( START,"agent_describer")
                .addEdge("evaluate_result", END)
                .compile();

        return app.stream( Map.of( "imageData", imageData ) );
    }

}
