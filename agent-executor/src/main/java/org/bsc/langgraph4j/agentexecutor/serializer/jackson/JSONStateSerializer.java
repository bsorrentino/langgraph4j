package org.bsc.langgraph4j.agentexecutor.serializer.jackson;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.module.SimpleModule;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import org.bsc.langgraph4j.agentexecutor.*;
import org.bsc.langgraph4j.agentexecutor.state.AgentAction;
import org.bsc.langgraph4j.agentexecutor.state.AgentFinish;
import org.bsc.langgraph4j.agentexecutor.state.AgentOutcome;
import org.bsc.langgraph4j.agentexecutor.state.IntermediateStep;
import org.bsc.langgraph4j.serializer.plain_text.jackson.JacksonStateSerializer;

import java.io.*;
import java.util.*;

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
            .addDeserializer(AgentOutcome.class, new AgentOutcomeDeserializer())
            .addDeserializer(AgentAction.class, new AgentActionDeserializer())
            .addDeserializer(AgentFinish.class, new AgentFinishDeserializer())
            .addDeserializer(ToolExecutionRequest.class, new ToolExecutionRequestDeserializer())
            .addDeserializer(IntermediateStep.class, new IntermediateStepDeserializer())
            .addDeserializer(AgentExecutor.State.class, new StateDeserializer())
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
 * Deserializer for the IntermediateStep class, extending JsonDeserializer.
 * This class is responsible for converting JSON data into an IntermediateStep object.
 */
class IntermediateStepDeserializer extends JsonDeserializer<IntermediateStep> {

    /**
     * Deserializes JSON data into an IntermediateStep object.
     *
     * @param parser the JsonParser used to read the JSON data
     * @param ctx the DeserializationContext for reading values
     * @return an IntermediateStep object created from the JSON data
     * @throws IOException if an input or output exception occurs
     * @throws JacksonException if a Jackson-specific exception occurs
     */
    @Override
    public IntermediateStep deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JacksonException {
        JsonNode node = parser.getCodec().readTree(parser);
        var actionNode = node.get("action");
        var action = ( actionNode != null && !actionNode.isNull()) ?
                ctx.readValue(actionNode.traverse(parser.getCodec()), AgentAction.class) :
                null;

        return new IntermediateStep(action, node.get("observation").asText());
    }
}


/**
 * Deserializer for the ToolExecutionRequest class.
 * This class extends JsonDeserializer to provide custom deserialization logic for ToolExecutionRequest objects.
 */
class ToolExecutionRequestDeserializer extends JsonDeserializer<ToolExecutionRequest> {

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
        return ToolExecutionRequest.builder()
                .id(node.get("id").asText())
                .name(node.get("name").asText())
                .arguments(node.get("arguments").asText())
                .build();
    }
}


/**
 * Deserializes JSON data into an AgentAction object.
 */
class AgentActionDeserializer extends JsonDeserializer<AgentAction> {

    /**
     * Deserializes the JSON representation of an AgentAction.
     *
     * @param parser the JsonParser used to read the JSON data
     * @param ctx the DeserializationContext for reading values
     * @return the deserialized AgentAction object
     * @throws IOException if an I/O error occurs during deserialization
     * @throws JacksonException if a Jackson-specific error occurs
     */
    @Override
    public AgentAction deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JacksonException {
        JsonNode node = parser.getCodec().readTree(parser);

        var toolExecutionRequestNode = node.get("toolExecutionRequest");
        var toolExecutionRequest = ctx.readValue(toolExecutionRequestNode.traverse(parser.getCodec()), ToolExecutionRequest.class);

        return new AgentAction(
                toolExecutionRequest,
                node.get("log").asText()
        );
    }
}


/**
 * Deserializer for the AgentFinish class, extending JsonDeserializer.
 * This class is responsible for converting JSON data into an AgentFinish object.
 */
class AgentFinishDeserializer extends JsonDeserializer<AgentFinish> {

