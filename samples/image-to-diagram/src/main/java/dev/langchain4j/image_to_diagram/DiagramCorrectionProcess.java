package dev.langchain4j.image_to_diagram;

import dev.langchain4j.image_to_diagram.actions.correction.EvaluateResult;
import dev.langchain4j.image_to_diagram.actions.correction.ReviewResult;
import dev.langchain4j.image_to_diagram.actions.correction.RouteEvaluationResult;
import dev.langchain4j.image_to_diagram.serializer.gson.JSONStateSerializer;

import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.AsyncEdgeAction;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.serializer.StateSerializer;

import java.util.Map;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;

/**
 * Represents the process for correcting diagrams from images using asynchronous node and edge actions.
 */
public class DiagramCorrectionProcess implements ImageToDiagram {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger("DiagramCorrectionProcess");

    final AsyncNodeAction<State> evaluateResult = new EvaluateResult();
    final AsyncNodeAction<State> reviewResult = new ReviewResult(getModel());
    final AsyncEdgeAction<State> routeEvaluationResult = edge_async(new RouteEvaluationResult());

    /**
     * Executes the workflow for diagram correction using the default JSONStateSerializer.
     *
     * @return The resulting StateGraph
     * @throws Exception If an error occurs during the workflow execution
     */
    public StateGraph<State> workflow() throws Exception {
        return workflow(new JSONStateSerializer());
    }

    /**
     * Executes the workflow for diagram correction using a specified state serializer.
     *
     * @param stateSerializer The state serializer to use
     * @return The resulting StateGraph
     * @throws Exception If an error occurs during the workflow execution
     */
    public StateGraph<State> workflow(StateSerializer<State> stateSerializer) throws Exception {
        return new StateGraph<>(State.SCHEMA, stateSerializer)
                .addNode("evaluate_result", evaluateResult)
                .addNode("agent_review", reviewResult)
                .addEdge("agent_review", "evaluate_result")
                .addConditionalEdges("evaluate_result",
                        routeEvaluationResult,
                        Map.of(
                                "OK", END,
                                "ERROR", "agent_review",
                                "UNKNOWN", END
                        )
                )
                .addEdge(START, "evaluate_result");
    }

}
