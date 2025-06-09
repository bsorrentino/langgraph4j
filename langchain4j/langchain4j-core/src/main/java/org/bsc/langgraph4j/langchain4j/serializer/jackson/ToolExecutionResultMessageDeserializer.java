package org.bsc.langgraph4j.langchain4j.serializer.jackson;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import dev.langchain4j.data.message.ToolExecutionResultMessage;

import java.io.IOException;

public class ToolExecutionResultMessageDeserializer extends StdDeserializer<ToolExecutionResultMessage> {

    protected ToolExecutionResultMessageDeserializer() {
        super(ToolExecutionResultMessage.class);
    }

    @Override
    public ToolExecutionResultMessage deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JacksonException {
        return deserialize(parser.getCodec().readTree(parser));
    }

    protected ToolExecutionResultMessage deserialize(JsonNode node) throws IOException {
        return new ToolExecutionResultMessage(
                    node.get("id").asText(),
                    node.get("toolName").asText(),
                    node.get("text").asText() );
    }

}