package org.bsc.langgraph4j.multi_agent.springai;


import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.util.json.schema.JsonSchemaGenerator;

import java.util.Map;

public class MultiAgentHandoffITest {

    enum AiModel {

        OPENAI_GPT_4O_MINI(
                OpenAiChatModel.builder()
                .openAiApi(OpenAiApi.builder()
                        .baseUrl("https://api.openai.com")
                        .apiKey(System.getenv("OPENAI_API_KEY"))
                        .build())
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4o-mini")
                        .logprobs(false)
                        .temperature(0.1)
                        .build())
                .build()),
        OLLAMA_QWEN3_14B(
                OllamaChatModel.builder()
                .ollamaApi( new OllamaApi("http://localhost:11434") )
                .defaultOptions(OllamaOptions.builder()
                        .model("qwen3.1:14b")
                        .temperature(0.1)
                        .build())
                .build() ),
        OLLAMA_QWEN2_5_7B(
                OllamaChatModel.builder()
                        .ollamaApi( new OllamaApi("http://localhost:11434") )
                        .defaultOptions(OllamaOptions.builder()
                                .model("qwen2.5:7b")
                                .temperature(0.1)
                                .build())
                        .build());
        ;

        public final ChatModel model;

        AiModel(  ChatModel model ) {
            this.model = model;
        }
    }

    record Request( @JsonPropertyDescription("this is my property") String input ) {};

    @Test
    public void testInputType() {
        var schema = JsonSchemaGenerator.generateForType(Request.class, JsonSchemaGenerator.SchemaOption.ALLOW_ADDITIONAL_PROPERTIES_BY_DEFAULT);

        System.out.println( schema );

    }

    @Test
    public void testHandoff() throws Exception {
        var agentMarketPlace = AgentMarketplace.builder()
                .chatModel( AiModel.OLLAMA_QWEN2_5_7B.model )
                .build();

        var handoffExecutor = AgentHandoff.builder()
                .chatModel( AiModel.OLLAMA_QWEN2_5_7B.model )
                .agent( agentMarketPlace )
                .build()
                .compile();

        var input = "search for product 'X' and purchase it";

        var result = handoffExecutor.invoke( Map.of( "messages", new UserMessage(input)));

        System.out.println( result );
    }
}
