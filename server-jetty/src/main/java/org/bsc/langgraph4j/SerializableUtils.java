package org.bsc.langgraph4j;

import java.io.*;

public interface Utils {

    static String toString( InputStream inputStream ) throws IOException {
        var stringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        }
        return stringBuilder.toString();

    }

    static ObjectInputStream toObjectInputStream(String data ) throws IOException {
        var outStream =  new ByteArrayOutputStream();

        try(var out = new ObjectOutputStream( outStream )) {
            out.writeUTF(data);
            out.flush();
        }

        return new ObjectInputStream( new ByteArrayInputStream( outStream.toByteArray() ) );

    }

    static ObjectInputStream toObjectInputStream(InputStream inputStream ) throws IOException {
        var outStream =  new ByteArrayOutputStream();

        try(var out = new ObjectOutputStream( outStream )) {
            out.writeUTF( toString(inputStream) );
            out.flush();
        }

        return new ObjectInputStream( new ByteArrayInputStream( outStream.toByteArray() ) );

    }

}
