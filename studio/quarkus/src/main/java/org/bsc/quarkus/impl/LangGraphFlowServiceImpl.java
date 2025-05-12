package org.bsc.quarkus.impl;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.EdgeAction;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.quarkus.LangGraphFlow;

import java.util.Map;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

@ApplicationScoped
public class LangGraphFlowServiceImpl {

	private LangGraphFlow flow;

	@PostConstruct
	void init() throws GraphStateException {
		flow = sampleFlow();
	}

	@Produces
	public LangGraphFlow getFlow() {
		return flow;
	}

	private LangGraphFlow sampleFlow() throws GraphStateException {

		final EdgeAction<AgentState> conditionalAge = new EdgeAction<>() {
			int steps = 0;

			@Override
			public String apply(AgentState state) {
				if (++steps == 2) {
					steps = 0;
					return "end";
				}
				return "next";
			}
		};

		var workflow = new StateGraph<>(AgentState::new).addNode("agent", node_async((state) -> {
			System.out.println("agent ");
			System.out.println(state);
			if (state.value("action_response").isPresent()) {
				return Map.of("agent_summary", "This is just a DEMO summary");
			}
			return Map.of("agent_response", "This is an Agent DEMO response");
		})).addNode("action", node_async(state -> {
			System.out.print("action: ");
			System.out.println(state);
			return Map.of("action_response", "This is an Action DEMO response");
		}))
			.addEdge(START, "agent")
			.addEdge("action", "agent")
			.addConditionalEdges("agent", edge_async(conditionalAge), Map.of("next", "action", "end", END));

		return LangGraphFlow.builder().title("LangGraph Studio (Sample)").stateGraph(workflow).build();

	}

}
