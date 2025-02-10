package org.bsc.langgraph4j;

import org.bsc.langgraph4j.checkpoint.BaseCheckpointSaver;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;


public class CompileConfig {

    private BaseCheckpointSaver checkpointSaver;
    private List<String> interruptsBefore = List.of();
    private List<String> interruptsAfter = List.of();

    @Deprecated
    public String[] getInterruptBefore() { return interruptsBefore.toArray( new String[0]); }
    @Deprecated
    public String[] getInterruptAfter() { return interruptsAfter.toArray( new String[0]); }

    public List<String> interruptsBefore() { return interruptsBefore; }
    public List<String> interruptsAfter() { return interruptsAfter; }
    public Optional<BaseCheckpointSaver> checkpointSaver() { return ofNullable(checkpointSaver); }

    public static Builder builder() {
        return new Builder(new CompileConfig());
    }
    public static Builder builder( CompileConfig config ) {
        return new Builder(config);
    }

    public static class Builder {
        private final CompileConfig config;

        protected Builder( CompileConfig config ) {
            this.config = new CompileConfig(config);
        }
        public Builder checkpointSaver(BaseCheckpointSaver checkpointSaver) {
            this.config.checkpointSaver = checkpointSaver;
            return this;
        }
        public Builder interruptBefore(String... interruptBefore) {
            this.config.interruptsBefore = List.of(interruptBefore);
            return this;
        }
        public Builder interruptAfter(String... interruptAfter) {
            this.config.interruptsAfter = List.of(interruptAfter);
            return this;
        }
        public Builder interruptsBefore(Collection<String> interruptsBefore) {
            this.config.interruptsBefore = interruptsBefore.stream().toList();
            return this;
        }
        public Builder interruptsAfter(Collection<String> interruptsAfter) {
            this.config.interruptsAfter = interruptsAfter.stream().toList();
            return this;
        }
        public CompileConfig build() {
            return config;
        }
    }


    private CompileConfig() {}

    private CompileConfig( CompileConfig config ) {
        this.checkpointSaver = config.checkpointSaver;
        this.interruptsBefore = config.interruptsBefore;
        this.interruptsAfter = config.interruptsAfter;
    }

}
