package org.bsc.langgraph4j.langchain4j.serializer.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import dev.langchain4j.agent.tool.ToolExecutionRequest;

import java.io.IOException;

public class ToolExecutionRequestSerializer extends StdSerializer<ToolExecutionRequest> {

    public ToolExecutionRequestSerializer() {
        super(ToolExecutionRequest.class);
    }

    @Override
    public void serialize(ToolExecutionRequest msg, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        if( msg.id() == null )
            gen.writeNullField( "id");
        else
            gen.writeStringField("id", msg.id());
        gen.writeStringField("name", msg.name());
        gen.writeStringField("arguments", msg.arguments());
        gen.writeEndObject();
    }
}

