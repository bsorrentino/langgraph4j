package org.bsc.langgraph4j.state;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public interface Channels {

    static <T> Channel<List<T>> appender(Supplier<List<T>> defaultProvider ) {
        return new AppenderChannel<T>((left, right) -> {
            if (left == null) {
                return right;
            }
            for (T rValue : right) {
                // remove duplicate
                if (left.stream().noneMatch(lValue -> Objects.hash(lValue) == Objects.hash(rValue))) {
                    left.add(rValue);
                }
            }
            return left;
        }, defaultProvider );
    }

    static <T> Channel<List<T>> appenderWithDuplicate( Supplier<List<T>> defaultProvider ) {
        return new AppenderChannel<>((left, right) -> {
            if (left == null) {
                return right;
            }
            left.addAll(right);
            return left;
        }, defaultProvider );
    }

    static <T>  Channel<T> base( Supplier<T> defaultProvider) {
        return new BaseChannel<>(null, defaultProvider);
    }

    static <T>  Channel<T> base( Reducer<T> reducer ) {
        return new BaseChannel<>(reducer, null);
    }

    static <T> Channel<T> base( Reducer<T> reducer, Supplier<T> defaultProvider ) {
        return new BaseChannel<>(reducer, defaultProvider);
    }
}
