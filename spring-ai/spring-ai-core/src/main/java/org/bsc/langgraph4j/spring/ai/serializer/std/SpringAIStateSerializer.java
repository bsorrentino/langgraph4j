package org.bsc.langgraph4j.spring.ai.serializer.std;

import org.bsc.langgraph4j.serializer.std.ObjectStreamStateSerializer;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AgentStateFactory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.ToolResponseMessage;

/**
 * This class is responsible for serializing and deserializing the state of an agent executor.
 * It extends {@link ObjectStreamStateSerializer} for handling the serialization of the AgentExecutor.State object.
 */
public class SpringAIStateSerializer<State extends AgentState> extends ObjectStreamStateSerializer<State>  {

    /**
     * Constructor that initializes the serializer with a supplier for creating new AgentExecutor.State instances and registers various serializers for different types.
     */
    public SpringAIStateSerializer(AgentStateFactory<State> stateFactory ) {
        super( stateFactory );

        mapper().register(Message.class, new MessageSerializer());
        mapper().register(AssistantMessage.ToolCall.class, new ToolCallSerializer());
        mapper().register(ToolResponseMessage.ToolResponse.class, new ToolResponseSerializer());

    }


}