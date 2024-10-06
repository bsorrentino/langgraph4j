package org.bsc.langgraph4j.serializer;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.*;

public class StateSerializer extends MapSerializer {

    public static StateSerializer of() {
        return new StateSerializer();
    }

    private StateSerializer() {
        super();
        register( Collection.class, ListSerializer.of() );
        register( Map.class, MapSerializer.of() );
    }

    @Override
    public Map<String, Object> read(ObjectInput in) throws IOException, ClassNotFoundException {
        return Collections.unmodifiableMap(super.read(in));
    }
}
