package dev.langchain4j.image_to_diagram;

import com.google.gson.Gson;
import dev.langchain4j.DotEnvConfig;
import dev.langchain4j.data.message.*;
import dev.langchain4j.image_to_diagram.state.Diagram;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.input.PromptTemplate;

import org.bsc.langgraph4j.GraphRepresentation;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ImageToDiagramTest {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ImageToDiagramTest.class);

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

        dev.langchain4j.model.input.Prompt systemPrompt = PromptTemplate.from(template).apply( Map.of());

        String imageData = ImageLoader.loadImageAsBase64( "supervisor-diagram.png" );

        assertNotNull(imageData);

        var imageUrlOrData = ImageToDiagramProcess.ImageUrlOrData.of( new URI("https://blog.langchain.dev/content/images/2024/01/supervisor-diagram.png")  );

        ImageContent imageContent = (imageUrlOrData.url()!=null) ?
                ImageContent.from(imageUrlOrData.url(), ImageContent.DetailLevel.AUTO) :
                ImageContent.from(imageUrlOrData.data(), "image/png", ImageContent.DetailLevel.AUTO );
        TextContent textContent = new TextContent(systemPrompt.text());
        UserMessage message = UserMessage.from(textContent, imageContent);

        var imageToDiagram = new ImageToDiagram() {};

        var request = ChatRequest.builder()
                .messages( message )
                .build();
        var response = imageToDiagram.getVisionModel().chat(request );

        var outputParser = new DiagramOutputParser();

        String text = response.aiMessage().text();

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
        var result = process.workflow().compile().stream( Map.of( "diagramCode", diagramCode ) )
                .collectAsync( list, (l, v) -> log.trace(v.toString()) )
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

    @Test
    public void getGraph() throws Exception {
        var agentExecutor = new ImageToDiagramProcess();

        var plantUml = agentExecutor.workflow()
                .getGraph( GraphRepresentation.Type.PLANTUML,
                        "Image to diagram with correction",
                        false );

        assertNotNull(plantUml);
        var expected_workflow = readTextResource("01_expected_plantuml.txt");
        assertEquals( expected_workflow, plantUml.content() );

        var plantUmlWithCorrection = agentExecutor.workflowWithCorrection()
                            .getGraph( GraphRepresentation.Type.PLANTUML,
                                    "Image to diagram with correction",
                                    false );

        assertNotNull(plantUmlWithCorrection);
        var expected_workflow_with_correction = readTextResource("02_expected_plantuml.txt");
        assertEquals( expected_workflow_with_correction, plantUmlWithCorrection.content() );

        var mermaid = agentExecutor.workflow()
                .getGraph( GraphRepresentation.Type.MERMAID,
                        "Image to diagram with correction",
                        false );

        assertNotNull(mermaid);
        expected_workflow = readTextResource("01_expected_mermaid.txt");
        assertEquals( expected_workflow, mermaid.content() );

        var mermaidWithCorrection = agentExecutor.workflowWithCorrection()
                .getGraph( GraphRepresentation.Type.MERMAID,
                        "Image to diagram with correction",
                        false );

        assertNotNull(mermaidWithCorrection);
        expected_workflow_with_correction = readTextResource("02_expected_mermaid.txt");
        assertEquals( expected_workflow_with_correction, mermaidWithCorrection.content() );

        var correctionProcess = new DiagramCorrectionProcess();

        var correctionPlantUml = correctionProcess.workflow().getGraph( GraphRepresentation.Type.MERMAID,
                        "Correction Process",
                        false );

        assertEquals( "---\n" +
                "title: Correction Process\n" +
                "---\n" +
                "flowchart TD\n" +
                "\t__START__((start))\n" +
                "\t__END__((stop))\n" +
                "\tevaluate_result(\"evaluate_result\")\n" +
                "\tagent_review(\"agent_review\")\n" +
                "\t%%\tcondition1{\"check state\"}\n" +
                "\t__START__:::__START__ --> evaluate_result:::evaluate_result\n" +
                "\tagent_review:::agent_review --> evaluate_result:::evaluate_result\n" +
                "\t%%\tevaluate_result:::evaluate_result --> condition1:::condition1\n" +
                "\t%%\tcondition1:::condition1 -->|ERROR| agent_review:::agent_review\n" +
                "\tevaluate_result:::evaluate_result -->|ERROR| agent_review:::agent_review\n" +
                "\t%%\tcondition1:::condition1 -->|UNKNOWN| __END__:::__END__\n" +
                "\tevaluate_result:::evaluate_result -->|UNKNOWN| __END__:::__END__\n" +
                "\t%%\tcondition1:::condition1 -->|OK| __END__:::__END__\n" +
                "\tevaluate_result:::evaluate_result -->|OK| __END__:::__END__\n" +
                "\n" +
                "\tclassDef ___START__ fill:black,stroke-width:1px,font-size:xx-small;\n" +
                "\tclassDef ___END__ fill:black,stroke-width:1px,font-size:xx-small;\n", correctionPlantUml.content());

    }

}
