package org.bsc.langgraph4j.prebuilt;

import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AppenderChannel;
import org.bsc.langgraph4j.state.Channel;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public class MessagesState<T> extends AgentState {

    static Map<String, Channel<?>> SCHEMA = Map.of(
            "messages", AppenderChannel.of(ArrayList::new)
    );

    public MessagesState(Map<String, Object> initData) {
        super( initData  );
    }

    List<T> messages() {
        return this.<List<T>>value( "messages" )
                .orElseThrow( () -> new RuntimeException( "messages not found" ) );
    }

    Optional<T> lastMessage() {
        List<T> messages = messages();
        if( messages.isEmpty() ) {
            return Optional.empty();
        }
        return Optional.of(messages.get( messages.size() - 1 ));
    }

}