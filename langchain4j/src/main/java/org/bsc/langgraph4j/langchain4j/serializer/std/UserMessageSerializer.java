package org.bsc.langgraph4j.langchain4j.serializer.std;

import dev.langchain4j.data.message.UserMessage;
import org.bsc.langgraph4j.serializer.std.NullableObjectSerializer;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class UserMessageSerializer implements NullableObjectSerializer<UserMessage> {
    @Override
    public void write(UserMessage object, ObjectOutput out) throws IOException {

        if( object.hasSingleText() ) {
            out.writeUTF( object.singleText() );
            writeNullableUTF( object.name(), out);
            return;
        }
        throw new IllegalArgumentException( "Unsupported content type: " + object.type() );
    }

    @Override
    public UserMessage read(ObjectInput in) throws IOException, ClassNotFoundException {
        String text = in.readUTF();
        return readNullableUTF(in)
                .map( name -> UserMessage.from(name, text) )
                .orElseGet( () -> UserMessage.from(text) );

    }
}
