package org.bsc.langgraph4j.multi_agent.springai;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.bsc.langgraph4j.GraphStateException;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

public class AgentPayment extends AbstractAgentExecutor<AgentPayment.Builder> {

    static class Tools {

        record Transaction(
                @JsonPropertyDescription("the product name bought") String product,
                @JsonPropertyDescription("code operation") String code
        ) {}

        @Tool(description="submit a payment for purchasing a specific product")
        Transaction submitPayment(
                @ToolParam( description="the product name to buy") String product,
                @ToolParam( description="the product price") double price,
                @ToolParam( description="the product price currency") String currency,
                @ToolParam( description="International Bank Account Number (IBAN)") String iban ) {
            return new Transaction( product,"123456789A" );

        }

        @Tool(description="retrieve IBAN information")
        String retrieveIBAN()  {
            return """
                    GB82WEST12345698765432
                    """;
        }

    }

    public static class Builder extends AbstractAgentExecutor.Builder<AgentPayment.Builder> {

        public AgentPayment build() throws GraphStateException {
            this.name("payment")
                .description("payment agent, request purchase and payment transactions")
                .parameterDescription("all information about purchasing to allow the payment")
                .defaultSystem( """
                You are the agent that provides payment service.
                """)
                .toolsFromObject( new Tools() )
                ;

            return new AgentPayment( this );
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    protected AgentPayment(Builder builder) throws GraphStateException {
        super(builder);
    }
}
