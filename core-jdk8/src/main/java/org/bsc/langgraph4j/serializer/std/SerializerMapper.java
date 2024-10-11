package org.bsc.langgraph4j.serializer.std;

import lombok.NonNull;
import org.bsc.langgraph4j.serializer.Serializer;

import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.*;

public class SerializerMapper {

    private final Map<Class<?>, Serializer<?>> _serializers = new HashMap<>();

    public SerializerMapper register(@NonNull  Class<?> clazz, @NonNull  Serializer<?> serializer ) {
        _serializers.put(clazz, serializer);
        return this;
    }

    public boolean unregister( @NonNull  Class<? extends Serializer<?>> clazz ) {
        Objects.requireNonNull( clazz, "Serializer's class cannot be null" );
        Serializer<?> serializer = _serializers.remove( clazz );
        return serializer != null;
    }

    protected Optional<Serializer<Object>> getSerializer( @NonNull Class<?> clazz ) {
        Serializer<?> result = _serializers.get( clazz );

        if( result != null ) {
            return Optional.of((Serializer<Object>)result);
        }

        return _serializers.entrySet().stream()
                .filter( e -> e.getKey().isAssignableFrom(clazz) )
                .map(Map.Entry::getValue)
                .findFirst()
                .map( s -> (Serializer<Object>)s )
                ;

    }

    protected final ObjectOutput objectOutputWithMapper(@NonNull  ObjectOutput out) {

        final ObjectOutputWithMapper mapperOut ;
        if( out instanceof ObjectOutputWithMapper ) {
            mapperOut = (ObjectOutputWithMapper)out;
        } else {
            mapperOut = new ObjectOutputWithMapper( out, this );
        }

        return mapperOut;
    }

    protected final ObjectInput objectOutputWithMapper(@NonNull  ObjectInput in) {

        final ObjectInputWithMapper mapperIn ;
        if( in instanceof ObjectInputWithMapper ) {
            mapperIn = (ObjectInputWithMapper)in;
        } else {
            mapperIn = new ObjectInputWithMapper( in, this );
        }

        return mapperIn;

    }

}
