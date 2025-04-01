package dev.langchain4j.image_to_diagram.actions.correction;

import dev.langchain4j.image_to_diagram.ImageToDiagram;
import org.bsc.langgraph4j.action.EdgeAction;

/**
 * Represents the result of evaluating a route.
 */
public class RouteEvaluationResult implements EdgeAction<ImageToDiagram.State> {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RouteEvaluationResult.class);

    /**
     * Determines the evaluation result based on the current state of the image-to-diagram conversion process.
     *
     * @param state The current state of the image-to-diagram conversion, which must contain an evaluation result.
     * @return A string representing the name of the evaluation result.
     * @throws IllegalArgumentException if no evaluation result is provided in the state.
     */
    @Override
    public String apply(ImageToDiagram.State state) {
        ImageToDiagram.EvaluationResult evaluationResult = state.evaluationResult()
                .orElseThrow(() -> new IllegalArgumentException("no evaluationResult provided!"));

        if( evaluationResult == ImageToDiagram.EvaluationResult.ERROR ) {
            if( state.isExecutionError() ) {
                log.warn("evaluation execution error: [{}]", state.evaluationError().orElse("unknown") );
                return ImageToDiagram.EvaluationResult.UNKNOWN.name();
            }
            if (state.lastTwoDiagramsAreEqual()) {
                log.warn("correction failed! ");
                return ImageToDiagram.EvaluationResult.UNKNOWN.name();
            }
        }

        return evaluationResult.name();
    };

}