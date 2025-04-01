package dev.langchain4j.image_to_diagram.actions;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.image_to_diagram.state.Diagram;
import dev.langchain4j.image_to_diagram.ImageToDiagram;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.bsc.langgraph4j.action.NodeAction;

import java.util.Map;
import java.util.Objects;

/**
 * TranslateGenericDiagramToPlantUML class implementation.
 *
 * This class implements the {@literal NodeAction<ImageToDiagram.State> } interface and is responsible for translating a generic diagram to PlantUML code.
 */
public class TranslateGenericDiagramToPlantUML implements NodeAction<ImageToDiagram.State>  {

    /**
     * Final OpenAiChatModel instance, represents the model used for natural language processing.
     */
    final OpenAiChatModel model;

    /**
     * Constructor to initialize the TranslateGenericDiagramToPlantUML object with a provided OpenAiChatModel instance.
     *
     * @param model The non-null OpenAiChatModel instance.
     */
    public TranslateGenericDiagramToPlantUML( OpenAiChatModel model) {
        this.model = Objects.requireNonNull(model, "model cannot be null");
    }

    /**
     * Applies the translation logic to the given ImageToDiagram.State object.
     *
     * @param state The current state of the image diagram conversion process.
     * @return A map containing the translated PlantUML code as key "diagramCode".
     * @throws Exception If an error occurs during the translation process, e.g., if no diagram is provided.
     */
    @Override
    public Map<String, Object> apply(ImageToDiagram.State state) throws Exception {
        Diagram.Element diagram = state.diagram()
                .orElseThrow(() -> new IllegalArgumentException("no diagram provided!"));

        var systemPrompt = ImageToDiagram.loadPromptTemplate( "convert_generic_diagram_to_plantuml.txt" )
                .apply( Map.of( "diagram_description", diagram));

        var request = ChatRequest.builder()
                .messages( SystemMessage.from(systemPrompt.text()) )
                .build();
       var response = model.chat(request );

        String result = response.aiMessage().text();

        return Map.of("diagramCode", result );
    }
}
