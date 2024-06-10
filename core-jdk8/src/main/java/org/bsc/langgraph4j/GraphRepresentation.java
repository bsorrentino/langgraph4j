package org.bsc.langgraph4j;

import lombok.Value;

/**
 * The graph representation in diagram-as-a-code format.
 */
@Value
public class GraphRepresentation {

    /**
     * The supported types.
     */
    public enum Type {
        /**
         * A drawable graph using PlantUML syntax.
         */
        PLANTUML
    }

    Type type;
    String content;
}