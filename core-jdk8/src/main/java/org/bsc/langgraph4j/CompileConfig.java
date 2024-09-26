package org.bsc.langgraph4j;

import lombok.Getter;
import org.bsc.langgraph4j.checkpoint.BaseCheckpointSaver;
import org.bsc.langgraph4j.serializer.Serializer;
import org.bsc.langgraph4j.serializer.StateSerializer;

import java.util.Map;
import java.util.Optional;


public class CompileConfig {

    private BaseCheckpointSaver checkpointSaver;
    @Getter
    private String[] interruptBefore = {};
    @Getter
    private String[] interruptAfter = {};
    @Getter
    private Serializer<Map<String,Object>> stateSerializer;

    public Optional<BaseCheckpointSaver> checkpointSaver() { return Optional.ofNullable(checkpointSaver); }

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
        public Builder stateSerializer(Serializer<Map<String,Object>> stateSerializer) {
            this.config.stateSerializer = stateSerializer;
            return this;
        }
        public CompileConfig build() {
            if( config.stateSerializer == null ) {
                config.stateSerializer = StateSerializer.of();
            }
            return config;
        }
    }


    private CompileConfig() {}

}
