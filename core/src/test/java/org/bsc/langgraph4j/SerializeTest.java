package org.bsc.langgraph4j;

import org.bsc.langgraph4j.serializer.Serializer;
import org.bsc.langgraph4j.serializer.std.NullableObjectSerializer;
import org.bsc.langgraph4j.serializer.std.ObjectStreamStateSerializer;
import org.bsc.langgraph4j.state.AgentState;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;

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

    static class ValueWithNull {
        private final String name;

        public ValueWithNull(String name) {
            this.name = name;
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void serializeStateTest() throws Exception {

        stateSerializer.mapper().register( ValueWithNull.class, new NullableObjectSerializer<ValueWithNull>() {

            @Override
            public void write(ValueWithNull object, ObjectOutput out) throws IOException {
                writeNullableUTF(object.name, out);
            }

            @Override
            public ValueWithNull read(ObjectInput in) throws IOException, ClassNotFoundException {
                return new ValueWithNull( readNullableUTF(in).orElse(null) );
            }
        } );

        AgentState state = stateSerializer.stateOf( new HashMap<>() {{
                put("a", "b");
                put("f", null);
                put("c", 100);
                put("e", new ValueWithNull(null));
                put("list", new ArrayList<>() {{ add("aa"); add(null); add("cc"); add(200); }} );
            }}
        );

        byte[] bytes = serializeState( state );

        assertNotNull(bytes);
        Map<String,Object> deserializeState = deserializeState( bytes ).data();

        assertEquals( 5, deserializeState.size() );
        assertEquals( "b", deserializeState.get("a") );
        assertEquals( 100, deserializeState.get("c") );
        assertNull( deserializeState.get("f") );
        assertInstanceOf( ValueWithNull.class, deserializeState.get("e") );
        assertNull( ((ValueWithNull)deserializeState.get("e")).name );
        assertInstanceOf( List.class, deserializeState.get("list") );
        List<String> list = (List<String>)deserializeState.get("list");
        assertEquals( 4, list.size() );
        assertEquals( "aa", list.get(0) );
        assertNull(  list.get(1) );
        assertEquals( "cc", list.get(2) );
        assertEquals( 200, list.get(3) );

    }

    public record NonSerializableElement(  String value )  {
    }

    @Test
    public void partiallySerializeStateTest() throws Exception {

        AgentState state = stateSerializer.stateOf(Map.of(
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

        AgentState state = stateSerializer.stateOf(Map.of(
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
