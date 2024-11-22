package dev.langchain4j.image_to_diagram.actions;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.image_to_diagram.Diagram;
import dev.langchain4j.image_to_diagram.DiagramOutputParser;
import dev.langchain4j.image_to_diagram.ImageToDiagram;
import dev.langchain4j.image_to_diagram.ImageToDiagramProcess;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.NonNull;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.action.NodeAction;

import java.util.Map;

import static dev.langchain4j.image_to_diagram.ImageToDiagram.loadPromptTemplate;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

import dev.langchain4j.image_to_diagram.ImageToDiagramProcess.ImageUrlOrData;

public class DescribeDiagramImage implements NodeAction<ImageToDiagram.State> {
    final ImageUrlOrData imageUrlOrData;
    final ChatLanguageModel visionModel;

    public static AsyncNodeAction<ImageToDiagram.State> of( @NonNull ImageToDiagramProcess.ImageUrlOrData imageUrlOrData,
                                                            @NonNull ChatLanguageModel visionModel) {
        return node_async(new DescribeDiagramImage(imageUrlOrData, visionModel));
    }

    private DescribeDiagramImage(ImageToDiagramProcess.ImageUrlOrData imageUrlOrData,
                                 ChatLanguageModel visionModel) {
        this.visionModel = visionModel;
        this.imageUrlOrData = imageUrlOrData;
    }

    @Override
    public Map<String, Object> apply(ImageToDiagram.State state) throws Exception {
        dev.langchain4j.model.input.Prompt systemPrompt = loadPromptTemplate( "describe_diagram_image.txt" )
                .apply( Map.of() );

        ImageContent imageContent = (imageUrlOrData.url()!=null) ?
                ImageContent.from(imageUrlOrData.url(), ImageContent.DetailLevel.AUTO) :
                ImageContent.from(imageUrlOrData.data(), "image/png", ImageContent.DetailLevel.AUTO);
        TextContent textContent = new TextContent(systemPrompt.text());
        UserMessage message = UserMessage.from(textContent, imageContent);

        dev.langchain4j.model.output.Response<AiMessage> response = visionModel.generate( message );

        DiagramOutputParser outputParser = new DiagramOutputParser();

        Diagram.Element result = outputParser.parse( response.content().text() );

        return Map.of( "diagram",result );

    }
}
