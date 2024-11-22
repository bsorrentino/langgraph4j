package org.bsc.assemblyai.actions;

import dev.langchain4j.model.input.PromptTemplate;
import org.bsc.assemblyai.AgenticFlow;
import org.bsc.assemblyai.LLMAgent;
import org.bsc.assemblyai.MindmapApplication;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class ExtractKeypointsFromTranscript implements NodeAction<AgenticFlow.State> {

    @Value("classpath:prompt-keypoints.txt")
    private Resource promptKeypoints;

    public final LLMAgent agent;

    public ExtractKeypointsFromTranscript( LLMAgent agent) throws Exception {
        this.agent = agent;

    }
    @Override
    public Map<String, Object> apply(AgenticFlow.State state) throws Exception {

        var promptTemplate = PromptTemplate.from( promptKeypoints.getContentAsString(StandardCharsets.UTF_8) );

        var conversation = state.<String>value("conversation")
                .orElseThrow( () -> new RuntimeException("No conversation property found") );

        var prompt = promptTemplate.apply( Map.of("conversation", conversation) );

        var response = agent.model.generate( prompt.toUserMessage() );

        var message = response.content();

        return Map.of("summary", message.text());
    }
}
