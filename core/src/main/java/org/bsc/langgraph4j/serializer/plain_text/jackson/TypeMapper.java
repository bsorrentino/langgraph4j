package org.bsc.langgraph4j.serializer.plain_text.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.*;

public class TypeMapper {

    public static String TYPE_PROPERTY = "@type";

    public static abstract class Reference<T> extends TypeReference<T> {

        private final String typeName;

        public Reference( String typeName ) {
            super();
            this.typeName = Objects.requireNonNull(typeName, "typeName cannot be null");
        }

        public String getTypeName() { return typeName; }


    }


    private final Set<Reference<?>> references = new HashSet<>();

    public <T> TypeMapper register( Reference<T> reference ) {
        Objects.requireNonNull( reference, "reference cannot be null");
        references.add( reference );
        return this;
    }

    public <T> boolean unregister( Reference<T> reference) {
        Objects.requireNonNull( reference, "reference cannot be null");
        return references.remove( reference );
    }

    public Optional<Reference<?>> getReference( String type ) {
        Objects.requireNonNull( type, "type cannot be null");
        return references.stream()
                    .filter( ref -> Objects.equals( ref.getTypeName(), type) )
                    .findFirst();
    }


}
