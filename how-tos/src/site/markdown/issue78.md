# Use case proposed in [issue #78](https://github.com/bsorrentino/langgraph4j/issues/78) by [ikwattro](https://github.com/ikwattro)

## How to Reset memory thread 


**Initialize Logger**


```java
try( var file = new java.io.FileInputStream("./logging.properties")) {
    java.util.logging.LogManager.getLogManager().readConfiguration( file );
}

var log = org.slf4j.LoggerFactory.getLogger("AdaptiveRag");

```


```java
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.Channel;
import java.util.Optional;

public class State extends AgentState {
    static Map<String, Channel<?>> SCHEMA = Map.of();
    
    public State(Map<String,Object> initData) {
        super(initData);
    }
}
```


```java
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.chat.Capability;

var chatLanguageModel = OllamaChatModel.builder()
            .modelName("llama3.1")
            .baseUrl("http://localhost:11434")
            //.supportedCapabilities( Capability.RESPONSE_FORMAT_JSON_SCHEMA )
            .temperature(0.0d)
            .logRequests(true)
            .logResponses(true)
            .build();

```


```java
import org.bsc.langgraph4j.action.NodeActionWithConfig;
import org.bsc.langgraph4j.RunnableConfig;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;

class TestAction implements NodeActionWithConfig<State> {

    private final TestAssistant testAssistant;

    public TestAction(ChatModel chatModel) {
        this.testAssistant = AiServices.builder(TestAssistant.class)
                .chatModel(chatModel)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(20))
                .build();
    }

    @Override
    public Map<String, Object> apply(State state, RunnableConfig runnableConfig) throws Exception {
        var conversation = state.<String>value("conversation")
                                .orElseThrow(() -> new IllegalStateException("No conversation found in state"));

        var threadId = runnableConfig.threadId().orElse( "unknown" );

        return Map.of("response", testAssistant.test( threadId, conversation));
    }

    public interface TestAssistant {
        String test(@MemoryId String memoryId, @UserMessage String query);
    }
}

```


```java
import org.bsc.langgraph4j.StateGraph;
import static org.bsc.langgraph4j.action.AsyncNodeActionWithConfig.node_async;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.StateGraph.END;

var testAction = new TestAction(chatLanguageModel);

var workflow =  new StateGraph<>(State.SCHEMA, State::new)
            .addNode("agent", node_async(testAction))
            .addEdge(START, "agent")
            .addEdge("agent", END);
```


```java
import org.bsc.langgraph4j.checkpoint.MemorySaver;
import org.bsc.langgraph4j.CompileConfig;
import org.bsc.langgraph4j.RunnableConfig;

var memory = new MemorySaver();
var compileConfig = CompileConfig.builder()
        .checkpointSaver(memory)
        .build();

var runnableConfig1 = RunnableConfig.builder()
        .threadId("conversation-num-1")
        .build();
var runnableConfig2 = RunnableConfig.builder()
        .threadId("conversation-num-2")
        .build();

var app = workflow.compile(compileConfig);

app.invoke( Map.of("conversation", "Hi, my name is Chris"), runnableConfig1 )
        .map( r -> r.data() )
        .ifPresent( System.out::println  );

app.invoke( Map.of("conversation", "what's my name ?"), runnableConfig1 )
        .map( r -> r.data() )
        .ifPresent( System.out::println );

app.invoke( Map.of("conversation", "What's my name ?"), runnableConfig2 )
        .map( r -> r.data() )
        .ifPresent( System.out::println );

    
```

    START 


    {response=Nice to meet you, Chris! Is there something I can help you with or would you like to chat?, conversation=Hi, my name is Chris}


    START 


    {response=Your name is Chris. We just established that a minute ago!, conversation=what's my name ?}


    START 


    {response=I don't have any information about your name. This is the beginning of our conversation, and I'm a large language model, I don't retain any personal data or context from previous conversations. If you'd like to share your name with me, I can use it in our conversation!, conversation=What's my name ?}

