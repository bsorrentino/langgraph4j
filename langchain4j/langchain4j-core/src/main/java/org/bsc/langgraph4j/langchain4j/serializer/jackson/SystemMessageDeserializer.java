package org.bsc.langgraph4j.langchain4j.serializer.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import dev.langchain4j.data.message.SystemMessage;

import java.io.IOException;

public class SystemMessageDeserializer extends StdDeserializer<SystemMessage> {

    protected SystemMessageDeserializer() {
        super(SystemMessage.class);
    }

    @Override
    public SystemMessage deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return deserialize(p.getCodec().readTree(p));
    }

    protected SystemMessage deserialize(JsonNode node ) throws IOException {

        //var text = node.get("contents").iterator().next().get("text").asText();
        var text = node.get("text").asText();

        return SystemMessage.from( text );
    }
}

