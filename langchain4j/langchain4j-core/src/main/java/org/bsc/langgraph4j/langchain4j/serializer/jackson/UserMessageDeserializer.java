package org.bsc.langgraph4j.langchain4j.serializer.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import dev.langchain4j.data.message.UserMessage;

import java.io.IOException;

public class UserMessageDeserializer extends StdDeserializer<UserMessage> {

    public UserMessageDeserializer() {
        super(UserMessage.class);
    }

    @Override
    public UserMessage deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        return deserialize(p.getCodec().readTree(p));
    }

    protected UserMessage deserialize(JsonNode node) throws IOException {
        var text = node.get("text").asText();

        return UserMessage.from( text );
    }
}
