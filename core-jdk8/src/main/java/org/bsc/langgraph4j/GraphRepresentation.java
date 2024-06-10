package org.bsc.langgraph4j;

import lombok.Value;

/**
 * Enum representing a drawable graph with different types of content.
 */
@Value
public class GraphRepresentation {

    public enum Type {
        /**
         * A drawable graph using PlantUML syntax.
         */
        PLANTUML
    }

    Type type;
    String content;
}