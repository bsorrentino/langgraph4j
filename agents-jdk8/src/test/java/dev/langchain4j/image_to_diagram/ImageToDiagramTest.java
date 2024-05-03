package dev.langchain4j.image_to_diagram;

import com.google.gson.Gson;
import dev.langchain4j.DotEnvConfig;
import dev.langchain4j.data.message.*;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.time.Duration;

import static java.util.Optional.ofNullable;
import static org.bsc.langgraph4j.utils.CollectionsUtils.mapOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ImageToDiagramTest {


    private String readTextResource( String resourceName ) throws Exception {
        final ClassLoader classLoader = getClass().getClassLoader();
        final InputStream inputStream = classLoader.getResourceAsStream(resourceName);
        if (inputStream == null) {
            throw new IllegalArgumentException("File not found: " + resourceName);
        }
        try( final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream)) ) {
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append('\n');
            }
            return result.toString();
        }
    }

    @BeforeEach
    public void init() {
        DotEnvConfig.load();
    }
    @Test
    public void parseDiagramSchema() throws Exception {
        String json = readTextResource("diagram_data.json");
        assertNotNull(json);

        Gson gson = new Gson();
        Diagram.Element diagram = gson.fromJson(json, Diagram.Element.class);

        assertNotNull(diagram);

        System.out.println( diagram );

    }

    @Test
    public void parseDescription() throws Exception {
        String result = readTextResource("describe_result.txt");

        var outputParser = new DiagramOutputParser();
        System.out.println( outputParser.parse( result ) );
    }

    @Test
    public void describeDiagramImage() throws Exception {
        var openApiKey = DotEnvConfig.valueOf("OPENAI_API_KEY")
                .orElseThrow( () -> new IllegalArgumentException("no APIKEY provided!"));

        var chatLanguageModel = OpenAiChatModel.builder()
                .apiKey( openApiKey )
                .modelName( "gpt-4-vision-preview" )
                .timeout(Duration.ofMinutes(2))
                .logRequests(true)
                .logResponses(true)
                .maxRetries(2)
                .temperature(0.0)
                .maxTokens(2000)
                .build();

        var template = readTextResource( "describe_diagram_image.txt" );

        var systemPrompt = PromptTemplate.from(template).apply( mapOf());

        var imageData = ImageLoader.loadImageAsBase64( "supervisor-diagram.png" );

        System.out.println(imageData );

        var imageUrlOrData = ImageToDiagram.ImageUrlOrData.of( new URI("https://blog.langchain.dev/content/images/2024/01/supervisor-diagram.png")  );
        // var imageUrlOrData = ImageToDiagram.ImageUrlOrData.of( imageData );

        ImageContent imageContent = (imageUrlOrData.url()!=null) ?
                ImageContent.from(imageUrlOrData.url(), ImageContent.DetailLevel.AUTO) :
                ImageContent.from(imageUrlOrData.data(), "image/png", ImageContent.DetailLevel.AUTO );
        TextContent textContent = new TextContent(systemPrompt.text());
        var message = UserMessage.from(textContent, imageContent);

        // var json = ChatMessageSerializer.messageToJson(message);
        var response = chatLanguageModel.generate( message );

        var outputParser = new DiagramOutputParser();

        var text = response.content().text();

        System.out.println( text );

        System.out.println( outputParser.parse( text ) );
    }


    @Test
    public void imageToDiagram() throws Exception {

        var agentExecutor = new ImageToDiagram("supervisor-diagram.png");

        var result = agentExecutor.execute( mapOf() );

        ImageToDiagram.State state = null;
        for( var r : result ) {
            state = r.state();
            System.out.println( state.diagram() );
        }


        System.out.println( ofNullable(state).flatMap(ImageToDiagram.State::diagramCode).orElse("NO DIAGRAM CODE") );

    }
}
