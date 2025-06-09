package org.bsc.langgraph4j.state;

import org.bsc.langgraph4j.prebuilt.MessagesState;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import static org.bsc.langgraph4j.utils.CollectionsUtils.mapOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AgentStateTest {

    static class State extends MessagesState<String> {

        public State(Map<String, Object> initData) {
            super( initData  );
        }

        int steps() {
            return this.<Integer>value("steps").orElse(0);
        }

    }

    public Map<String,Object> initDataFromSchema( Map<String,Channel<?>> schema )  {
        return  schema.entrySet().stream()
                .filter( c -> c.getValue().getDefault().isPresent() )
                .collect(Collectors.toMap(Map.Entry::getKey, e ->
                        e.getValue().getDefault().get().get()
                ));
    }

    @Test
    public void clearAppenderTest() {

        AgentStateFactory<State> sf = State::new;

        var schema = Map.<String,Channel<?>> of( "messages", Channels.appender(ArrayList::new) );
        var state = sf.applyFromSchema( schema );

        assertTrue( state.messages().isEmpty() );

        var newStateData = AgentState.updateState( state,
                Map.of( "messages", "item1"),
                schema);

        state = sf.apply( newStateData );

        assertEquals( 1, state.messages().size() );

        newStateData = AgentState.updateState( state,
                Map.of( "messages", "item2"),
                schema);

        state = sf.apply( newStateData );

        assertEquals( 2, state.messages().size() );

        newStateData = AgentState.updateState( state,
                mapOf( "messages", null),
                schema);

        state = sf.apply( newStateData );

        assertTrue( state.messages().isEmpty() );

    }

}
