package dev.langchain4j.image_to_diagram.actions;

import dev.langchain4j.image_to_diagram.DiagramCorrectionProcess;
import dev.langchain4j.image_to_diagram.ImageToDiagram;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.bsc.langgraph4j.NodeOutput;
import org.bsc.langgraph4j.action.AsyncNodeAction;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * The EvaluateResult class is designed to evaluate and process results asynchronously,
 * specifically targeting the conversion of image data to diagram data using an AI chat model.
 * It implements the AsyncNodeAction interface, which allows for asynchronous processing
 * of a State object into a Map. The class leverages the OpenAiChatModel for its operations.
 */
public class EvaluateResult implements AsyncNodeAction<ImageToDiagram.State> {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(EvaluateResult.class);

    /** Final field for storing an instance of OpenAiChatModel. */
    final OpenAiChatModel model;

    /**
     * Constructor for initializing the EvaluateResult with an OpenAiChatModel.
     *
     * @param model The instance of OpenAiChatModel to be used for processing.
     */
    public EvaluateResult(OpenAiChatModel model) {
        this.model = model;
    }

    /**
     * Converts an ImageToDiagram state to a diagram using the {@link DiagramCorrectionProcess}.
     * This method processes the input state asynchronously and returns a {@link CompletableFuture} with
     * the resulting diagram data. If no results are generated, it throws a {@link RuntimeException}.
     *
     * @param state The input state for the diagram correction process.
     * @return A {@link CompletableFuture} that will contain the diagram data upon successful completion,
     *         or be completed exceptionally if an error occurs during the processing.
     */
    @Override
    public CompletableFuture<Map<String, Object>> apply(ImageToDiagram.State state) {
        CompletableFuture<Map<String, Object>> result = new CompletableFuture<>();

        DiagramCorrectionProcess diagramCorrectionProcess = new DiagramCorrectionProcess();

        ArrayList<NodeOutput<ImageToDiagram.State>> list = new ArrayList<NodeOutput<ImageToDiagram.State>>();
        try {
            return diagramCorrectionProcess.workflow().compile().stream(state.data())
                    .collectAsync(list, (l,v) -> log.info(v.toString()))
                    .thenApply(v -> {
                        if (list.isEmpty()) {
                            throw new RuntimeException("no results");
                        }
                        NodeOutput<ImageToDiagram.State> last = list.get(list.size() - 1);
                        return last.state().data();
                    });
        } catch (Exception e) {
            result.completeExceptionally(e);
        }
        return result;
    }
}