    /**
     * Deserializes JSON data into an AgentFinish object.
     *
     * @param parser the JsonParser used to read the JSON data
     * @param ctx the DeserializationContext for the deserialization process
     * @return an instance of AgentFinish populated with data from the JSON
     * @throws IOException if an input or output exception occurs
     * @throws JacksonException if a Jackson-specific exception occurs
     */
    @Override
    public AgentFinish deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JacksonException {
        JsonNode node = parser.getCodec().readTree(parser);
        var log = node.get("log").asText();

        var returnValuesNode = node.get("returnValues");

        if (returnValuesNode == null || returnValuesNode.isNull()) {
            return new AgentFinish(null, log);
        }

        if (returnValuesNode.isObject()) { // GUARD
            Map<String, Object> returnValues = new HashMap<>();
            for (var entries = returnValuesNode.fields(); entries.hasNext(); ) {
                var entry = entries.next();
                returnValues.put(entry.getKey(), entry.getValue());
            }
            return new AgentFinish(returnValues, log);
        }
        throw new IOException("Unsupported return values Node: " + returnValuesNode.getNodeType());
    }
}


/**
 * Deserializes JSON data into an instance of {@link AgentOutcome}.
 * This class extends {@link JsonDeserializer} to provide custom deserialization logic.
 */
class AgentOutcomeDeserializer extends JsonDeserializer<AgentOutcome> {

    /**
     * Deserializes the JSON content into an {@link AgentOutcome} object.
     *
     * @param parser the JSON parser used to read the JSON content
     * @param ctx the deserialization context
     * @return an instance of {@link AgentOutcome} populated with the deserialized data
     * @throws IOException if an I/O error occurs during deserialization
     * @throws JacksonException if a Jackson-specific error occurs during deserialization
     */
    @Override
    public AgentOutcome deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JacksonException {
        JsonNode node = parser.getCodec().readTree(parser);

        var actionNode = node.get("action");
        var action = ( actionNode != null && !actionNode.isNull()) ?
                ctx.readValue(actionNode.traverse(parser.getCodec()), AgentAction.class) :
                null;

        var finishNode = node.get("finish");
        var finish = ( finishNode != null && !finishNode.isNull()) ?
                ctx.readValue(finishNode.traverse(parser.getCodec()), AgentFinish.class) :
                null;

        return new AgentOutcome( action, finish );
    }
}


/**
 * The StateDeserializer class is responsible for deserializing JSON data 
 * into an instance of AgentExecutor.State. It extends the JsonDeserializer 
 * class provided by the Jackson library.
 */
class StateDeserializer extends JsonDeserializer<AgentExecutor.State> {

    /**
     * Deserializes JSON data from the given parser into an 
     * AgentExecutor.State object.
     *
     * @param parser the JsonParser used to read the JSON data
     * @param ctx the DeserializationContext used for deserialization
     * @return an instance of AgentExecutor.State populated with the deserialized data
     * @throws IOException if an I/O error occurs during deserialization
     * @throws JsonProcessingException if there is a problem processing the JSON
     */
    @Override
    public AgentExecutor.State deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JsonProcessingException {
        JsonNode node = parser.getCodec().readTree(parser);

        Map<String,Object> data = new HashMap<>();

        var dataNode = node.has("data") ? node.get("data") : node;
        data.put( "input", dataNode.get("input").asText() );

        var intermediateStepsNode = dataNode.get("intermediate_steps");

        if( intermediateStepsNode == null || intermediateStepsNode.isNull() ) { // GUARD
            throw new IOException("intermediate_steps must not be null!");
        }
        if(  !intermediateStepsNode.isArray()) { // GUARD
            throw new IOException("intermediate_steps must be an array!");
        }
        var intermediateStepList = new ArrayList<IntermediateStep>();
        for (JsonNode intermediateStepNode : intermediateStepsNode) {

            var intermediateStep = ctx.readValue(intermediateStepNode.traverse(parser.getCodec()), IntermediateStep.class);
            intermediateStepList.add(intermediateStep); // intermediateStepList
        }
        data.put("intermediate_steps", intermediateStepList);

        var agentOutcomeNode = dataNode.get("agent_outcome");
        if( agentOutcomeNode != null && !agentOutcomeNode.isNull() ) { // GUARD
            var agentOutcome = ctx.readValue(agentOutcomeNode.traverse(parser.getCodec()), AgentOutcome.class);
            data.put("agent_outcome", agentOutcome);
        }
        return new AgentExecutor.State( data );
    }
}
