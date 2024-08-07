package org.bsc.langgraph4j;

import org.bsc.langgraph4j.serializer.AgentStateSerializer;
import org.bsc.langgraph4j.state.AgentState;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SerializeTest {


    private byte[] serializeState(AgentState state) throws Exception {
        try( ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
            ObjectOutputStream oas = new ObjectOutputStream(baos);
            AgentStateSerializer.INSTANCE.write(state, oas);
            oas.flush();
            return baos.toByteArray();
        }
    }
    private AgentState deserializeState( byte[] bytes ) throws Exception {
        try(ByteArrayInputStream bais = new ByteArrayInputStream( bytes ) ) {
            ObjectInputStream ois = new ObjectInputStream( bais );
            return AgentStateSerializer.INSTANCE.read( ois );
        }
    }

    @Test
    public void serializeStateTest() throws Exception {

        Map<String,Object> data = new HashMap<>();
        data.put("a", "b");
        data.put("f", null);
        data.put("c", "d");

        final AgentState state = new AgentState(data);

        byte[] bytes = serializeState(state);

        assertNotNull(bytes);
        AgentState deserializeState = deserializeState( bytes );

        assertEquals( 3, deserializeState.data().size() );
        assertEquals( "b", deserializeState.data().get("a") );
        assertEquals( "d", deserializeState.data().get("c") );
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

        final AgentState state = new AgentState(data);

        assertThrows(IOException.class, () -> {
                serializeState(state);
        });

    }

}
