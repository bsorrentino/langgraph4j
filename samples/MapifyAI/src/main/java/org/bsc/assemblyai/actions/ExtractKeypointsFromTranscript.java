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

/**
 * This class is responsible for extracting key points from transcripts using an LLM agent.
 * It implements a NodeAction to process the AgenticFlow state and extract relevant information.
 *
 * @Component indicates that this class is a Spring-managed component.
 */
@Component
public class ExtractKeypointsFromTranscript implements NodeAction<AgenticFlow.State> {

    /**
     * The resource containing the prompt keypoints.
     * @Value injects the resource from the classpath:prompt-keypoints.txt file.
     */
    @Value("classpath:prompt-keypoints.txt")
    private Resource promptKeypoints;

    /**
     * The LLMAgent instance used to generate responses based on the input prompt.
     */
    public final LLMAgent agent;

    /**
     * Constructs an instance of ExtractKeypointsFromTranscript with the provided LLMAgent.
     *
     * @param agent The LLM agent to be used for generating responses.
     * @throws Exception If there's an issue initializing the agent.
     */
    public ExtractKeypointsFromTranscript(LLMAgent agent) throws Exception {
        this.agent = agent;
    }

    /**
     * Applies the extraction logic on the provided AgenticFlow state.
     *
     * @param state The current AgenticFlow state containing conversation data.
     * @return A map with a key "summary" and the extracted summary text as its value.
     * @throws Exception If there's an error processing the state or accessing the agent.
     */
    @Override
    public Map<String, Object> apply(AgenticFlow.State state) throws Exception {

        var promptTemplate = PromptTemplate.from(promptKeypoints.getContentAsString(StandardCharsets.UTF_8));

        // Retrieve or throw exception if conversation property is not found
        var conversation = state.<String>value("conversation")
                .orElseThrow(() -> new RuntimeException("No conversation property found"));

        var prompt = promptTemplate.apply(Map.of("conversation", conversation));

        var response = agent.model.generate(prompt.toUserMessage());

        // Extract the content from the generated message
        var message = response.content();

        // Return a map containing the summary text
        return Map.of("summary", message.text());
    }
}
