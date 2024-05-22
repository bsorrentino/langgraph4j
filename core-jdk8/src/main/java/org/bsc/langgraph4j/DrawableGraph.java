package org.bsc.langgraph4j;

/**
 * Enum representing a drawable graph with different types of content.
 */
public enum DrawableGraph {

    /**
     * A drawable graph using PlantUML syntax.
     */
    PLANTUML( "@startuml\n@enduml\n" );

    private String content;

    /**
     * Constructs a DrawableGraph with the specified content.
     *
     * @param content the content of the drawable graph
     */
    DrawableGraph(String content ) {
        this.content = content;
    }

    /**
     * Sets the content of the drawable graph and returns the updated instance.
     *
     * @param content the new content to set
     * @return the updated DrawableGraph instance
     */
    public DrawableGraph withContent(String content) {
        this.content = content;
        return this;
    }

    /**
     * Returns the content of the drawable graph.
     *
     * @return the content of the drawable graph
     */
    public String getContent() {
        return content;
    }
}