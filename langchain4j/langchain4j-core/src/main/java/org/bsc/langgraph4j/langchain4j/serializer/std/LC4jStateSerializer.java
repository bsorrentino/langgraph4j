package org.bsc.langgraph4j.langchain4j.serializer.std;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.ChatMessage;
import org.bsc.langgraph4j.serializer.std.ObjectStreamStateSerializer;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AgentStateFactory;

public class LC4jStateSerializer<State extends AgentState> extends ObjectStreamStateSerializer<State> {

    public LC4jStateSerializer( AgentStateFactory<State> stateFactory ) {
        super(stateFactory);

        mapper().register(ToolExecutionRequest.class, new ToolExecutionRequestSerializer());
        mapper().register(ChatMessage.class, new ChatMesssageSerializer());
    }
}
