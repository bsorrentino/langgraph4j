package org.bsc.langgraph4j.multi_agent.lc4j;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.model.output.structured.Description;
import org.bsc.langgraph4j.GraphStateException;


public class AgentMarketplace extends AbstractAgentExecutor<AgentMarketplace.Builder> {

    static class Tools {
        record Product(
                @Description("the product name") String name,
                @Description("the product price") double price,
                @Description("the product price currency") String currency) {}

        @Tool("search for a specific product in the marketplace")
        Product searchByProduct( @P("the product name to search") String product ) {
            return new Product( "X", 1000, "EUR" );
        }

    }
    public static class Builder extends AbstractAgentExecutor.Builder<Builder> {


        public AgentMarketplace build() throws GraphStateException {
            this.name("marketplace")
                .description("marketplace agent, ask for information about products")
                .singleParameter("all information request about the products")
                .systemMessage( SystemMessage.from("""
                    You are the agent that provides the information on the product marketplace.
                """) )
                .toolFromObject( new Tools() );
            return new AgentMarketplace( this );
        }

    }

    public static Builder builder() {
        return new Builder();
    }


    public AgentMarketplace( Builder builder ) throws GraphStateException {
        super( builder );
    }

}
