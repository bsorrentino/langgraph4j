package org.bsc.langgraph4j.state;

import java.util.*;

import static java.util.Collections.unmodifiableList;

public interface AppendableValue<T> {

    List<T> values();

    boolean isEmpty() ;
    int size() ;

    Optional<T> last() ;
    Optional<T> lastMinus( int n ) ;
}
