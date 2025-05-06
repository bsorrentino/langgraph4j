package org.bsc.langgraph4j.multi_agent.lc4j;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.model.output.structured.Description;
import org.bsc.langgraph4j.GraphStateException;

public class AgentPayment extends AbstractAgentExecutor<AgentPayment.Builder> {

    static class Tools {

        record Transaction(
                @Description("the product name bought") String product,
                @Description("code operation") String code
        ) {}

        @Tool("submit a payment for purchasing a specific product")
        Transaction submitPayment(
                @P("the product name to buy") String product,
                @P("the product price") double price,
                @P("the product price currency") String currency,
                @P(value = "International Bank Account Number (IBAN)") String iban ) {
            return new Transaction( product,"123456789A" );

        }

        @Tool("retrieve IBAN information")
        String retrieveIBAN()  {
            return """
                    GB82WEST12345698765432
                    """;
        }

    }

    public static class Builder extends AbstractAgentExecutor.Builder<AgentPayment.Builder> {

        public AgentPayment build() throws GraphStateException {
            return new AgentPayment( this.name("payment")
                    .description("payment agent, request purchase and payment transactions")
                    .singleParameter("all information about purchasing to allow the payment")
                    .systemMessage( SystemMessage.from("""
                    You are the agent that provides payment service.
                    """) )
                    .toolFromObject( new Tools() ));
        }

    }

    public static AgentPayment.Builder builder() {
        return new Builder();
    }


    public AgentPayment( Builder builder ) throws GraphStateException {
        super(builder);
    }

}
