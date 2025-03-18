package org.bsc.langgraph4j.langchain4j.serializer.jackson;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import dev.langchain4j.data.message.AiMessage;

import java.io.IOException;

public class AiMessageDeserializer extends StdDeserializer<AiMessage> {

    final ToolExecutionRequestDeserializer toolExecutionRequest = new ToolExecutionRequestDeserializer();

    protected AiMessageDeserializer() {
        super(AiMessage.class);
    }

    @Override
    public AiMessage deserialize(JsonParser jsonParser, DeserializationContext ctx) throws IOException, JacksonException {
        return deserialize(jsonParser.getCodec().readTree(jsonParser));
    }

    protected AiMessage deserialize(JsonNode node ) throws IOException, JacksonException {

        var text = node.get("text").asText();
        return AiMessage.from(text);

    }

}
