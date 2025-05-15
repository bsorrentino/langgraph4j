package org.bsc.langgraph4j.multi_agent.springai;


import org.bsc.langgraph4j.prebuilt.MessagesState;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.content.Content;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;

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
                        .ollamaApi(OllamaApi.builder().baseUrl("http://localhost:11434").build())
                        .defaultOptions(OllamaOptions.builder()
                                .model("qwen3.1:14b")
                                .temperature(0.1)
                                .build())
                        .build()),
        OLLAMA_QWEN2_5_7B(
                OllamaChatModel.builder()
                        .ollamaApi(OllamaApi.builder().baseUrl("http://localhost:11434").build())
                        .defaultOptions(OllamaOptions.builder()
                                .model("qwen2.5:7b")
                                .temperature(0.1)
                                .build())
                        .build());;

        public final ChatModel model;

        AiModel(ChatModel model) {
            this.model = model;
        }
    }

    @Test
    public void testHandoff() throws Exception {
        var agentMarketPlace = AgentMarketplace.builder()
                .chatModel(AiModel.OLLAMA_QWEN2_5_7B.model)
                .build();

        var agentPayment = AgentPayment.builder()
                .chatModel(AiModel.OLLAMA_QWEN2_5_7B.model)
                .build();

        var handoffExecutor = AgentHandoff.builder()
                .chatModel(AiModel.OLLAMA_QWEN2_5_7B.model)
                .agent(agentMarketPlace)
                .agent(agentPayment)
                .build()
                .compile();

        var input = "search for product 'X' and purchase it";

        var result = handoffExecutor.invoke(Map.of("messages", new UserMessage(input)));

        var response = result.flatMap(MessagesState::lastMessage)
                .map(Content::getText)
                .orElseThrow();

        System.out.println(response);
    }
}
