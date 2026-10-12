package org.bsc.langgraph4j.spring.ai.tool;


import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import static java.lang.String.format;

public class TestTools {

    @Tool( description="tool for test AI agent executor")
    String execTest(@ToolParam( description = "test message") String message, ToolContext context) {
        return format( "test tool ('%s') executed with result 'OK'", message);
    }

    @Tool( description="return current number of system thread allocated by application")
    int threadCount( ToolContext context ) {
        return Thread.getAllStackTraces().size();
    }

}
