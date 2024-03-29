package org.bsc.langgraph4j.utils;

import lombok.var;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

public final class CollectionsUtils {


    public static <K,V> Map<K,V> copyOf( Map<K,V> map  ) {
        return unmodifiableMap(map);
    }
    public static <T> List<T> listOf( T... objects  ) {
        return Arrays.asList(objects);
    }

    public static <K,V> Map<K,V> mapOf() {
        return emptyMap();
    }
    public static <K,V> Map<K,V> mapOf( K k1, V v1 ) {
        var result = new HashMap<K,V>();
        result.put(k1,v1);
        return unmodifiableMap(result);
    }
    public static <K,V> Map<K,V> mapOf( K k1, V v1, K k2, V v2 ) {
        var result = new HashMap<K,V>();
        result.put(k1,v1);
        result.put(k2,v2);
        return unmodifiableMap(result);
    }
}
