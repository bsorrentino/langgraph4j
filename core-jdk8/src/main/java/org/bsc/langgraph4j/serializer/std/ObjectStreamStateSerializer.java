package org.bsc.langgraph4j.serializer.std;

import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.serializer.Serializer;
import org.bsc.langgraph4j.serializer.StateSerializer;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AgentStateFactory;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.*;

@Slf4j
public class ObjectStreamStateSerializer<State extends AgentState> extends StateSerializer<State> {

    static class ListSerializer implements Serializer<List<Object>> {

        @Override
        public void write(List<Object> object, ObjectOutput out) throws IOException {
            out.writeInt( object.size() );

            for( Object value : object ) {
                try {
                    out.writeObject( value );
                } catch (IOException ex) {
                    log.error( "Error writing collection value", ex );
                    throw ex;
                }
            }

            out.flush();

        }

        @Override
        public List<Object> read(ObjectInput in) throws IOException, ClassNotFoundException {
            List<Object> result = new ArrayList<>();

            int size = in.readInt();

            for (int i = 0; i < size; i++) {

                Object value = in.readObject();

                result.add(value);

            }

            return result;
        }
    }

    static class MapSerializer implements Serializer<Map<String,Object>> {

        @Override
        public void write(Map<String,Object> object, ObjectOutput out) throws IOException {
            out.writeInt( object.size() );

            for( Map.Entry<String,Object> e : object.entrySet() ) {
                try {
                    out.writeUTF(e.getKey());

                    out.writeObject( e.getValue() );

                } catch (IOException ex) {
                    log.error( "Error writing map key '{}'", e.getKey(), ex );
                    throw ex;
                }
            }

            out.flush();

        }

        @Override
        public Map<String, Object> read(ObjectInput in) throws IOException, ClassNotFoundException {
            Map<String, Object> result = new HashMap<>();

            int size = in.readInt();

            for( int i = 0; i < size; i++ ) {
                String key = in.readUTF();

                Object value = in.readObject();

                result.put(key, value);

            }
            return result;
        }

    }

    private final SerializerMapper mapper = new SerializerMapper();
    private final MapSerializer mapSerializer = new MapSerializer();

    public ObjectStreamStateSerializer( AgentStateFactory<State> stateFactory ) {
        super(stateFactory);
        mapper.register( Collection.class, new ListSerializer() );
        mapper.register( Map.class, new MapSerializer() );
    }

    public SerializerMapper mapper() {
        return mapper;
    }

    @Override
    public void write(State object, ObjectOutput out) throws IOException {
        mapSerializer.write(object.data(), mapper.objectOutputWithMapper(out));
    }

    @Override
    public final State read(ObjectInput in) throws IOException, ClassNotFoundException {
        return stateFactory().apply(mapSerializer.read( mapper.objectOutputWithMapper(in) ));
    }
}
