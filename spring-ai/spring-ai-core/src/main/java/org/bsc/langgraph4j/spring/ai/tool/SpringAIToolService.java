package org.bsc.langgraph4j.spring.ai.tool;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.ToolCallback;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Service class responsible for managing tools and their callbacks.
 */
public class SpringAIToolService {

    private final List<ToolCallback> agentFunctions;

    public SpringAIToolService(List<ToolCallback> agentFunctions ) {
        this.agentFunctions = agentFunctions;
    }

    /**
     * Retrieves all registered function callback wrappers.
     *
     * @return A list of function callback wrappers.
     */
    public List<ToolCallback> agentFunctionsCallback() {
        return agentFunctions;
    }

    /**
     * Retrieves a specific function callback wrapper by name.
     *
     * @param name The name of the function callback to retrieve.
     * @return An optional containing the function callback wrapper, or an empty optional if not found.
     */
    public Optional<ToolCallback> agentFunction( String name ) {
        Objects.requireNonNull( name, "name cannot be null" );

        return this.agentFunctions.stream()
                .filter( tool -> Objects.equals(tool.getToolDefinition().name(), name ))
                .findFirst();
    }

    public CompletableFuture<ToolResponseMessage> executeFunctions(List<AssistantMessage.ToolCall> toolCalls, Map<String,Object> toolContextMap) {

        CompletableFuture<ToolResponseMessage> result = new CompletableFuture<>();

        var toolContext =  new ToolContext( toolContextMap );

        var responses = new ArrayList<ToolResponseMessage.ToolResponse>();

        for( var toolCall : toolCalls ) {

            var functionName = toolCall.name();

            var functionCallback = agentFunction( functionName );

            if( functionCallback.isEmpty() ) {
                result.completeExceptionally( new IllegalStateException("No function callback found for function name: " + functionName) );
                return result;
            }

            var functionResponse = functionCallback.get().call(toolCall.arguments(), toolContext );
            var toolResponse = new ToolResponseMessage.ToolResponse(toolCall.id(), functionName, functionResponse);

            responses.add( toolResponse );

        }
        result.complete( new ToolResponseMessage( responses ) );

        return result;
    }

    public CompletableFuture<ToolResponseMessage> executeFunctions(List<AssistantMessage.ToolCall> toolCalls) {
        return executeFunctions( toolCalls, Map.of() );
    }

}
