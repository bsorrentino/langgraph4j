package org.bsc.langgraph4j.spring.ai.tool.gemini;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.Objects;

public class TestTools4Gemini {

    @Tool( description="tool for test AI agent executor")
    String execTest(@ToolParam( description = "test message") String message) {
        return "test tool ('%s') executed with result 'OK'".formatted(message);
    }

    @Tool( description="return current number of system thread allocated by application")
    String threadCount() {
        // FIX for GEMINI MODEL
        return Objects.toString(Thread.getAllStackTraces().size());
    }

}
