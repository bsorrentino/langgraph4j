package org.bsc.langgraph4j.spring.ai.serializer.std;

import org.bsc.langgraph4j.serializer.Serializer;
import org.springframework.ai.chat.messages.SystemMessage;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Objects;

class SystemMessageSerializer implements Serializer<SystemMessage> {

    @Override
    public void write(SystemMessage object, ObjectOutput out) throws IOException {
        var text = Objects.requireNonNull( object.getText(), "text cannot be null" );
        out.writeUTF( text );

    }

    @Override
    public SystemMessage read(ObjectInput in) throws IOException, ClassNotFoundException {
        var text = in.readUTF();
        return new SystemMessage( text );
    }
}
