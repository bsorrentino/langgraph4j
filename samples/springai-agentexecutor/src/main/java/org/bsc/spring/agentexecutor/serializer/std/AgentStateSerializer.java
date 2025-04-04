package org.bsc.spring.agentexecutor.serializer.std;

import org.bsc.langgraph4j.serializer.std.ObjectStreamStateSerializer;
import org.bsc.langgraph4j.spring.ai.serializer.std.SpringAIStateSerializer;
import org.bsc.spring.agentexecutor.AgentExecutor;

/**
 * This class is responsible for serializing and deserializing the state of an agent executor.
 * It extends {@link ObjectStreamStateSerializer} for handling the serialization of the AgentExecutor.State object.
 */
public class AgentStateSerializer extends SpringAIStateSerializer<AgentExecutor.State> {
    /**
     * Constructor that initializes the serializer with a supplier for creating new AgentExecutor.State instances and registers various serializers for different types.
     */
    public AgentStateSerializer() {
        super(AgentExecutor.State::new);
    }


}