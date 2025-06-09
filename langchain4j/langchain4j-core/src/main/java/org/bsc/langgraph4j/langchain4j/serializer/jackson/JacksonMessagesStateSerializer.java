package org.bsc.langgraph4j.langchain4j.serializer.jackson;

import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AgentStateFactory;

/**
 *
 *
 * @param <State>
 * @deprecated use {@link LC4jJacksonStateSerializer}
 */
@Deprecated
public class JacksonMessagesStateSerializer<State extends AgentState>  extends LC4jJacksonStateSerializer<State>  {

    protected JacksonMessagesStateSerializer(AgentStateFactory<State> stateFactory) {
        super(stateFactory);
    }
}





