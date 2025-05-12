package org.bsc.langgraph4j.serializer;

import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AgentStateFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public abstract class StateSerializer<State extends AgentState> implements Serializer<State> {

	private final AgentStateFactory<State> stateFactory;

	protected StateSerializer(AgentStateFactory<State> stateFactory) {
		this.stateFactory = Objects.requireNonNull(stateFactory, "stateFactory cannot be null");
	}

	public final AgentStateFactory<State> stateFactory() {
		return stateFactory;
	}

	public final State stateOf(Map<String, Object> data) {
		Objects.requireNonNull(data, "data cannot be null");
		return stateFactory.apply(data);
	}

	public final State cloneObject(Map<String, Object> data) throws IOException, ClassNotFoundException {
		Objects.requireNonNull(data, "data cannot be null");
		return cloneObject(stateFactory().apply(data));
	}

}
