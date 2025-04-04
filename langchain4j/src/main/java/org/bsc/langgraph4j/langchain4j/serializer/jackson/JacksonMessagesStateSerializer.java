package org.bsc.langgraph4j.langchain4j.serializer.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import dev.langchain4j.data.message.*;
import org.bsc.langgraph4j.serializer.plain_text.jackson.JacksonStateSerializer;
import org.bsc.langgraph4j.serializer.plain_text.jackson.TypeMapper;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AgentStateFactory;

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

        objectMapper.registerModule( module );
    }
}





