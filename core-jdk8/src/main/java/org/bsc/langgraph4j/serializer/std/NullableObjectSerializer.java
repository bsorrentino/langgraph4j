package org.bsc.langgraph4j.serializer.std;

import org.bsc.langgraph4j.serializer.Serializer;

import java.io.*;
import java.util.Optional;

public interface NullableObjectSerializer<T> extends Serializer<T> {

    default void writeNullableObject(Object object, ObjectOutput out) throws IOException {
        if( object == null ) {
            out.writeObject( new ClassHolder(null) );
        }
        else {
            out.writeObject( object );
        }
    }

    default Optional<Object> readNullableObject(ObjectInput in) throws IOException, ClassNotFoundException {
        try {
            return Optional.of(in.readObject());
        }
        catch( NullPointerException ex ) {
            return Optional.empty();
        }

    }

    default void writeNullableUTF(String object, ObjectOutput out) throws IOException {
        out.writeObject( new StringHolder(object) );
    }

    default Optional<String> readNullableUTF(ObjectInput in) throws IOException {
        try {
            StringHolder holder = (StringHolder) in.readObject();
            return holder.value();
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }


}
