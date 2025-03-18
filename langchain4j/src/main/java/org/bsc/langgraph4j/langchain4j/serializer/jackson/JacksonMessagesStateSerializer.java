package org.bsc.langgraph4j.langchain4j.serializer.jackson;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.module.SimpleModule;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import org.bsc.langgraph4j.serializer.plain_text.jackson.JacksonStateSerializer;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AgentStateFactory;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;
import java.util.Map;

public class JacksonMessagesStateSerializer<State extends AgentState>  extends JacksonStateSerializer<State>  {

    protected JacksonMessagesStateSerializer(AgentStateFactory<State> stateFactory) {
        super(stateFactory);

        var chatMessageSerializer = new ChatMessageSerializer();
        var chatMessageDeserializer = new ChatMessageDeserializer();

        var module = new SimpleModule();
        module.addDeserializer(ChatMessage.class, chatMessageDeserializer);
        module.addDeserializer(SystemMessage.class, chatMessageDeserializer.system );
        module.addDeserializer(UserMessage.class, chatMessageDeserializer.user );
        module.addDeserializer(AiMessage.class, chatMessageDeserializer.ai );
        module.addSerializer(ChatMessage.class, chatMessageSerializer);
        module.addSerializer(SystemMessage.class, chatMessageSerializer.system);
        module.addSerializer(UserMessage.class, chatMessageSerializer.user);
        module.addSerializer(AiMessage.class, chatMessageSerializer.ai);
        module.addDeserializer(Map.class, new GenericMapDeserializer());
        module.addDeserializer(List.class, new GenericListDeserializer());

        objectMapper.registerModule( module );
    }
}





