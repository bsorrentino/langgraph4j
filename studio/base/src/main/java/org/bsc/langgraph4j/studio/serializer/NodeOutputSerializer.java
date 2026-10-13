package org.bsc.langgraph4j.studio.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.bsc.langgraph4j.GraphPath;
import org.bsc.langgraph4j.LG4JLoggable;
import org.bsc.langgraph4j.NodeOutput;
import org.bsc.langgraph4j.RunnableConfig;
import org.bsc.langgraph4j.dsl.JsonDslGenerator;
import org.bsc.langgraph4j.serializer.StateSerializer;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.StateSnapshot;
import org.bsc.langgraph4j.studio.LangGraphStudioServer;
import org.bsc.langgraph4j.subgraph.SubGraphOutput;
import org.bsc.langgraph4j.utils.TypeRef;

import java.io.IOException;
import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * Serializer for NodeOutput objects, extending the StdSerializer class.
 * This class is responsible for converting NodeOutput instances into JSON format.
 */
@SuppressWarnings("rawtypes")
public class NodeOutputSerializer extends StdSerializer<NodeOutput> implements LG4JLoggable {

    public static final String STATE_SERIALIZER = "stateSerializer";
    public static final String THREAD_ID = "threadId";

    /**
     * Constructs a new NodeOutputSerializer.
     * Calls the superclass constructor with the NodeOutput class type.
     */
    public NodeOutputSerializer() {
        super( NodeOutput.class );
    }

    @SuppressWarnings("unchecked")
    private <State extends AgentState> Optional<StateSerializer<State>> stateSerializer(SerializerProvider serializerProvider) {
        return ofNullable(serializerProvider.getAttribute(STATE_SERIALIZER))
                .map( s -> (StateSerializer<State>) s );
    }

    private Optional<String> threadId(SerializerProvider serializerProvider) {
        return ofNullable(serializerProvider.getAttribute(THREAD_ID))
                .map(Object::toString);
    }
    /**
     * Serializes a NodeOutput instance into JSON.
     *
     * @param nodeOutput the NodeOutput instance to serialize
     * @param gen the JsonGenerator used to write JSON
     * @param serializerProvider the provider that can be used to get serializers for other types
     * @throws IOException if an I/O error occurs during serialization
     */
    @Override
    public void serialize(NodeOutput nodeOutput, JsonGenerator gen, SerializerProvider serializerProvider) throws
            IOException {
        log.trace( "NodeOutputSerializer start! {}", nodeOutput.getClass() );

        final var threadId = threadId(serializerProvider).orElse("default");

        gen.writeStartArray();
        gen.writeString(threadId);

        gen.writeStartObject();

        if( nodeOutput instanceof StateSnapshot<?> snapshot) {
            var checkpoint = snapshot.checkpointId();
            log.trace( "checkpoint: {}", checkpoint );
            if( checkpoint != null ) {
                gen.writeStringField("checkpoint", checkpoint);
            }
        }

        gen.writeStringField("node", nodeOutput.node());
        if( nodeOutput instanceof SubGraphOutput<?> subgraph) {

            final String subGraphNode;

            if( LangGraphStudioServer.LEGACY_MERMAID_SUPPORT ) {
                final var node = (nodeOutput.isSTART() || nodeOutput.isEND()) ?
                        nodeOutput.node() :
                        nodeOutput.node().concat("_");

                subGraphNode = node.concat(subgraph.subGraphId());
            }
            else {
                final var nodePath = subgraph.metadata(RunnableConfig.GRAPH_NODE_PATH, new TypeRef<GraphPath>() {})
                        .orElseThrow( () -> new IllegalStateException( "No '%s' key found in metadata".formatted(RunnableConfig.GRAPH_NODE_PATH)));

                subGraphNode = JsonDslGenerator.nodeIdFromNodePath(nodePath);
                // subGraphNode = JsonDslGenerator.subgraphNodePrefix(subgraph.subGraphId())
                //                 .concat(nodeOutput.node());
            }
            gen.writeStringField("subgraphNode", subGraphNode);
        }

        final var stateSerializer = stateSerializer(serializerProvider);
        if( stateSerializer.isPresent() ) {
            gen.writeFieldName("state");
            final var json = stateSerializer.get().writeDataAsString(nodeOutput.state());
            gen.writeRawValue(json);
        }
        else {
            // serializerProvider.defaultSerializeField("state", nodeOutput.state().data(), gen);
            gen.writeObjectField("state", nodeOutput.state().data());
        }

        if( nodeOutput instanceof StateSnapshot<?> snapshot ) {
            gen.writeObjectField("next", snapshot.nextNodeId() );
        }
        gen.writeEndObject();
        gen.writeEndArray();
    }
}
