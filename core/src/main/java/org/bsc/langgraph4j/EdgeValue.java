package org.bsc.langgraph4j;

import org.bsc.langgraph4j.state.AgentState;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @param <State>
 * @param id The unique identifier for the edge value.
 * @param value The condition associated with the edge value.
 */
record EdgeValue<State extends AgentState>( String id, EdgeCondition<State> value) {

    EdgeValue<State> withTargetIdUpdated(String from, Function<String, String> targetId) {
        if( id != null ) {
//            if(Objects.equals( from, id ) ) {
//                return this;
//            }
            return new EdgeValue<>( targetId.apply(id), null );
        }

        if( !value.mappings().containsValue(from) ) {
            return this;
        }

        var newMappings = value.mappings().entrySet().stream()
                .map( e ->
                    Objects.equals( e.getValue(), from ) ?
                        new AbstractMap.SimpleEntry<>(e.getKey(), targetId.apply(from) ) : e
                )
                .collect(Collectors.toMap( Map.Entry::getKey, Map.Entry::getValue ));

        return new EdgeValue<>(null, new EdgeCondition<>( value.action(), newMappings));

    }
}
