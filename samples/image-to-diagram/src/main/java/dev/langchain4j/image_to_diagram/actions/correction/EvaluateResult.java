package dev.langchain4j.image_to_diagram.actions.correction;

import dev.langchain4j.image_to_diagram.ImageToDiagram;
import org.bsc.langgraph4j.action.AsyncNodeAction;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.bsc.langgraph4j.utils.CollectionsUtils.last;

/**
 * Class declaration for EvaluateResult, which implements the AsyncNodeAction interface for ImageToDiagram.State.
 * This class is responsible for evaluating a diagram code and returning an evaluation result wrapped in a CompletableFuture.
 */
public class EvaluateResult implements AsyncNodeAction<ImageToDiagram.State> {

    /**
     * Default constructor for EvaluateResult.
     */
    public EvaluateResult() {}

    /**
     * Applies the async action to the given state, evaluating the diagram code.
     *
     * @param state The current state of ImageToDiagram, containing the diagram code to be evaluated.
     * @return A CompletableFuture that resolves with a map containing the evaluation result.
     */
    @Override
    public CompletableFuture<Map<String, Object>> apply(ImageToDiagram.State state) {
        String diagramCode = last( state.diagramCode() )
                .orElseThrow(() -> new IllegalArgumentException("no diagram code provided!"));

        return PlantUMLAction.validate( diagramCode )
                .thenApply( v -> Map.of( "evaluationResult", (Object) ImageToDiagram.EvaluationResult.OK ) )
                .exceptionally( e -> {
                    if( e.getCause() instanceof PlantUMLAction.Error ) {
                        return Map.of("evaluationResult", ImageToDiagram.EvaluationResult.ERROR,
                                "evaluationError",  e.getCause().getMessage(),
                                "evaluationErrorType", ((PlantUMLAction.Error)e.getCause()).getType());
                    }
                    throw new RuntimeException(e);
                });

    }
}
