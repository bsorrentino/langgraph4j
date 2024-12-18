package org.bsc.assemblyai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import dev.langchain4j.model.openai.OpenAiChatModel;


/**
 * Service class for the Large Language Model (LLM) agent.
 */
@Service
public class LLMAgent {

    /** Resource containing prompt keypoints. */
    @Value("classpath:prompt-keypoints.txt")
    private Resource promptKeypoints;

    /** Pre-initialized OpenAI chat model instance. */
    public final OpenAiChatModel model;

    /**
     * Constructor for the LLMAgent.
     * 
     * Initializes the agent by loading configuration and setting up the OpenAI chat model.
     *
     * @param config DotEnv configuration object.
     */
    public LLMAgent(DotEnvConfig config ) {

        var openApiKey = config.valueOf("OPENAI_API_KEY")
                .orElseThrow( () -> new IllegalArgumentException("no APIKEY provided!"));

        model = OpenAiChatModel.builder()
                .apiKey( openApiKey )
                .modelName( "gpt-4o-mini" )
                .logResponses(true)
                .maxRetries(2)
                .temperature(0.0)
                .maxTokens(2000)
                .build();

    }
}
