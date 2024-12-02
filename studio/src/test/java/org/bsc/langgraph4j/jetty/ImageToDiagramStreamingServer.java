package org.bsc.langgraph4j.jetty;

import dev.langchain4j.image_to_diagram.ImageToDiagramProcess;
import org.bsc.langgraph4j.DotEnvConfig;
import org.bsc.langgraph4j.GraphRepresentation;
import org.bsc.langgraph4j.studio.jetty.LangGraphStreamingServerJetty;

public class ImageToDiagramStreamingServer {


    public static void main(String[] args) throws Exception {

        DotEnvConfig.load();

        // var openApiKey = DotEnvConfig.valueOf("OPENAI_API_KEY")
        //        .orElseThrow( () -> new IllegalArgumentException("no OPENAI API KEY provided!"));

        // var imageData = ImageLoader.loadImageAsBase64( "LangChainAgents.png" );

        var agentExecutor = new ImageToDiagramProcess();

        var workflow = agentExecutor.workflowWithCorrection();

        System.out.println (
                workflow.getGraph(GraphRepresentation.Type.MERMAID, "Image To Diagram", false)
                        .getContent()
        );

        var server = LangGraphStreamingServerJetty.builder()
                .port(8080)
                .title("Image To Diagram")
                .addInputImageArg( "imageData" )
                .stateGraph(workflow)
                .build();

        server.start().join();

    }

}
