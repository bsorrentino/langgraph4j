package org.bsc.langgraph4j.studio;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bsc.async.AsyncGenerator;
import org.bsc.langgraph4j.*;
import org.bsc.langgraph4j.checkpoint.BaseCheckpointSaver;
import org.bsc.langgraph4j.serializer.plain_text.PlainTextStateSerializer;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.StateSnapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;

import static java.util.Optional.ofNullable;
import org.bsc.langgraph4j.diagram.MermaidGenerator;


/**
 * Interface for a LangGraph Streaming Server.
 * Provides methods to start the server and manage streaming of graph data.
 */
public interface LangGraphStreamingServer {

    Logger log = LoggerFactory.getLogger(LangGraphStreamingServer.class);

    /**
     * Starts the streaming server.
     *
     * @return a CompletableFuture that will complete when the server has started.
     * @throws Exception if an error occurs during startup.
     */
    CompletableFuture<Void> start() throws Exception;

    /**
     * Configuration for persistent session data.
     *
     * @param sessionId the ID of the session.
     * @param threadId the ID of the thread.
     */
    record PersistentConfig(String sessionId, String threadId) {
        public PersistentConfig {
            Objects.requireNonNull(sessionId);
        }
    }

    /**
     * Servlet for handling graph stream requests.
     */
    class GraphStreamServlet extends HttpServlet {
        Logger log = LangGraphStreamingServer.log;
        final BaseCheckpointSaver saver;
        final StateGraph<? extends AgentState> stateGraph;
        final ObjectMapper objectMapper;
        final Map<PersistentConfig, CompiledGraph<? extends AgentState>> graphCache = new HashMap<>();

        /**
         * Constructs a GraphStreamServlet.
         *
         * @param stateGraph the state graph to use.
         * @param saver the checkpoint saver.
         */
        public GraphStreamServlet(StateGraph<? extends AgentState> stateGraph,
                                  BaseCheckpointSaver saver) {
            Objects.requireNonNull(stateGraph, "stateGraph cannot be null");
            this.stateGraph = stateGraph;
            this.saver = saver;

            this.objectMapper = new ObjectMapper();
            this.objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            var module = new SimpleModule();
            module.addSerializer(NodeOutput.class, new NodeOutputSerializer());
            objectMapper.registerModule(module);
        }

        @Override
        public void init(ServletConfig config) throws ServletException {
            super.init(config);
        }

        /**
         * Compiles the configuration for the given persistent configuration.
         *
         * @param config the persistent configuration.
         * @return the compiled configuration.
         */
        private CompileConfig compileConfig(PersistentConfig config) {
            return CompileConfig.builder()
                    .checkpointSaver(saver)
                    //.stateSerializer(stateSerializer)
                    .build();
        }

        /**
         * Creates a runnable configuration based on the persistent configuration.
         *
         * @param config the persistent configuration.
         * @return the runnable configuration.
         */
        RunnableConfig runnableConfig(PersistentConfig config) {
            return RunnableConfig.builder()
                    .threadId(config.threadId())
                    .build();
        }

        /**
         * Serializes the output to the given writer.
         *
         * @param writer the writer to serialize to.
         * @param threadId the ID of the thread.
         * @param output the output to serialize.
         */
        private void serializeOutput(PrintWriter writer, String threadId, NodeOutput<? extends AgentState> output) {
            try {
                writer.printf("[ \"%s\",", threadId);
                writer.println();
                var outputAsString = objectMapper.writeValueAsString(output);
                writer.println(outputAsString);
                writer.println("]");
            } catch (IOException e) {
                log.warn("error serializing state", e);
            }
        }

        /**
         * Handles POST requests to stream graph data.
         *
         * @param request the HTTP request.
         * @param response the HTTP response.
         * @throws ServletException if a servlet error occurs.
         * @throws IOException if an I/O error occurs.
         */
        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setHeader("Accept", "application/json");
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");

            var session = request.getSession(true);
            Objects.requireNonNull(session, "session cannot be null");

            var threadId = ofNullable(request.getParameter("thread"))
                    .orElseThrow(() -> new IllegalStateException("Missing thread id!"));

