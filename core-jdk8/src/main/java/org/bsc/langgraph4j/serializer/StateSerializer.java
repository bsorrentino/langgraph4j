package org.bsc.langgraph4j.serializer;

import java.util.Collection;
import java.util.Map;

public class StateSerializer extends MapSerialize {

    public static StateSerializer of() {
        return new StateSerializer();
    }

    private StateSerializer() {
        super();
        register( Collection.class, CollectionSerializer.of() );
        register( Map.class, MapSerialize.of() );

    }


}
