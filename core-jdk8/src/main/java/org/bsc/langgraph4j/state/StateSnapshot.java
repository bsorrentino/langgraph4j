package org.bsc.langgraph4j.state;

import lombok.NonNull;
import lombok.Value;
import lombok.var;
import org.bsc.langgraph4j.RunnableConfig;
import org.bsc.langgraph4j.checkpoint.Checkpoint;

@Value
public class StateSnapshot {
    AgentState state;
    String next;
    RunnableConfig config;

    private StateSnapshot(@NonNull AgentState state, @NonNull String next, @NonNull RunnableConfig config) {
        this.state = state;
        this.next = next;
        this.config = config;
    }

    public static StateSnapshot of(Checkpoint checkpoint, RunnableConfig config) {

        var newConfig = RunnableConfig.builder(config)
                                .checkPointId( checkpoint.getId() )
                                .build() ;
        return new StateSnapshot(checkpoint.getState(), checkpoint.getNextNodeId(), newConfig);
    }

}
