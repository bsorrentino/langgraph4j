package org.bsc.langgraph4j.serializer;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class StateSerializer implements Serializer<Map<String,Object>> {

    private final Map<Class<?>, Serializer<?> > serializers = new HashMap<>();

    public static final StateSerializer INSTANCE = new StateSerializer();
    private StateSerializer() {}

    public <T> void register( Class<T> clazz, Serializer<T> serializer ) {
        Objects.requireNonNull( clazz, "Serializer's class cannot be null" );
        Objects.requireNonNull( serializer, "Serializer cannot be null" );

        serializers.put( clazz, serializer );
    }

    public <T> boolean unregister( Class<T> clazz ) {
        Objects.requireNonNull( clazz, "Serializer's class cannot be null" );
        Serializer<?> serializer = serializers.remove( clazz );

        return serializer != null;
    }

    private Optional<Serializer<Object>> getSerializer(Class<?> clazz ) {
        Objects.requireNonNull( clazz, "Serializer's class cannot be null" );
        return Optional.ofNullable( (Serializer<Object>)serializers.get( clazz ) );
    }

    @Override
    public void write(Map<String,Object> object, ObjectOutput out) throws IOException {
        try( ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
            int actualSize = 0;

            final ObjectOutputStream tupleStream = new ObjectOutputStream( baos );
            for( Map.Entry<String,Object> e : object.entrySet() ) {
                try {
                    tupleStream.writeUTF(e.getKey());

                    Object value = e.getValue();
                    Optional<Serializer<Object>> serializer = (value != null) ?
                            getSerializer(value.getClass()) :
                            Optional.empty();
                    // check if written by serializer
                    if (serializer.isPresent()) {
                        tupleStream.writeObject(value.getClass());
                        serializer.get().write(value, tupleStream);
                    }
                    else {
                        tupleStream.writeObject(value);
                    }
                    tupleStream.flush();

                    ++actualSize;
                } catch (IOException ex) {
                    log.error( "Error writing map key '{}'", e.getKey(), ex );
                    throw ex;
                }
            }

            out.writeInt( object.size() );
            out.writeInt( actualSize ); // actual size
            byte[] data = baos.toByteArray();
            out.writeInt( data.length );
            out.write( data );

        }

    }

    @Override
    public Map<String, Object> read(ObjectInput in) throws IOException, ClassNotFoundException {
        Map<String, Object> data = new HashMap<>();

        int expectedSize = in.readInt();
        int actualSize = in.readInt();
        if( expectedSize > 0 && actualSize > 0 ) {

            if( expectedSize != actualSize ) {
                final String message = String.format( "Deserialize map: Expected size %d and actual size %d do not match!", expectedSize, actualSize ) ;
                log.error( message ) ;
                throw new IOException( message ) ;
            }

            int byteLen = in.readInt();
            byte[] bytes = new byte[byteLen];
            in.readFully(bytes);

            try( ByteArrayInputStream bais = new ByteArrayInputStream( bytes ) ) {
                ObjectInputStream ois = new ObjectInputStream( bais );

                for( int i = 0; i < actualSize; i++ ) {
                    String key = ois.readUTF();
                    Object value = ois.readObject();
                    // check if it's a serializer
                    if( value instanceof Class<?>) {
                        final Class<?> serializerClass = (Class<?>)value;
                        Serializer<Object> serializer = getSerializer( serializerClass )
                                .orElseThrow( () -> new IllegalArgumentException( "No serializer found for class " + serializerClass ) );

                        value = serializer.read(ois);
                    }

                    data.put(key, value);

                }
            }

        }
        return data;
    }

}
