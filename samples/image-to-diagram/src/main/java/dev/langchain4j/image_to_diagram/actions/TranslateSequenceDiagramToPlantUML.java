package dev.langchain4j.image_to_diagram.actions;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.image_to_diagram.state.Diagram;
import dev.langchain4j.image_to_diagram.ImageToDiagram;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.bsc.langgraph4j.action.NodeAction;

import java.util.Map;

/**
 * The `TranslateSequenceDiagramToPlantUML` class implements the {@literal NodeAction<ImageToDiagram.State>} interface.
 * It is responsible for translating a sequence diagram into PlantUML code using an OpenAI chat model.
 */
public class TranslateSequenceDiagramToPlantUML implements NodeAction<ImageToDiagram.State> {

    /**
     * The `model` field represents the OpenAiChatModel used for generating PlantUML code from sequence diagrams.
     */
    final OpenAiChatModel model;

    /**
     * Constructor for `TranslateSequenceDiagramToPlantUML`.
     *
     * @param model the OpenAiChatModel to use for translation
     */
    public TranslateSequenceDiagramToPlantUML(OpenAiChatModel model) {
        this.model = model;
    }

    /**
     * Converts a sequence diagram from an image to PlantUML code.
     *
     * This method processes the given state containing an image, generates a system prompt,
     * and uses a language model to produce PlantUML code representing the diagram.
     * If no diagram is provided in the state, it throws an IllegalArgumentException.
     *
     * @param state The input state containing an image for conversion.
     * @return A map with a key "diagramCode" containing the generated PlantUML code.
     * @throws Exception if there is an error during the conversion process.
     */
    @Override
    public Map<String, Object> apply(ImageToDiagram.State state) throws Exception {
        Diagram.Element diagram = state.diagram()
                .orElseThrow(() -> new IllegalArgumentException("no diagram provided!"));

        var systemPrompt = ImageToDiagram.loadPromptTemplate( "convert_sequence_diagram_to_plantuml.txt" )
                .apply( Map.of( "diagram_description", diagram));

        var request = ChatRequest.builder()
                .messages( SystemMessage.from(systemPrompt.text()) )
                .build();
        var response = model.chat(request );

        var result = response.aiMessage().text();

        return Map.of("diagramCode", result );
    }
}