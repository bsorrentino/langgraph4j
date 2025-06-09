package org.bsc.langgraph4j.utils;

import java.util.*;

import static org.bsc.langgraph4j.StateGraph.END;

public class EdgeMappings {

    public static class Builder {

        private final Map<String, String> mappings = new LinkedHashMap<>();

        public Builder toEND() {
            mappings.put(END, END);
            return this;
        }

        public Builder toEND( String label ) {
            mappings.put(label, END);
            return this;
        }

        public Builder to( String destination ) {
            mappings.put(destination, destination);
            return this;
        }

        public Builder to( String destination, String label ) {
            mappings.put(label, destination);
            return this;
        }

        public Builder to( List<String> destinations ) {
            destinations.forEach(this::to);
            return this;
        }

        public Builder to( String[] destinations ) {
            return to( Arrays.asList(destinations) );
        }

        public Map<String, String> build() {
            return Collections.unmodifiableMap(mappings);
        }
    }

    public static Builder builder() { return new Builder(); }
}