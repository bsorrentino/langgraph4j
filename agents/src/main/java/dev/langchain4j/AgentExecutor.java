package dev.langchain4j;

import org.bsc.langgraph4j.state.AgentState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class AgentExecutor {

    record AgentAction ( String tool, String tool_input, String log ) {}
    record AgentFinish  ( Map<String,Object> return_values, String log ) {}

    record AgentOutcome(AgentAction action, AgentFinish finish) {}

    record IntermediateStep(AgentAction action, String observation) {
    }
    record BaseAgentState( Map<String,Object> data ) implements AgentState {

        BaseAgentState(Map<String, Object> data) {
            this.data = data;
            data.put("intermediate_steps", new ArrayList<>() );
        }
        Optional<String> input() {
            return ofNullable((String) data.get("input"));
        }
        Optional<AgentOutcome> agent_outcome() {
            return ofNullable((AgentOutcome) data.get("agent_outcome"));
        }
        List<IntermediateStep> intermediate_steps() {
            return (List<IntermediateStep>) data.get("intermediate_steps");
        }

    }
    public static void execute() {

        System.out.println("Hello World!");
    }
}
