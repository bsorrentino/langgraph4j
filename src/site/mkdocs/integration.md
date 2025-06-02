# 🦜🕸️ LangGraph4j Integrations

LangGraph4j seamlessly integrates with both **Langchain4j** and **Spring AI**, enabling developers to build powerful LLM-based applications using familiar tools in the Java ecosystem.

**Features**

- Serializer ( Java Stream based )

- LLM streaming support

- Tools

##  <img src="https://docs.langchain4j.dev/img/logo.svg" alt="logo" width="25"/> Langchain4j

Langchain4j is a Java framework that allows developers to interact with Large Language Models (LLMs) like OpenAI, Claude, Mistral, or Ollama.

How LangGraph4j Integrates:

-	**Graph Nodes as Langchain4j Components**: You can use Langchain4j tools (LLM, prompt templates, retrievers, etc.) as nodes in your LangGraph4j StateGraph.
-	**Function Call Handling**: Automatically supports Langchain4j’s structured function calling, parsing arguments directly into graph state.
-	**Chain of Thought**: Combine multiple Langchain4j calls in sequence, branching based on results.
-	**Streaming**: Langchain4j’s streaming responses can be processed and streamed through LangGraph4j’s async graph.

Benefits:

✅ No need to rebuild logic – reuse your Langchain4j services

✅ Compose LLM workflows visually or programmatically

✅ Scalable, conditional execution of LLM calls

✅ Easy to debug and visualize


**Adding Dependencies**

```xml
<dependency>
    <groupId>org.bsc.langgraph4j</groupId>
    <artifactId>langgraph4j-langchain4j</artifactId>
    <version>1.6-SNAPSHOT</version>
</dependency>
```


### ReACT Agent

The **Agent Executor** is a **runtime for agents**.

#### Diagram 

![diagram](images/agentexecutor.puml.png)

#### How to use

```java
public class TestTool {
    private String lastResult;

    Optional<String> lastResult() {
        return Optional.ofNullable(lastResult);
    }

    @Tool("tool for test AI agent executor")
    String execTest(@P("test message") String message) {

        lastResult = format( "test tool executed: %s", message);
        return lastResult;
    }
}

public void main( String args[] ) throws Exception {

    var toolSpecification = ToolSpecification.builder()
            .name("getPCName")
            .description("Returns a String - PC name the AI is currently running in. Returns null if station is not running")
            .build();

    var toolExecutor = (toolExecutionRequest, memoryId) -> getPCName();

    var chatModel = OpenAiChatModel.builder()
            .apiKey( System.getenv( "OPENAI_API_KEY" ) )
            .modelName( "gpt-4o-mini" )
            .logResponses(true)
            .maxRetries(2)
            .temperature(0.0)
            .maxTokens(2000)
            .build();


    var agentExecutor = AgentExecutor.builder()
            .chatModel(chatModel)
            // add dynamic tool
            .toolsFromObject(new TestTool())
            // add dynamic tool
            .tool(toolSpecification, toolExecutor)
            .build();

    var workflow = agentExecutor.compile();

    var iterator =  workflow.stream( Map.of( "messages", UserMessage.from("Run my test!") ) );

    for( var step : iterator ) {
        System.out.println( step );
    }
}
```


⸻

## ![logo](https://spring.io/img/favicon.ico) Spring AI

Spring AI is Spring’s official framework for integrating AI capabilities into Spring Boot apps.


How LangGraph4j Integrates:

-	**Auto-Configuration**: Spring Boot automatically wires LangGraph4j components when using Spring AI.
-	**GraphBeans**: You can define your graph flows as Spring beans, inject services, and use standard Spring dependency injection.
-	**Observability**: Built-in Spring support for tracing and monitoring your AI graphs.
-	**Web Integration**: Easily expose your graph endpoints via REST or WebSocket using Spring MVC or WebFlux.
-	**Graph Invocation**: Use LangGraph4j’s GraphInvoker in service layers or controllers with minimal configuration.


**Adding Dependencies**

```xml
<dependency>
    <groupId>org.bsc.langgraph4j</groupId>
    <artifactId>langgraph4j-spring-ai</artifactId>
    <version>1.6-SNAPSHOT</version>
</dependency>
```

### ReACT Agent 

This is an implementation of ReACT agent in [Spring AI] using Langgraph4j

#### Diagram

![diagram](images/agentexecutor.puml.png)

#### Getting Started


```java
@SpringBootApplication
public class SpringAiDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringAiDemoApplication.class, args);
    }
}
```

#### Configuration

```java
@Configuration
public class ChatModelConfiguration {

    @Bean
    @Profile("ollama")
    public ChatModel ollamaModel() {
        return  OllamaChatModel.builder()
                .ollamaApi( new OllamaApi( "http://localhost:11434" ) )
                .defaultOptions(OllamaOptions.builder()
                        .model("qwen2.5:7b")
                        .temperature(0.1)
                        .build())
                .build();
    }

    @Bean
    @Profile("openai")
    public ChatModel openaiModel() {
        return OpenAiChatModel.builder()
                .openAiApi(OpenAiApi.builder()
                        .baseUrl("https://api.openai.com")
                        .apiKey(System.getenv("OPENAI_API_KEY"))
                        .build())
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4o-mini")
                        .logprobs(false)
                        .temperature(0.1)
                        .build())
                .build();

    }

}
```

#### Console application

```java
@Controller
public class DemoConsoleController implements CommandLineRunner {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DemoConsoleController.class);

    private final ChatModel chatModel;
    private final List<ToolCallback> tools;

    public DemoConsoleController( ChatModel chatModel, List<ToolCallback> tools) {

        this.chatModel = chatModel;
        this.tools = tools;
    }

    @Override
    public void run(String... args) throws Exception {

        log.info("Welcome to the Spring Boot CLI application!");

        var graph = AgentExecutor.builder()
                        .chatModel(chatModel)
                        .tools(tools)
                        .build();

        var workflow = graph.compile();

        var iterator = workflow.stream( Map.of( "messages", new UserMessage("what is the result of 234 + 45") ));

        for( var step : iterator ) {
            System.out.println( step );
        }

    }
}
```

## 🚀 Studio configuration

```java
@Configuration
public class LangGraphStudioConfiguration extends AbstractLangGraphStudioConfig {

    final LangGraphFlow flow;

    public LangGraphStudioConfiguration(  ChatModel chatModel, List<ToolCallback> tools ) throws GraphStateException {

        var workflow = AgentExecutor.builder()
                .chatModel( chatModel )
                .tools( tools )
                .build();

        var mermaid = workflow.getGraph( GraphRepresentation.Type.MERMAID, "ReAct Agent", false );
        System.out.println( mermaid.content() );

        this.flow = agentWorkflow( workflow );
    }

    private LangGraphFlow agentWorkflow( StateGraph<AgentExecutor.State> workflow ) throws GraphStateException {

        return  LangGraphFlow.builder()
                .title("LangGraph Studio (Spring AI)")
                .addInputStringArg( "messages", true, v -> new UserMessage( Objects.toString(v) ) )
                .stateGraph( workflow )
                .compileConfig( CompileConfig.builder()
                        .checkpointSaver( new MemorySaver() )
                        .build())
                .build();

    }

    @Override
    public LangGraphFlow getFlow() {
        return this.flow;
    }
}
```

[Spring AI]: https://spring.io/projects/spring-ai