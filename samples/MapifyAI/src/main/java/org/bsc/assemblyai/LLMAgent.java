package org.bsc.assemblyai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import dev.langchain4j.model.openai.OpenAiChatModel;

@Service
public class LLMAgent {

    @Value("classpath:prompt-keypoints.txt")
    private Resource promptKeypoints;

    public final OpenAiChatModel model;

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
