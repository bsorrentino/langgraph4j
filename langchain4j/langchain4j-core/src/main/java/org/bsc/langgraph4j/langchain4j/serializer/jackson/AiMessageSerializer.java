package org.bsc.langgraph4j.langchain4j.serializer.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import dev.langchain4j.data.message.AiMessage;

import java.io.IOException;

public class AiMessageSerializer extends StdSerializer<AiMessage> {

    public AiMessageSerializer() {
        super(AiMessage.class);
    }

    @Override
    public void serialize(AiMessage msg, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("@type", msg.type().name());
        gen.writeStringField("text", msg.text());
        gen.writeObjectField("toolExecutionRequests", msg.toolExecutionRequests());
        gen.writeEndObject();
    }
}
