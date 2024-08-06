package org.bsc.langgraph4j;

import org.bsc.langgraph4j.state.BaseCheckpointSaver;

import java.util.Optional;


public class CompileConfig {

    private BaseCheckpointSaver checkpointSaver;
    private String[] interruptBefore = {};
    private String[] interruptAfter = {};

    public Optional<BaseCheckpointSaver> getCheckpointSaver() { return Optional.ofNullable(checkpointSaver); }
    public String[] getInterruptBefore() { return interruptBefore; }
    public String[] getInterruptAfter() { return interruptAfter; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final CompileConfig config = new CompileConfig();

        public Builder checkpointSaver(BaseCheckpointSaver checkpointSaver) {
            this.config.checkpointSaver = checkpointSaver;
            return this;
        }
        public Builder interruptBefore(String... interruptBefore) {
            this.config.interruptBefore = interruptBefore;
            return this;
        }
        public Builder interruptAfter(String... interruptAfter) {
            this.config.interruptAfter = interruptAfter;
            return this;
        }
        public CompileConfig build() {
            return config;
        }
    }


    private CompileConfig() {}

}
