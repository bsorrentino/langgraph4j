package org.bsc.langgraph4j.state;

import java.util.Objects;

public record RemoveByHash<T>(T value ) implements AppenderChannel.RemoveIdentifier<T> {
    @Override
    public int compareTo(T element, int atIndex) {
        return Objects.hashCode(value) - Objects.hashCode(element);
    }

    public static <T> RemoveByHash<T> of ( T value ) {
        return new RemoveByHash<>(value);
    }
}
