package org.bsc.assemblyai.actions;

import net.sourceforge.plantuml.SourceStringReader;
import org.bsc.assemblyai.AgenticFlow;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.Map;

@Service
public class GeneratePlantUMLImage implements NodeAction<AgenticFlow.State>  {
    @Override
    public Map<String, Object> apply(AgenticFlow.State state) throws Exception {

        var mindmapScript = state.<String>value("mindmap")
                .orElseThrow( () -> new RuntimeException("No mindmap property found") );

        SourceStringReader reader = new SourceStringReader(mindmapScript);

        try(OutputStream out = new java.io.FileOutputStream("mindmap.png")) {
            var description = reader.outputImage(out);
            return Map.of("description", description.toString());
        }


    }
}
