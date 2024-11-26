package dev.langchain4j.image_to_diagram;

import com.google.gson.Gson;
import dev.langchain4j.DotEnvConfig;
import dev.langchain4j.data.message.*;
import dev.langchain4j.image_to_diagram.state.Diagram;
import dev.langchain4j.model.input.PromptTemplate;
import lombok.extern.slf4j.Slf4j;

import org.bsc.langgraph4j.NodeOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.LogManager;

import static java.lang.String.format;
import static org.bsc.langgraph4j.utils.CollectionsUtils.last;
import static org.bsc.langgraph4j.utils.CollectionsUtils.mapOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class ImageToDiagramTest {

    public static final String VISION_MODEL_NAME = "gpt-4o";

    private String readTextResource(String resourceName ) throws Exception {
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
            int len = result.length();
            return (len>0) ? result.deleteCharAt(len - 1).toString() : "";
        }
    }

    @BeforeEach
    public void init() throws Exception {
        FileInputStream configFile = new FileInputStream("logging.properties");
        LogManager.getLogManager().readConfiguration(configFile);

        DotEnvConfig.load();
    }

    @Test
    public void parseDiagramSchema() throws Exception {
        String json = readTextResource("diagram_data.json");
        assertNotNull(json);

        var gson = new Gson();
        var diagram = gson.fromJson(json, Diagram.Element.class);

        assertNotNull(diagram);
        assertEquals( "process", diagram.type()  );
        assertEquals( "User Request Routing", diagram.title()  );
        assertEquals( 5, diagram.participants().size()  );
        assertEquals( 4, diagram.relations().size()  );
        assertTrue(diagram.containers().isEmpty());
        assertEquals( 3, diagram.description().size()  );
    }

    @Test
    public void parseDescription() throws Exception {
        String result = readTextResource("describe_result.txt");

        DiagramOutputParser outputParser = new DiagramOutputParser();
        var diagram = outputParser.parse( result );
        assertNotNull(diagram);
        assertEquals( "process", diagram.type()  );
        assertEquals( "User Request Routing", diagram.title()  );
        assertEquals( 5, diagram.participants().size()  );
        assertEquals( 4, diagram.relations().size()  );
        assertTrue(diagram.containers().isEmpty());
        assertEquals( 3, diagram.description().size()  );
    }

    @Test
    public void describeDiagramImage() throws Exception {

        String template = readTextResource( "describe_diagram_image.txt" );

        dev.langchain4j.model.input.Prompt systemPrompt = PromptTemplate.from(template).apply( mapOf());

        String imageData = ImageLoader.loadImageAsBase64( "supervisor-diagram.png" );

        assertNotNull(imageData);

        var imageUrlOrData = ImageToDiagramProcess.ImageUrlOrData.of( new URI("https://blog.langchain.dev/content/images/2024/01/supervisor-diagram.png")  );

        ImageContent imageContent = (imageUrlOrData.url()!=null) ?
                ImageContent.from(imageUrlOrData.url(), ImageContent.DetailLevel.AUTO) :
                ImageContent.from(imageUrlOrData.data(), "image/png", ImageContent.DetailLevel.AUTO );
        TextContent textContent = new TextContent(systemPrompt.text());
        UserMessage message = UserMessage.from(textContent, imageContent);

        var imageToDiagram = new ImageToDiagram() {};

        var response = imageToDiagram.getVisionModel().generate( message );

        var outputParser = new DiagramOutputParser();

        String text = response.content().text();

        assertNotNull( text );

       var diagram = outputParser.parse( text );

       assertNotNull(diagram);
       assertEquals( "process", diagram.type()  );
       assertEquals( "User-Supervisor-Agent Routing", diagram.title()  );
       assertEquals( 5, diagram.participants().size()  );
       assertEquals( 5, diagram.relations().size()  );
       assertTrue(diagram.containers().isEmpty());
       assertEquals( 5, diagram.description().size()  );
    }


    @Test
    public void imageToDiagram() throws Exception {

        var agentExecutor = new ImageToDiagramProcess();

        var imageData = ImageLoader.loadImageAsBase64( "LangChainAgents.png" );

        assertNotNull(imageData);

        var result = agentExecutor.executeWithCorrection( ImageToDiagramProcess.ImageUrlOrData.of(imageData) );

        var diagramCode = result.stream().reduce( (out1, out2) -> out2 )
                .map( NodeOutput::state )
                .flatMap( s -> last( s.diagramCode() ) )
                .orElse("NO DIAGRAM CODE") ;

        assertNotNull(diagramCode);
        assertNotEquals("NO DIAGRAM CODE", diagramCode);

    }

    private void reviewDiagram( String diagramId ) throws Exception {

        final var diagramCode = readTextResource(format("%s_wrong_result.txt", diagramId));

        final var expectedCode = readTextResource(format("%s_expected_result.txt", diagramId));

        final var process = new DiagramCorrectionProcess();

        ArrayList<NodeOutput<ImageToDiagram.State>> list = new ArrayList<NodeOutput<ImageToDiagram.State>>();
        var result = process.execute( Map.of( "diagramCode", diagramCode ) )
                .collectAsync( list, v -> log.trace(v.toString()) )
                .thenApply( v -> {
                    if( list.isEmpty() ) {
                        throw new RuntimeException("no results");
                    }
                    NodeOutput<ImageToDiagram.State> last = list.get( list.size() - 1 );
                    return last.state();
                })
                .join();

        java.util.Optional<String> code = last( result.diagramCode() );
        assertTrue( code.isPresent() );
        assertEquals( expectedCode, code.get().trim() );

        //return code.get();
    }


    @Test
    public void reviewDiagramWithRuntimeError() throws Exception {
        reviewDiagram("05");
    }
    @Test
    public void reviewDiagramWithSyntaxError() throws Exception {
        reviewDiagram("04");
    }
    @Test
    public void reviewDiagram3() throws Exception {
        reviewDiagram("03");
    }
    @Test
    public void reviewDiagram1() throws Exception {
        reviewDiagram("01");
    }
    @Test
    public void reviewDiagram2() throws Exception {
        reviewDiagram("02");
    }
    @Test
    public void reviewDiagram6() throws Exception {
        reviewDiagram("06");
    }
    @Test
    public void reviewDiagram7() throws Exception {
        reviewDiagram("07");
    }
}
