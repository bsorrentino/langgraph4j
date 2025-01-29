# Agent Demo

Simple function calling using **langchain4j** framework

![diagram](src/site/resources/ReACT.jpg)


**Initialize Logger**


```java
try( var file = new java.io.FileInputStream("./logging.properties")) {
    var lm = java.util.logging.LogManager.getLogManager();
    lm.checkAccess(); 
    lm.readConfiguration( file );
}

var log = org.slf4j.LoggerFactory.getLogger("AgentExecutor");
```

## Define Tools 

Define tools that will be used by Agent to perfrom actions


```java
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

import java.util.Optional;

import static java.lang.String.format;

public class TestTool {
    private String lastResult;

    Optional<String> lastResult() {
        return Optional.ofNullable(lastResult);
    }

    @Tool("tool for test AI agent executor")
    String execTest(@P("test message") String message) {

        lastResult = format( "test tool executed: %s", message);

        log.info( "exec test: {}", lastResult );
        return lastResult;
    }
}

```

## Initialize LLM for Agent


```java
import dev.langchain4j.model.openai.OpenAiChatModel;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

var llm = OpenAiChatModel.builder()
    .apiKey( System.getenv("OPENAI_API_KEY") )
    .modelName( "gpt-4o-mini" )
    .logResponses(true)
    .maxRetries(2)
    .temperature(0.0)
    .maxTokens(2000)
    .build();
```

## Invoke Agent


```java
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agent.tool.ToolSpecifications;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.openai.OpenAiChatModel;

var tools = ToolSpecifications.toolSpecificationsFrom( TestTool.class );

var systemMessage = SystemMessage.from("you are my useful assistant");
var userMessage = UserMessage.from("Hi i'm bartolomeo! please test with my name as input");
Response<AiMessage> response = llm.generate(List.of(systemMessage, userMessage), tools );
AiMessage aiMessage = response.content();

log.info(  "{}", aiMessage );
```

    AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = "call_O5Gb0hZnedKX4HG4DOF9ZiCe", name = "execTest", arguments = "{"message":"bartolomeo"}" }] } 



```java
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.service.tool.DefaultToolExecutor;
import dev.langchain4j.service.tool.ToolExecutor;
import static dev.langchain4j.agent.tool.ToolSpecifications.toolSpecificationFrom;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ToolNode {

    record Specification( ToolSpecification value, ToolExecutor executor ) {;
        
        public Specification(Object objectWithTool, Method method ) {
            this( toolSpecificationFrom(method), new DefaultToolExecutor(objectWithTool, method));
        }
    }

    public static ToolNode of( Collection<Object> objectsWithTools) {

        List<Specification> toolSpecifications = new ArrayList<>();

        for (Object objectWithTool : objectsWithTools ) {
            for (Method method : objectWithTool.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(Tool.class)) {
                    toolSpecifications.add( new Specification( objectWithTool, method));
                }
            }
        }
        return new ToolNode(toolSpecifications);
    }

    public static ToolNode of(Object ...objectsWithTools) {
        return of( Arrays.asList(objectsWithTools) );
    }

    private final List<Specification> entries;

    private ToolNode( List<Specification> entries) {
        if( entries.isEmpty() ) {
            throw new IllegalArgumentException("entries cannot be empty!");
        }
        this.entries = entries;
    }

    public List<ToolSpecification> toolSpecifications() {
        return this.entries.stream()
                .map(Specification::value)
                .collect(Collectors.toList());
    }

    public Optional<ToolExecutionResultMessage> execute( ToolExecutionRequest request, Object memoryId ) {
        log.trace( "execute: {}", request.name() );

        return entries.stream()
                .filter( v -> v.value().name().equals(request.name()))
                .findFirst()
                .map( e -> {
                    String value = e.executor().execute(request, memoryId);
                    return new ToolExecutionResultMessage( request.id(), request.name(), value );
                });
    }

    public Optional<ToolExecutionResultMessage> execute(Collection<ToolExecutionRequest> requests, Object memoryId ) {
        for( ToolExecutionRequest request : requests ) {

            Optional<ToolExecutionResultMessage> result = execute( request, memoryId );

            if( result.isPresent() ) {
                return result;
            }
        }
        return Optional.empty();
    }

    public Optional<ToolExecutionResultMessage> execute( ToolExecutionRequest request ) {
        return execute( request, null );
    }

    public Optional<ToolExecutionResultMessage> execute( Collection<ToolExecutionRequest> requests ) {
        return execute( requests, null );
    }

}

```


```java
var toolNode = ToolNode.of( new TestTool() );

toolNode.execute( aiMessage.toolExecutionRequests() )
                    .map( m -> m.text() )
                    .orElse( null );
```

    execute: execTest 
    exec test: test tool executed: bartolomeo 





    test tool executed: bartolomeo




```java
import dev.langchain4j.web.search.WebSearchEngine;
import dev.langchain4j.web.search.tavily.TavilyWebSearchEngine;
import dev.langchain4j.rag.content.retriever.WebSearchContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.rag.content.Content;
import java.util.Optional;

import static java.lang.String.format;

public class WebSearchTool {
    
    @Tool("tool for search topics on the web")
    public List<Content> execQuery( @P("search query") String query) {

        var webSearchEngine = TavilyWebSearchEngine.builder()
                .apiKey(System.getenv("TAVILY_API_KEY")) // get a free key: https://app.tavily.com/sign-in
                .build();

        var webSearchContentRetriever = WebSearchContentRetriever.builder()
                .webSearchEngine(webSearchEngine)
                .maxResults(3)
                .build();

        return webSearchContentRetriever.retrieve( new Query( query ) );
    }
}

```


```java
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agent.tool.ToolSpecifications;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.openai.OpenAiChatModel;

var toolNode = ToolNode.of( new TestTool(), new WebSearchTool() );

var systemMessage = SystemMessage.from("you are my useful assistant");
var userMessage = UserMessage.from("Who won 100m competition in Olympic 2024 in Paris ?");

Response<AiMessage> response = llm.generate(List.of(systemMessage, userMessage), toolNode.toolSpecifications() );

AiMessage aiMessage = response.content();

log.info(  "{}", aiMessage );

```

    AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = "call_fEm1bKZmq1gwI2PWn5qMrI5T", name = "execQuery", arguments = "{"query":"100m competition winner Olympic 2024 Paris"}" }] } 

