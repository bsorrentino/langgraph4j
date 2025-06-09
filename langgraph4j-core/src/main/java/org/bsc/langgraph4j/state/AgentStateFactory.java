package org.bsc.langgraph4j.state;


import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A factory interface for creating instances of {@link AgentState}.
 *
 * @param <State> the type of the agent state
 */
public interface AgentStateFactory<State extends AgentState> extends Function<Map<String,Object>, State> {


    default Map<String,Object> initialDataFromSchema( Map<String,Channel<?>> schema  ) {
        return schema.entrySet().stream()
                .filter( c -> c.getValue().getDefault().isPresent() )
                .collect(Collectors.toMap(Map.Entry::getKey, e ->
                        e.getValue().getDefault().get().get()
                ));
    }

    /**
     * Initializes this state from the given schema.
     * This method processes the provided schema map to extract default values for each channel that has a defined default value.
     *
     * @param schema the schema map containing channel definitions
     * @return the initialized state with default values applied
     */
    default State applyFromSchema(Map<String,Channel<?>> schema )  {
        return apply( initialDataFromSchema(schema) );
    }

}