package org.bsc.langgraph4j.agentexecutor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bsc.langgraph4j.agentexecutor.serializer.json.JSONStateSerializer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class SerializableTest {

    @Test
    public void jsonSerializeTest() throws Exception {

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

        var mapper = new ObjectMapper();
        var serializer = JSONStateSerializer.of( mapper );

        var state = serializer.read(data);

        assertNotNull(state);
        assertEquals("perform test twice", state.get("input") );
        assertNotNull(state.get("intermediate_steps") );
        assertInstanceOf( List.class, state.get("intermediate_steps") );
        var intermediateSteps = (List<IntermediateStep>)state.get("intermediate_steps");
        assertTrue(intermediateSteps.isEmpty());
        assertInstanceOf( AgentOutcome.class, state.get("agent_outcome") );
        var agentOutcome = (AgentOutcome)state.get("agent_outcome");
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

        var mapper = new ObjectMapper();
        var serializer = JSONStateSerializer.of( mapper );

        var state = serializer.read(data);

        assertNotNull(state);
        assertEquals("perform test another time", state.get("input") );
        assertNotNull(state.get("intermediate_steps") );
        assertInstanceOf( List.class, state.get("intermediate_steps") );
        var intermediateSteps = (List<IntermediateStep>)state.get("intermediate_steps");
        assertEquals(1,intermediateSteps.size());
        var intermediateStep = intermediateSteps.get(0);
        assertNotNull(intermediateStep);
        assertEquals("test tool executed: perform test once", intermediateStep.observation() );
        assertInstanceOf( AgentOutcome.class, state.get("agent_outcome") );
        var agentOutcome = (AgentOutcome)state.get("agent_outcome");
        assertNotNull(agentOutcome);
        var action = agentOutcome.action();
        assertNotNull(action);
        assertEquals("execTest", action.toolExecutionRequest().name());
        assertEquals("{\"arg0\":\"perform test once\"}", action.toolExecutionRequest().arguments());

    }

}

