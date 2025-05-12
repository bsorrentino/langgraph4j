package org.bsc.langgraph4j.spring.ai;

import org.bsc.async.AsyncGenerator;
import org.bsc.async.FlowGenerator;
import org.bsc.langgraph4j.NodeOutput;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.action.EdgeAction;
import org.bsc.langgraph4j.action.NodeAction;
import org.bsc.langgraph4j.prebuilt.MessagesState;
import org.bsc.langgraph4j.serializer.std.ObjectStreamStateSerializer;
import org.bsc.langgraph4j.spring.ai.generators.StreamingChatGenerator;
import org.bsc.langgraph4j.spring.ai.serializer.std.SpringAIStateSerializer;
import org.bsc.langgraph4j.spring.ai.tool.SpringAIToolService;
import org.bsc.langgraph4j.streaming.StreamingOutput;
import org.bsc.langgraph4j.utils.EdgeMappings;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.reactivestreams.FlowAdapters;
import org.reactivestreams.Publisher;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.tool.function.FunctionToolCallback;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static java.util.Optional.ofNullable;
import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

class SearchTool {

    @Tool( description = "Use to surf the web, fetch current information, check the weather, and retrieve other information.")
    String execQuery(@ToolParam( description = "The query to use in your search.") String query) {
        // This is a placeholder for the actual implementation
        return "Cold, with a low of 13 degrees";
    }
}

public class StreamingTestITest {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(StreamingTestITest.class);

    static class State extends MessagesState<Message> {
        public State(Map<String, Object> initData) {
            super(initData);
        }
    }

    enum AiModel {

        OPENAI_GPT_4O_MINI(
                OpenAiChatModel.builder()
                        .openAiApi(OpenAiApi.builder()
                                .baseUrl("https://api.openai.com")
                                .apiKey(System.getenv("OPENAI_API_KEY"))
                                .build())
                        .defaultOptions(OpenAiChatOptions.builder()
                                .model("gpt-4o-mini")
                                .logprobs(false)
                                .temperature(0.1)
                                .build())
                        .build()),
        OLLAMA_QWEN3_14B(
                OllamaChatModel.builder()
                        .ollamaApi( new OllamaApi("http://localhost:11434") )
                        .defaultOptions(OllamaOptions.builder()
                                .model("qwen3.1:14b")
                                .temperature(0.1)
                                .build())
                        .build() ),
        OLLAMA_QWEN2_5_7B(
                OllamaChatModel.builder()
                        .ollamaApi( new OllamaApi("http://localhost:11434") )
                        .defaultOptions(OllamaOptions.builder()
                                .model("qwen2.5:7b")
                                .temperature(0.1)
                                .build())
                        .build());
        ;

        public final ChatModel model;

        AiModel(  ChatModel model ) {
            this.model = model;
        }
    }


    public <T> AsyncGenerator<T> toGenerator( Flux<T> stream ) {

        return FlowGenerator.fromPublisher(FlowAdapters.toFlowPublisher(stream) );
    }

    @Test
    public void llmStreamingTest() throws InterruptedException {
        var chatClient = ChatClient.builder(AiModel.OLLAMA_QWEN2_5_7B.model)
                .defaultOptions(ToolCallingChatOptions.builder()
                        .internalToolExecutionEnabled(false) // Disable automatic tool execution
                        .build())
                .defaultSystem("You are a helpful AI Assistant answering questions." )
                .build();

        var flux = chatClient.prompt()
                .messages( new UserMessage("tell me a joke"))
                .stream()
                .chatResponse()
                ;
        flux.doOnNext( item -> System.out.println("Received: " + item) )
            .blockLast();

    }

    @Test
    public void llmStreamingGeneratorTest() throws InterruptedException {
        var chatClient = ChatClient.builder(AiModel.OLLAMA_QWEN2_5_7B.model)
                .defaultOptions(ToolCallingChatOptions.builder()
                        .internalToolExecutionEnabled(false) // Disable automatic tool execution
                        .build())
                .defaultSystem("You are a helpful AI Assistant answering questions." )
                .build();

        var flux = chatClient.prompt()
                .messages( new UserMessage("tell me a joke"))
                .stream()
                .chatResponse()
                ;

        var generator  = toGenerator( flux.map( item -> item.getResult().getOutput().getText() ) );

        generator.forEachAsync( item ->
                System.out.println("Received: " + item) )
                .join();
    }

