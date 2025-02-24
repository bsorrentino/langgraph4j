package org.bsc.langgraph4j.agentexecutor.serializer.std;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.ChatMessage;
import org.bsc.langgraph4j.agentexecutor.*;
import org.bsc.langgraph4j.langchain4j.serializer.std.ToolExecutionRequestSerializer;
import org.bsc.langgraph4j.serializer.std.ObjectStreamStateSerializer;
import org.bsc.langgraph4j.langchain4j.serializer.std.ChatMesssageSerializer;


/**
 * The STDStateSerializer class is responsible for serializing the state of the AgentExecutor.
 * It extends the ObjectStreamStateSerializer with a specific type of AgentExecutor.State.
 */
public class STDStateSerializer extends ObjectStreamStateSerializer<AgentExecutor.State> {

    /**
     * Constructs a new instance of STDStateSerializer.
     * It initializes the serializer by registering various classes with their corresponding serializers.
     */
    public STDStateSerializer() {
        super(AgentExecutor.State::new);

        mapper().register(ToolExecutionRequest.class, new ToolExecutionRequestSerializer());
        mapper().register(ChatMessage.class, new ChatMesssageSerializer());
    }
}

