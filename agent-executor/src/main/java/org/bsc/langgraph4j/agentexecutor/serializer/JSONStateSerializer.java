package org.bsc.langgraph4j.agentexecutor.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import org.bsc.langgraph4j.agentexecutor.*;
import org.bsc.langgraph4j.serializer.Serializer;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


class IntermediateStepDeserializer extends  JsonDeserializer<IntermediateStep> {

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

class ToolExecutionRequestDeserializer extends  JsonDeserializer<ToolExecutionRequest> {

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

class AgentActionDeserializer extends  JsonDeserializer<AgentAction> {

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

class AgentFinishDeserializer extends  JsonDeserializer<AgentFinish> {

    @Override
    public AgentFinish deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JacksonException {
        JsonNode node = parser.getCodec().readTree(parser);
        var returnValuesNode = node.get("returnValues");
        var returnValues = ctx.readValue(returnValuesNode.traverse(parser.getCodec()), Map.class);
        var log = node.get("log").asText();
        return new AgentFinish(returnValues, log);
    }
}

class AgentOutcomeDeserializer extends  JsonDeserializer<AgentOutcome> {

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

class StateDeserializer extends JsonDeserializer<AgentExecutor.State> {

    @Override
    public AgentExecutor.State deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JsonProcessingException {
        JsonNode node = parser.getCodec().readTree(parser);

        Map<String,Object> data = new HashMap<>();

        data.put( "input", node.get("input").asText() );

        var intermediateStepsNode = node.get("intermediate_steps");

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

        var agentOutcomeNode = node.get("agent_outcome");
        var agentOutcome = ctx.readValue(agentOutcomeNode.traverse(parser.getCodec()), AgentOutcome.class);

        data.put("agent_outcome", agentOutcome);

        return new AgentExecutor.State( data );
    }
}

public class JSONStateSerializer implements Serializer<Map<String,Object>> {

    final ObjectMapper objectMapper;

    public static JSONStateSerializer of( ObjectMapper objectMapper ) {
        return new JSONStateSerializer(objectMapper);
    }

    private JSONStateSerializer(ObjectMapper objectMapper) {
        Objects.requireNonNull(objectMapper, "objectMapper cannot be null");
        this.objectMapper = objectMapper;

        var module = new SimpleModule();
        module.addDeserializer(AgentExecutor.State.class, new StateDeserializer());
        module.addDeserializer(AgentOutcome.class, new AgentOutcomeDeserializer());
        module.addDeserializer(AgentAction.class, new AgentActionDeserializer());
        module.addDeserializer(AgentFinish.class, new AgentFinishDeserializer());
        module.addDeserializer(ToolExecutionRequest.class, new ToolExecutionRequestDeserializer());
        module.addDeserializer(IntermediateStep.class, new IntermediateStepDeserializer());

        objectMapper.registerModule(module);
    }

    @Override
    public String mimeType() {
        return "application/json";
    }

    @Override
    public void write(Map<String,Object> object, ObjectOutput out) throws IOException {
        var state = new AgentExecutor.State( object );
        var json = objectMapper.writeValueAsString(state);
        out.writeUTF(json);
    }

    @Override
    public Map<String,Object> read(ObjectInput in) throws IOException, ClassNotFoundException {
        var json = in.readUTF();
        System.out.println( json );
        return objectMapper.readValue(json, AgentExecutor.State.class).data();
    }

}
