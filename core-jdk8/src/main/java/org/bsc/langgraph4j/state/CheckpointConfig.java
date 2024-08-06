package org.bsc.langgraph4j.state;

import lombok.Value;

@Value(staticConstructor = "of")
public class CheckpointConfig {
    String threadId;
}
