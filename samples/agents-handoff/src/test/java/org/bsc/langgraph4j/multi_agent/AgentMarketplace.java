package org.bsc.langgraph4j.multi_agent;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.service.tool.ToolExecutor;
import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.langchain4j.tool.LC4jToolService;
import org.bsc.langgraph4j.multi_agent.executor.AgentExecutor;

import java.util.Map;


public class AgentMarketplace implements ToolExecutor {

    public static class Builder extends AbstractAgentBuilder<AgentMarketplace> {

        public AgentMarketplace build() throws GraphStateException {
            return new AgentMarketplace( delegate );
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    private final CompiledGraph<AgentExecutor.State> agentExecutor;

    public Map.Entry<ToolSpecification, ToolExecutor> asTool() {
        var spec = ToolSpecification.builder()
                .name("workplace")
                .description("workplace agent, ask for information about products")
                .parameters(JsonObjectSchema.builder()
                        .addStringProperty("context",
                                "all information request about the products")
                        .build())
                .build();
        return Map.entry(spec, this);
    }

    private Map.Entry<ToolSpecification, ToolExecutor> searchByProduct() {
        return Map.entry(
                ToolSpecification.builder()
                        .name("search_into_marketplace")
                        .description("search for a specific product in the marketplace")
                        .parameters(JsonObjectSchema.builder()
                                .addStringProperty("product", "the product name to search")
                                .build())
                        .build(),
                ( request, param ) -> {
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

    public AgentMarketplace( AgentExecutor.Builder builder ) throws GraphStateException {
        final var systemMessage = SystemMessage.from("""
        You are the agent that provides the information on the product marketplace.
        """);

        agentExecutor = builder
                .systemMessage( systemMessage )
                .tool( searchByProduct() )
                .build()
                .compile();
    }

    @Override
    public String execute(ToolExecutionRequest toolExecutionRequest, Object o) {

        var userMessage = UserMessage.from( toolExecutionRequest.arguments() );

        var result = agentExecutor.invoke( Map.of( "messages", userMessage ) );

        return result.flatMap(AgentExecutor.State::finalResponse).orElseThrow();
    }


}
