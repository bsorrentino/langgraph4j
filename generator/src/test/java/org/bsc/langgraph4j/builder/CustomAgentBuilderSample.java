package org.bsc.langgraph4j.builder;

import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.AsyncEdgeAction;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.serializer.StateSerializer;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AgentStateFactory;
import org.bsc.langgraph4j.state.Channel;
import org.bsc.langgraph4j.utils.EdgeMappings;

import java.util.Map;
import java.util.Objects;

import static org.bsc.langgraph4j.StateGraph.END;

interface CustomAgentBuilderSample {

	static <State extends AgentState> Builder<State> builder() {
		return new Builder<>();
	}

	class Builder<State extends AgentState> {

		private AsyncNodeAction<State> node_1;

		private AsyncEdgeAction<State> edge_1;

		public Builder<State> node_1(AsyncNodeAction<State> node_1) {
			this.node_1 = node_1;
			return this;
		}

		public Builder<State> edge_1(AsyncEdgeAction<State> edge_1) {
			this.edge_1 = edge_1;
			return this;
		}

		private StateGraph<State> of(AgentStateFactory<State> factory) {
			return new StateGraph<>(factory);
		}

		private StateGraph<State> of(StateSerializer<State> serializer) {
			return new StateGraph<>(serializer);
		}

		private StateGraph<State> of(Map<String, Channel<?>> channels, AgentStateFactory<State> factory) {
			return new StateGraph<>(factory);
		}

		private StateGraph<State> of(Map<String, Channel<?>> channels, StateSerializer<State> serializer) {
			return new StateGraph<>(serializer);
		}

		private StateGraph<State> build(StateGraph<State> graph) throws GraphStateException {
			Objects.requireNonNull(node_1, "node_1 cannot be null");
			Objects.requireNonNull(edge_1, "edge_1 cannot be null");

			return graph.addNode("node_1", node_1)
				.addEdge("node_1", END)
				.addConditionalEdges("node_1", edge_1, EdgeMappings.builder().to(END).build());

		}

		public StateGraph<State> build(AgentStateFactory<State> factory) throws GraphStateException {
			return build(of(factory));
		}

		public StateGraph<State> build(StateSerializer<State> serializer) throws GraphStateException {
			return build(of(serializer));
		}

		public StateGraph<State> build(AgentStateFactory<State> factory, Map<String, Channel<?>> channels)
				throws GraphStateException {
			return build(of(channels, factory));
		}

		public StateGraph<State> build(Map<String, Channel<?>> channels, StateSerializer<State> serializer)
				throws GraphStateException {
			return build(of(channels, serializer));
		}

	}

}
