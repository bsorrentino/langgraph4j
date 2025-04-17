package org.bsc.langgraph4j;


import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.*;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import org.bsc.langgraph4j.agentexecutor.AgentExecutor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class MCPIntegrationITest {

    interface Bot {
        @SystemMessage( "You are my assistant")
        String chat( String message );
    }


    @Test
    public void testMCPClientCall() throws Exception {

        var transport = new StdioMcpTransport.Builder()
                .command(List.of(
                        "docker",
                        "run",
                        "-i",
                        "--rm",
                        "mcp/postgres",
                        "postgresql://admin:bsorrentino@host.docker.internal:5432/mcp_db"))
                .logEvents(true) // only if you want to see the traffic in the log
                .environment( Map.of( ))
                .build();

        final var model = OllamaChatModel.builder()
                .baseUrl( "http://localhost:11434" )
                .temperature(0.0)
                .logRequests(true)
                .logResponses(true)
                .modelName("qwen2.5-coder:latest")
                .build();

        try( var mcpClient = new DefaultMcpClient.Builder()
                .transport(transport)
                .build() ) {

            var toolProvider = McpToolProvider.builder()
                    .mcpClients(List.of(mcpClient))
                    .build();

            var dbTableRes = mcpClient.listResources()
                    .stream()
                    .toList();

            var dbColumnsRes = dbTableRes.stream()
                    .map( res -> mcpClient.readResource( res.uri()) )
                    .flatMap( res -> res.contents().stream())
                    .filter( content -> content.type() == McpResourceContents.Type.TEXT )
                    .map(McpTextResourceContents.class::cast)
                    .map(McpTextResourceContents::text)
                    .toList();

            var context = new StringBuilder();
            for( var i = 0; i < dbTableRes.size() ; ++i ) {

                context.append( dbTableRes.get(i).name() )
                        .append(" = ")
                        .append( dbColumnsRes.get(i) )
                        .append("\n\n");

            }

            var bot = AiServices.builder(Bot.class)
                    .chatLanguageModel(model)
                    .toolProvider(toolProvider)
                    .build();

            var agentBuilder = AgentExecutor.builder()
                    .chatLanguageModel(model);

            for( var toolSpecification : mcpClient.listTools() ) {
                agentBuilder.toolSpecification( toolSpecification, ( request, memoryId) -> mcpClient.executeTool( request) );
            }

            var agent = agentBuilder.build().compile();

            var prompt = PromptTemplate.from(
                    """
                            You have access to the following tables:
                            
                            {{schema}}
                            
                            Answer the question using the tables above.
                            
                            {{input}}
                            """
            );

            var message = prompt.apply( Map.of(
                    "schema", context,
                    "input", "get all issues names and project" ) )
                    .toUserMessage();

            var result = agent.invoke( Map.of( "messages", message) )
                    .flatMap(AgentExecutor.State::finalResponse)
                    .orElse("no response");

            System.out.println( result );
        }

    }

}
