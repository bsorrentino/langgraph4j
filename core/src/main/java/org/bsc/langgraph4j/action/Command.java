package org.bsc.langgraph4j.action;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public record Command(Optional<String> gotoNode, Map<String,Object> update) {

    public Command {
        Objects.requireNonNull(gotoNode, "gotoNode cannot be null");
        Objects.requireNonNull(update, "update cannot be null");
    }

    public Command( String gotoNode, Map<String,Object> update  ) {
        this( ofNullable(gotoNode), ofNullable(update).orElse(Map.of()) );
    }

    public Command( String gotoNode ) {
        this( ofNullable(gotoNode), Map.of() );
    }

    public Command( Map<String,Object> update ) {
        this( Optional.empty(), ofNullable(update).orElse(Map.of()) );
    }
}
