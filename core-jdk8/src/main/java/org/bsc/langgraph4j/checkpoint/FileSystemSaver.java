package org.bsc.langgraph4j.checkpoint;

import lombok.NonNull;
import org.bsc.langgraph4j.RunnableConfig;
import org.bsc.langgraph4j.serializer.Serializer;
import org.bsc.langgraph4j.serializer.StateSerializer;
import org.bsc.langgraph4j.serializer.std.NullableObjectSerializer;
import org.bsc.langgraph4j.state.AgentState;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.String.format;

public class FileSystemSaver extends MemorySaver {

    private final StateSerializer<AgentState> stateSerializer;
    private final Path targetFolder;
    private final Serializer<Checkpoint> serializer = new NullableObjectSerializer<Checkpoint>() {

        @Override
        public void write(Checkpoint object, ObjectOutput out) throws IOException {
            out.writeUTF( object.getId() );
            writeNullableUTF(object.getNodeId(), out);
            writeNullableUTF(object.getNextNodeId(), out);
            AgentState state = stateSerializer.stateFactory().apply(object.getState());
            stateSerializer.write( state, out);
        }

        @Override
        public Checkpoint read(ObjectInput in) throws IOException, ClassNotFoundException {
            return Checkpoint.builder()
                                    .id( in.readUTF() )
                                    .state( stateSerializer.read(in) )
                                    .nextNodeId( readNullableUTF(in).orElse(null) )
                                    .nodeId( readNullableUTF(in).orElse(null) )
                                    .build();
        }
    };

    @SuppressWarnings("unchecked")
    public FileSystemSaver( @NonNull Path targetFolder, @NonNull StateSerializer<? extends AgentState> stateSerializer) {
        File targetFolderAsFile = targetFolder.toFile();

        if( targetFolderAsFile.exists() ) {
            if (targetFolderAsFile.isFile()) {
                throw new IllegalArgumentException( format("targetFolder '%s' must be a folder", targetFolder) ); // TODO: format"targetFolder must be a directory");
            }
        }
        else {
            if( !targetFolderAsFile.mkdirs() ) {
                throw new IllegalArgumentException( format("targetFolder '%s' cannot be created", targetFolder) ); // TODO: format"targetFolder cannot be created");
            }
        }

        this.targetFolder = targetFolder;
        this.stateSerializer = (StateSerializer<AgentState>) stateSerializer;
    }

    private File getFile(RunnableConfig config) {
        return config.threadId()
                .map( threadId -> Paths.get( targetFolder.toString(), format( "thread-%s.saver", threadId) ) )
                .orElseGet( () -> Paths.get( targetFolder.toString(), "thread-default.saver" ) )
                .toFile();

    }
    private void serialize(@NonNull LinkedList<Checkpoint> checkpoints, @NonNull File outFile ) throws IOException {

        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(outFile.toPath())) ) {

            oos.writeInt( checkpoints.size() );
            for(Checkpoint checkpoint : checkpoints) {
                serializer.write(checkpoint, oos);
            }
        }
    }

    private void deserialize(@NonNull File file, @NonNull LinkedList<Checkpoint> result) throws IOException, ClassNotFoundException {

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(file.toPath())) ) {
            int size = ois.readInt();
            for( int i = 0; i < size; i++ ) {
                result.add( serializer.read(ois) );
            }
        }
    }
    @Override
    protected LinkedList<Checkpoint> getCheckpoints(RunnableConfig config) {
        LinkedList<Checkpoint> result = super.getCheckpoints(config);

        File targetFile = getFile(config);
        if( targetFile.exists() && result.isEmpty() ) {
            try {
                deserialize( targetFile, result );
            }
            catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    @Override
    public Collection<Checkpoint> list(RunnableConfig config) {
        return super.list(config);
    }

    @Override
    public Optional<Checkpoint> get(RunnableConfig config) {
        return super.get(config);
    }

    @Override
    public RunnableConfig put(RunnableConfig config, Checkpoint checkpoint) throws Exception {
        RunnableConfig result = super.put(config, checkpoint);

        File targetFile = getFile(config);
        serialize( super.getCheckpoints(config), targetFile );
        return result;
    }
}
