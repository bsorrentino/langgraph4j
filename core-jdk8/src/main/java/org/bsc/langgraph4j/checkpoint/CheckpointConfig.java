package org.bsc.langgraph4j.checkpoint;

import lombok.Value;

@Value(staticConstructor = "of")
public class CheckpointConfig {
    String threadId;
}
