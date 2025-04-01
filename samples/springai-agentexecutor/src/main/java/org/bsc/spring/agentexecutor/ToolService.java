package org.bsc.spring.agentexecutor;

import org.bsc.spring.agentexecutor.function.AgentFunctionCallbackWrapper;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.model.function.*;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Service class responsible for managing tools and their callbacks.
 */
@Service
public class ToolService {

    private final List<FunctionCallback> agentFunctionCallbackWrappers;
    private final ApplicationContext applicationContext;

    /**
     * Constructs a new instance of ToolService with the given ApplicationContext.
     *
     * @param applicationContext The application context to retrieve function callback wrappers.
     */
    public ToolService(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        var AgentFunctionCallbackMap = applicationContext.getBeansOfType(AgentFunctionCallbackWrapper.class);
        agentFunctionCallbackWrappers = AgentFunctionCallbackMap.values().stream()
                .map(FunctionCallback.class::cast)
                .toList();
    }

    /**
     * Retrieves all registered function callback wrappers.
     *
     * @return A list of function callback wrappers.
     */
    public List<FunctionCallback> agentFunctionsCallback() {
        return agentFunctionCallbackWrappers;
    }

    /**
     * Retrieves a specific function callback wrapper by name.
     *
     * @param name The name of the function callback to retrieve.
     * @return An optional containing the function callback wrapper, or an empty optional if not found.
     */
    @SuppressWarnings("unchecked")
    public <I,O> Optional<AgentFunctionCallbackWrapper<I,O>> agentFunction( String name ) {
        Objects.requireNonNull( name, "name cannot be null" );
        return ( applicationContext.containsBean( name ) )
                ? Optional.of( applicationContext.getBean( name, AgentFunctionCallbackWrapper.class ) )
                : Optional.empty();
    }

    /**
     * Retrieves the result of a function based on the given tool response.
     *
     * @param response The tool response message.
     * @return An optional containing the function result, or an empty optional if no function is found.
     */
    @SuppressWarnings("unchecked")
    public <O> Optional<O> getFunctionResult(ToolResponseMessage.ToolResponse response) {
        Objects.requireNonNull( response, "response cannot be null" );
        return agentFunction( response.name() )
                .map( functionCallback -> (O)functionCallback.convertResponse(response) );
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

        String functionName = toolCall.name();
        String functionArguments = toolCall.arguments();

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
