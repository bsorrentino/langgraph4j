package org.bsc.langgraph4j.serializer;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public abstract class BaseSerializer<T> implements Serializer<T> {

    private static final Map<Class<?>, Serializer<?> > _serializers = new HashMap<>();

    public static void register( Class<?> clazz, Serializer<?> serializer ) {
        Objects.requireNonNull( clazz, "Serializer's class cannot be null" );
        Objects.requireNonNull( serializer, "Serializer cannot be null" );

        _serializers.put(clazz, serializer);

    }

    public static <S> boolean unregister( Class<S> clazz ) {
        Objects.requireNonNull( clazz, "Serializer's class cannot be null" );
        Serializer<?> serializer = _serializers.remove( clazz );
        return serializer != null;
    }

    protected static Optional<Serializer<Object>> getSerializer( Class<?> clazz ) {
        Objects.requireNonNull( clazz, "Serializer's class cannot be null" );
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

    protected void writeObjectWithSerializer(Object value, ObjectOutput out) throws IOException {
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

    protected <S> S readObjectWithSerializer(ObjectInput in ) throws IOException, ClassNotFoundException {
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


    protected void writeObjectNullable(T object, ObjectOutput out) throws IOException {
        if( object == null ) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            write(object, out);
        }
    }
    protected Optional<T> readObjectNullable(ObjectInput in) throws IOException, ClassNotFoundException {
        if( in.readBoolean() ) {
            return Optional.ofNullable(read(in));
        }
        return Optional.empty();
    }

    protected void writeUTFNullable(String object, ObjectOutput out) throws IOException {
        if( object == null ) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            out.writeUTF(object);
        }
    }
    protected Optional<String> readUTFNullable(ObjectInput in) throws IOException {
        if( in.readBoolean() ) {
            return Optional.of(in.readUTF());
        }
        return Optional.empty();
    }


}
