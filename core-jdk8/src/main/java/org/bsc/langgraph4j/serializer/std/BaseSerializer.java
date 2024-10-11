package org.bsc.langgraph4j.serializer.std;

import org.bsc.langgraph4j.serializer.Serializer;

import java.io.*;
import java.util.Optional;

public abstract class BaseSerializer<T> implements Serializer<T> {

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
