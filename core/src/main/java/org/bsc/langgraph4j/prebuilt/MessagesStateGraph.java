package org.bsc.langgraph4j.prebuilt;

import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.serializer.StateSerializer;

public class MessagesStateGraph<T> extends StateGraph< MessagesState<T>> {

    public MessagesStateGraph( StateSerializer<MessagesState<T>> stateSerializer) {
        super(MessagesState.SCHEMA, stateSerializer);
    }

    public MessagesStateGraph() {
        super( MessagesState.SCHEMA, MessagesState::new);
    }
}

