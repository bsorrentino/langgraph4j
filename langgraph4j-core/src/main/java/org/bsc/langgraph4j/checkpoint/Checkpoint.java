package org.bsc.langgraph4j.checkpoint;

import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.Channel;
import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.util.*;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 * Represents a checkpoint of an agent state.
 * The checkpoint is an immutable object that holds an {@link AgentState}
 * and a {@code String} that represents the next state.
 * The checkpoint is serializable and can be persisted and restored.
 *
 * <p>Every checkpoint carries the {@link #createdAt() creation date} that
 * can be used to order checkpoints reliably ( ie. by custom checkpoint savers )
 * even when the underlying storage doesn't preserve insertion order.
 *
 * @see AgentState
 * @since 1.0.0
 */
public record Checkpoint(
        String id,
        Map<String, Object> state,
        String nodeId,
        String nextNodeId,
        Instant createdAt
) {

    public Checkpoint {

        requireNonNull( id, "id cannot be null" );
        requireNonNull( state, "state cannot be null" );
        requireNonNull( nodeId, "nodeId cannot be null" );
        requireNonNull( nextNodeId, "Checkpoint.nextNodeId cannot be null" );
        requireNonNull( createdAt, "Checkpoint.createdAt cannot be null" );

    }

    /**
     * Creates a checkpoint without an explicit creation date.
     * The creation date defaults to {@link Instant#now()}.
     *
     * @param id         the checkpoint id
     * @param state      the checkpoint state
     * @param nodeId     the node that produced the checkpoint
     * @param nextNodeId the next node to execute
     */
    public Checkpoint( String id, Map<String,Object> state, String nodeId, String nextNodeId ) {
        this( id, state, nodeId, nextNodeId, Instant.now() );
    }

    @Deprecated
    public String getId() { return id; }

    @Deprecated
    public Map<String, Object> getState() {
        return state;
    }

    @Deprecated
    public String getNodeId() {
        return nodeId;
    }

    @Deprecated
    public String getNextNodeId() {
        return nextNodeId;
    }

    /**
     * create a copy of given checkpoint with a new id
     * @param checkpoint value from which copy is created
     * @return new copy with different id
     */
    public static Checkpoint copyOf( Checkpoint checkpoint ) {
        requireNonNull( checkpoint, "checkpoint cannot be null" );
        return new Checkpoint( UUID.randomUUID().toString(),
                                checkpoint.state,
                                checkpoint.nodeId,
                                checkpoint.nextNodeId,
                                Instant.now());
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id = UUID.randomUUID().toString();
        private Map<String,Object> state = null;
        private String nodeId = null ;
        private String nextNodeId = null;
        private Instant createdAt = Instant.now();

        public Builder id( String id ) {
            this.id = id;
            return this;
        }
        public Builder state( AgentState state ) {
            this.state = state.data();
            return this;
        }
        public Builder state( Map<String,Object> state ) {
            this.state = state;
            return this;
        }
        public Builder nodeId( String nodeId ) {
            this.nodeId = nodeId;
            return this;
        }
        public Builder nextNodeId( String nextNodeId ) {
            this.nextNodeId = nextNodeId;
            return this;
        }

        /**
         * Set the creation date of the checkpoint.
         * It defaults to {@link Instant#now()} if not provided.
         *
         * @param createdAt the creation date (cannot be null)
         * @return this builder
         * @since 1.9.6
         */
        public Builder createdAt( Instant createdAt ) {
            this.createdAt = requireNonNull( createdAt, "createdAt cannot be null" );
            return this;
        }

        public Checkpoint build() {
            return new Checkpoint(  id,
                                    state,
                                    nodeId,
                                    nextNodeId,
                                    createdAt );
        }
    }

    /**
     * Returns a new Checkpoint with updated state, keeping the current nextNodeId.
     *
     * @param values   the partial state updates to merge into this checkpoint's state
     * @param channels the registered channels used to resolve and update values;
     *                 may be null if values are already resolved
     * @return a new immutable Checkpoint; the original is unchanged
     * @see AgentState#updateState
     */
    public Checkpoint updateState(Map<String,Object> values, Map<String, Channel<?>> channels ) {
        return updateState( values, channels, this.nextNodeId );
    }

    /**
     * Returns a new Checkpoint with updated state and a new nextNodeId.
     *
     * <p>The creation date is preserved because the checkpoint keeps its identity
     * ( ie. the same {@link #id()} ) and it is updated in place.
     *
     * @param values     the partial state updates to merge into this checkpoint's state
     * @param channels   the registered channels used to resolve and update values;
     *                   may be null if values are already resolved
     * @param nextNodeId the identifier of the next node to execute; must not be null
     * @return a new immutable Checkpoint; the original is unchanged
     * @throws NullPointerException if nextNodeId is null
     * @see AgentState#updateState
     */
    public Checkpoint updateState(Map<String,Object> values, Map<String, Channel<?>> channels, String nextNodeId ) {

        return new Checkpoint( this.id,
                AgentState.updateState( this.state, values, channels ),
                this.nodeId,
                requireNonNull(nextNodeId, "nextNodeId cannot be null"),
                this.createdAt );
    }

    @Override
    @NonNull
    public String toString() {
        return  "Checkpoint{ id=%s, nodeId=%s, nextNodeId=%s, createdAt=%s, state=%s }".formatted(
                id,
                nodeId,
                nextNodeId,
                createdAt,
                state
        );
    }


}
