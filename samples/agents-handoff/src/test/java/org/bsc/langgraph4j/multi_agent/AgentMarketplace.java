package org.bsc.langgraph4j.multi_agent;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.service.tool.ToolExecutor;
import org.bsc.langgraph4j.GraphStateException;

import java.util.Map;


public class AgentMarketplace extends AbstractAgent<AgentMarketplace.Builder,AgentMarketplace> {

    public static class Builder extends AbstractAgent.Builder<Builder,AgentMarketplace> {

        public AgentMarketplace build() throws GraphStateException {
            return new AgentMarketplace( this );
        }

    }

    private static final Map.Entry<ToolSpecification, ToolExecutor> searchByProduct =
        Map.entry(
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

    public static Builder builder() {
        return new Builder();
    }


    public AgentMarketplace( Builder builder ) throws GraphStateException {
        super( builder.name("marketplace")
                .description("marketplace agent, ask for information about products")
                .singleParameter("all information request about the products")
                .systemMessage( SystemMessage.from("""
                    You are the agent that provides the information on the product marketplace.
                    """) )
                .tool( searchByProduct )
        );
    }

}
