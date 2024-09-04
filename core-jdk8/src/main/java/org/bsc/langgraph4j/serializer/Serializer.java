package org.bsc.langgraph4j.serializer;

import java.io.*;
import java.util.Objects;
import java.util.Optional;

public interface Serializer<T> {
    void write(T object, ObjectOutput out) throws IOException;
    T read(ObjectInput in) throws IOException, ClassNotFoundException;

    default void writeObjectNullable(T object, ObjectOutput out) throws IOException {
        if( object == null ) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            write(object, out);
        }
    }
    default Optional<T> readObjectNullable(ObjectInput in) throws IOException, ClassNotFoundException {
        if( in.readBoolean() ) {
            return Optional.ofNullable(read(in));
        }
        return Optional.empty();
    }

    static void writeUTFNullable(String object, ObjectOutput out) throws IOException {
        if( object == null ) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            out.writeUTF(object);
        }
    }
    static Optional<String> readUTFNullable(ObjectInput in) throws IOException {
        if( in.readBoolean() ) {
            return Optional.of(in.readUTF());
        }
        return Optional.empty();
    }

    default byte[] writeObject(T object) throws IOException {
        Objects.requireNonNull( object, "object cannot be null" );
        try( ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
            ObjectOutputStream oas = new ObjectOutputStream(baos);
            write(object, oas);
            oas.flush();
            return baos.toByteArray();
        }
    }

    default T readObject(byte[] bytes) throws IOException, ClassNotFoundException {
        Objects.requireNonNull( bytes, "bytes cannot be null" );
        if( bytes.length == 0 ) {
            throw new IllegalArgumentException("bytes cannot be empty");
        }
        try( ByteArrayInputStream bais = new ByteArrayInputStream( bytes ) ) {
            ObjectInputStream ois = new ObjectInputStream(bais);
            return read(ois);
        }
    }

    default T cloneObject(T object) throws IOException, ClassNotFoundException {
        Objects.requireNonNull( object, "object cannot be null" );
        return readObject(writeObject(object));
    }

}
