package dev.langchain4j.image_to_diagram.actions.correction;

import dev.langchain4j.image_to_diagram.ImageToDiagram;
import org.bsc.langgraph4j.action.AsyncNodeAction;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.bsc.langgraph4j.utils.CollectionsUtils.last;

public class EvaluateResult implements AsyncNodeAction<ImageToDiagram.State> {

    public static AsyncNodeAction<ImageToDiagram.State> of() {
        return new EvaluateResult();
    }

    private EvaluateResult() {}

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
