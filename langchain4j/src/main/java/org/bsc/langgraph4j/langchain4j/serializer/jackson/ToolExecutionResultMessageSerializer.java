package org.bsc.langgraph4j.langchain4j.serializer.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import dev.langchain4j.data.message.ToolExecutionResultMessage;

import java.io.IOException;

public class ToolExecutionResultMessageSerializer extends StdSerializer<ToolExecutionResultMessage> {

    public ToolExecutionResultMessageSerializer() {
        super(ToolExecutionResultMessage.class);
    }

    @Override
    public void serialize(ToolExecutionResultMessage msg, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("@type", msg.type().name());
        gen.writeStringField("id", msg.id());
        gen.writeStringField("toolName", msg.toolName());
        gen.writeStringField("text", msg.text());
        gen.writeEndObject();
    }

}