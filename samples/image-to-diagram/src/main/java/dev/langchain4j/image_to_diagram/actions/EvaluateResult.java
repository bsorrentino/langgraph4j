package dev.langchain4j.image_to_diagram.actions;

import dev.langchain4j.image_to_diagram.DiagramCorrectionProcess;
import dev.langchain4j.image_to_diagram.ImageToDiagram;
import dev.langchain4j.image_to_diagram.ImageToDiagramProcess;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.NodeOutput;
import org.bsc.langgraph4j.action.AsyncNodeAction;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class EvaluateResult implements AsyncNodeAction<ImageToDiagram.State> {

    public static AsyncNodeAction<ImageToDiagram.State> of() {
        return new EvaluateResult();
    }

    private EvaluateResult() {}

    @Override
    public CompletableFuture<Map<String, Object>> apply(ImageToDiagram.State state) {
        CompletableFuture<Map<String,Object>> result = new CompletableFuture<>();

        DiagramCorrectionProcess diagramCorrectionProcess = new DiagramCorrectionProcess();

        ArrayList<NodeOutput<ImageToDiagram.State>> list = new ArrayList<NodeOutput<ImageToDiagram.State>>();
        try {
            return diagramCorrectionProcess.execute( state.data() )
                    .collectAsync(list, v -> log.info( v.toString() ) )
                    .thenApply( v -> {
                        if( list.isEmpty() ) {
                            throw new RuntimeException("no results");
                        }
                        NodeOutput<ImageToDiagram.State> last = list.get( list.size() - 1 );
                        return last.state().data();
                    });
        } catch (Exception e) {
            result.completeExceptionally(e);
        }
        return result;

    }
}
