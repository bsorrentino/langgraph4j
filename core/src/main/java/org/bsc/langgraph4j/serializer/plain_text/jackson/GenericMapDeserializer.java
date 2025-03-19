package org.bsc.langgraph4j.serializer.plain_text.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.bsc.langgraph4j.serializer.plain_text.jackson.TypeMapper.TYPE_PROPERTY;

class GenericMapDeserializer extends StdDeserializer<Map<String, Object>> {

    final TypeMapper typeMapper;

    public GenericMapDeserializer( TypeMapper mapper ) {
        super(Map.class);
        this.typeMapper = mapper;
    }

    @Override
    public Map<String, Object> deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        var mapper = (ObjectMapper) p.getCodec();
        ObjectNode node = mapper.readTree(p);

        Map<String, Object> result = new HashMap<>();

        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();

        while (fields.hasNext()) {
            var entry = fields.next();

            String key = entry.getKey();
            JsonNode valueNode = entry.getValue();

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

            result.put(key, value);
        }

        return result;
    }
}
