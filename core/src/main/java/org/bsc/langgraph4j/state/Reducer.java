package org.bsc.langgraph4j.state;

import java.util.function.BiFunction;

public interface Reducer<T> extends BiFunction<T,T,T> {
}
