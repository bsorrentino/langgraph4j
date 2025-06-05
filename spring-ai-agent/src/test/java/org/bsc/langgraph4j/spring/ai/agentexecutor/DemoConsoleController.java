package org.bsc.langgraph4j.spring.ai.agentexecutor;

import org.bsc.langgraph4j.NodeOutput;
import org.bsc.langgraph4j.streaming.StreamingOutput;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

/**
 * Demonstrates the use of Spring Boot CLI to execute a task using an agent executor.
 */
@Controller
public class DemoConsoleController implements CommandLineRunner {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DemoConsoleController.class);

    private final ChatModel chatModel;

    public DemoConsoleController( ChatModel chatModel ) {

        this.chatModel = chatModel;
    }

    /**
     * Executes the command-line interface to demonstrate a Spring Boot application.
     * This method logs a welcome message, constructs a graph using an agent executor,
     * compiles it into a workflow, invokes the workflow with a specific input,
     * and then logs the final result.
     *
     * @param args Command line arguments (Unused in this context)
     * @throws Exception If any error occurs during execution
     */
    @Override
    public void run(String... args) throws Exception {

        log.info("Welcome to the Spring Boot CLI application!");

        var agent = AgentExecutorEx.builder()
                        //.streamingChatModel(chatModel)
                        .chatModel(chatModel)
                        .toolsFromObject( new TestTool()) // Support without providing tools
                        .build()
                        .compile();

        var result = agent.stream( Map.of( "messages", new UserMessage("perform test twice") ));

        var state = result.stream()
                .peek( s -> {
                    if( s instanceof StreamingOutput<?> sout ) {
                        System.out.printf( "%s: (%s)\n", sout.node(), sout.chunk());
                    }
                    else {
                        System.out.println(s.node());
                    }
                })
                .reduce((a, b) -> b)
                .map( NodeOutput::state)
                .orElseThrow();

        log.info( "result: {}", state.lastMessage()
                                    .map(AssistantMessage.class::cast)
                                    .map(AssistantMessage::getText)
                                    .orElseThrow() );
    }
}