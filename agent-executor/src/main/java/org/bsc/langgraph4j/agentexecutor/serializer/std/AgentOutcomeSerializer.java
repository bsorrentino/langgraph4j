package org.bsc.langgraph4j.agentexecutor.serializer.std;

import org.bsc.langgraph4j.agentexecutor.AgentAction;
import org.bsc.langgraph4j.agentexecutor.AgentFinish;
import org.bsc.langgraph4j.agentexecutor.AgentOutcome;
import org.bsc.langgraph4j.serializer.std.BaseSerializer;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class AgentOutcomeSerializer extends BaseSerializer<AgentOutcome> {
    @Override
    public void write(AgentOutcome object, ObjectOutput out) throws IOException {
        out.writeObject(object.action());
        out.writeObject(object.finish());
    }

    @Override
    public AgentOutcome read(ObjectInput in) throws IOException, ClassNotFoundException {
        AgentAction action = (AgentAction) in.readObject();
        AgentFinish finish = (AgentFinish) in.readObject();
        return new AgentOutcome(action, finish);
    }
}
