package dev.langchain4j;

import lombok.Value;
import lombok.experimental.Accessors;

import java.util.Map;

@Value
@Accessors( fluent = true)
class AgentFinish  {
    Map<String,Object> returnValues;
    String log;

}