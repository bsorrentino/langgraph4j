package org.bsc.langgraph4j.async;

/**
 * this interface has been created exclusively to clear separate the Java17 features in order to simplify backport to java8
 * @param <T>
 */
public interface AsyncIteratorData<T> {
    record Data<T>(T data, boolean done) {}
}
