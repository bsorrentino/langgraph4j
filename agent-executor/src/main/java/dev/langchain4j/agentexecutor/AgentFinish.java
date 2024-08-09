package dev.langchain4j.agentexecutor;

import lombok.Value;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Map;
import java.util.Objects;

@Value
@Accessors( fluent = true)
public class AgentFinish {
    public static final Serializer SERIALIZER = new Serializer();

    Map<String, Object> returnValues;
    String log;

    public static class Serializer implements org.bsc.langgraph4j.serializer.Serializer<AgentFinish> {

        private Serializer() {
        }

        @Override
        public void write(AgentFinish object, ObjectOutput out) throws IOException {
            out.writeObject(object.returnValues);
            out.writeUTF(object.log);
        }

        @Override
        public AgentFinish read(ObjectInput in) throws IOException, ClassNotFoundException {
            return new AgentFinish((Map<String, Object>) in.readObject(), in.readUTF());
        }
    }
}