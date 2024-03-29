package org.bsc.langgraph4j.async;

import lombok.Value;
import lombok.experimental.Accessors;

/**
 * this interface has been created exclusively to clear separate the Java17 features in order to simplify backport to java8
 * @param <T>
 */
public interface AsyncIteratorData<T> {

    @Value
    @Accessors(fluent = true)
    class Data<T> {
        T data;
        boolean done;
    }
}