    @Test
    public void llmStreamingGeneratorIteratorTest() throws InterruptedException {
        var chatClient = ChatClient.builder(AiModel.OLLAMA_QWEN2_5_7B.model)
                .defaultOptions(ToolCallingChatOptions.builder()
                        .internalToolExecutionEnabled(false) // Disable automatic tool execution
                        .build())
                .defaultSystem("You are a helpful AI Assistant answering questions." )
                .build();

        var flux = chatClient.prompt()
                .messages( new UserMessage("tell me a joke"))
                .stream()
                .chatResponse()
                ;

        for( var item : toGenerator( flux.map( item -> item.getResult().getOutput().getText() ) ) ) {
            System.out.println("Received: " + item );
        }
    }


    @Test
    public void streamingTest() throws Exception {

        final var stateSerializer = new SpringAIStateSerializer<State>(State::new);

        final var tools = ToolCallbacks.from( new SearchTool() );

        final var chatClient = ChatClient.builder(AiModel.OPENAI_GPT_4O_MINI.model)
                .defaultOptions(ToolCallingChatOptions.builder()
                        .internalToolExecutionEnabled(false) // Disable automatic tool execution
                        .build())
                .defaultTools( tools )
                .defaultSystem("You are a helpful AI Assistant answering questions." )
                .build();

        // Call Model
        NodeAction<State> callModel = state -> {
            log.info("CallModel:\n{}", state.messages());

            var flux = chatClient.prompt()
                    .messages(state.messages())
                    .stream()
                    .chatResponse();

            var generator  = StreamingChatGenerator.builder()
                    .startingNode("agent")
                    .startingState( state )
                    .mapResult( response -> Map.of( "messages", response.getResult().getOutput()))
                    .build(flux);

            return Map.of("messages", generator);
        };

        // Route Message
        EdgeAction<State> routeMessage = state -> {
            log.info("routeMessage:\n{}", state.messages());

            var lastMessage = state.lastMessage()
                    .orElseThrow(() -> (new IllegalStateException("last message not found!")));

            if( lastMessage instanceof  AssistantMessage assistantMessage ) {
                if( assistantMessage.hasToolCalls() ) {
                    return "continue";
                }
            }

            // If no tools are called, we can finish (respond to the user)
            return END;
        };

        // setup tools
        var toolService = new SpringAIToolService( List.of(tools) );

        // Invoke Tool
        AsyncNodeAction<State> invokeTool = state -> {
            log.info("invokeTool:\n{}", state.messages());

            var lastMessage = state.lastMessage()
                    .orElseThrow(() -> (new IllegalStateException("last message not found!")));

            if( lastMessage instanceof AssistantMessage assistantMessage ) {
                if( assistantMessage.hasToolCalls() ) {

                    return toolService.executeFunctions( assistantMessage.getToolCalls() )
                            .thenApply( result -> Map.of( "messages", result ));

                }
            }

            return CompletableFuture.failedFuture( new IllegalStateException("no AssistantMessage found!"));
        };

        // Define Graph
        var workflow = new StateGraph<>(MessagesState.SCHEMA, stateSerializer)
                .addNode("agent", node_async(callModel))
                .addNode("tools", invokeTool)
                .addEdge(START, "agent")
                .addConditionalEdges("agent",
                        edge_async(routeMessage),
                        EdgeMappings.builder()
                                .to("tools", "continue")
                                .toEND()
                                .build())
                .addEdge("tools", "agent");

        var app = workflow.compile();

        var generator = app.stream( Map.of( "messages", new UserMessage( "what is the whether today?")) );

        generator.forEachAsync( out -> System.out.println( out ) ).join();

    }

}
