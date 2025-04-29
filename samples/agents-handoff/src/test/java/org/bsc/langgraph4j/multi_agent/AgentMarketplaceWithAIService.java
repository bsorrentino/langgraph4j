package org.bsc.langgraph4j.multi_agent;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.output.structured.Description;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.tool.ToolExecutor;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.action.NodeAction;
import org.bsc.langgraph4j.langchain4j.tool.LC4jToolService;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AgentMarketplaceWithAIService implements NodeAction<MultiAgentHandoff.State>  {

    record FinalResult(
            @Description("final overall summary") String response,
            @Description("comprehensive handoff information") String handoff ) {}


    interface Service {

        @SystemMessage("""
        You are the agent that provides the information on the product marketplace.
        
        your job is evaluate every needed tools after that return the final result.
      
        After complete your job you can handoff control to other agents fo accomplishing the user request
        
        """)
        FinalResult chat( String userMessage );
    }

    public static class Builder {

        final Map<ToolSpecification, ToolExecutor> tools = new HashMap<>();
        ChatLanguageModel model;

        public Builder chatLanguageModel(ChatLanguageModel model ) {
            this.model = model;
            return this;
        }

        public Builder toolSpecification(LC4jToolService.Specification tool ) {
            tools.put( tool.value(), tool.executor() );
            return this;
        }

        public AgentMarketplaceWithAIService build() throws GraphStateException {
            return new AgentMarketplaceWithAIService( this );
        }

    }

    public static Builder builder() {
        return new Builder();
    }


    private final Service service;

    private LC4jToolService.Specification searchIntoMarketplace() {
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

    AgentMarketplaceWithAIService( Builder builder ) {
        var internalTool = searchIntoMarketplace();
        builder.tools.put( internalTool.value(), internalTool.executor() );

        service = AiServices.builder(Service.class)
                .chatLanguageModel(Objects.requireNonNull( builder.model, "chat model cannot be null"))
                .tools( builder.tools )
                .build();
    }

    @Override
    public Map<String, Object> apply(MultiAgentHandoff.State state) throws Exception {
        String userMessage = state.lastMessage()
                .filter( v -> v instanceof UserMessage)
                .map( UserMessage.class::cast)
                .filter(UserMessage::hasSingleText)
                .map(UserMessage::singleText)
                .orElseThrow();

        var result = service.chat( userMessage );
        return Map.of();
    }

}
