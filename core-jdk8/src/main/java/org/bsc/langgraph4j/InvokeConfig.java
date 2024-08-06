package org.bsc.langgraph4j;

import org.bsc.langgraph4j.state.CheckpointConfig;

import java.util.Optional;

public class InvokeConfig {

    private CheckpointConfig checkpointConfig;

    public Optional<CheckpointConfig> getCheckpointConfig() {
        return Optional.ofNullable(checkpointConfig);
    }

    static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String  checkpointThreadId;

        public Builder checkpointThreadId(String threadId) {
            this.checkpointThreadId = threadId;
            return this;
        }
        public InvokeConfig build() {
            InvokeConfig result = new InvokeConfig();

            if( checkpointThreadId != null ) {
                result.checkpointConfig = CheckpointConfig.of(checkpointThreadId);
            }

            return result;
        }
    }

    private InvokeConfig() {}
}
