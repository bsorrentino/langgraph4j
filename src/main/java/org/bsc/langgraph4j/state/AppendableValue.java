package org.bsc.langgraph4j.state;

import lombok.Data;

import java.util.List;

@Data
public class AppendableValue<T> {
    List<T> values;
}
