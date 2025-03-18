package org.bsc.langgraph4j.langchain4j.serializer.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;

import java.io.IOException;
import java.util.Objects;

class ChatMessageDeserializer extends StdDeserializer<ChatMessage> {
    final SystemMessageDeserializer system = new SystemMessageDeserializer();
    final UserMessageDeserializer user = new UserMessageDeserializer();
    final AiMessageDeserializer ai = new AiMessageDeserializer();

    protected ChatMessageDeserializer() {
        super(ChatMessage.class);
    }

    @Override
    public ChatMessage deserialize(JsonParser p, DeserializationContext ctx) throws IOException {

        JsonNode node = p.getCodec().readTree(p);
        var type = node.get("@type").asText();

        if(Objects.equals( type, ChatMessageType.SYSTEM.name())) {
            return system.deserialize(node);
        }
        if(Objects.equals( type, ChatMessageType.USER.name())) {
            return user.deserialize(node);
        }
        if(Objects.equals( type, ChatMessageType.AI.name())) {
            return ai.deserialize(node);
        }
        throw new IllegalArgumentException("Unknown type: " + type);
    }
}
