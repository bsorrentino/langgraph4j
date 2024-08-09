package dev.langchain4j.agentexecutor;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class IntermediateStep implements Externalizable {
    private AgentAction action;
    private String observation;

    AgentAction action() { return action; }
    String observation() { return observation; }

    public IntermediateStep() {}
    public IntermediateStep( AgentAction action, String observation) {
        this.action = action;
        this.observation = observation;
    }
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        AgentAction.SERIALIZER.write(action, out);
        out.writeUTF(observation);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        action = AgentAction.SERIALIZER.read(in);
        observation = in.readUTF();
    }
}
