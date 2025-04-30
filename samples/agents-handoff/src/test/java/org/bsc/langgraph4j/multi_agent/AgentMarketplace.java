package org.bsc.langgraph4j.multi_agent;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.action.NodeAction;
import org.bsc.langgraph4j.langchain4j.tool.LC4jToolService;
import org.bsc.langgraph4j.multi_agent.executor.AgentExecutor;

import java.util.Map;


public class AgentMarketplace implements NodeAction<MultiAgentHandoff.State> {

    public static class Builder extends AbstractAgentBuilder<AgentMarketplace> {

        public AgentMarketplace build() throws GraphStateException {
            return new AgentMarketplace( delegate );
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    private final CompiledGraph<AgentExecutor.State> agentExecutor;

    private LC4jToolService.Specification searchByProduct() {
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

    ResponseFormat responseFormat() {
        return ResponseFormat.builder()
                .type(ResponseFormatType.JSON) // type can be either TEXT (default) or JSON
                .jsonSchema(JsonSchema.builder()
                        .name("AgentResult") // OpenAI requires specifying the name for the schema
                        .rootElement(JsonObjectSchema.builder() // see [1] below
                                .description("final result")
                                .addStringProperty("response", "final overall summary")
                                .addStringProperty("handoff", "comprehensive handoff information")
                                //.required("response", "handoff")
                                .build())
                        .build())
                .build();
    }

    public AgentMarketplace( AgentExecutor.Builder builder ) throws GraphStateException {
        final var systemMessage = SystemMessage.from("""
        You are the agent that provides the information on the product marketplace.
        
        After complete your job you can handoff control to other agents to accomplishing the user request.
        report into result the function name requesting handoff.
        """);

        agentExecutor = builder
                //.responseFormat(responseFormat())
                .systemMessage( systemMessage )
                .toolSpecification( searchByProduct() )
                .build()
                .compile();
    }

    @Override
    public Map<String, Object> apply(MultiAgentHandoff.State state) throws Exception {

        var result = agentExecutor.invoke( Map.of( "messages", state.messages() ) );

        return result.flatMap(AgentExecutor.State::finalResponse)
                .map( response -> Map.<String,Object>of( MultiAgentHandoff.State.AGENT_RESPONSE, response ))
                .orElseGet(Map::of);

        //return result.map(AgentState::data).orElseGet(Map::of);
    }
}
