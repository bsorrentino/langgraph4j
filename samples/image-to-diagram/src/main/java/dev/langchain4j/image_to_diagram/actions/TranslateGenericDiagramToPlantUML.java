package dev.langchain4j.image_to_diagram.actions;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.image_to_diagram.state.Diagram;
import dev.langchain4j.image_to_diagram.ImageToDiagram;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.NonNull;
import org.bsc.langgraph4j.action.NodeAction;

import java.util.Map;

/**
 * TranslateGenericDiagramToPlantUML class implementation.
 *
 * This class implements the NodeAction<ImageToDiagram.State> interface and is responsible for translating a generic diagram to PlantUML code.
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
    public TranslateGenericDiagramToPlantUML( @NonNull OpenAiChatModel model) {
        this.model = model;
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

        dev.langchain4j.model.input.Prompt systemPrompt = ImageToDiagram.loadPromptTemplate( "convert_generic_diagram_to_plantuml.txt" )
                .apply( Map.of( "diagram_description", diagram));

        dev.langchain4j.model.output.Response<AiMessage> response = model.generate( new SystemMessage(systemPrompt.text()) );

        String result = response.content().text();

        return Map.of("diagramCode", result );
    }
}