            var resume = ofNullable(request.getParameter("resume"))
                    .map(Boolean::parseBoolean).orElse(false);

            final PrintWriter writer = response.getWriter();

            // Start asynchronous processing
            var asyncContext = request.startAsync();

            try {
                AsyncGenerator<? extends NodeOutput<? extends AgentState>> generator = null;

                var persistentConfig = new PersistentConfig(session.getId(), threadId);

                var compiledGraph = graphCache.get(persistentConfig);

                final Map<String, Object> dataMap;
                if (resume && stateGraph.getStateSerializer() instanceof PlainTextStateSerializer<? extends AgentState> textSerializer) {
                    dataMap = textSerializer.read(new InputStreamReader(request.getInputStream())).data();
                } else {
                    dataMap = objectMapper.readValue(request.getInputStream(), new TypeReference<>() {});
                }

                if (resume) {
                    log.trace("RESUME REQUEST PREPARE");

                    if (compiledGraph == null) {
                        throw new IllegalStateException("Missing CompiledGraph in session!");
                    }

                    var checkpointId = ofNullable(request.getParameter("checkpoint"))
                            .orElseThrow(() -> new IllegalStateException("Missing checkpoint id!"));

                    var node = request.getParameter("node");
                    var config = RunnableConfig.builder()
                            .threadId(threadId)
                            .checkPointId(checkpointId)
                            .build();

                    var stateSnapshot = compiledGraph.getState(config);

                    config = stateSnapshot.config();

                    log.trace("RESUME UPDATE STATE FORM {} USING CONFIG {}\n{}", node, config, dataMap);

                    config = compiledGraph.updateState(config, dataMap, node);

                    log.trace("RESUME REQUEST STREAM {}", config);

                    generator = compiledGraph.streamSnapshots(null, config);
                } else {
                    log.trace("dataMap: {}", dataMap);

                    if (compiledGraph == null) {
                        compiledGraph = stateGraph.compile(compileConfig(persistentConfig));
                        graphCache.put(persistentConfig, compiledGraph);
                    }

                    generator = compiledGraph.streamSnapshots(dataMap, runnableConfig(persistentConfig));
                }

                generator.forEachAsync(s -> {
                    try {
                        serializeOutput(writer, threadId, s);
                        writer.flush();
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        throw new CompletionException(e);
                    }
                })
                .thenAccept(v -> writer.close())
                .thenAccept(v -> asyncContext.complete())
                .exceptionally(e -> {
                    log.error("Error streaming", e);
                    writer.close();
                    asyncContext.complete();
                    return null;
                });

            } catch (Throwable e) {
                log.error("Error streaming", e);
                throw new ServletException(e);
            }
        }
    }

    /**
     * Metadata for an argument in a request.
     *
     * @param name the name of the argument.
     * @param type the type of the argument.
     * @param required whether the argument is required.
     */
    record ArgumentMetadata(
            String name,
            ArgumentType type,
            boolean required) {
        public enum ArgumentType { STRING, IMAGE };
    }

    /**
     * Represents an entry in a thread with its outputs.
     *
     * @param id the ID of the thread.
     * @param entries the outputs of the thread.
     */
    record ThreadEntry(String id, List<? extends NodeOutput<? extends AgentState>> entries) {}

    /**
     * Initialization data for the graph.
     *
     * @param title the title of the graph.
     * @param graph the graph content.
     * @param args the arguments for the graph.
     * @param threads the thread entries.
     */
    record InitData(
            String title,
            String graph,
            List<ArgumentMetadata> args,
            List<ThreadEntry> threads) {

        public InitData(String title, String graph, List<ArgumentMetadata> args) {
            this(title, graph, args, List.of(new ThreadEntry("default", List.of())));
        }
    }

    /**
     * Serializer for InitData objects.
     */
    class InitDataSerializer extends StdSerializer<InitData> {
        Logger log = LangGraphStreamingServer.log;

        protected InitDataSerializer(Class<InitData> t) {
            super(t);
        }

        /**
         * Serializes the InitData object to JSON.
         *
         * @param initData the InitData object to serialize.
         * @param jsonGenerator the JSON generator.
         * @param serializerProvider the serializer provider.
         * @throws IOException if an I/O error occurs.
         */
        @Override
        public void serialize(InitData initData, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            log.trace("InitDataSerializer start!");
            jsonGenerator.writeStartObject();

            jsonGenerator.writeStringField("graph", initData.graph());
            jsonGenerator.writeStringField("title", initData.title());
            jsonGenerator.writeObjectField("args", initData.args());

            jsonGenerator.writeArrayFieldStart("threads");
            for (var thread : initData.threads()) {
                jsonGenerator.writeStartArray();
                jsonGenerator.writeString(thread.id());
                jsonGenerator.writeStartArray(thread.entries());
                jsonGenerator.writeEndArray();
                jsonGenerator.writeEndArray();
            }
            jsonGenerator.writeEndArray();

            jsonGenerator.writeEndObject();
        }
    }

    /**
     * Servlet for initializing the graph in mermaid format.
     */
    class GraphInitServlet extends HttpServlet {

        Logger log = LangGraphStreamingServer.log;

        final StateGraph<? extends AgentState> stateGraph;
        final ObjectMapper objectMapper = new ObjectMapper();
        InitData initData;

        /**
         * Constructs a GraphInitServlet.
         *
         * @param stateGraph the state graph to use.
         * @param title the title of the graph.
         * @param args the arguments for the graph.
         */
        public GraphInitServlet(StateGraph<? extends AgentState> stateGraph, String title, List<ArgumentMetadata> args) {
            Objects.requireNonNull(stateGraph, "stateGraph cannot be null");
            this.stateGraph = stateGraph;
            this.initData = new InitData(title, null, args);
        }

        @Override
        public void init(ServletConfig config) throws ServletException {
            super.init(config);

            var module = new SimpleModule();
            module.addSerializer(InitData.class, new InitDataSerializer(InitData.class));
            objectMapper.registerModule(module);

            try {
                var compiledGraph = stateGraph.compile();

                var graph = compiledGraph.getGraph(GraphRepresentation.Type.MERMAID, initData.title(), false);

                initData = new InitData(initData.title(), graph.content(), initData.args(), initData.threads());
            }
            catch( GraphStateException ex ) {
                throw new ServletException(ex);
            }
        }

        /**
         * Handles GET requests to retrieve the graph initialization data.
         *
         * @param request the HTTP request.
         * @param response the HTTP response.
         * @throws ServletException if a servlet error occurs.
         * @throws IOException if an I/O error occurs.
         */
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String resultJson = objectMapper.writeValueAsString(initData);

            log.trace("{}", resultJson);

            // Start asynchronous processing
            final PrintWriter writer = response.getWriter();
            writer.println(resultJson);
            writer.close();
        }
    }
}


/**
 * Serializer for NodeOutput objects, extending the StdSerializer class.
 * This class is responsible for converting NodeOutput instances into JSON format.
 */
@SuppressWarnings("rawtypes")
class NodeOutputSerializer extends StdSerializer<NodeOutput>  {
    Logger log = LangGraphStreamingServer.log;

    /**
     * Constructs a new NodeOutputSerializer.
     * Calls the superclass constructor with the NodeOutput class type.
     */
    protected NodeOutputSerializer() {
        super( NodeOutput.class );
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
        gen.writeStartObject();
        if( nodeOutput instanceof StateSnapshot<?> snapshot) {
            var checkpoint = snapshot.config().checkPointId();
            log.trace( "checkpoint: {}", checkpoint );
            if( checkpoint.isPresent() ) {
                gen.writeStringField("checkpoint", checkpoint.get());
            }
        }
        if(nodeOutput.isSubGraph()) {
            gen.writeStringField("node", MermaidGenerator.SUBGRAPH_PREFIX + nodeOutput.node());
        }
        else {
            gen.writeStringField("node", nodeOutput.node());

        }
        gen.writeObjectField("state", nodeOutput.state().data());
        gen.writeEndObject();
    }
}
