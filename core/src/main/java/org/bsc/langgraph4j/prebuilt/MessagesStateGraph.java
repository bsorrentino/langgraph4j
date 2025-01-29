package org.bsc.langgraph4j.prebuilt;

import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.serializer.Serializer;
import org.bsc.langgraph4j.serializer.StateSerializer;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class MessagesStateGraph<T> extends StateGraph<MessagesState<T>> {

    public MessagesStateGraph( Serializer<MessagesState<T>> stateSerializer) {
        super(MessagesState.SCHEMA, new MessagesStateSerializer<>(stateSerializer) );
    }

    public MessagesStateGraph() {
        super(MessagesState.SCHEMA, MessagesState::new);
    }
}

class MessagesStateSerializer<T> extends StateSerializer<MessagesState<T>> {

    final Serializer <MessagesState<T>> stateSerializer;

    public MessagesStateSerializer( Serializer <MessagesState<T>> stateSerializer) {
        super(MessagesState::new);
        this.stateSerializer = stateSerializer;
    }

    @Override
    public void write(MessagesState<T> object, ObjectOutput out) throws IOException {
        stateSerializer.write( object, out );
    }

    @Override
    public MessagesState<T> read(ObjectInput in) throws IOException, ClassNotFoundException {
        return stateSerializer.read( in );
    }
}
