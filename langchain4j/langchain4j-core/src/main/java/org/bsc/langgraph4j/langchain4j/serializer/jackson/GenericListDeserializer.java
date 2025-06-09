package org.bsc.langgraph4j.langchain4j.serializer.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import dev.langchain4j.data.message.ChatMessage;

import java.io.IOException;
import java.util.*;

public class GenericListDeserializer extends StdDeserializer<List<Object>> {

    public GenericListDeserializer() {
        super(List.class);
    }

    @Override
    public List<Object> deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        var mapper = (ObjectMapper) p.getCodec();
        ArrayNode node = mapper.readTree(p);

        List<Object> result = new LinkedList<>();

        for (JsonNode valueNode : node) {
            // Example: Detect type based on field name or value structure
            Object value;
            if (valueNode.isObject() && valueNode.has("@type")) {
                // Deserialize to a specific class
                value = mapper.treeToValue(valueNode, ChatMessage.class);
            } else if (valueNode.isInt()) {
                value = valueNode.intValue();
            } else if (valueNode.isTextual()) {
                value = valueNode.textValue();
            } else {
                // Fallback generic deserialization
                value = mapper.treeToValue(valueNode, Object.class);
            }

            result.add(value);
        }

        return result;
    }
}

