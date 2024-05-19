package org.bsc.langgraph4j;

public enum DrawableGraph {

    PLANTUML( "@startuml\n@enduml\n" );

    private String content;

    DrawableGraph(String content ) {
        this.content = content;
    }
    public DrawableGraph withContent(String content) {
        this.content = content;
        return this;
    }
    public String getContent() {
        return content;
    }
}