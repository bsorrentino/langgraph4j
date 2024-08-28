package org.bsc.langgraph4j.state;

import lombok.NonNull;
import lombok.Value;
import lombok.var;
import org.bsc.langgraph4j.RunnableConfig;
import org.bsc.langgraph4j.checkpoint.Checkpoint;

@Value
public class StateSnapshot<State extends AgentState> {
    State state;
    RunnableConfig config;

    public String getNext( ) {
        return config.nextNode().orElse(null);
    }

    private StateSnapshot(@NonNull State state, @NonNull RunnableConfig config) {
        this.state = state;
        this.config = config;
    }

    public static <State extends AgentState> StateSnapshot<State> of(Checkpoint checkpoint, RunnableConfig config, AgentStateFactory<State> factory) {

        var newConfig = RunnableConfig.builder(config)
                                .checkPointId( checkpoint.getId() )
                                .nextNode( checkpoint.getNextNodeId() )
                                .build() ;
        return new StateSnapshot<>( factory.apply(checkpoint.getState()), newConfig);
    }

}
