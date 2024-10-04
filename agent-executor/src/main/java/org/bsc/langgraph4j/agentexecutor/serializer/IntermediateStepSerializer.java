package dev.langchain4j.agentexecutor.serializer;

import dev.langchain4j.agentexecutor.AgentAction;
import dev.langchain4j.agentexecutor.IntermediateStep;
import org.bsc.langgraph4j.serializer.BaseSerializer;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class IntermediateStepSerializer extends BaseSerializer<IntermediateStep> {
    @Override
    public void write(IntermediateStep object, ObjectOutput out) throws IOException {
        out.writeUTF(object.observation());
        writeObjectWithSerializer(object.action(), out);
    }

    @Override
    public IntermediateStep read(ObjectInput in) throws IOException, ClassNotFoundException {
        String observation = in.readUTF();
        AgentAction action = readObjectWithSerializer(in);
        return new IntermediateStep(action, observation);
    }
}
