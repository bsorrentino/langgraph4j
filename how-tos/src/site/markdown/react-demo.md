# Agent Demo

Simple function calling using **langchain4j** framework

![diagram](src/site/resources/ReACT.jpg)


```java
String userHomeDir = System.getProperty("user.home");
String localRespoUrl = "file://" + userHomeDir + "/.m2/repository/";
String langchain4jVersion = "0.35.0"
```

add local maven repository


```java
%dependency /add-repo local \{localRespoUrl} release|never snapshot|always
%dependency /list-repos
```

    [0mRepository [1m[32mlocal[0m url: [1m[32mfile:///Users/bsorrentino/.m2/repository/[0m added.
    [0mRepositories count: 4
    [0mname: [1m[32mcentral [0murl: [1m[32mhttps://repo.maven.apache.org/maven2/ [0mrelease:[32mtrue [0mupdate:[32mnever [0msnapshot:[32mfalse [0mupdate:[32mnever 
    [0m[0mname: [1m[32mjboss [0murl: [1m[32mhttps://repository.jboss.org/nexus/content/repositories/releases/ [0mrelease:[32mtrue [0mupdate:[32mnever [0msnapshot:[32mfalse [0mupdate:[32mnever 
    [0m[0mname: [1m[32matlassian [0murl: [1m[32mhttps://packages.atlassian.com/maven/public [0mrelease:[32mtrue [0mupdate:[32mnever [0msnapshot:[32mfalse [0mupdate:[32mnever 
    [0m[0mname: [1m[32mlocal [0murl: [1m[32mfile:///Users/bsorrentino/.m2/repository/ [0mrelease:[32mtrue [0mupdate:[32mnever [0msnapshot:[32mtrue [0mupdate:[32malways 
    [0m

Remove installed package from Jupiter cache


```bash
%%bash 
rm -rf \{userHomeDir}/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/bsc/langgraph4j
```

Install required maven dependencies


```java
%dependency /add org.slf4j:slf4j-jdk14:2.0.9
%dependency /add dev.langchain4j:langchain4j:\{langchain4jVersion}
%dependency /add dev.langchain4j:langchain4j-open-ai:\{langchain4jVersion}
%dependency /add dev.langchain4j:langchain4j-web-search-engine-tavily:\{langchain4jVersion}

%dependency /resolve
```

    Adding dependency [0m[1m[32morg.slf4j:slf4j-jdk14:2.0.9
    [0mAdding dependency [0m[1m[32mdev.langchain4j:langchain4j:0.35.0
    [0mAdding dependency [0m[1m[32mdev.langchain4j:langchain4j-open-ai:0.35.0
    [0mAdding dependency [0m[1m[32mdev.langchain4j:langchain4j-web-search-engine-tavily:0.35.0
    [0mSolving dependencies
    Resolved artifacts count: 24
    Add to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/slf4j/slf4j-jdk14/2.0.9/slf4j-jdk14-2.0.9.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/dev/langchain4j/langchain4j/0.35.0/langchain4j-0.35.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/dev/langchain4j/langchain4j-core/0.35.0/langchain4j-core-0.35.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/apache/opennlp/opennlp-tools/1.9.4/opennlp-tools-1.9.4.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/dev/langchain4j/langchain4j-open-ai/0.35.0/langchain4j-open-ai-0.35.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/dev/ai4j/openai4j/0.22.0/openai4j-0.22.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/squareup/okhttp3/okhttp-sse/4.12.0/okhttp-sse-4.12.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/jetbrains/kotlin/kotlin-stdlib-jdk8/1.9.10/kotlin-stdlib-jdk8-1.9.10.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/jetbrains/kotlin/kotlin-stdlib/1.9.10/kotlin-stdlib-1.9.10.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/jetbrains/kotlin/kotlin-stdlib-common/1.9.10/kotlin-stdlib-common-1.9.10.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/jetbrains/annotations/13.0/annotations-13.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/jetbrains/kotlin/kotlin-stdlib-jdk7/1.9.10/kotlin-stdlib-jdk7-1.9.10.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/knuddels/jtokkit/1.1.0/jtokkit-1.1.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/dev/langchain4j/langchain4j-web-search-engine-tavily/0.35.0/langchain4j-web-search-engine-tavily-0.35.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/squareup/retrofit2/retrofit/2.9.0/retrofit-2.9.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/squareup/retrofit2/converter-jackson/2.9.0/converter-jackson-2.9.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/fasterxml/jackson/core/jackson-databind/2.16.1/jackson-databind-2.16.1.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/fasterxml/jackson/core/jackson-annotations/2.16.1/jackson-annotations-2.16.1.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/fasterxml/jackson/core/jackson-core/2.16.1/jackson-core-2.16.1.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/squareup/okhttp3/okhttp/4.12.0/okhttp-4.12.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/squareup/okio/okio/3.6.0/okio-3.6.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/squareup/okio/okio-jvm/3.6.0/okio-jvm-3.6.0.jar[0m
    [0m

Initialize Logger


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

    AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = "call_hCQjilDkNz3x7e04hmLnAGne", name = "execTest", arguments = "{"message":"bartolomeo"}" }] } 



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

    AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = "call_r7HmTqUhMXdDjrnpISJXAGM6", name = "execQuery", arguments = "{"query":"100m competition winner Olympic 2024 Paris"}" }] } 

