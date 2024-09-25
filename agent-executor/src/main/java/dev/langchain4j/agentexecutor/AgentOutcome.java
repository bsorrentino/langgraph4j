package dev.langchain4j.agentexecutor;

public class AgentOutcome {
    private AgentAction action;
    private AgentFinish finish;

    public AgentAction action() { return action; }
    public AgentFinish finish() { return finish; }

    public AgentOutcome() {}
    public AgentOutcome( AgentAction action, AgentFinish finish) {
        this.action = action;
        this.finish = finish;
    }
}
