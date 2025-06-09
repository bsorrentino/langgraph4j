package org.bsc.langgraph4j.state;

import org.bsc.langgraph4j.NodeOutput;
import org.bsc.langgraph4j.RunnableConfig;
import org.bsc.langgraph4j.checkpoint.Checkpoint;

import java.util.Objects;

import static java.lang.String.*;

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

    private StateSnapshot( String node, State state, RunnableConfig config) {
        super(  Objects.requireNonNull(node, "node cannot be null"),
                Objects.requireNonNull(state, "state cannot be null") );
        this.config = Objects.requireNonNull(config, "config cannot be null");
    }

    @Override
    public String toString() {

        return format("StateSnapshot{node=%s, state=%s, config=%s}", node(), state(), config());
    }

    public static <State extends AgentState> StateSnapshot<State> of(Checkpoint checkpoint, RunnableConfig config, AgentStateFactory<State> factory) {

        RunnableConfig newConfig = RunnableConfig.builder(config)
                                .checkPointId( checkpoint.getId() )
                                .nextNode( checkpoint.getNextNodeId() )
                                .build() ;
        return new StateSnapshot<>( checkpoint.getNodeId(), factory.apply(checkpoint.getState()), newConfig);
    }


}
