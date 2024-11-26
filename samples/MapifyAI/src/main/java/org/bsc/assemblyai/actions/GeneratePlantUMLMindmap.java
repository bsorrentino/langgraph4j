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

@Component
public class GeneratePlantUMLMindmap implements NodeAction<AgenticFlow.State>  {

    @Value("classpath:prompt-plantuml-mindmap.txt")
    private Resource promptFile;

    private final LLMAgent agent;

    public GeneratePlantUMLMindmap( LLMAgent agent)  {
        this.agent = agent;
    }

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
