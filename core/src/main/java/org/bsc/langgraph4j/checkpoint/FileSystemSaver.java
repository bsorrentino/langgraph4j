package org.bsc.langgraph4j.checkpoint;

import org.bsc.langgraph4j.RunnableConfig;
import org.bsc.langgraph4j.serializer.Serializer;
import org.bsc.langgraph4j.serializer.StateSerializer;
import org.bsc.langgraph4j.serializer.std.NullableObjectSerializer;
import org.bsc.langgraph4j.state.AgentState;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;


/**
 * A CheckpointSaver that stores Checkpoints in the filesystem.
 *
 * <p>
 *     Each RunnableConfig is associated with a file in the provided targetFolder.
 *     The file is named "thread-<i>threadId</i>.saver" if the RunnableConfig has a
 *     threadId, or "thread-$default.saver" if it doesn't.
 * </p>
 *
 */
public class FileSystemSaver implements BaseCheckpointSaver {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FileSystemSaver.class);
    public static final String EXTENSION = ".saver";

    private final Path targetFolder;
    private final Serializer<Checkpoint> serializer;
    private final MemorySaver memorySaver = new MemorySaver();

    @SuppressWarnings("unchecked")
    public FileSystemSaver( Path targetFolder, StateSerializer<? extends AgentState> stateSerializer) {
        Objects.requireNonNull( stateSerializer, "stateSerializer cannot be null");
        this.targetFolder = Objects.requireNonNull(targetFolder, "targetFolder cannot be null");
        this.serializer = new CheckPointSerializer( (StateSerializer<AgentState>) stateSerializer );

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

    }

    private String getBaseName( RunnableConfig config ) {
        var threadId = config.threadId().orElse( THREAD_ID_DEFAULT );
        return format( "thread-%s", threadId );
    }

    private Path getPath(RunnableConfig config) {
        return Paths.get( targetFolder.toString(), getBaseName( config ).concat(EXTENSION) );
    }

    private File getFile(RunnableConfig config) {
        return getPath(config).toFile();
    }

    private void serialize(LinkedList<Checkpoint> checkpoints, File outFile ) throws IOException {
        Objects.requireNonNull( checkpoints, "checkpoints cannot be null");
        Objects.requireNonNull( outFile, "outFile cannot be null");
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(outFile.toPath())) ) {

            oos.writeInt( checkpoints.size() );
            for(Checkpoint checkpoint : checkpoints) {
                serializer.write(checkpoint, oos);
            }
        }
    }

    private void deserialize( File file, LinkedList<Checkpoint> result) throws IOException, ClassNotFoundException {
        Objects.requireNonNull(file,"file cannot be null");
        Objects.requireNonNull(result,"result cannot be null");

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(file.toPath())) ) {
            int size = ois.readInt();
            for( int i = 0; i < size; i++ ) {
                result.add( serializer.read(ois) );
            }
        }
    }

    protected LinkedList<Checkpoint> getCheckpoints(RunnableConfig config) {
        LinkedList<Checkpoint> result = memorySaver.getCheckpoints(config);

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


    /**
     * Clears the checkpoint file associated with the given RunnableConfig.
     *
     * @param config the RunnableConfig for which the checkpoint file should be cleared
     * @return true if the file existed and was successfully deleted, false otherwise
     */
    public boolean clear(RunnableConfig config) {
        File targetFile = getFile(config);
        return targetFile.exists() && targetFile.delete();
    }

    @Override
    public Collection<Checkpoint> list(RunnableConfig config) {
        return memorySaver.list(config);
    }

    @Override
    public Optional<Checkpoint> get(RunnableConfig config) {
        return memorySaver.get(config);
    }

    @Override
    public RunnableConfig put(RunnableConfig config, Checkpoint checkpoint) throws Exception {
        RunnableConfig result = memorySaver.put(config, checkpoint);

        File targetFile = getFile(config);
        serialize( memorySaver.getCheckpoints(config), targetFile );
        return result;
    }

    private boolean createVersionedBackup( RunnableConfig config ) throws IOException {
        Path currentPath = getPath(config);
        Path targetFolder = currentPath.getParent();

        if( !Files.exists(currentPath) ) {
            log.warn( "file {} doesn't exist. Skipping file operations.", currentPath );
            return false;
        }

        var versionPattern = Pattern.compile( format( "%s-v(\\d+)\\%s$", getBaseName(config), EXTENSION));

        int maxVersion = 0;
        try (var stream = Files.list(targetFolder) ) {
            maxVersion = stream
                    .map(path -> path.getFileName().toString())
                    .map(versionPattern::matcher)
                    .filter(Matcher::matches)
                    .mapToInt(matcher -> Integer.parseInt(matcher.group(1)))
                    .max()
                    .orElse(0); // Default to 0 if no versioned files found
        } catch (IOException e) {
            log.error("Failed to list directory {} to determine next version number for backup. Skipping file operations.", targetFolder, e);
            return false;
        }

        int nextVersion = maxVersion + 1;
        var backupFilename = format("%s-v%d%s", getBaseName(config), nextVersion, EXTENSION);
        Path backupPath = targetFolder.resolve(backupFilename);

        Files.copy(currentPath, backupPath, StandardCopyOption.REPLACE_EXISTING);

        Files.delete(currentPath);

        return true;

    }

    /**
     * Releases the checkpoints associated with the given configuration.
     * This involves copying the current checkpoint file (e.g., "thread-123.saver")
     * to a versioned backup file (e.g., "thread-123-v1.saver", "thread-123-v2.saver", etc.)
     * based on existing versioned files, deleting the original unversioned file,
     * and then clearing the in-memory checkpoints.
     *
     * @param config The configuration for which to release checkpoints.
     * @return The Tag representing the released checkpoint state in memory.
     * @throws Exception If an error occurs during file operations or releasing from memory.
     */
    @Override
    public Tag release(RunnableConfig config) throws Exception {

        createVersionedBackup(config);

        return memorySaver.release(config);
    }
}

record CheckPointSerializer(
        StateSerializer<AgentState> stateSerializer) implements NullableObjectSerializer<Checkpoint> {

    @Override
    public void write(Checkpoint object, ObjectOutput out) throws IOException {
        out.writeUTF(object.getId());
        writeNullableUTF(object.getNodeId(), out);
        writeNullableUTF(object.getNextNodeId(), out);
        AgentState state = stateSerializer.stateFactory().apply(object.getState());
        stateSerializer.write(state, out);
    }

    @Override
    public Checkpoint read(ObjectInput in) throws IOException, ClassNotFoundException {
        return Checkpoint.builder()
                .id(in.readUTF())
                .nextNodeId(readNullableUTF(in).orElse(null))
                .nodeId(readNullableUTF(in).orElse(null))
                .state(stateSerializer.read(in))
                .build();
    }

}