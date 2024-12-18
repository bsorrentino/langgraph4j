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
 * Class that extracts keypoints from a transcript using an AI agent.
 *
 * This class implements the NodeAction interface, allowing it to operate within a specific flow state. It uses an LLM agent to extract meaningful insights (keypoints) from a given transcript.
 */
@Component
public class ExtractKeypointsFromTranscript implements NodeAction<AgenticFlow.State> {

    /**
     * Resource containing the prompt for keypoint extraction.
     */
    @Value("classpath:prompt-keypoints.txt")
    private Resource promptKeypoints;

    /**
     * Final instance of an LLMAgent used to generate responses based on prompts.
     */
    public final LLMAgent agent;

    /**
     * Constructs ExtractKeypointsFromTranscript with an LLM agent.
     *
     * @param agent The LLM agent to use for processing the transcript and generating keypoint summaries.
     * @throws Exception if there's an issue initializing the agent or loading resources.
     */
    public ExtractKeypointsFromTranscript(LLMAgent agent) throws Exception {
        this.agent = agent;
    }

    /**
     * Applies the NodeAction by extracting keypoints from a transcript in the provided state.
     *
     * @param state The AgenticFlow state containing the conversation data.
     * @return Map containing the extracted summary of keypoints.
     * @throws Exception if there's an issue accessing the conversation property or generating the response.
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
