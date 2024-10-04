package dev.langchain4j.agentexecutor.serializer;

import dev.langchain4j.agentexecutor.AgentAction;
import dev.langchain4j.agentexecutor.AgentFinish;
import dev.langchain4j.agentexecutor.AgentOutcome;
import org.bsc.langgraph4j.serializer.BaseSerializer;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class AgentOutcomeSerializer extends BaseSerializer<AgentOutcome> {
    @Override
    public void write(AgentOutcome object, ObjectOutput out) throws IOException {
        writeObjectWithSerializer(object.action(), out);
        writeObjectWithSerializer(object.finish(), out);
    }

    @Override
    public AgentOutcome read(ObjectInput in) throws IOException, ClassNotFoundException {
        AgentAction action = readObjectWithSerializer(in);
        AgentFinish finish = readObjectWithSerializer(in);
        return new AgentOutcome(action, finish);
    }
}
