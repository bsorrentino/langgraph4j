package org.bsc.langgraph4j.agentexecutor.serializer.std;

import org.bsc.langgraph4j.agentexecutor.AgentAction;
import org.bsc.langgraph4j.agentexecutor.IntermediateStep;
import org.bsc.langgraph4j.serializer.std.BaseSerializer;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class IntermediateStepSerializer extends BaseSerializer<IntermediateStep> {
    @Override
    public void write(IntermediateStep object, ObjectOutput out) throws IOException {
        out.writeUTF(object.observation());
        out.writeObject(object.action());
    }

    @Override
    public IntermediateStep read(ObjectInput in) throws IOException, ClassNotFoundException {
        String observation = in.readUTF();
        AgentAction action = (AgentAction)in.readObject();
        return new IntermediateStep(action, observation);
    }
}
