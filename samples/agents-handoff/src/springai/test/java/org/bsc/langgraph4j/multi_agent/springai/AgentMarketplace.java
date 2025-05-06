package org.bsc.langgraph4j.multi_agent.springai;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.bsc.langgraph4j.GraphStateException;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

public class AgentMarketplace extends AbstractAgentExecutor<AgentMarketplace.Request,AgentMarketplace.Builder> {

    public record Request(
            @JsonPropertyDescription("all information request about the products") String input )
            implements AbstractAgentExecutor.Request
    {
        @Override
        public String asText() {
            return input;
        }
    };

    static class Tools {
        record Product(
                @JsonPropertyDescription("the product name") String name,
                @JsonPropertyDescription("the product price") double price,
                @JsonPropertyDescription("the product price currency") String currency) {}

        @Tool( description="search for a specific product in the marketplace")
        Product searchByProduct(@ToolParam( description="the product name to search") String product ) {
            return new Product( "X", 1000, "EUR" );
        }

    }

    public static class Builder extends AbstractAgentExecutor.Builder<AgentMarketplace.Builder> {

        public AgentMarketplace build() throws GraphStateException {
            this.name("marketplace")
                .description("marketplace agent, ask for information about products")
                .parameterDescription("all information request about the products")
                .defaultSystem( """
                    You are the agent that provides the information on the product marketplace.
                """)
                .toolsFromObject( new Tools() )
            ;

            return new AgentMarketplace( this );
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    protected AgentMarketplace(Builder builder) throws GraphStateException {
        super(builder.inputType(Request.class));


    }


}
