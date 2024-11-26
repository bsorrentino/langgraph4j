package dev.langchain4j.image_to_diagram.actions.correction;

import dev.langchain4j.image_to_diagram.ImageToDiagram;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.AsyncEdgeAction;
import org.bsc.langgraph4j.action.EdgeAction;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;

@Slf4j
public class RouteEvaluationResult implements EdgeAction<ImageToDiagram.State> {

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
