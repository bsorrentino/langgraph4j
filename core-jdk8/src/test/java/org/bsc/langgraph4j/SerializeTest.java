package org.bsc.langgraph4j;

import org.bsc.langgraph4j.serializer.MapSerializer;
import org.bsc.langgraph4j.state.AgentState;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SerializeTest {


    private byte[] serializeState(Map<String,Object> state) throws Exception {
        try( ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
            ObjectOutputStream oas = new ObjectOutputStream(baos);
            MapSerializer.INSTANCE.write(state, oas);
            oas.flush();
            return baos.toByteArray();
        }
    }
    private Map<String,Object> deserializeState( byte[] bytes ) throws Exception {
        try(ByteArrayInputStream bais = new ByteArrayInputStream( bytes ) ) {
            ObjectInputStream ois = new ObjectInputStream( bais );
            return MapSerializer.INSTANCE.read( ois );
        }
    }

    @Test
    public void serializeStateTest() throws Exception {

        Map<String,Object> data = new HashMap<>();
        data.put("a", "b");
        data.put("f", null);
        data.put("c", "d");

        byte[] bytes = serializeState(data);

        assertNotNull(bytes);
        Map<String,Object> deserializeState = deserializeState( bytes );

        assertEquals( 3, deserializeState.size() );
        assertEquals( "b", deserializeState.get("a") );
        assertEquals( "d", deserializeState.get("c") );
    }

    static class NonSerializableElement {
        String value = "TEST";
        public NonSerializableElement() {
        }
    }
    @Test
    public void partiallySerializeStateTest() throws Exception {

        Map<String,Object> data = new HashMap<>();
        data.put("a", "b");
        data.put("f", new NonSerializableElement() );
        data.put("c", "d");

        assertThrows(IOException.class, () -> {
                serializeState(data);
        });

    }

}
