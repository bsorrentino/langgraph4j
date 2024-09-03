package org.bsc.langgraph4j.serializer;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MapSerialize extends BaseSerializer<Map<String,Object>> {

    public static Serializer<Map<String,Object>> of() {
        return new MapSerialize();
    }
    protected MapSerialize() {}

    @Override
    public void write(Map<String,Object> object, ObjectOutput out) throws IOException {
        try( ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
            int actualSize = 0;

            final ObjectOutputStream tupleStream = new ObjectOutputStream( baos );
            for( Map.Entry<String,Object> e : object.entrySet() ) {
                try {
                    tupleStream.writeUTF(e.getKey());

                    writeObjectWithSerializer( e.getValue(), tupleStream );

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
        Map<String, Object> result = null;
        try {
            result = newInstance(HashMap::new);
        } catch (InstantiationException|IllegalAccessException e) {
            throw new ClassNotFoundException( "error on create new instance! see root cause", e );
        }

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
                ObjectInputStream tupleStream = new ObjectInputStream( bais );

                for( int i = 0; i < actualSize; i++ ) {
                    String key = tupleStream.readUTF();

                    Object value = readObjectWithSerializer( tupleStream );

                    result.put(key, value);

                }
            }

        }
        return result;
    }

}
