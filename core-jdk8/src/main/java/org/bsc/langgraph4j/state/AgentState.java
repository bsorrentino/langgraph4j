package org.bsc.langgraph4j.state;

import lombok.var;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableMap;
import static java.util.Optional.ofNullable;

/**
 * Represents the state of an agent with a map of data.
 */
public class AgentState {

    private final java.util.Map<String,Object> data;

    /**
     * Constructs an AgentState with the given initial data.
     *
     * @param initData the initial data for the agent state
     */
    public AgentState(Map<String,Object> initData) {
        this.data = new HashMap<>(initData);
    }

    /**
     * Returns an unmodifiable view of the data map.
     *
     * @return an unmodifiable map of the data
     */
    public final java.util.Map<String,Object> data() {
        return unmodifiableMap(data);
    }

    /**
     * Retrieves the value associated with the given key, if present.
     *
     * @param key the key whose associated value is to be returned
     * @param <T> the type of the value
     * @return an Optional containing the value if present, otherwise an empty Optional
     */
    public final <T> Optional<T> value(String key) {
        return ofNullable((T) data().get(key));
    }

    /**
     * Retrieves or creates an AppendableValue associated with the given key.
     *
     * @param key the key whose associated AppendableValue is to be returned or created
     * @param <T> the type of the value
     * @return an AppendableValue associated with the given key
     */
    public final <T> AppendableValue<T> appendableValue(String key) {
        Object value = this.data.get(key);

        if (value instanceof AppendableValue) {
            return (AppendableValue<T>) value;
        }
        if (value instanceof Collection) {
            return new AppendableValueRW<>((Collection<T>) value);
        }
        AppendableValueRW<T> rw = new AppendableValueRW<>();
        if (value != null) {
            rw.append(value);
        }
        this.data.put(key, rw);
        return rw;
    }

    /**
     * Merges the current value with the new value using the appropriate merge function.
     *
     * @param currentValue the current value
     * @param newValue the new value
     * @return the merged value
     */
    private Object mergeFunction(Object currentValue, Object newValue) {
        if (currentValue instanceof AppendableValueRW<?>) {
            ((AppendableValueRW<?>) currentValue).append(newValue);
            return currentValue;
        }
        return newValue;
    }

    /**
     * Merges the current state with a partial state and returns a new state.
     *
     * @param partialState the partial state to merge with
     * @param factory the factory to create a new state
     * @param <State> the type of the agent state
     * @return a new state resulting from the merge
     */
    public <State extends AgentState> State mergeWith(Map<String,Object> partialState, AgentStateFactory<State> factory) {
        if (partialState == null || partialState.isEmpty()) {
            return factory.apply(data());
        }
        var mergedMap = Stream.concat(data().entrySet().stream(), partialState.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        this::mergeFunction));

        return factory.apply(mergedMap);
    }

    /**
     * Returns a string representation of the agent state.
     *
     * @return a string representation of the data map
     */
    @Override
    public String toString() {
        return data.toString();
    }
}
