package org.bsc.spring.agentexecutor;

import lombok.Getter;
import lombok.NonNull;
import org.bsc.spring.agentexecutor.function.AgentFunctionCallbackWrapper;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.model.AbstractToolCallSupport;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.function.*;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Service
public class ToolService {

    private final List<FunctionCallback> agentFunctionCallbackWrappers;
    private final ApplicationContext applicationContext;

    public ToolService(ApplicationContext applicationContext) {

        this.applicationContext = applicationContext;
        var AgentFunctionCallbackMap = applicationContext.getBeansOfType( AgentFunctionCallbackWrapper.class );

        agentFunctionCallbackWrappers = AgentFunctionCallbackMap.values().stream()
                .map(FunctionCallback.class::cast)
                .toList();
    }

    public List<FunctionCallback> agentFunctionsCallback() {
        return agentFunctionCallbackWrappers;
    }

    public <I,O> Optional<AgentFunctionCallbackWrapper<I,O>> agentFunction(@NonNull String name) {
        return ( applicationContext.containsBean( name ) ) ?
                Optional.of( applicationContext.getBean( name, AgentFunctionCallbackWrapper.class ) ) :
                Optional.empty();
    }

    public <O> Optional<O> getFunctionResult(@NonNull ToolResponseMessage.ToolResponse response) {
        return agentFunction( response.name() )
                .map( functionCallback -> (O)functionCallback.convertResponse(response) );
    }

    public ToolResponseMessage buildToolResponseMessage( @NonNull  ToolResponseMessage.ToolResponse response) {
        return new ToolResponseMessage(List.of(response), Map.of());
    }

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

    public CompletableFuture<ToolResponseMessage.ToolResponse> executeFunction(AssistantMessage.ToolCall toolCall) {
        return executeFunction( toolCall, Map.of() );
    }

}
