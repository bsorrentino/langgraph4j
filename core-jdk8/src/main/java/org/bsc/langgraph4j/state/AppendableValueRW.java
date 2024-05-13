package org.bsc.langgraph4j.state;

import java.util.*;

import static java.util.Collections.unmodifiableList;

public class AppendableValueRW<T> implements AppendableValue<T> {
    private final List<T> values;

    public AppendableValueRW( Collection<T> values) {
        this.values = new ArrayList<>(values);
    }
    public AppendableValueRW() {
        this(Collections.emptyList());
    }
    public void append(Object value) {
        if (value instanceof Collection) {
            this.values.addAll((Collection<? extends T>) value);
        }
        else {
            this.values.add((T)value);
        }
    }

    public List<T> values() {
        return unmodifiableList(values);
    }

    public boolean isEmpty() {
        return values().isEmpty();
    }
    public int size() {
        return values().size();
    }
    public Optional<T> last() {
        List<T> values = values();
        return (  values == null || values.isEmpty() ) ? Optional.empty() : Optional.of(values.get(values.size()-1));
    }
    public Optional<T> lastMinus( int n ) {
        if(  values == null || values.isEmpty() ) return Optional.empty();
        if( n < 0 ) return Optional.empty();
        if( values.size() - n - 1 < 0 ) return Optional.empty();
        return Optional.of(values.get(values.size()-n-1));
    }

    public String toString() {
        return String.valueOf(values);
    }

}
