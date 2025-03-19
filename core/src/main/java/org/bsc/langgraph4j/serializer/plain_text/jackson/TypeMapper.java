package org.bsc.langgraph4j.serializer.plain_text.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.NonNull;
import java.util.*;

public class TypeMapper {

    public static String TYPE_PROPERTY = "@type";

    public static abstract class Reference<T> extends TypeReference<T> {

        private final String typeName;

        public Reference( @NonNull  String typeName ) {
            super();
            this.typeName = typeName;
        }

        public String getTypeName() { return typeName; }


    }


    private final Set<Reference<?>> references = new HashSet<>();

    public <T> TypeMapper register(@NonNull Reference<T> reference ) {
        references.add( reference );
        return this;
    }

    public <T> boolean unregister( @NonNull  Reference<T> reference) {
        return references.remove( reference );
    }

    public Optional<Reference<?>> getReference(@NonNull String type ) {
        return references.stream()
                    .filter( ref -> Objects.equals( ref.getTypeName(), type) )
                    .findFirst();
    }


}
