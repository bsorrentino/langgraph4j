package org.bsc.assemblyai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import dev.langchain4j.model.openai.OpenAiChatModel;

/**
 * LLMAgent class that manages interactions with the OpenAI language model.
 */
@Service
public class LLMAgent {

    /**
     * The resource containing prompt keypoints.
     */
    @Value("classpath:prompt-keypoints.txt")
    private Resource promptKeypoints;

    /**
     * The final instance of the OpenAiChatModel used for generating responses.
     */
    public final OpenAiChatModel model;

    /**
     * Constructor for LLMAgent class that initializes the OpenAI chat model with configuration details.
     *
     * @param config Configuration object containing API key and other settings.
     * @throws IllegalArgumentException if no API key is provided in the configuration.
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
