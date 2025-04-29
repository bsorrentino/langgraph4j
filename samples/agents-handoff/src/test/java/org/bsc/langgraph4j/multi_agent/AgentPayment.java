package org.bsc.langgraph4j.multi_agent;

import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.action.NodeAction;

import java.util.Map;

public class AgentPayment implements NodeAction<MultiAgentHandoff.State> {

    @Override
    public Map<String, Object> apply(MultiAgentHandoff.State state) throws Exception {
        return Map.of();
    }

    public static AsyncNodeAction<MultiAgentHandoff.State> of() {
        return AsyncNodeAction.node_async( new AgentPayment() );
    }
}
