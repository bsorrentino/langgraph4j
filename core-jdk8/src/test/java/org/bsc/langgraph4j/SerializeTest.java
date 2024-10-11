package org.bsc.langgraph4j;

import lombok.ToString;
import org.bsc.langgraph4j.serializer.Serializer;
import org.bsc.langgraph4j.serializer.std.ObjectStreamStateSerializer;
import org.bsc.langgraph4j.state.AgentState;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Map;

import static org.bsc.langgraph4j.utils.CollectionsUtils.mapOf;
import static org.junit.jupiter.api.Assertions.*;

public class SerializeTest {

    private final ObjectStreamStateSerializer<AgentState> stateSerializer = new ObjectStreamStateSerializer<>( AgentState::new );

    private byte[] serializeState(AgentState state) throws Exception {
        try( ByteArrayOutputStream stream = new ByteArrayOutputStream() ) {
            ObjectOutputStream oas = new ObjectOutputStream(stream);
            stateSerializer.write(state, oas);
            oas.flush();
            return stream.toByteArray();
        }
    }
    private AgentState deserializeState( byte[] bytes ) throws Exception {
        try(ByteArrayInputStream stream = new ByteArrayInputStream( bytes ) ) {
            ObjectInputStream ois = new ObjectInputStream( stream );
            return stateSerializer.read( ois );
        }
    }

    @Test
    public void serializeStateTest() throws Exception {

        AgentState state = stateSerializer.stateOf(mapOf(
            "a", "b",
            "f", null,
            "c", "d"
        ));

        byte[] bytes = serializeState( state );

        assertNotNull(bytes);
        Map<String,Object> deserializeState = deserializeState( bytes ).data();

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

        AgentState state = stateSerializer.stateOf(mapOf(
            "a", "b",
            "f", new NonSerializableElement("I'M NOT SERIALIZABLE") ,
            "c", "d"
        ));

        assertThrows(java.io.NotSerializableException.class, () -> {
                serializeState( state );
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

        AgentState state = stateSerializer.stateOf(mapOf(
            "a", "b",
            "x", new NonSerializableElement("I'M NOT SERIALIZABLE") ,
            "f", "H",
            "c", "d"));

        System.out.println( state );

        byte[] bytes = serializeState(state);

        assertNotNull(bytes);
        assertTrue(bytes.length > 0);

        Map<String,Object> deserializedData = deserializeState( bytes ).data();

        assertNotNull(deserializedData);

        System.out.println( deserializedData.get( "x" ).getClass() );
        System.out.println( deserializedData );
    }

}
