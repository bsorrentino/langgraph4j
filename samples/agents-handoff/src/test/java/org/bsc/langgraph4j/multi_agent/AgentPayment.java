package org.bsc.langgraph4j.multi_agent;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.action.NodeAction;
import org.bsc.langgraph4j.langchain4j.tool.LC4jToolService;
import org.bsc.langgraph4j.multi_agent.executor.AgentExecutor;

import java.util.Map;

public class AgentPayment implements NodeAction<MultiAgentHandoff.State> {
    public static class Builder extends AbstractAgentBuilder<AgentPayment> {

        public AgentPayment build() throws GraphStateException {
            return new AgentPayment( delegate );
        }

    }

    public static AgentMarketplace.Builder builder() {
        return new AgentMarketplace.Builder();
    }

    private final CompiledGraph<AgentExecutor.State> agentExecutor;

    private LC4jToolService.Specification submitPayment() {
        return new LC4jToolService.Specification(
                ToolSpecification.builder()
                        .name("search_into_marketplace")
                        .description("search for a specific product in the marketplace")
                        .parameters(JsonObjectSchema.builder()
                                .addStringProperty("product", "the product name to search")
                                .build())
                        .build(),
                ( request, param ) -> {
                    System.out.println( request );
                    return """
                        {
                            product: "X",
                            price: 1000
                            currency: "EUR"
                        }
                        """;
                }
        );

    }

    public AgentPayment( AgentExecutor.Builder builder ) throws GraphStateException {
        final var systemMessage = SystemMessage.from("""
        You are the agent that provides the payment service.
        
        After complete your job you can handoff control to other agents to accomplishing the user request.
        Report into result the function name requesting handoff.
        """);
        
        agentExecutor = builder
                //.responseFormat(responseFormat())
                .systemMessage( systemMessage )
                .toolSpecification( submitPayment() )
                .build()
                .compile();

    }

    @Override
    public Map<String, Object> apply(MultiAgentHandoff.State state) throws Exception {
        return Map.of();
    }
}
