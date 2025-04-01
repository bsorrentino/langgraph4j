package org.bsc.langgraph4j.serializer.plain_text.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.*;

import static org.bsc.langgraph4j.serializer.plain_text.jackson.TypeMapper.TYPE_PROPERTY;

class GenericListDeserializer extends StdDeserializer<List<Object>> {

    final TypeMapper typeMapper;

    public GenericListDeserializer(TypeMapper typeMapper) {
        super(List.class);
        this.typeMapper = Objects.requireNonNull( typeMapper, "typeMapper cannot be null");
    }

    @Override
    public List<Object> deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        var mapper = (ObjectMapper) p.getCodec();
        ArrayNode node = mapper.readTree(p);

        List<Object> result = new LinkedList<>();

        for (JsonNode valueNode : node) {
            // Example: Detect type based on field name or value structure
            Object value;
            if (valueNode.isObject()) {
                if (valueNode.has(TYPE_PROPERTY)) {
                    var type = valueNode.get(TYPE_PROPERTY).asText();
                    // Deserialize to a specific class
                    var ref = typeMapper.getReference(type)
                            .orElseThrow( () -> new IllegalStateException("Type not found: " + type) );
                    value =
                            mapper.treeToValue(valueNode, ref);
                } else {
                    value = mapper.treeToValue(valueNode, Object.class);
                }
            } else if (valueNode.isInt()) {
                value = valueNode.intValue();
            } else if (valueNode.isTextual()) {
                value = valueNode.textValue();
            }
            else if (valueNode.isBoolean()) {
                value = valueNode.booleanValue();
            }
            else if (valueNode.isArray() ) {
                value = mapper.treeToValue(valueNode, List.class);
            }
            else {
                // Fallback generic deserialization
                // value = mapper.treeToValue(valueNode, Object.class);
                throw new IllegalStateException("Value type not supported: " + valueNode.getNodeType() );
            }

            result.add(value);
        }

        return result;
    }
}

