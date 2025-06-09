package org.bsc.langgraph4j.langchain4j.serializer.std;

import dev.langchain4j.data.message.UserMessage;
import org.bsc.langgraph4j.serializer.std.NullableObjectSerializer;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * The UserMessageSerializer class implements the NullableObjectSerializer interface for the UserMessage type.
 * It provides methods to serialize and deserialize UserMessage objects.
 */
public class UserMessageSerializer implements NullableObjectSerializer<UserMessage> {

    /**
     * Serializes the given UserMessage object to the specified ObjectOutput.
     *
     * @param object the UserMessage object to serialize
     * @param out the ObjectOutput to write the serialized data to
     * @throws IOException if an I/O error occurs during serialization
     * @throws IllegalArgumentException if the content type of the UserMessage is unsupported
     */
    @Override
    public void write(UserMessage object, ObjectOutput out) throws IOException {

        if( object.hasSingleText() ) {
            out.writeUTF( object.singleText() );
            writeNullableUTF( object.name(), out);
            return;
        }
        throw new IllegalArgumentException( "Unsupported content type: " + object.type() );
    }

    /**
     * Deserializes a UserMessage object from the specified ObjectInput.
     *
     * @param in the ObjectInput to read the serialized data from
     * @return the deserialized UserMessage object
     * @throws IOException if an I/O error occurs during deserialization
     * @throws ClassNotFoundException if the class of a serialized object cannot be found
     */
    @Override
    public UserMessage read(ObjectInput in) throws IOException, ClassNotFoundException {
        String text = in.readUTF();
        return readNullableUTF(in)
                .map( name -> UserMessage.from(name, text) )
                .orElseGet( () -> UserMessage.from(text) );

    }
}
