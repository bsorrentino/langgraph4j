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
 * The GeneratePlantUMLMindmap class implements the NodeAction interface for generating a PlantUML mindmap based on an agentic flow state.
 */
/**
 * The GeneratePlantUMLMindmap class is a component responsible for generating a PlantUML mindmap based on the provided agentic flow state.
 * It implements the NodeAction interface and utilizes an LLMAgent to process the generated prompt.
 */
@Component
public class GeneratePlantUMLMindmap implements NodeAction<AgenticFlow.State>  {

    /**
     * The promptFile field holds a resource containing the template for generating a PlantUML mindmap.
     */
    @Value("classpath:prompt-plantuml-mindmap.txt")
    private Resource promptFile;

    /**
     * The agent field is the natural language processing (NLP) agent used to generate the mindmap.
     */
    private final LLMAgent agent;

    /**
     * Constructor for GeneratePlantUMLMindmap that initializes the agent field with the provided agent.
     *
     * @param agent the NLP agent to be used for generating the mindmap
     */
    public GeneratePlantUMLMindmap( LLMAgent agent)  {
        this.agent = agent;
    }

    /**
     * The apply method generates a PlantUML mindmap based on the provided agentic flow state.
     *
     * @param state the current agentic flow state containing relevant data
     * @return a map containing the generated mindmap as a string
     * @throws Exception if an error occurs during the generation process
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
