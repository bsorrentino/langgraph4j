package org.bsc.langgraph4j;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import dev.langchain4j.agent.tool.*;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.service.tool.ToolExecutor;
import org.bsc.langgraph4j.langchain4j.tool.ToolNode;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ToolNodeTest {

    static class TestTool {

        @Tool("tool for test AI agent executor")
        String execTest(@P("test message") String message) {

            return format( "test tool executed: %s", message);
        }
    }


    @Test
    public void invokeToolNode() {
        Gson gson = new Gson();

        ToolNode.Builder builder = ToolNode.builder();

        builder.specification( new TestTool() );

        ToolSpecification toolSpecification = ToolSpecification.builder()
                .name("getPCName")
                .description("Returns a String - PC name the AI is currently running in. Returns null if station is not running")
                .build();

        ToolExecutor toolExecutor = (toolExecutionRequest, memoryId) -> "bsorrentino";

        builder.specification( toolSpecification, toolExecutor);

        toolSpecification = ToolSpecification.builder()
                .name("specialSumTwoNumbers")
                .parameters(JsonObjectSchema.builder()
                        .addNumberProperty("operand1","Operand 1 for specialK operation" )
                        .addNumberProperty( "operand2", "Operand 2 for specialK operation" )
                        .build())
                .description("Returns a Float - sum of two numbers")
                .build();

        toolExecutor = (toolExecutionRequest, memoryId) -> {
            LinkedTreeMap<String, Object> arguments = null;
            Object arguments1 = gson.fromJson(toolExecutionRequest.arguments(), Map.class);
            if (arguments1 instanceof LinkedTreeMap)
            {
                @SuppressWarnings("unchecked")
                LinkedTreeMap<String, Object> arguments2 = (LinkedTreeMap<String, Object>)arguments1;
                arguments = arguments2;
            }
            float operand1 = Float.parseFloat(arguments.get("operand1").toString());
            float operand2 = Float.parseFloat(arguments.get("operand2").toString());
            float sum = operand1 + operand2;
            return String.valueOf(sum);
        };

        builder.specification(toolSpecification, toolExecutor);

        ToolNode toolNode = builder.build();

        Optional<ToolExecutionResultMessage> result = toolNode.execute(
                ToolExecutionRequest.builder()
                        .name("specialSumTwoNumbers")
                        .arguments("{\"operand1\": 1.0, \"operand2\": 2.0}")
                        .build() );

        assertTrue( result.isPresent() );
        assertEquals("specialSumTwoNumbers", result.get().toolName());
        assertEquals("3.0", result.get().text());

        result = toolNode.execute(
                ToolExecutionRequest.builder()
                        .name("getPCName")
                        .build() );

        assertTrue( result.isPresent() );
        assertEquals("getPCName", result.get().toolName());
        assertEquals("bsorrentino", result.get().text());

        result = toolNode.execute(
                ToolExecutionRequest.builder()
                        .name("execTest")
                        .arguments("{ \"arg0\": \"test succeeded\"}")
                        .build() );

        assertTrue( result.isPresent() );
        assertEquals("execTest", result.get().toolName());
        assertEquals("test tool executed: test succeeded", result.get().text());
    }
}
