# Use case proposed in [issue #51](https://github.com/bsorrentino/langgraph4j/issues/51) by [pakamona](https://github.com/pakamona)


**Initialize Logger**


```java
try( var file = new java.io.FileInputStream("./logging.properties")) {
    java.util.logging.LogManager.getLogManager().readConfiguration( file );
}

var log = org.slf4j.LoggerFactory.getLogger("AdaptiveRag");

```


```java
import org.bsc.langgraph4j.state.AgentState;
import java.util.Optional;

public class MyAgentState extends AgentState {

    public MyAgentState(Map<String,Object> initData) {
        super(initData);
    }

    Optional<String> input() {
        return value( "input" );
    }
    Optional<String> orchestratorOutcome() { 
        return value( "orchestrator_outcome" );
    }
}
```


```java
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.action.NodeAction;

import java.util.Map;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.chat.request.ChatRequest;

class OrchestratorAgent implements NodeAction<MyAgentState> {

    private final ChatModel chatModel;

    public OrchestratorAgent( ChatModel chatModel ) {
        this.chatModel = chatModel;
    }
 
    public Map<String, Object> apply(MyAgentState state) throws Exception {
        
        var input = state.input().orElseThrow( () -> new IllegalArgumentException("input is not provided!"));

        var userMessageTemplate = PromptTemplate.from("{{input}}").apply(Map.of("input", input));
        
        var messages = new ArrayList<ChatMessage>();
        
        messages.add(new SystemMessage("""
        You are a helpful assistant. Evaluate the user request and if the request concerns a story return 'story_teller' otherwise 'greeting'
        """));
        messages.add(new UserMessage(userMessageTemplate.text()));

        var request = ChatRequest.builder()
                .messages( messages )
                .build();
       var result = model.chat(request );

        return Map.of( "orchestrator_outcome", result.aiMessage().text() );
    }

};


```


```java
import org.bsc.langgraph4j.action.EdgeAction;

class RouteOrchestratorOutcome implements EdgeAction<MyAgentState> {

    public String apply(MyAgentState state) throws Exception {
        
        var orchestrationOutcome = state.orchestratorOutcome()
                                        .orElseThrow( () -> new IllegalArgumentException("orchestration outcome is not provided!"));

        return orchestrationOutcome;
    }

}
```


```java
class StoryTellerAgent implements NodeAction<MyAgentState> {

    public Map<String, Object> apply(MyAgentState state) throws Exception {
        log.info( "Story Teller Agent invoked");
        return Map.of();
    }
}
```


```java
class GreetingAgent implements NodeAction<MyAgentState> {

    public Map<String, Object> apply(MyAgentState state) throws Exception {
        log.info( "Greeting Agent invoked");
        return Map.of();
    }
}
```


```java
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import org.bsc.langgraph4j.StateGraph;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.StateGraph.END;
import dev.langchain4j.model.openai.OpenAiChatModel;

var model = OpenAiChatModel.builder()
        .apiKey( System.getenv("OPENAI_API_KEY")  )
        .modelName( "gpt-4o-mini" )
        .logResponses(true)
        .maxRetries(2)
        .temperature(0.0)
        .maxTokens(2000)
        .build();

var orchestratorAgent = node_async( new OrchestratorAgent(model) );
var storyTellerAgent = node_async(new StoryTellerAgent());
var greetingAgent = node_async(new GreetingAgent());
var routeOrchestratorOutcome = edge_async( new RouteOrchestratorOutcome() );

var workflow = new StateGraph<>( MyAgentState::new ) 
                .addNode("orchestrator_agent", orchestratorAgent  )
                .addNode("story_teller_agent", storyTellerAgent )
                .addNode("greetings_agent", greetingAgent )
                .addConditionalEdges("orchestrator_agent",
                        routeOrchestratorOutcome,
                        Map.of( "story_teller", "story_teller_agent",
                                "greeting", "greetings_agent" ))
                .addEdge(START, "orchestrator_agent")
                .addEdge("story_teller_agent", END)
                .addEdge("greetings_agent", END);

var app = workflow.compile();        
```


```java


for( var node : app.stream( Map.of( "input", "tell me a xmas story"))) {
    log.info( "{}", node );
}
```

    START 
    NodeOutput{node=__START__, state={input=tell me a xmas story}} 
    Story Teller Agent invoked 
    NodeOutput{node=orchestrator_agent, state={input=tell me a xmas story, orchestrator_outcome=story_teller}} 
    NodeOutput{node=story_teller_agent, state={input=tell me a xmas story, orchestrator_outcome=story_teller}} 
    NodeOutput{node=__END__, state={input=tell me a xmas story, orchestrator_outcome=story_teller}} 



```java
for( var node : app.stream( Map.of( "input", "hi there"))) {
    log.info( "{}", node );
}
```

    START 
    NodeOutput{node=__START__, state={input=hi there}} 
    Greeting Agent invoked 
    NodeOutput{node=orchestrator_agent, state={input=hi there, orchestrator_outcome=greeting}} 
    NodeOutput{node=greetings_agent, state={input=hi there, orchestrator_outcome=greeting}} 
    NodeOutput{node=__END__, state={input=hi there, orchestrator_outcome=greeting}} 

