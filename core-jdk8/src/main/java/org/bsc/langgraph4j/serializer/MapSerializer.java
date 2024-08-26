package org.bsc.langgraph4j.serializer;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class StateSerializer implements Serializer<Map<String,Object>> {
    public static final StateSerializer INSTANCE = new StateSerializer();
    private StateSerializer() {}

    @Override
    public void write(Map<String,Object> object, ObjectOutput out) throws IOException {
        try( ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
            int actualSize = 0;

            final ObjectOutputStream tupleStream = new ObjectOutputStream( baos );
            for( Map.Entry<String,Object> e : object.entrySet() ) {
                try {
                    tupleStream.writeUTF(e.getKey());
                    tupleStream.writeObject(e.getValue());
                    ++actualSize;
                } catch (IOException ex) {
                    log.error( "Error writing state key '{}' - {}", e.getKey(), ex.getMessage() );
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
                final String message = String.format( "Deserialize State: Expected size %d and actual size %d do not match!", expectedSize, actualSize ) ;
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
                    data.put(key, value);
                }
            }

        }
        return data;
    }

}
