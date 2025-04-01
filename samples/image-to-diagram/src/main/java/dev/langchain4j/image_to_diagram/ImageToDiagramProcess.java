package dev.langchain4j.image_to_diagram;

import dev.langchain4j.image_to_diagram.actions.*;
import dev.langchain4j.image_to_diagram.serializer.gson.JSONStateSerializer;

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

/**
 * Class for processing images to generate diagrams.
 */
public class ImageToDiagramProcess implements ImageToDiagram {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger("ImageToDiagramProcess");
    /**
     * Represents an image that can be identified by a URL or encapsulated as data.
     *
     * The {@code ImageUrlOrData} record is designed to hold either a URL pointing to an image
     * or the image data itself. It provides two static factory methods for creating instances:
     * one from a URL and another from the image data.
     * @param url  the URI representing the image URL
     * @param data the image data as a string
     */
    public record ImageUrlOrData(
        URI url,
        String data
    ) {
        /**
         * Creates an instance of {@code ImageUrlOrData} from a given URL.
         *
         * @param url the URI representing the image URL
         * @return a new instance of {@code ImageUrlOrData} with the specified URL and no data
         */
        public static ImageUrlOrData of( URI url) {
            Objects.requireNonNull(url);
            return new ImageUrlOrData(url, null);
        }
        /**
         * Creates a new instance of the {@code ImageUrlOrData} class with the provided data.
         *
         * @param data  image data as a string
         * @return      a new {@code ImageUrlOrData} instance containing the specified image data
         */
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
    // final AsyncNodeAction<State> evaluateResult = new EvaluateResult(getModel());
    final AsyncEdgeAction<State> routeDiagramTranslation = new RouteDiagramTranslation();


    /**
     * Generates a workflow graph using the StateGraph framework with specific nodes and conditional edges.
     *
     * @return A configured StateGraph instance representing the workflow.
     * @throws Exception if an error occurs during the configuration of the graph.
     */
    public StateGraph<State> workflow() throws Exception {
        var stateSerializer = new JSONStateSerializer();

        return new StateGraph<>(State.SCHEMA,stateSerializer)
                .addNode("agent_describer", describeDiagramImage  )
                .addNode("agent_sequence_plantuml", translateSequenceDiagramToPlantUML)
                .addNode("agent_generic_plantuml", translateGenericDiagramToPlantUML )
                //.addNode( "evaluate_result", evaluateResult )
                .addConditionalEdges("agent_describer",
                        routeDiagramTranslation,
                        Map.of( "sequence", "agent_sequence_plantuml",
                                "generic", "agent_generic_plantuml" )
                )
                //.addEdge("agent_sequence_plantuml", "evaluate_result")
                //.addEdge("agent_generic_plantuml", "evaluate_result")
                .addEdge("agent_sequence_plantuml", END)
                .addEdge("agent_generic_plantuml", END)
                .addEdge( START,"agent_describer")
                //.addEdge("evaluate_result", END)
                ;
    }

    /**
     * This method orchestrates a workflow for processing diagrams. It initializes a state graph and adds nodes and subgraphs
     * to represent different stages of diagram correction and translation. The method also sets up conditional edges based on
     * the type of diagram being translated.
     *
     * @return A StateGraph object representing the complete workflow diagram.
     * @throws Exception if any errors occur during the setup of the workflow.
     */
    public StateGraph<State> workflowWithCorrection() throws Exception {

        var stateSerializer = new JSONStateSerializer();

        var diagramCorrectionProcess = new DiagramCorrectionProcess().workflow(stateSerializer).compile();

        return new StateGraph<>(State.SCHEMA,stateSerializer)
                .addNode("agent_describer", describeDiagramImage  )
                .addNode("agent_sequence_plantuml", translateSequenceDiagramToPlantUML )
                .addNode("agent_generic_plantuml", translateGenericDiagramToPlantUML )
                .addNode( "evaluate_result", diagramCorrectionProcess )
                .addConditionalEdges("agent_describer",
                        routeDiagramTranslation,
                        Map.of( "sequence", "agent_sequence_plantuml",
                                "generic", "agent_generic_plantuml" )
                )
                .addEdge("agent_sequence_plantuml", "evaluate_result")
                .addEdge("agent_generic_plantuml", "evaluate_result")
                .addEdge( START,"agent_describer")
                .addEdge("evaluate_result", END);
    }

    /**
     * Executes the workflow using the provided image data and returns a stream of NodeOutput objects.
     *
     * @param imageData The image data to be processed, which must not be null.
     * @return A stream of NodeOutput objects generated by the workflow.
     * @throws Exception if an error occurs during execution.
     */
    public AsyncGenerator<NodeOutput<State>> execute( ImageUrlOrData imageData ) throws Exception {
        Objects.requireNonNull( imageData, "imageData cannot be null");
        return workflow().compile().stream( Map.of( "imageData", imageData ) );
    }

    /**
     * Executes the workflow with correction for the given image data.
     *
     * @param imageData The image data to process, cannot be null.
     * @return An asynchronous generator of node outputs.
     * @throws Exception If an error occurs during execution.
     */
    public AsyncGenerator<NodeOutput<State>> executeWithCorrection( ImageUrlOrData imageData ) throws Exception {
        Objects.requireNonNull( imageData, "imageData cannot be null");
        return workflowWithCorrection().compile().stream( Map.of( "imageData", imageData ) );
    }

}