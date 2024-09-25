package dev.langchain4j.agentexecutor;

public class IntermediateStep  {
    private AgentAction action;
    private String observation;

    public AgentAction action() { return action; }
    public String observation() { return observation; }

    public IntermediateStep() {}
    public IntermediateStep( AgentAction action, String observation) {
        this.action = action;
        this.observation = observation;
    }
}
