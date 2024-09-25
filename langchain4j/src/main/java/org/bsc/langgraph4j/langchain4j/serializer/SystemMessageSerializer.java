package org.bsc.langgraph4j.langchain4j.serializer;

import dev.langchain4j.data.message.SystemMessage;
import org.bsc.langgraph4j.serializer.Serializer;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

class SystemMessageSerializer implements Serializer<SystemMessage> {
    @Override
    public void write(SystemMessage object, ObjectOutput out) throws IOException {
        out.writeUTF( object.text() );
    }

    @Override
    public SystemMessage read(ObjectInput in) throws IOException, ClassNotFoundException {
        String text = in.readUTF();
        return SystemMessage.systemMessage(text);
    }
}
