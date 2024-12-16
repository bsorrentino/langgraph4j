package org.bsc.assemblyai;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import org.bsc.langgraph4j.langchain4j.serializer.std.AiMessageSerializer;
import org.bsc.langgraph4j.langchain4j.serializer.std.UserMessageSerializer;
import org.bsc.langgraph4j.serializer.StateSerializer;
import org.bsc.langgraph4j.serializer.std.ObjectStreamStateSerializer;
import org.bsc.langgraph4j.state.AgentState;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

/**
 * Main application class for a Mind Map application built using Spring Boot.
 * This class serves as the entry point to the Spring Boot application.
 */
@SpringBootApplication
public class MindmapApplication {

	/**
	 * The main method of the application. Called to start the application context.
	 * @param args Command line arguments passed to the application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(MindmapApplication.class, args);
	}

}
