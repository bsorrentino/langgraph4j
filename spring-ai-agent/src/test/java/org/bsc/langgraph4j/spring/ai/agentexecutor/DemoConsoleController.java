package org.bsc.langgraph4j.spring.ai.agentexecutor;

import org.bsc.langgraph4j.NodeOutput;
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

	private final List<ToolCallback> tools;

	public DemoConsoleController( /* @Qualifier("ollama") */ChatModel chatModel, List<ToolCallback> tools) {

		this.chatModel = chatModel;
		this.tools = tools;
	}

	/**
	 * Executes the command-line interface to demonstrate a Spring Boot application. This
	 * method logs a welcome message, constructs a graph using an agent executor, compiles
	 * it into a workflow, invokes the workflow with a specific input, and then logs the
	 * final result.
	 * @param args Command line arguments (Unused in this context)
	 * @throws Exception If any error occurs during execution
	 */
	@Override
	public void run(String... args) throws Exception {

		log.info("Welcome to the Spring Boot CLI application!");

		var graph = AgentExecutor.builder().chatModel(chatModel).tools(tools).build();

		var workflow = graph.compile();

		var result = workflow.stream(Map.of("messages", new UserMessage("what is the result of 234 + 45")));

		var state = result.stream()
			.peek(s -> System.out.println(s.node()))
			.reduce((a, b) -> b)
			.map(NodeOutput::state)
			.orElseThrow();

		log.info("result: {}",
				state.lastMessage().map(AssistantMessage.class::cast).map(AssistantMessage::getText).orElseThrow());
	}

}