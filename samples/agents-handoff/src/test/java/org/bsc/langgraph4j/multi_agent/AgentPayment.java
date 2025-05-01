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

public class AgentPayment implements ToolExecutor {

    public static class Builder extends AbstractAgentBuilder<AgentPayment> {

        public AgentPayment build() throws GraphStateException {
            return new AgentPayment( delegate );
        }

    }

    public static AgentPayment.Builder builder() {
        return new AgentPayment.Builder();
    }

    private final CompiledGraph<AgentExecutor.State> agentExecutor;

    public Map.Entry<ToolSpecification, ToolExecutor> asTool() {
        var spec = ToolSpecification.builder()
                .name("payment")
                .description("payment agent, request purchase and payment transactions")
                .parameters(JsonObjectSchema.builder()
                        .addStringProperty("context",
                                "all information provided about the payment")
                        .build())
                .build();
        return Map.entry(spec, this);
    }

    private  Map.Entry<ToolSpecification, ToolExecutor> submitPayment() {
        return Map.entry(
                ToolSpecification.builder()
                        .name("submit_payment")
                        .description("submit a payment for a specific product")
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

    }

    private Map.Entry<ToolSpecification, ToolExecutor> retrieveIBAN() {
        return Map.entry(
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

    }

    public AgentPayment( AgentExecutor.Builder builder ) throws GraphStateException {
        final var systemMessage = SystemMessage.from("""
        You are the agent that provides payment service.
        """);
        
        agentExecutor = builder
                .systemMessage( systemMessage )
                .tool( submitPayment() )
                .tool( retrieveIBAN() )
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
