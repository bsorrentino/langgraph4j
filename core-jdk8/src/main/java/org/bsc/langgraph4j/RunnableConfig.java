package org.bsc.langgraph4j;

import lombok.ToString;

import java.util.Objects;
import java.util.Optional;

@ToString
public class RunnableConfig {

    private String threadId;
    private String checkPointId;
    private String nextNode;

    public Optional<String> threadId() {
        return Optional.ofNullable(threadId);
    }
    public Optional<String> checkPointId() {
        return Optional.ofNullable(checkPointId);
    }
    public Optional<String> nextNode() {
        return Optional.ofNullable(nextNode);
    }

    public static Builder builder() {
        return new Builder();
    }
    public static Builder builder( RunnableConfig config ) { return new Builder(config); }

    public static class Builder {
        private final RunnableConfig config;

        Builder() {;
            this.config = new RunnableConfig();
        }
        Builder( RunnableConfig config ) {
            this.config = new RunnableConfig(config);
        }
        public Builder threadId(String threadId) {
            this.config.threadId = threadId;
            return this;
        }
        public Builder checkPointId(String checkPointId) {
            this.config.checkPointId = checkPointId;
            return this;
        }
        public Builder nextNode(String nextNode) {
            this.config.nextNode = nextNode;
            return this;
        }

        public RunnableConfig build() {
            return config;
        }
    }

    private RunnableConfig( RunnableConfig config ) {
        Objects.requireNonNull( config, "config cannot be null" );
        this.threadId = config.threadId;
        this.checkPointId = config.checkPointId;
        this.nextNode = config.nextNode;
    }
    private RunnableConfig() {}


}
