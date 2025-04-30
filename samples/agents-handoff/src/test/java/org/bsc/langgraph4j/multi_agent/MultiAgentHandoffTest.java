package org.bsc.langgraph4j.multi_agent;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.langchain4j.serializer.std.LC4jStateSerializer;
import org.bsc.langgraph4j.langchain4j.tool.LC4jToolService;
import org.bsc.langgraph4j.utils.EdgeMappings;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

public class MultiAgentHandoffTest {

    LC4jToolService.Specification handoffToPayment() {
        return new LC4jToolService.Specification(
                ToolSpecification.builder()
                        .name("handoff_to_payment")
                        .description("pass control to payment agent to perform the purchases operations")
                        .parameters(JsonObjectSchema.builder()
                                .addStringProperty("context", "all the information that concerns payment allowing agent to perform the needed actions")
                                .build())
                        .build(),
                ( request, param ) -> {
                    return "handoff needs to the 'payment' agent with context: " + request.arguments();
                }
        );


    }

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
                .toolSpecification( handoffToPayment() )
                .build();
        var agentPayment = new AgentPayment();

        var handoffPreProcessor = new HandoffPreProcessor( AiModel.OLLAMA_QWEN3_14B.model );
        var handoffPostProcessor = new HandoffPostProcessor();

        var stateSerializer = new LC4jStateSerializer<>(MultiAgentHandoff.State::new);

        var workflow = new StateGraph<>( MultiAgentHandoff.State.SCHEMA, stateSerializer )
                .addNode( "marketplace", node_async(agentMarketplace) )
                .addNode( "handoff_preprocessor", node_async(handoffPreProcessor) )
                .addNode( "handoff_postprocessor", node_async(handoffPostProcessor) )
                .addNode( "payment", node_async(agentPayment) )
                .addEdge( START, "marketplace" )
                .addEdge( "marketplace", "handoff_preprocessor")
                .addConditionalEdges( "handoff_preprocessor",
                        edge_async( state ->
                            state.isHandoff() ? "handoff_postprocessor" : END
                        ),
                        EdgeMappings.builder()
                                .to( "handoff_postprocessor")
                                .toEND()
                                .build()
                        )
                .addConditionalEdges( "handoff_postprocessor",
                        edge_async( state -> state.handoffFunction().orElse(END) ),
                        EdgeMappings.builder()
                                .to( "marketplace", "handoff_to_marketplace")
                                .to( "payment", "handoff_to_payment" )
                                .toEND()
                                .build()
                )
                .compile();
                ;

        var input = "search for product 'X' and buy it";

        workflow.stream( Map.of( "messages", UserMessage.from(input )))
                .toCompletableFuture()
                .join();
    }
}
