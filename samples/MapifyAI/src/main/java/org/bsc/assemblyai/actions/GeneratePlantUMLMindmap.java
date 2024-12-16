package org.bsc.assemblyai.actions;

import dev.langchain4j.model.input.PromptTemplate;
import org.bsc.assemblyai.AgenticFlow;
import org.bsc.assemblyai.LLMAgent;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * This class is a Spring component that implements the NodeAction interface for generating PlantUML mindmaps.
 */
@Component
public class GeneratePlantUMLMindmap implements NodeAction<AgenticFlow.State>  {

    /**
     * Resource representing the prompt template file for PlantUML.
     */
    @Value("classpath:prompt-plantuml-mindmap.txt")
    private Resource promptFile;

    /**
     * Reference to an instance of LLMAgent, used for generating responses.
     */
    private final LLMAgent agent;

    /**
     * Constructor for GeneratePlantUMLMindmap.
     *
     * @param agent The LLMAgent instance to be used for generating responses.
     */
    public GeneratePlantUMLMindmap(LLMAgent agent)  {
        this.agent = agent;
    }

    /**
     * Applies this action to the given state, generating a PlantUML mindmap based on a summary property.
     *
     * @param state The AgenticFlow.State object containing the necessary data for generation.
     * @return A map with a key "mindmap" and the generated mindmap as its value.
     * @throws Exception if an error occurs during the generation process, such as missing summary properties or issues with the agent's response.
     */
    @Override
    public Map<String, Object> apply(AgenticFlow.State state) throws Exception {

        var promptTemplate = PromptTemplate.from( promptFile.getContentAsString(StandardCharsets.UTF_8) );

        var summary = state.<String>value("summary")
                .orElseThrow( () -> new RuntimeException("No summary property found") );

        var prompt = promptTemplate.apply( Map.of("summary", summary) );

        var response = agent.model.generate( prompt.toUserMessage() );

        var message = response.content();

        return Map.of("mindmap", message.text());

    }
}
