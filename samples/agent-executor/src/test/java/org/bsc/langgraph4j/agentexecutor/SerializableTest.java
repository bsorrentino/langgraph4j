package org.bsc.langgraph4j.agentexecutor;

import org.bsc.langgraph4j.agentexecutor.serializer.jackson.JSONStateSerializer;
import org.bsc.langgraph4j.agentexecutor.state.AgentOutcome;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class SerializableTest {

    @Test
    public void customJsonStateDeserializeTest() throws Exception {

        var data = """
                {
                "input":"perform test twice",
                "intermediate_steps":[],
                "agent_outcome":{
                    "action":{
                        "toolExecutionRequest":{
                            "id":"call_m6TnU4B1Net6tm6zMPzXKJxP",
                            "name":"execTest",
                            "arguments":"{\\"arg0\\":\\"perform test\\"}"
                        },
                        "log":""
                    },
                    "finish":null
                    }
                }
                """;

        var serializer = new JSONStateSerializer();


        var state = serializer.read( data );

        assertNotNull(state);
        assertTrue(state.input().isPresent());
        assertEquals("perform test twice", state.input().get() );
        assertNotNull(state.intermediateSteps());
        assertInstanceOf( List.class, state.intermediateSteps() );
        var intermediateSteps = state.intermediateSteps();
        assertTrue(intermediateSteps.isEmpty());
        assertTrue( state.agentOutcome().isPresent());
        assertInstanceOf( AgentOutcome.class, state.agentOutcome().get() );
        var agentOutcome = state.agentOutcome().get();
        assertNotNull(agentOutcome);
        var action = agentOutcome.action();
        assertNotNull(action);
        assertEquals("execTest", action.toolExecutionRequest().name());
        assertEquals("{\"arg0\":\"perform test\"}", action.toolExecutionRequest().arguments());

    }

    @Test
    public void jsonSerializeTest2() throws Exception {

        var data = """
                {"input":"perform test another time",
                "intermediate_steps":[
                        { "action": {
                        "toolExecutionRequest":{
                            "id":"call_B4KyzWwytOlrVG6cY3HfVeYq",
                            "name":"execTest",
                            "arguments":"{\\"arg0\\":\\"perform test once\\"}"
                            },
                        "log":""
                        },
                        "observation":"test tool executed: perform test once"}
                     ],
                     "agent_outcome":{
                        "action":{
                            "toolExecutionRequest":{
                                "id":"call_0LiS88saSYysfgAKHBMrIVEF",
                                "name":"execTest",
                                "arguments":"{\\"arg0\\":\\"perform test once\\"}"
                            },
                            "log":""
                        },
                        "finish":null
                    }
                }
                """;

        var serializer = new JSONStateSerializer() ;

        var state = serializer.read(data);

        assertNotNull(state);
        assertTrue(state.input().isPresent());
        assertEquals("perform test another time", state.input().get() );
        assertNotNull(state.intermediateSteps() );
        assertInstanceOf( List.class, state.intermediateSteps() );
        var intermediateSteps =state.intermediateSteps();
        assertEquals(1,intermediateSteps.size());
        var intermediateStep = intermediateSteps.get(0);
        assertNotNull(intermediateStep);
        assertEquals("test tool executed: perform test once", intermediateStep.observation() );
        assertTrue(state.agentOutcome().isPresent());
        assertInstanceOf( AgentOutcome.class, state.agentOutcome().get() );
        var agentOutcome = state.agentOutcome().get();
        assertNotNull(agentOutcome);
        var action = agentOutcome.action();
        assertNotNull(action);
        assertEquals("execTest", action.toolExecutionRequest().name());
        assertEquals("{\"arg0\":\"perform test once\"}", action.toolExecutionRequest().arguments());

    }

}

