package org.bsc.langgraph4j.state;

import lombok.NonNull;
import org.bsc.langgraph4j.NodeOutput;
import org.bsc.langgraph4j.RunnableConfig;
import org.bsc.langgraph4j.checkpoint.Checkpoint;

public final class StateSnapshot<State extends AgentState> extends NodeOutput<State> {
    private final RunnableConfig config;

    public String next( ) {
        return config.nextNode().orElse(null);
    }

    public RunnableConfig config() {
        return config;
    }

    /**
     * @deprecated Use {@link #config()} instead.
     */
    @Deprecated
    public RunnableConfig getConfig( ) {
        return config();
    }

    /**
     * @deprecated Use {@link #next()} instead.
     */
    @Deprecated
    public String getNext( ) {
        return next();
    }

    private StateSnapshot(@NonNull String node, @NonNull State state, @NonNull RunnableConfig config) {
        super( node, state );
        this.config = config;
    }

    public static <State extends AgentState> StateSnapshot<State> of(Checkpoint checkpoint, RunnableConfig config, AgentStateFactory<State> factory) {

        RunnableConfig newConfig = RunnableConfig.builder(config)
                                .checkPointId( checkpoint.getId() )
                                .nextNode( checkpoint.getNextNodeId() )
                                .build() ;
        return new StateSnapshot<>( checkpoint.getNodeId(), factory.apply(checkpoint.getState()), newConfig);
    }

}
