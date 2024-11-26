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

@SpringBootApplication
public class MindmapApplication {

	public static void main(String[] args) {
		SpringApplication.run(MindmapApplication.class, args);
	}

}
