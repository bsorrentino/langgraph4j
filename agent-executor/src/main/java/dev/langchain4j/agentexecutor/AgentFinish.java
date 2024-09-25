package dev.langchain4j.agentexecutor;

import lombok.Value;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Map;
import java.util.Objects;

@Value
@Accessors( fluent = true)
public class AgentFinish {
    Map<String, Object> returnValues;
    String log;

}