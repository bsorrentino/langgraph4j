package org.bsc.langgraph4j.state;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Supplier;

import static java.util.Optional.ofNullable;
import static org.bsc.langgraph4j.utils.CollectionsUtils.listOf;


/**
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

    /**
     * Returns an {@link Optional} containing the current reducer if it is non-null.
     *
     * @return an {@code Optional} describing the current reducer wrapped in a non-empty optional,
     *         or an empty optional if no such reducer exists
     */
    @Override
    public Optional<Reducer<List<T>>> getReducer() {
        return ofNullable(reducer);
    }

    /**
     * Returns the default provider or {@code Optional.empty()} if no default provider is set.
     *
     * @return an {@code Optional} containing the default provider, or {@code Optional.empty()}
     */
    @Override
    public Optional<Supplier<List<T>>> getDefault() {
        return ofNullable(defaultProvider);
    }

    /**
     * Creates an instance of `AppenderChannel` using the provided supplier to get the default list.
     *
     * @param <T> the type of elements in the list
     * @param defaultProvider a supplier that provides the default list of elements
     * @return a new instance of `AppenderChannel`
     */
    public static <T> AppenderChannel<T> of( Supplier<List<T>> defaultProvider ) {
        return new AppenderChannel<>(defaultProvider);
    }

    /**
     * Constructs a new instance of {@code AppenderChannel} with the specified default provider.
     *
     * @param <T> the type of elements in the lists to be processed
     * @param defaultProvider a supplier for the default list that will be used when no other list is available
     */
    private AppenderChannel( Supplier<List<T>> defaultProvider) {
        this.reducer = new Reducer<List<T>>() {
            /**
             * Combines two lists into one. If the first list is null, the second list is returned.
             * Otherwise, the second list is added to the end of the first list and the resulting list is returned.
             *
             * @param <T>  the type of elements in the lists
             * @param left the first list; may be null
             * @param right the second list
             * @return a new list containing all elements from both input lists
             */
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

    /**
     * Updates the value for a given key in the channel.
     * 
     * @param key     The key for which the value needs to be updated.
     * @param oldValue    The old value that is being replaced.
     * @param newValue    The new value to be set. If null, the old value will be returned.
     * 
     * @return          The updated old value or the new value if the update was successful.
     * 
     * @throws UnsupportedOperationException   If the channel does not support updates, typically due to an immutable list being used.
     */
    public Object update( String key, Object oldValue, Object newValue) {

        if( newValue == null ) {
            return oldValue;
        }
        try {
            List<?> list = null;
            if (newValue instanceof List) {
                list = (List<?>) newValue;
            } else if (newValue.getClass().isArray()) {
                list = Arrays.asList((Object[]) newValue);
            }
            if (list != null) {
                if (list.isEmpty()) {
                    return oldValue;
                }
                return Channel.super.update(key, oldValue, list);
            }
            // this is to allow single value other than List or Array
            try {
                T typedValue = (T)newValue;
                return Channel.super.update(key, oldValue, listOf(typedValue));
            } catch (ClassCastException e) {
                log.error("Unsupported content type: {}", newValue.getClass());
                throw e;
            }
        }
        catch (UnsupportedOperationException ex) {
            log.error("Unsupported operation: probably because the appendable channel has been initialized with a immutable List. Check please !");
            throw ex;
        }
    }

}