package org.bsc.assemblyai.actions;

import net.sourceforge.plantuml.SourceStringReader;
import org.bsc.assemblyai.AgenticFlow;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.Map;

/**
 * The GeneratePlantUMLImage class is a service that implements the NodeAction interface for handling AgenticFlow.State.
 * It provides a method to apply PlantUML scripts to generate images and returns the image描述 as part of a Map.
 */
@Service
public class GeneratePlantUMLImage implements NodeAction<AgenticFlow.State>  {

    /**
     * Applies PlantUML to the provided state by reading a mindmap script from the state,
     * rendering it, and saving the output as a PNG file named "mindmap.png".
     * 
     * @param state The object containing the AgenticFlow context.
     * @return A map containing the description of the rendered image.
     * @throws Exception If there is an error processing the mindmap script or saving the image.
     */
    @Override
    public Map<String, Object> apply(AgenticFlow.State state) throws Exception {
        var mindmapScript = state.<String>value("mindmap")
                .orElseThrow( () -> new RuntimeException("No mindmap property found") );

        // Create a reader for PlantUML source strings using the retrieved mindmap script.
        SourceStringReader reader = new SourceStringReader(mindmapScript);

        // Output the image to a file and capture its description.
        try(OutputStream out = new java.io.FileOutputStream("mindmap.png")) {
            var description = reader.outputImage(out);
            return Map.of("description", description.toString());
        }
    }
}
