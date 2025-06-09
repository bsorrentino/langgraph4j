package org.bsc.langgraph4j.langchain4j.serializer.std;

import dev.langchain4j.data.message.ToolExecutionResultMessage;
import org.bsc.langgraph4j.serializer.std.NullableObjectSerializer;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * This class is responsible for serializing and deserializing 
 * instances of ToolExecutionResultMessage. It implements the 
 * Serializer interface to provide custom serialization logic.
 */
public class ToolExecutionResultMessageSerializer implements NullableObjectSerializer<ToolExecutionResultMessage> {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ToolExecutionResultMessageSerializer.class);
    /**
     * Serializes the given ToolExecutionResultMessage object to the 
     * provided ObjectOutput stream.
     *
     * @param object the ToolExecutionResultMessage object to serialize
     * @param out the ObjectOutput stream to write the serialized data to
     * @throws IOException if an I/O error occurs during serialization
     */
    @Override
    public void write(ToolExecutionResultMessage object, ObjectOutput out) throws IOException {
        if( object.id() == null ) {
            log.trace( "ToolExecutionResultMessage id is null!" );
        }
        writeNullableUTF( object.id(), out );
        out.writeUTF( object.toolName() );
        out.writeUTF( object.text() );
    }

    /**
     * Deserializes a ToolExecutionResultMessage object from the 
     * provided ObjectInput stream.
     *
     * @param in the ObjectInput stream to read the serialized data from
     * @return the deserialized ToolExecutionResultMessage object
     * @throws IOException if an I/O error occurs during deserialization
     * @throws ClassNotFoundException if the class of a serialized object 
     *         cannot be found
     */
    @Override
    public ToolExecutionResultMessage read(ObjectInput in) throws IOException, ClassNotFoundException {
        String id = readNullableUTF( in ).orElse( null );
        String toolName = in.readUTF();
        String text = in.readUTF();
        return new ToolExecutionResultMessage( id, toolName, text );
    }
}
