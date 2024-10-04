package dev.langchain4j.agentexecutor.serializer;

import dev.langchain4j.agentexecutor.AgentFinish;
import org.bsc.langgraph4j.serializer.Serializer;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Map;

public class AgentFinishSerializer implements Serializer<AgentFinish> {

    @Override
    public void write(AgentFinish object, ObjectOutput out) throws IOException {
        out.writeObject(object.returnValues());
        out.writeUTF(object.log());
    }

    @Override
    public AgentFinish read(ObjectInput in) throws IOException, ClassNotFoundException {
        Map<String, Object> returnValues = (Map<String, Object>)in.readObject();
        String log = in.readUTF();
        return new AgentFinish(returnValues, log);
    }

}
