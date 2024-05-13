package org.bsc.langgraph4j.state;

import lombok.var;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableMap;
import static java.util.Optional.ofNullable;

public class AgentState {

    private final java.util.Map<String,Object> data;

    public AgentState( Map<String,Object> initData ) {
        this.data = new HashMap<>(initData);
    }
    public final java.util.Map<String,Object> data() {
        return unmodifiableMap(data);
    }

    public final <T> Optional<T> value(String key) {
        return ofNullable((T) data().get(key));
    };

    public final <T> AppendableValue<T> appendableValue(String key ) {
        Object value = this.data.get(key);

        if( value instanceof AppendableValue ) {
            return (AppendableValue<T>) value;
        }
        if( value instanceof Collection) {
            return new AppendableValueRW<>((Collection<T>)value);
        }
        AppendableValueRW<T> rw = new AppendableValueRW<>();
        if ( value != null ) {
            rw.append(value);
        }
        this.data.put(key, rw);
        return rw;

    }

    private Object mergeFunction(Object currentValue, Object newValue) {
        if (currentValue instanceof AppendableValueRW<?>) {
            ((AppendableValueRW<?>) currentValue).append( newValue );
            return currentValue;
        }
        return newValue;
    }
    public <State extends AgentState> State mergeWith(Map<String,Object> partialState, AgentStateFactory<State> factory) {

        if( partialState == null || partialState.isEmpty() ) {
            return factory.apply(data());
        }
        var mergedMap = Stream.concat(data().entrySet().stream(), partialState.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        this::mergeFunction));

        return factory.apply(mergedMap);
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
