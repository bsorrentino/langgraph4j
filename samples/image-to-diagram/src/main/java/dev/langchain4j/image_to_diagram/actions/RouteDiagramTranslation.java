package dev.langchain4j.image_to_diagram.actions;

import dev.langchain4j.image_to_diagram.ImageToDiagram;
import org.bsc.langgraph4j.action.AsyncEdgeAction;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * RouteDiagramTranslation class implements AsyncEdgeAction interface for processing 
 * ImageToDiagram.State and determining the type of diagram.
 */
public class RouteDiagramTranslation implements AsyncEdgeAction<ImageToDiagram.State> {

    /**
     * Applies the transformation to convert an {@code ImageToDiagram.State}
     * to a diagram type. If the state's diagram type is "sequence" (case-insensitive),
     * it returns "sequence"; otherwise, it returns "generic".
     *
     * @param state The current state of the image-to-diagram conversion process.
     * @return A {@link CompletableFuture} that completes with the determined diagram type as a string.
     */
    @Override
    public CompletableFuture<String> apply(ImageToDiagram.State state) {

        var diagramType = state.diagram()
                .filter(d -> d.type().equalsIgnoreCase("sequence"))
                .map(d -> "sequence")
                .orElse("generic");
        return completedFuture(diagramType);

    }
}