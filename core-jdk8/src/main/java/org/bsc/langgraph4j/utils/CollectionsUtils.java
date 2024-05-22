package org.bsc.langgraph4j.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

/**
 * Utility class for creating collections.
 */
public final class CollectionsUtils {

    /**
     * Creates a list containing the provided elements.
     *
     * @param objects the elements to be included in the list
     * @param <T> the type of the elements
     * @return a list containing the provided elements
     */
    public static <T> List<T> listOf(T... objects) {
        return Arrays.asList(objects);
    }

    /**
     * Creates an empty map.
     *
     * @param <K> the type of the keys
     * @param <V> the type of the values
     * @return an empty map
     */
    public static <K, V> Map<K, V> mapOf() {
        return emptyMap();
    }

    /**
     * Creates a map containing a single key-value pair.
     *
     * @param k1 the key
     * @param v1 the value
     * @param <K> the type of the key
     * @param <V> the type of the value
     * @return an unmodifiable map containing the provided key-value pair
     */
    public static <K, V> Map<K, V> mapOf(K k1, V v1) {
        Map<K, V> result = new HashMap<>();
        result.put(k1, v1);
        return unmodifiableMap(result);
    }

    /**
     * Creates a map containing two key-value pairs.
     *
     * @param k1 the first key
     * @param v1 the first value
     * @param k2 the second key
     * @param v2 the second value
     * @param <K> the type of the keys
     * @param <V> the type of the values
     * @return an unmodifiable map containing the provided key-value pairs
     */
    public static <K, V> Map<K, V> mapOf(K k1, V v1, K k2, V v2) {
        Map<K, V> result = new HashMap<K, V>();
        result.put(k1, v1);
        result.put(k2, v2);
        return unmodifiableMap(result);
    }

    /**
     * Creates a map containing three key-value pairs.
     *
     * @param k1 the first key
     * @param v1 the first value
     * @param k2 the second key
     * @param v2 the second value
     * @param k3 the third key
     * @param v3 the third value
     * @param <K> the type of the keys
     * @param <V> the type of the values
     * @return an unmodifiable map containing the provided key-value pairs
     */
    public static <K, V> Map<K, V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3) {
        Map<K, V> result = new HashMap<K, V>();
        result.put(k1, v1);
        result.put(k2, v2);
        result.put(k3, v3);
        return unmodifiableMap(result);
    }
}
