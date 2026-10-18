package org.bsc.langgraph4j.state;

import org.bsc.langgraph4j.NodeOutput;
import org.bsc.langgraph4j.RunnableConfig;
import org.bsc.langgraph4j.SnapshotOutput;
import org.bsc.langgraph4j.checkpoint.Checkpoint;

import java.time.Instant;
import java.util.Objects;

import static java.lang.String.*;

public final class StateSnapshot<State extends AgentState> extends NodeOutput<State> implements SnapshotOutput {
    public static <State extends AgentState> StateSnapshot<State> of(Checkpoint checkpoint, AgentStateFactory<State> factory) {

        /*
        RunnableConfig newConfig = RunnableConfig.builder(config)
                .checkPointId( checkpoint.getId() )
                .nextNode( checkpoint.getNextNodeId() )
                .build() ;

         */
        return new StateSnapshot<>( checkpoint.nodeId(), checkpoint, factory);
    }

    private final String checkpointId;
    private final String checkpointNextNodeId;
    private final Instant checkpointCreatedAt;

    public String nextNodeId( ) {
        return checkpointNextNodeId;
    }

    public String checkpointId() {
        return checkpointId;
    }

    /**
     * The creation date of the checkpoint this snapshot refers to.
     *
     * @return the checkpoint creation date
     * @since 1.9.6
     */
    public Instant createdAt() {
        return checkpointCreatedAt;
    }

    private StateSnapshot( String node, Checkpoint checkpoint, AgentStateFactory<State> factory) {
        super( node, factory.apply(checkpoint.state()) );
        this.checkpointId = Objects.requireNonNull(checkpoint.id(), "checkpointId cannot be null");
        this.checkpointNextNodeId = Objects.requireNonNull(checkpoint.nextNodeId(), "checkpointNextNodeId cannot be null");
        this.checkpointCreatedAt = Objects.requireNonNull(checkpoint.createdAt(), "checkpointCreatedAt cannot be null");
    }

    @Override
    public String toString() {

        return "StateSnapshot{node=%s, state=%s, checkpointId=%s, checkpointNextNodeId=%s, createdAt=%s}"
                .formatted(node(), state(), checkpointId(), nextNodeId(), checkpointCreatedAt);
    }



}
