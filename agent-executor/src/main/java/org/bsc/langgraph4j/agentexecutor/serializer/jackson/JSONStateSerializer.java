package org.bsc.langgraph4j.agentexecutor.serializer.jackson;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.module.SimpleModule;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import org.bsc.langgraph4j.agentexecutor.AgentExecutor;
import org.bsc.langgraph4j.serializer.plain_text.jackson.JacksonStateSerializer;

import java.io.*;

/**
 * JSONStateSerializer is a class that extends JacksonStateSerializer for serializing and deserializing
 * the state of an AgentExecutor using JSON format.
 */
public class JSONStateSerializer extends JacksonStateSerializer<AgentExecutor.State> {

    /**
     * Constructs a new JSONStateSerializer and registers custom deserializers for various agent-related classes.
     */
    public JSONStateSerializer() {
        super( AgentExecutor.State::new );
        objectMapper.registerModule( new SimpleModule()
                .addDeserializer(ToolExecutionRequest.class, new ToolExecutionRequestDeserializer())
        );
    }

    /**
     * Serializes the given AgentExecutor.State object to an output stream in JSON format.
     *
     * @param object the AgentExecutor.State object to serialize
     * @param out the output stream to write the serialized object to
     * @throws IOException if an I/O error occurs during serialization
     */
    @Override
    public void write(AgentExecutor.State object, ObjectOutput out) throws IOException {
        var json = objectMapper.writeValueAsString(object);
        out.writeUTF(json);
    }

    /**
     * Deserializes an AgentExecutor.State object from an input stream in JSON format.
     *
     * @param in the input stream to read the serialized object from
     * @return the deserialized AgentExecutor.State object
     * @throws IOException if an I/O error occurs during deserialization
     * @throws ClassNotFoundException if the class of a serialized object cannot be found
     */
    @Override
    public AgentExecutor.State read(ObjectInput in) throws IOException, ClassNotFoundException {
        var json = in.readUTF();
        return objectMapper.readValue(json, AgentExecutor.State.class);
    }

}

/**
        * Deserializer for the ToolExecutionRequest class.
        * This class extends JsonDeserializer to provide custom deserialization logic for ToolExecutionRequest objects.
        */
class ToolExecutionRequestDeserializer extends JsonDeserializer<dev.langchain4j.agent.tool.ToolExecutionRequest> {

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
        JsonNode node = parser.getCodec().readTree(parser);
        return dev.langchain4j.agent.tool.ToolExecutionRequest.builder()
                .id(node.get("id").asText())
                .name(node.get("name").asText())
                .arguments(node.get("arguments").asText())
                .build();
    }
}

