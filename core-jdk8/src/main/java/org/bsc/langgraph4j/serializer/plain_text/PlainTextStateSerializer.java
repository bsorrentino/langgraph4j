package org.bsc.langgraph4j.serializer.plain_text;

import org.bsc.langgraph4j.serializer.Serializer;

import java.io.*;
import java.util.Map;

public abstract class PlainTextStateSerializer implements Serializer<Map<String,Object>> {
    @Override
    public String mimeType() {
        return "plain/text";
    }

    public Map<String,Object> read( String data ) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bytesStream =  new ByteArrayOutputStream();

        try(ObjectOutputStream out = new ObjectOutputStream( bytesStream )) {
            out.writeUTF(data);
            out.flush();
        }

        try(ObjectInput in = new ObjectInputStream( new ByteArrayInputStream( bytesStream.toByteArray() ) ) ) {
            return read(in);
        }

    }

    public Map<String,Object> read( Reader reader ) throws IOException, ClassNotFoundException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
        }
        return read( sb.toString() );
    }

}
