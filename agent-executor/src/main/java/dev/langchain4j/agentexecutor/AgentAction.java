package dev.langchain4j.agentexecutor;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

@Value
@Accessors( fluent = true)
public class AgentAction  {
    public static final Serializer SERIALIZER = new Serializer();
    @NonNull
    ToolExecutionRequest toolExecutionRequest;
    String log;

    public static class Serializer implements org.bsc.langgraph4j.serializer.Serializer<AgentAction> {

        private Serializer() {}

        @Override
        public void write(AgentAction action, ObjectOutput out) throws IOException {
            ToolExecutionRequest ter =  action.toolExecutionRequest();
            out.writeUTF( ter.id() );
            out.writeUTF( ter.name() );
            out.writeUTF( ter.arguments() );
            out.writeUTF( action.log() );

        }

        @Override
        public AgentAction read(ObjectInput in) throws IOException, ClassNotFoundException {
            ToolExecutionRequest ter = ToolExecutionRequest.builder()
                    .id(in.readUTF())
                    .name(in.readUTF())
                    .arguments(in.readUTF())
                    .build();

            return  new AgentAction(  ter, in.readUTF() );

        }
    }
}