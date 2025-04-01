package org.bsc.langgraph4j.state;

import java.util.function.BiFunction;

/**
 * Represents a binary operator that takes two values of the same type and produces a value of the same type.
 * the first value is the old value and the second value is the new value.
 * @param <T> the type of operands and result
 */
public interface Reducer<T> extends BiFunction<T,T,T> {
}