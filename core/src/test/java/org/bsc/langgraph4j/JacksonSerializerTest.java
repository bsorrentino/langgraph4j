package org.bsc.langgraph4j;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.bsc.langgraph4j.serializer.plain_text.jackson.JacksonStateSerializer;
import org.bsc.langgraph4j.serializer.plain_text.jackson.TypeMapper;
import org.bsc.langgraph4j.state.AgentState;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JacksonSerializerTest {

    static class State extends AgentState {

        /**
         * needed for Jackson deserialization unless use a custom deserializer
         */
        protected State() {
            super( Map.of() );
        }

        /**
         * Constructs an AgentState with the given initial data.
         *
         * @param initData the initial data for the agent state
         */
        public State(Map<String, Object> initData) {
            super(initData);
        }
    }

    @Test
    public void serializeWithTypeInferenceTest() throws IOException, ClassNotFoundException {

        State state = new State( Map.of( "prop1", "value1") );

        JacksonStateSerializer<State> serializer = new JacksonStateSerializer<State>(State::new) {};

        Class<?> type = serializer.getStateType();

        assertEquals(State.class, type);

        byte[] bytes = serializer.writeObject(state);

        assertNotNull(bytes);
        assertTrue(bytes.length > 0);

        AgentState deserializedState = serializer.readObject(bytes);

        assertNotNull(deserializedState);
        assertEquals( 1, deserializedState.data().size() );
        assertEquals( "value1", deserializedState.data().get("prop1") );
    }

    static class JacksonSerializer extends JacksonStateSerializer<AgentState> {

        public JacksonSerializer() {
            super(AgentState::new);
        }

        ObjectMapper getObjectMapper() {
            return objectMapper;
        }
    }

    @Test
    public void NodOutputJacksonSerializationTest() throws Exception {

        JacksonSerializer serializer = new JacksonSerializer();

        NodeOutput<AgentState> output = NodeOutput.of("node", null);
        output.setSubGraph(true);
        var mapper = serializer.getObjectMapper()
                            .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        var json = mapper.writeValueAsString(output);
        assertEquals("""
                {"end":false,"node":"node","state":null,"subGraph":true}""", json );

        output.setSubGraph(false);
        json = serializer.getObjectMapper().writeValueAsString(output);

        assertEquals( """
                {"end":false,"node":"node","state":null,"subGraph":false}""", json );
    }

    @Test
    public void TypeMapperTest() throws Exception {

        var mapper = new TypeMapper();

        var tr = new TypeReference<State>() {};
        System.out.println(tr.getType());
        mapper.register( new TypeMapper.Reference<State>("MyState") { } );

        var ref = mapper.getReference("MyState");

        assertTrue( ref.isPresent() );
        assertEquals( "MyState", ref.get().getTypeName() );
        System.out.println( ref.get().getType() );
        assertEquals( State.class, ref.get().getType() );
    }
}
