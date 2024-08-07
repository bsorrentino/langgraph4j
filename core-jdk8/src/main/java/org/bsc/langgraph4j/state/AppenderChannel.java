package org.bsc.langgraph4j.state;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Optional.ofNullable;
import static org.bsc.langgraph4j.utils.CollectionsUtils.listOf;


/*
 * AppenderChannel is a {@link Channel} implementation that
 * is used to accumulate a list of values.
 *
 * @param <T> the type of the values being accumulated
 * @see Channel
 */
@Slf4j
public class AppenderChannel<T> implements Channel<List<T>> {

    private final Reducer<List<T>> reducer;
    private final Supplier<List<T>> defaultProvider;

    @Override
    public Optional<Reducer<List<T>>> getReducer() {
        return ofNullable(reducer);
    }

    @Override
    public Optional<Supplier<List<T>>> getDefault() {
        return ofNullable(defaultProvider);
    }

    public static <T> AppenderChannel<T> of( Supplier<List<T>> defaultProvider ) {
        return new AppenderChannel<>(defaultProvider);
    }

    private AppenderChannel( Supplier<List<T>> defaultProvider) {
        this.reducer = new Reducer<List<T>>() {
            @Override
            public List<T> apply(List<T> left, List<T> right) {
                if( left == null ) {
                    return right;
                }
                left.addAll(right);
                return left;
            }
        };
        this.defaultProvider = defaultProvider;
    }

    public Object update( String key, Object oldValue, Object newValue) {
        try {
            try { // this is to allow single value other than
                T typedValue = (T) newValue;
                return Channel.super.update(key, oldValue, listOf(typedValue));
            } catch (ClassCastException e) {
                return Channel.super.update(key, oldValue, newValue);
            }
        } catch (UnsupportedOperationException ex) {
            log.error("Unsupported operation: probably because the appendable channel has been initialized with a immutable List. Check please !");
            throw ex;
        }
    }

}
