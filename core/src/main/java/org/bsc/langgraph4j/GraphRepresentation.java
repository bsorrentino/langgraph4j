package org.bsc.langgraph4j;

import lombok.Value;
import org.bsc.langgraph4j.diagram.MermaidGenerator;
import org.bsc.langgraph4j.diagram.PlantUMLGenerator;

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
        PLANTUML( new PlantUMLGenerator()),
        /**
         * A drawable graph using Mermaid syntax.
         */
        MERMAID( new MermaidGenerator() );

        final DiagramGenerator generator;

        Type(DiagramGenerator generator) {
            this.generator = generator;
        }
    }

    Type type;
    String content;

}