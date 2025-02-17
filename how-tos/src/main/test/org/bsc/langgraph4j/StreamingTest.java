package org.bsc.langgraph4j;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.EdgeAction;
import org.bsc.langgraph4j.action.NodeAction;
import org.bsc.langgraph4j.langchain4j.generators.LLMStreamingGenerator;
import org.bsc.langgraph4j.langchain4j.serializer.std.ChatMesssageSerializer;
import org.bsc.langgraph4j.langchain4j.serializer.std.ToolExecutionRequestSerializer;
import org.bsc.langgraph4j.langchain4j.tool.ToolNode;
import org.bsc.langgraph4j.serializer.std.ObjectStreamStateSerializer;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.Channel;
import org.bsc.langgraph4j.state.AppenderChannel;
import dev.langchain4j.data.message.ChatMessage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

class MessageState extends AgentState {

    static Map<String, Channel<?>> SCHEMA = Map.of(
            "messages", AppenderChannel.<ChatMessage>of(ArrayList::new)
    );

    public MessageState(Map<String, Object> initData) {
        super(initData);
    }

    List<ChatMessage> messages() {
        return this.<List<ChatMessage>>value("messages")
                .orElseThrow(() -> new RuntimeException("messages not found"));
    }

    // utility method to quick access to last message
    Optional<ChatMessage> lastMessage() {
        List<ChatMessage> messages = messages();
        return (messages.isEmpty()) ?
                Optional.empty() :
                Optional.of(messages.get(messages.size() - 1));
    }
}


class SearchTool {

    @Tool("Use to surf the web, fetch current information, check the weather, and retrieve other information.")
    String execQuery(@P("The query to use in your search.") String query) {

        // This is a placeholder for the actual implementation
        return "Cold, with a low of 13 degrees";
    }
}

@Slf4j
public class StreamingTest {
    @BeforeAll
    public static void loadEnv() {
        DotEnvConfig.load();
    }

    @Test
    public void streamingTest() throws Exception {
        var openApiKey = DotEnvConfig.valueOf("OPENAI_API_KEY")
                .orElseThrow(() -> new IllegalArgumentException("no APIKEY provided!"));

        var stateSerializer = new ObjectStreamStateSerializer<>(MessageState::new);
        stateSerializer.mapper()
                // Setup custom serializer for Langchain4j ToolExecutionRequest
                .register(ToolExecutionRequest.class, new ToolExecutionRequestSerializer())
                // Setup custom serializer for Langchain4j AiMessage
                .register(ChatMessage.class, new ChatMesssageSerializer());

        // setup streaming model
        var model = OpenAiStreamingChatModel.builder()
                .apiKey(openApiKey)
                .modelName("gpt-4o-mini")
                .logResponses(true)
                .temperature(0.0)
                .maxTokens(2000)
                .build();

        // setup tools
        var tools = ToolNode.builder()
                .specification(new SearchTool())
                .build();

        // Call Model
        NodeAction<MessageState> callModel = state -> {
            log.info("CallModel:\n{}", state.messages());

            var generator = LLMStreamingGenerator.<AiMessage, MessageState>builder()
                    .mapResult(response -> {
                        log.info("MapResult: {}", response);
                        return Map.of("messages", response.content());
                    })
                    .startingNode("agent")
                    .startingState(state)
                    .build();

            model.generate(
                    state.messages(),
                    tools.toolSpecifications(),
                    generator.handler());

            return Map.of("messages", generator);
        };

        // Route Message
        EdgeAction<MessageState> routeMessage = state -> {
            log.info("routeMessage:\n{}", state.messages());

            var lastMessage = state.lastMessage()
                    .orElseThrow(() -> (new IllegalStateException("last message not found!")));

            if (lastMessage instanceof AiMessage message) {
                // If tools should be called
                if (message.hasToolExecutionRequests()) return "next";
            }

            // If no tools are called, we can finish (respond to the user)
            return "exit";
        };

        // Invoke Tool
        NodeAction<MessageState> invokeTool = state -> {
            log.info("invokeTool:\n{}", state.messages());

            var lastMessage = state.lastMessage()
                    .orElseThrow(() -> (new IllegalStateException("last message not found!")));


            if (lastMessage instanceof AiMessage lastAiMessage) {

                var result = tools.execute(lastAiMessage.toolExecutionRequests(), null)
                        .orElseThrow(() -> (new IllegalStateException("no tool found!")));

                return Map.of("messages", result);

            }

            throw new IllegalStateException("invalid last message");
        };

        // Define Graph
        var workflow = new StateGraph<MessageState>(MessageState.SCHEMA, stateSerializer)
                .addNode("agent", node_async(callModel))
                .addNode("tools", node_async(invokeTool))
                .addEdge(START, "agent")
                .addConditionalEdges("agent",
                        edge_async(routeMessage),
                        Map.of("next", "tools", "exit", END))
                .addEdge("tools", "agent");

        var app = workflow.compile();

        for( var out : app.stream( Map.of( "messages", UserMessage.from( "what is the whether today?")) ) ) {
            log.info( "{}", out );
        }

    }

}
