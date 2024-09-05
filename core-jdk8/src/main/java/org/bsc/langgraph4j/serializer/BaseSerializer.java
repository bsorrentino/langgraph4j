package org.bsc.langgraph4j.serializer;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class BaseSerializer<T> implements Serializer<T> {

    private static final Map<Class<?>, Serializer<?> > serializers = new HashMap<>();

    public static <S> void register( Class<?> clazz, Serializer<S> serializer ) {
        Objects.requireNonNull( clazz, "Serializer's class cannot be null" );
        Objects.requireNonNull( serializer, "Serializer cannot be null" );

        serializers.put( clazz, serializer );
    }

    public static <S> boolean unregister( Class<S> clazz ) {
        Objects.requireNonNull( clazz, "Serializer's class cannot be null" );
        Serializer<?> serializer = serializers.remove( clazz );

        return serializer != null;
    }

    protected static <S> Optional<Serializer<S>> getSerializer( Class<? extends S> clazz ) {
        Objects.requireNonNull( clazz, "Serializer's class cannot be null" );
        Serializer<?> result = serializers.get( clazz );

        if( result != null ) {
            return Optional.of((Serializer<S>)result);
        }

        return serializers.entrySet().stream()
                .filter( e -> e.getKey().isAssignableFrom(clazz) )
                .map( e -> (Serializer<S>)e.getValue() )
                .findFirst();

    }

    protected static <S> void writeObjectWithSerializer(S value, ObjectOutput out) throws IOException {
        Optional<Serializer<Object>> serializer = (value != null) ?
                getSerializer(value.getClass()) :
                Optional.empty();
        // check if written by serializer
        if (serializer.isPresent()) {
            out.writeObject(value.getClass());
            serializer.get().write(value, out);
        }
        else {
            out.writeObject(value);
        }
        out.flush();
    }

    protected static <S> S readObjectWithSerializer(ObjectInput in ) throws IOException, ClassNotFoundException {
        Object value = in.readObject();
        // check if it's a serializer
        if( value instanceof Class<?>) {
            final Class<?> serializerClass = (Class<?>)value;
            Serializer<?> serializer = getSerializer( serializerClass )
                    .orElseThrow( () -> new IllegalArgumentException( "No serializer found for class " + serializerClass ) );

            value = serializer.read(in);
        }
        return (S)value;
    }
}
