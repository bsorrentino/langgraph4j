package dev.langchain4j.image_to_diagram.actions.correction;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.image_to_diagram.ImageToDiagram;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.bsc.langgraph4j.action.AsyncNodeAction;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static dev.langchain4j.image_to_diagram.ImageToDiagram.loadPromptTemplate;
import static org.bsc.langgraph4j.utils.CollectionsUtils.last;

/**
 * The ReviewResult class implements the AsyncNodeAction interface for processing ImageToDiagram.State objects.
 * It handles the review of diagram code and error messages using a language model from OpenAiChatModel.
 */
public class ReviewResult implements AsyncNodeAction<ImageToDiagram.State> {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ReviewResult.class);

    /**
     * The final instance variable holding the OpenAiChatModel. It is initialized via the constructor.
     */
    final OpenAiChatModel model;

    /**
     * Constructor for ReviewResult initializing the model.
     *
     * @param model The OpenAiChatModel instance to use for processing.
     */
    public ReviewResult(OpenAiChatModel model) {
        this.model = model;
    }

    /**
     * Asynchronously applies the logic to a given ImageToDiagram.State object.
     *
     * @param state The ImageToDiagram.State containing diagram code and evaluation errors.
     * @return A CompletableFuture containing the review result or an exception if processing fails.
     */
    @Override
    public CompletableFuture<Map<String, Object>> apply(ImageToDiagram.State state) {
        CompletableFuture<Map<String, Object>> future = new CompletableFuture<>();
        try {

            /*
              Retrieves the diagram code from the state or throws an IllegalArgumentException if not available.
             */
            String diagramCode = last(state.diagramCode())
                    .orElseThrow(() -> new IllegalArgumentException("no diagram code provided!"));

            /*
              Retrieves the evaluation error from the state or throws an IllegalArgumentException if not available.
             */
            String error = state.evaluationError()
                    .orElseThrow(() -> new IllegalArgumentException("no evaluation error provided!"));

            log.trace("evaluation error: {}", error);

            /*
              Loads a prompt template and applies it with the given values for diagram code and error message.
             */
            Prompt systemPrompt = loadPromptTemplate("review_diagram.txt")
                    .apply(Map.of("evaluationError", error, "diagramCode", diagramCode));

            /*
              Uses the language model to generate a response based on the system prompt.
             */
            var request = ChatRequest.builder()
                    .messages( SystemMessage.from( systemPrompt.text() ))
                    .build();
            var response = model.chat(request);

            String result = response.aiMessage().text();

            log.trace("review result: {}", result);

            /*
              Completes the future with the review result as a Map.
             */
            future.complete(Map.of("diagramCode", result));

        } catch (Exception e) {
            /*
              If any exception occurs, completes the future exceptionally.
             */
            future.completeExceptionally(e);
        }

        return future;

    }
}
