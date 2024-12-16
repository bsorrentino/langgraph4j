package org.bsc.assemblyai.actions;

import net.sourceforge.plantuml.SourceStringReader;
import org.bsc.assemblyai.AgenticFlow;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.Map;

/**
 * Generates a PlantUML image from an AgenticFlow.State object.
 * This service class implements the NodeAction interface for processing state objects that contain
 * a "mindmap" property.
 */
@Service
public class GeneratePlantUMLImage implements NodeAction<AgenticFlow.State>  {

    /**
     * Applies this NodeAction to the given AgenticFlow.State object, generating a PlantUML image and returning its description.
     * If no "mindmap" property is found in the state object, a RuntimeException is thrown.
     *
     * @param state The AgenticFlow.State object containing the mindmap script to be processed
     * @return A map with a single entry: "description", which holds the description of the generated image
     * @throws Exception If an error occurs during the processing of the mindmap script or file output
     */
    @Override
    public Map<String, Object> apply(AgenticFlow.State state) throws Exception {

        // Retrieve the mindmap script from the state object. If not found, throw a RuntimeException.
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
