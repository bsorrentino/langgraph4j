package org.bsc.spring.agentexecutor;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static java.util.Optional.ofNullable;

/**
 * Service class responsible for managing tools and their callbacks.
 */
@Service
public class ToolService {

    private final List<ToolCallback> agentFunctionCallbackWrappers;

    /**
     * Constructs a new instance of ToolService with the given ApplicationContext.
     *
     * @param applicationContext The application context to retrieve function callback wrappers.
     */
    public ToolService(ApplicationContext applicationContext) {

        agentFunctionCallbackWrappers = applicationContext.getBeansOfType(ToolCallback.class)
                .values()
                .stream()
                .map(ToolCallback.class::cast)
                .toList();
    }

    /**
     * Retrieves all registered function callback wrappers.
     *
     * @return A list of function callback wrappers.
     */
    public List<ToolCallback> agentFunctionsCallback() {
        return agentFunctionCallbackWrappers;
    }

    /**
     * Retrieves a specific function callback wrapper by name.
     *
     * @param name The name of the function callback to retrieve.
     * @return An optional containing the function callback wrapper, or an empty optional if not found.
     */
    public Optional<ToolCallback> agentFunction( String name ) {
        Objects.requireNonNull( name, "name cannot be null" );

        return this.agentFunctionCallbackWrappers.stream()
                .filter( tool -> Objects.equals(tool.getToolDefinition().name(), name ))
                .findFirst();
    }

    /**
     * Retrieves the result of a function based on the given tool response.
     *
     * @param response The tool response message.
     * @return An optional containing the function result, or an empty optional if no function is found.
     */
    public Optional<String> getFunctionResult(ToolResponseMessage.ToolResponse response) {
        return ofNullable(response.responseData());

//        Objects.requireNonNull( response, "response cannot be null" );
//        return agentFunction( response.name() )
//                .map( callback -> (O)callback.convertResponse(response) );
    }

    /**
     * Builds a tool response message from a given tool response.
     *
     * @param response The tool response to convert into a message.
     * @return A new ToolResponseMessage containing the provided response.
     */
    public ToolResponseMessage buildToolResponseMessage( ToolResponseMessage.ToolResponse response) {
        Objects.requireNonNull( response, "response cannot be null" );
        return new ToolResponseMessage(List.of(response), Map.of());
    }

    /**
     * Executes a function based on the given tool call and context map.
     *
     * @param toolCall The tool call to execute.
     * @param toolContextMap The context map for the tool execution.
     * @return A CompletableFuture containing the result of the executed function.
     */
    public CompletableFuture<ToolResponseMessage.ToolResponse> executeFunction(AssistantMessage.ToolCall toolCall, Map<String,Object> toolContextMap) {
        CompletableFuture<ToolResponseMessage.ToolResponse> result = new CompletableFuture<>();

        var functionName = toolCall.name();
        var functionArguments = toolCall.arguments();

        var functionCallback = agentFunction( functionName );

        if( functionCallback.isPresent() ) {
            var functionResponse = functionCallback.get().call(functionArguments, new ToolContext( toolContextMap ) );
            result.complete( new ToolResponseMessage.ToolResponse(toolCall.id(), functionName, functionResponse) );
        }
        else {
            result.completeExceptionally( new IllegalStateException("No function callback found for function name: " + functionName) );
        }

        return result;
    }

    /**
     * Executes a function based on the given tool call with default context map.
     *
     * @param toolCall The tool call to execute.
     * @return A CompletableFuture containing the result of the executed function.
     */
    public CompletableFuture<ToolResponseMessage.ToolResponse> executeFunction(AssistantMessage.ToolCall toolCall) {
        return executeFunction( toolCall, Map.of() );
    }

}
