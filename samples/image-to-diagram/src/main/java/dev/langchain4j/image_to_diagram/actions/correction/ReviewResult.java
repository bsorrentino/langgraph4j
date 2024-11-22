package dev.langchain4j.image_to_diagram.actions.correction;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.image_to_diagram.DiagramCorrectionProcess;
import dev.langchain4j.image_to_diagram.ImageToDiagram;
import dev.langchain4j.image_to_diagram.actions.TranslateGenericDiagramToPlantUML;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.AsyncNodeAction;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static dev.langchain4j.image_to_diagram.ImageToDiagram.loadPromptTemplate;
import static org.bsc.langgraph4j.utils.CollectionsUtils.last;
import static org.bsc.langgraph4j.utils.CollectionsUtils.mapOf;

@Slf4j
public class ReviewResult implements AsyncNodeAction<ImageToDiagram.State> {
    public static AsyncNodeAction<ImageToDiagram.State> of( @NonNull OpenAiChatModel model) {
        return new ReviewResult(model);
    }

    final OpenAiChatModel model;

    private ReviewResult( OpenAiChatModel model) {

        this.model = model;
    }


    @Override
    public CompletableFuture<Map<String, Object>> apply(ImageToDiagram.State state) {
        CompletableFuture<Map<String,Object>> future = new CompletableFuture<>();
        try {

            String diagramCode = last( state.diagramCode() )
                    .orElseThrow(() -> new IllegalArgumentException("no diagram code provided!"));

            String error = state.evaluationError()
                    .orElseThrow(() -> new IllegalArgumentException("no evaluation error provided!"));

            log.trace("evaluation error: {}", error);

            Prompt systemPrompt = loadPromptTemplate( "review_diagram.txt" )
                    .apply( Map.of( "evaluationError", error, "diagramCode", diagramCode));
            dev.langchain4j.model.output.Response<dev.langchain4j.data.message.AiMessage> response = model.generate( new SystemMessage(systemPrompt.text()) );

            String result = response.content().text();

            log.trace("review result: {}", result);

            future.complete(mapOf("diagramCode", result ) );

        } catch (Exception e) {
            future.completeExceptionally(e);
        }

        return future;

    }
}
