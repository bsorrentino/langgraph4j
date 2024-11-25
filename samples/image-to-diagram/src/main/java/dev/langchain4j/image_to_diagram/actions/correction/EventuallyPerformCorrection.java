package dev.langchain4j.image_to_diagram.actions.correction;

import dev.langchain4j.image_to_diagram.ImageToDiagram;
import org.bsc.langgraph4j.RunnableConfig;
import org.bsc.langgraph4j.action.AsyncNodeActionWithConfig;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class EventuallyPerformCorrection implements AsyncNodeActionWithConfig<ImageToDiagram.State> {

    @Override
    public CompletableFuture<Map<String, Object>> apply(ImageToDiagram.State t, RunnableConfig config) {
        return null;
    }
}
