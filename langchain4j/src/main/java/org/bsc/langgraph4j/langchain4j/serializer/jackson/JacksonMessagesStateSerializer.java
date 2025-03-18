package org.bsc.langgraph4j.langchain4j.serializer.jackson;

<<<<<<< HEAD
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import dev.langchain4j.data.message.*;
import org.bsc.langgraph4j.serializer.plain_text.jackson.JacksonStateSerializer;
import org.bsc.langgraph4j.serializer.plain_text.jackson.TypeMapper;
=======
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
>>>>>>> 44eb32c (feat: add support for JSON serialization using Jackson for langchain4j Chat Messages)
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AgentStateFactory;

import java.io.IOException;
<<<<<<< HEAD
import java.util.Objects;

import static org.bsc.langgraph4j.serializer.plain_text.jackson.TypeMapper.TYPE_PROPERTY;

public class JacksonMessagesStateSerializer<State extends AgentState>  extends JacksonStateSerializer<State>  {

    interface ChatMessageDeserializer {
        SystemMessageDeserializer system = new SystemMessageDeserializer();
        UserMessageDeserializer user = new UserMessageDeserializer();
        AiMessageDeserializer ai = new AiMessageDeserializer();

        static void registerTo( SimpleModule module ) {
            module
                .addDeserializer(SystemMessage.class, system )
                .addDeserializer(UserMessage.class, user )
                .addDeserializer(AiMessage.class, ai )
                ;
        }

    }

    interface ChatMessageSerializer  {
        SystemMessageSerializer system = new SystemMessageSerializer();
        UserMessageSerializer user = new UserMessageSerializer();
        AiMessageSerializer ai = new AiMessageSerializer();

        static void registerTo( SimpleModule module ) {
            module
                .addSerializer(SystemMessage.class, system)
                .addSerializer(UserMessage.class, user)
                .addSerializer(AiMessage.class, ai)
                ;

        }

    }

    protected JacksonMessagesStateSerializer(AgentStateFactory<State> stateFactory) {
        super(stateFactory);

        var module = new SimpleModule();

        ChatMessageSerializer.registerTo(module);
        ChatMessageDeserializer.registerTo(module);

        typeMapper
                .register(new TypeMapper.Reference<SystemMessage>(ChatMessageType.SYSTEM.name()) {} )
                .register(new TypeMapper.Reference<UserMessage>(ChatMessageType.USER.name()) {} )
                .register(new TypeMapper.Reference<AiMessage>(ChatMessageType.AI.name()) {} )
        ;
=======
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
>>>>>>> 44eb32c (feat: add support for JSON serialization using Jackson for langchain4j Chat Messages)

        objectMapper.registerModule( module );
    }
}





