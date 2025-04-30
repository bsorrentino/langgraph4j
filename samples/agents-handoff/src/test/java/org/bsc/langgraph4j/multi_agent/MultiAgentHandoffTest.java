package org.bsc.langgraph4j.multi_agent;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.bsc.langgraph4j.langchain4j.serializer.std.LC4jStateSerializer;
import org.bsc.langgraph4j.multi_agent.executor.AgentExecutor;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

public class MultiAgentHandoffTest {

    enum AiModel {

        OPENAI( OpenAiChatModel.builder()
                .apiKey( System.getenv("OPENAI_API_KEY") )
                .modelName( "gpt-4o-mini" )
                .supportedCapabilities(Set.of(Capability.RESPONSE_FORMAT_JSON_SCHEMA))
                .logResponses(true)
                .maxRetries(2)
                .temperature(0.0)
                .build() ),
        OLLAMA_QWEN3_14B( OllamaChatModel.builder()
                .modelName( "qwen3:14b" )
                .baseUrl("http://localhost:11434")
                .supportedCapabilities(Capability.RESPONSE_FORMAT_JSON_SCHEMA)
                .logRequests(true)
                .logResponses(true)
                .maxRetries(2)
                .temperature(0.0)
                .build() )
        ;

        public final ChatLanguageModel model;

        AiModel(  ChatLanguageModel model ) {
            this.model = model;
        }
    }


    @Test
    public void testHandoff() throws Exception {

        var agentMarketplace = AgentMarketplace.builder()
                .chatLanguageModel( AiModel.OLLAMA_QWEN3_14B.model )
                    .build();

        var agentPayment = AgentPayment.builder()
                .chatLanguageModel( AiModel.OLLAMA_QWEN3_14B.model )
                .build();

        var agentExecutor = AgentExecutor.builder()
                .chatLanguageModel(AiModel.OLLAMA_QWEN3_14B.model)
                .toolSpecification( agentMarketplace.specification() )
                .toolSpecification( agentPayment.specification() )
                .build()
                .compile()
                ;

        var input = "search for product 'X' and purchase it";

        var result = agentExecutor.invoke( Map.of( "messages", UserMessage.from(input)));

        System.out.println( result );

    }
}
