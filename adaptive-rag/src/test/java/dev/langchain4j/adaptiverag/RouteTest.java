package dev.langchain4j.adaptiverag;

import dev.langchain4j.DotEnvConfig;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.time.Duration;
import java.util.logging.LogManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RouteTest {

    @BeforeEach
    public void init() throws Exception {
        FileInputStream configFile = new FileInputStream("logging.properties");
        LogManager.getLogManager().readConfiguration(configFile);

        DotEnvConfig.load();
    }
    @Test
    public void route() {
        String openApiKey = DotEnvConfig.valueOf("OPENAI_API_KEY")
                .orElseThrow( () -> new IllegalArgumentException("no APIKEY provided!"));

        ChatLanguageModel chatLanguageModel = OpenAiChatModel.builder()
                .apiKey( openApiKey )
                .modelName( "gpt-3.5-turbo-0125" )
                .timeout(Duration.ofMinutes(2))
                .logRequests(true)
                .logResponses(true)
                .maxRetries(2)
                .temperature(0.0)
                .maxTokens(2000)
                .build();

        Router.Extractor extractor = AiServices.create(Router.Extractor.class, chatLanguageModel);

        String text = "What are the stock options?";

        Router.DataSource ds = extractor.route(text);

        assertEquals( Router.Type.websearch, ds.datasource );
    }
}
