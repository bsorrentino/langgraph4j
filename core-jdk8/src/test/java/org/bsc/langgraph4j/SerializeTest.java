package org.bsc.langgraph4j;

import lombok.ToString;
import org.bsc.langgraph4j.serializer.Serializer;
import org.bsc.langgraph4j.serializer.std.ObjectStreamStateSerializer;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SerializeTest {
    private final ObjectStreamStateSerializer stateSerializer = new ObjectStreamStateSerializer();

    private byte[] serializeState(Map<String,Object> state) throws Exception {
        try( ByteArrayOutputStream bytesStream = new ByteArrayOutputStream() ) {
            ObjectOutputStream oas = new ObjectOutputStream(bytesStream);
            stateSerializer.write(state, oas);
            oas.flush();
            return bytesStream.toByteArray();
        }
    }
    private Map<String,Object> deserializeState( byte[] bytes ) throws Exception {
        try(ByteArrayInputStream bais = new ByteArrayInputStream( bytes ) ) {
            ObjectInputStream ois = new ObjectInputStream( bais );
            return stateSerializer.read( ois );
        }
    }

    @Test
    public void serializeStateTest() throws Exception {

        Map<String,Object> data = new HashMap<>();
        data.put("a", "b");
        data.put("f", null );
        data.put("c", "d");

        byte[] bytes = serializeState(data);

        assertNotNull(bytes);
        Map<String,Object> deserializeState = deserializeState( bytes );

        assertEquals( 3, deserializeState.size() );
        assertEquals( "b", deserializeState.get("a") );
        assertEquals( "d", deserializeState.get("c") );
    }

    @ToString
    public static class NonSerializableElement  {

        String value;
        public NonSerializableElement() {
            this.value = "default";
        }
        public NonSerializableElement( String value ) {
            this.value = value;
        }

    }

    @Test
    public void partiallySerializeStateTest() throws Exception {

        Map<String,Object> data = new HashMap<>();
        data.put("a", "b");
        data.put("f", new NonSerializableElement("I'M NOT SERIALIZABLE") );
        data.put("c", "d");

        assertThrows(java.io.NotSerializableException.class, () -> {
                serializeState(data);
        });

    }

    @Test
    public void customSerializeStateTest() throws Exception {

        stateSerializer.mapper().register(NonSerializableElement.class, new Serializer<NonSerializableElement>() {

            @Override
            public void write(NonSerializableElement object, ObjectOutput out) throws IOException {
                out.writeUTF(object.value);
            }

            @Override
            public NonSerializableElement read(ObjectInput in) throws IOException, ClassNotFoundException {
                return new NonSerializableElement(in.readUTF());
            }
        });

        Map<String,Object> data = new HashMap<>();
        data.put("a", "b");
        data.put("x", new NonSerializableElement("I'M NOT SERIALIZABLE 2") );
        data.put("f", 'H' );
        data.put("c", "d");

        System.out.println( data );

        byte[] bytes = serializeState(data);

        assertNotNull(bytes);
        assertTrue(bytes.length > 0);

        Map<String,Object> deserializedData = deserializeState( bytes );

        assertNotNull(deserializedData);

        System.out.println( deserializedData.get( "x" ).getClass() );
        System.out.println( deserializedData );
    }

}
