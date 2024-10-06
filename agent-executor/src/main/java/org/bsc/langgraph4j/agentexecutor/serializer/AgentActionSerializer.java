package org.bsc.langgraph4j.agentexecutor.serializer;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import org.bsc.langgraph4j.agentexecutor.AgentAction;
import org.bsc.langgraph4j.serializer.Serializer;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class AgentActionSerializer implements Serializer<AgentAction> {

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

