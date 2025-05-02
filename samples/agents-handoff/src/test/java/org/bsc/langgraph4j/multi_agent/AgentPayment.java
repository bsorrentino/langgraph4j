package org.bsc.langgraph4j.multi_agent;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.service.tool.ToolExecutor;
import org.bsc.langgraph4j.GraphStateException;

import java.util.Map;

public class AgentPayment extends AbstractAgent<AgentPayment.Builder,AgentPayment>  {

    public static class Builder extends AbstractAgent.Builder<AgentPayment.Builder,AgentPayment> {

        public AgentPayment build() throws GraphStateException {
            return new AgentPayment( this );
        }

    }

    public static AgentPayment.Builder builder() {
        return new AgentPayment.Builder();
    }


    private static final Map.Entry<ToolSpecification, ToolExecutor> submitPayment =
        Map.entry(
                ToolSpecification.builder()
                        .name("submit_payment")
                        .description("submit a payment for purchasing a specific product")
                        .parameters(JsonObjectSchema.builder()
                                .addStringProperty("product", "the product name to buy")
                                .addNumberProperty("price", "the product price")
                                .addStringProperty("currency", "the product price currency")
                                .addStringProperty( "iban", "International Bank Account Number" +
                                        "\n")
                                .build())
                        .build(),
                ( request, param ) -> {
                    return """
                        product bought successfully. code operation # 123456789A
                        """;
                }
        );

    private static final Map.Entry<ToolSpecification, ToolExecutor> retrieveIBAN =
            Map.entry(
                ToolSpecification.builder()
                        .name("get_iban")
                        .description("retrieve IBAN information")
                        .parameters(JsonObjectSchema.builder()
                                .build())
                        .build(),
                ( request, param ) -> {
                    return """
                        GB82WEST12345698765432
                        """;
                }
        );


    public AgentPayment( Builder builder ) throws GraphStateException {
        super(builder
                .name("payment")
                .description("payment agent, request purchase and payment transactions")
                .singleParameter("all information provided about the payment")
                .systemMessage( SystemMessage.from("""
                    You are the agent that provides payment service.
                    """) )
                .tool( submitPayment )
                .tool( retrieveIBAN ) );
    }

}
