package org.bsc.langgraph4j.langchain4j.serializer.jackson;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import dev.langchain4j.agent.tool.ToolExecutionRequest;

import java.io.IOException;

public class ToolExecutionRequestDeserializer extends StdDeserializer<ToolExecutionRequest> {

    protected ToolExecutionRequestDeserializer() {
        super(ToolExecutionRequest.class);
    }

    /**
     * Deserializes a JSON representation of a ToolExecutionRequest.
     *
     * @param parser the JsonParser used to read the JSON data
     * @param ctx the DeserializationContext that can be used to access additional information during deserialization
     * @return a ToolExecutionRequest object populated with data from the JSON
     * @throws IOException if there is an issue reading the JSON data
     * @throws JacksonException if there is a problem with Jackson processing
     */
    @Override
    public ToolExecutionRequest deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JacksonException {
        return deserialize( parser.getCodec().readTree(parser) );
    }

    protected ToolExecutionRequest deserialize(JsonNode node) throws IOException, JacksonException {
        var idNode = node.get("id");
        return dev.langchain4j.agent.tool.ToolExecutionRequest.builder()
                .id( idNode.isNull() ? null : idNode.asText() )
                .name(node.get("name").asText())
                .arguments(node.get("arguments").asText())
                .build();
    }


}
