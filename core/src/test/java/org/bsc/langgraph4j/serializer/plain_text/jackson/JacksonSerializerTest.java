package org.bsc.langgraph4j.serializer.plain_text.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.bsc.langgraph4j.serializer.plain_text.gson.GsonStateSerializer;
import org.bsc.langgraph4j.state.AgentState;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.bsc.langgraph4j.utils.CollectionsUtils.mapOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JacksonSerializerTest {

    static class State extends AgentState {

        /**
         * needed for Jackson deserialization unless use a custom deserializer
         */
        protected State() {
            super( mapOf() );
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

        State state = new State( mapOf( "prop1", "value1") );

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

}
