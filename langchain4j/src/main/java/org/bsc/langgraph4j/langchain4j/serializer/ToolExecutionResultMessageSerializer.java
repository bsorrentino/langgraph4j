package org.bsc.langgraph4j.langchain4j.serializer;

import dev.langchain4j.data.message.ToolExecutionResultMessage;
import org.bsc.langgraph4j.serializer.Serializer;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

class ToolExecutionResultMessageSerializer implements Serializer<ToolExecutionResultMessage> {
    @Override
    public void write(ToolExecutionResultMessage object, ObjectOutput out) throws IOException {
        out.writeUTF( object.id() );
        out.writeUTF( object.toolName() );
        out.writeUTF( object.text() );
    }

    @Override
    public ToolExecutionResultMessage read(ObjectInput in) throws IOException, ClassNotFoundException {
        String id = in.readUTF();
        String toolName = in.readUTF();
        String text = in.readUTF();
        return new ToolExecutionResultMessage( id, toolName, text );
    }
}
