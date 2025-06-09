package org.bsc.langgraph4j.langchain4j.serializer.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import dev.langchain4j.data.message.UserMessage;

import java.io.IOException;

public class UserMessageSerializer extends StdSerializer<UserMessage> {

    public UserMessageSerializer() {
        super(UserMessage.class);
    }

    @Override
    public void serialize(UserMessage msg, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("@type", msg.type().name());
        if( msg.hasSingleText() )
            gen.writeStringField("text", msg.singleText());
        else
            gen.writeObjectField("contents", msg.contents());
        gen.writeEndObject();
    }
}
