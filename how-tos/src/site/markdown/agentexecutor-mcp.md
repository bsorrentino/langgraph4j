# Agent Executor + [MCP]

Example how to use [MCP] in agent executor

[MCP]: https://github.com/modelcontextprotocol]


**Initialize Logger**


```java
try( var file = new java.io.FileInputStream("./logging.properties")) {
    java.util.logging.LogManager.getLogManager().readConfiguration( file );
}

var log = org.slf4j.LoggerFactory.getLogger("AgentExecutor");

```

## Use MCP 

In this example we use [mcp/postgres] server to interact with a **Postgres DB**

**Requirements**
* Docker

### Run Postgres DB

Let's runs

```
docker compose -f how-tos/src/docker/docker-compose.yml up
```

Now the Postgres DB is up and run 

### Install MCP server

```
docker pull mcp/postgres
```

[mcp/postgres]: https://github.com/modelcontextprotocol/servers/tree/main/src/postgres

### Setup MCP client


```java
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.*;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;

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


var mcpClient = new DefaultMcpClient.Builder()
                            .transport(transport)
                            .build();

var toolProvider = McpToolProvider.builder()
    .mcpClients(List.of(mcpClient))
    .build();

// get the MCP resources ( available tables )    
var dbTableRes = mcpClient.listResources()
    .stream()
    .toList();

// get the tables schemas     
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

context;
```


```java
import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import org.bsc.langgraph4j.CompileConfig;
import org.bsc.langgraph4j.RunnableConfig;
import org.bsc.langgraph4j.checkpoint.BaseCheckpointSaver;
import org.bsc.langgraph4j.checkpoint.MemorySaver;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.serializer.StateSerializer;

import org.bsc.langgraph4j.agentexecutor.AgentExecutor;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


final var model = OllamaChatModel.builder()
    .baseUrl( "http://localhost:11434" )
    .temperature(0.0)
    .logRequests(true)
    .logResponses(true)
    .modelName("qwen2.5-coder:latest")
    .build();

var toolProvider = McpToolProvider.builder()
                .mcpClients(List.of(mcpClient))
                .build();

var agentBuilder = AgentExecutor.builder()
                        .chatModel(model);

for( var toolSpecification : mcpClient.listTools() ) {      
    agentBuilder.toolSpecification( toolSpecification, ( request, memoryId) -> mcpClient.executeTool( request) );
}

var agent = agentBuilder.build().compile();



```


```java
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.input.PromptTemplate;

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

agent.invoke( Map.of( "messages", message) )
    .flatMap(AgentExecutor.State::finalResponse)
    .orElse("no response");

```

    START 
    callAgent 
    ToolExecutionRequest id is null! 
    ToolExecutionRequest id is null! 
    executeTools 
    execute: query 
    ToolExecutionRequest id is null! 
    ToolExecutionResultMessage id is null! 
    ToolExecutionRequest id is null! 
    ToolExecutionResultMessage id is null! 
    callAgent 
    ToolExecutionRequest id is null! 
    ToolExecutionResultMessage id is null! 
    ToolExecutionRequest id is null! 
    ToolExecutionResultMessage id is null! 





    The query has been executed successfully. The results are as follows:
    
    - **Issue Name**: Implement StateGraph Checkpointing, **Project**: LangGraph4J Core
    - **Issue Name**: Refactor Agent Executor Node, **Project**: LangGraph4J Core
    - **Issue Name**: Add Support for Anthropic Models, **Project**: LangGraph4J Core
    - **Issue Name**: Improve Documentation for Prebuilt Tools, **Project**: LangGraph4J Core
    - **Issue Name**: Write Unit Tests for Graph Validation, **Project**: LangGraph4J Core
    - **Issue Name**: Fix Bug in Conditional Edge Routing, **Project**: LangGraph4J Core
    - **Issue Name**: Explore Async Generator Support, **Project**: LangGraph4J Core
    - **Issue Name**: Optimize Memory Usage for Large Graphs, **Project**: LangGraph4J Core
    - **Issue Name**: Add Example: Multi-Agent Collaboration, **Project**: LangGraph4J Core
    - **Issue Name**: Release Version 1.0.0, **Project**: LangGraph4J Core
    - **Issue Name**: Design Login Page Mockups, **Project**: Project Phoenix UI
    - **Issue Name**: Implement User Authentication Flow, **Project**: Project Phoenix UI
    - **Issue Name**: Develop Dashboard Widget Component, **Project**: Project Phoenix UI
    - **Issue Name**: Set up React Router Navigation, **Project**: Project Phoenix UI
    - **Issue Name**: Integrate Charting Library, **Project**: Project Phoenix UI
    - **Issue Name**: Fix CSS Alignment on Settings Page, **Project**: Project Phoenix UI
    - **Issue Name**: Implement User Profile Editing, **Project**: Project Phoenix UI
    - **Issue Name**: Write E2E Tests for Login, **Project**: Project Phoenix UI
    - **Issue Name**: Optimize Bundle Size, **Project**: Project Phoenix UI
    - **Issue Name**: Accessibility Audit (WCAG), **Project**: Project Phoenix UI
    - **Issue Name**: Create Reusable Button Component, **Project**: Project Phoenix UI
    - **Issue Name**: Analyze Legacy Database Schema, **Project**: Data Migration Initiative
    - **Issue Name**: Develop ETL Script for Users Table, **Project**: Data Migration Initiative
    - **Issue Name**: Develop ETL Script for Orders Table, **Project**: Data Migration Initiative
    - **Issue Name**: Set up Cloud Database Instance, **Project**: Data Migration Initiative
    - **Issue Name**: Define Data Validation Rules, **Project**: Data Migration Initiative
    - **Issue Name**: Perform Test Migration Run 1, **Project**: Data Migration Initiative
    - **Issue Name**: Analyze Performance of ETL Scripts, **Project**: Data Migration Initiative
    - **Issue Name**: Handle Data Cleansing for Addresses, **Project**: Data Migration Initiative
    - **Issue Name**: Plan Production Cutover Strategy, **Project**: Data Migration Initiative
    - **Issue Name**: Document Migration Process, **Project**: Data Migration Initiative


