package org.bsc.langgraph4j.langchain4j.serializer.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;

import java.io.IOException;

public class ChatMessageSerializer extends StdSerializer<ChatMessage> {
    final SystemMessageSerializer system = new SystemMessageSerializer();
    final UserMessageSerializer user = new UserMessageSerializer();
    final AiMessageSerializer ai = new AiMessageSerializer();

    public ChatMessageSerializer() {
        super(ChatMessage.class);
    }

    @Override
    public void serialize(ChatMessage msg, JsonGenerator gen, SerializerProvider provider) throws IOException {
        switch( msg.type() ) {
            case SYSTEM -> system.serialize( (SystemMessage) msg, gen, provider);
            case USER -> user.serialize( (UserMessage) msg, gen, provider);
            case AI -> ai.serialize( (AiMessage) msg, gen, provider);
            default -> throw new IllegalArgumentException("Unknown type: " + msg.type());
        }
    }
}
