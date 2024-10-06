package org.bsc.langgraph4j;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bsc.async.AsyncGenerator;
import org.bsc.langgraph4j.checkpoint.BaseCheckpointSaver;
import org.bsc.langgraph4j.checkpoint.MemorySaver;
import org.bsc.langgraph4j.serializer.Serializer;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.StateSnapshot;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;

import static java.util.Optional.ofNullable;
import static org.bsc.langgraph4j.SerializableUtils.toObjectInputStream;


/**
 * LangGraphStreamingServer is an interface that represents a server that supports streaming
 * of LangGraph.
 * Implementations of this interface can be used to create a web server
 * that exposes an API for interacting with compiled language graphs.
 */
public interface LangGraphStreamingServer {

    Logger log = LoggerFactory.getLogger(LangGraphStreamingServer.class);

    CompletableFuture<Void> start() throws Exception;

    static Builder builder() {
        return new Builder();
    }

    class Builder {
        private int port = 8080;
        private final Map<String, ArgumentMetadata> inputArgs = new HashMap<>();
        private String title = null;
        private ObjectMapper objectMapper;
        private BaseCheckpointSaver saver;
        private StateGraph<? extends AgentState>  stateGraph;
        private Serializer<Map<String,Object>> stateSerializer; //<State>

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder objectMapper(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder addInputStringArg(String name, boolean required) {
            inputArgs.put(name, new ArgumentMetadata("string", required));
            return this;
        }

        public Builder addInputStringArg(String name) {
            inputArgs.put(name, new ArgumentMetadata("string", true));
            return this;
        }

        public Builder checkpointSaver(BaseCheckpointSaver saver) {
            this.saver = saver;
            return this;
        }

        public <State extends AgentState> Builder stateGraph(StateGraph<State> stateGraph) {
            this.stateGraph = stateGraph;
            return this;
        }

        public Builder stetSerialize(Serializer<Map<String,Object>> stateSerializer) {
            this.stateSerializer = stateSerializer;
            return this;
        }

        public LangGraphStreamingServer build() throws Exception {
            Objects.requireNonNull( stateGraph, "stateGraph cannot be null");

//            Objects.requireNonNull( saver, "checkpoint saver cannot be null");
            if (saver == null) {
                saver = new MemorySaver();
            }

            Server server = new Server();

            ServerConnector connector = new ServerConnector(server);
            connector.setPort(port);
            server.addConnector(connector);

            var resourceHandler = new ResourceHandler();

//            Path publicResourcesPath = Paths.get("jetty", "src", "main", "webapp");
//            Resource baseResource = ResourceFactory.of(resourceHandler).newResource(publicResourcesPath));
            var baseResource = ResourceFactory.of(resourceHandler).newClassLoaderResource("webapp");
            resourceHandler.setBaseResource(baseResource);

            resourceHandler.setDirAllowed(true);

            var context = new ServletContextHandler(ServletContextHandler.SESSIONS);

            if (objectMapper == null) {
                objectMapper = new ObjectMapper();
            }

            context.setSessionHandler(new org.eclipse.jetty.ee10.servlet.SessionHandler());

            context.addServlet(new ServletHolder(new GraphInitServlet(stateGraph, title, inputArgs)), "/init");

            // context.setContextPath("/");
            // Add the streaming servlet
            context.addServlet(new ServletHolder(new GraphStreamServlet(stateGraph, objectMapper, saver, stateSerializer)), "/stream");

            var handlerList = new Handler.Sequence( resourceHandler, context);

            server.setHandler(handlerList);

            return new LangGraphStreamingServer() {
                @Override
                public CompletableFuture<Void> start() throws Exception {
                    return CompletableFuture.runAsync(() -> {
                        try {
                            server.start();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }, Runnable::run);

                }
            };

        }
    }
}


class NodeOutputSerializer extends StdSerializer<NodeOutput>  {
    Logger log = LangGraphStreamingServer.log;

    protected NodeOutputSerializer() {
        super( NodeOutput.class );
    }

    @Override
    public void serialize(NodeOutput nodeOutput, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        log.trace( "NodeOutputSerializer start! {}", nodeOutput.getClass() );
        gen.writeStartObject();
            if( nodeOutput instanceof StateSnapshot<?> snapshot) {
                var checkpoint = snapshot.config().checkPointId();
                log.trace( "checkpoint: {}", checkpoint );
                if( checkpoint.isPresent() ) {
                    gen.writeStringField("checkpoint", checkpoint.get());
                }
            }
            gen.writeStringField("node", nodeOutput.node());
            gen.writeObjectField("state", nodeOutput.state().data());
        gen.writeEndObject();
    }
}

record PersistentConfig(String sessionId, String threadId) {
    public PersistentConfig {
        Objects.requireNonNull(sessionId);
    }

}

class GraphStreamServlet extends HttpServlet {
    Logger log = LangGraphStreamingServer.log;
    final BaseCheckpointSaver saver;

    final StateGraph<? extends AgentState> stateGraph;
    final ObjectMapper objectMapper;
    final Map<PersistentConfig, CompiledGraph<? extends AgentState>> graphCache = new HashMap<>();
    final Serializer<Map<String,Object>> stateSerializer;

    public GraphStreamServlet(StateGraph<? extends AgentState> stateGraph,
                              ObjectMapper objectMapper,
                              BaseCheckpointSaver saver,
                              Serializer<Map<String,Object>> stateSerializer) {

        Objects.requireNonNull(stateGraph, "stateGraph cannot be null");
        this.stateGraph = stateGraph;
        this.objectMapper = objectMapper;
        var module = new SimpleModule();
        module.addSerializer(NodeOutput.class, new NodeOutputSerializer());
        objectMapper.registerModule(module);
        this.saver = saver;
        this.stateSerializer = stateSerializer;
    }

    private CompileConfig compileConfig(PersistentConfig config) {
        return CompileConfig.builder()
                .checkpointSaver(saver)
                //.stateSerializer(stateSerializer)
                .build();
    }

    RunnableConfig runnableConfig( PersistentConfig config ) {
        return RunnableConfig.builder()
                .threadId(config.threadId())
                .build();
    }

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

            final Map<String,Object> dataMap;
            if( resume && stateSerializer != null  ) {

                dataMap = stateSerializer.read( toObjectInputStream(request.getInputStream()) );
            }
            else {

                dataMap = objectMapper.readValue(request.getInputStream(), new TypeReference<>() {
                });
            }

            if( resume ) {

                log.trace( "RESUME REQUEST PREPARE" );

                if (compiledGraph == null) {
                    throw new IllegalStateException( "Missing CompiledGraph in session!" );
                }

                var checkpointId = ofNullable(request.getParameter("checkpoint"))
                        .orElseThrow(() -> new IllegalStateException("Missing checkpoint id!"));

                var config = RunnableConfig.builder()
                                        .threadId(threadId)
                                        .checkPointId(checkpointId)
                                        .build();

                var stateSnapshot = compiledGraph.getState(config);

                config = stateSnapshot.config();

                log.trace( "RESUME UPDATE STATE USING CONFIG {}\n{}", config, dataMap);

                config = compiledGraph.updateState(config, dataMap );

                log.trace( "RESUME REQUEST STREAM {}", config);

                generator = compiledGraph.streamSnapshots(null, config);

            }
            else {

                log.trace( "dataMap: {}", dataMap );

                if (compiledGraph == null) {
                    compiledGraph = stateGraph.compile(compileConfig(persistentConfig));
                    graphCache.put(persistentConfig, compiledGraph);
                }

                generator = compiledGraph.streamSnapshots(dataMap, runnableConfig(persistentConfig));
            }

            generator.forEachAsync(s -> {
                        try {
                            try {
                                writer.printf("[ \"%s\",", threadId);
                                writer.println();
                                var outputAsString = objectMapper.writeValueAsString(s);
                                writer.println(outputAsString);
                                writer.println( "]" );
                            } catch (IOException e) {
                                log.warn("error serializing state", e);
                            }
                            writer.flush();
                            TimeUnit.SECONDS.sleep(1);
                        } catch ( InterruptedException e) {
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
                    })
            ;

        } catch (Throwable e) {
            log.error("Error streaming", e);
            throw new ServletException(e);
        }
    }
}

record ArgumentMetadata(
        String type,
        boolean required) {
}

record ThreadEntry( String id, List<? extends NodeOutput<? extends AgentState>> entries) {

}

record InitData(
        String title,
        String graph,
        Map<String, ArgumentMetadata> args,
        List<ThreadEntry> threads
        ) {

    public InitData( String title, String graph, Map<String, ArgumentMetadata> args ) {
        this( title, graph, args, Collections.singletonList( new ThreadEntry("default", Collections.emptyList())) );
    }
}

class InitDataSerializer extends StdSerializer<InitData> {
    Logger log = LangGraphStreamingServer.log;

    protected InitDataSerializer(Class<InitData> t) {
        super(t);
    }

    @Override
    public void serialize(InitData initData, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        log.trace( "InitDataSerializer start!" );
        jsonGenerator.writeStartObject();

            jsonGenerator.writeStringField("graph", initData.graph());
            jsonGenerator.writeStringField("title", initData.title());
            jsonGenerator.writeObjectField("args", initData.args());


//            jsonGenerator.writeArrayFieldStart("nodes" );
//            for( var node : initData.nodes() ) {
//                jsonGenerator.writeString(node);
//            }
//            jsonGenerator.writeEndArray();

            jsonGenerator.writeArrayFieldStart("threads" );
            for( var thread : initData.threads() ) {
                jsonGenerator.writeStartArray();
                    jsonGenerator.writeString(thread.id());
                    jsonGenerator.writeStartArray( thread.entries() );
                    jsonGenerator.writeEndArray();
                jsonGenerator.writeEndArray();
            }
            jsonGenerator.writeEndArray();

        jsonGenerator.writeEndObject();
    }
}

/**
 * return the graph representation in mermaid format
 */
class GraphInitServlet extends HttpServlet {

    Logger log = LangGraphStreamingServer.log;

    final StateGraph<? extends AgentState> stateGraph;
    final ObjectMapper objectMapper = new ObjectMapper();
    final InitData initData;

    public GraphInitServlet(StateGraph<? extends AgentState> stateGraph, String title, Map<String, ArgumentMetadata> args) {
        Objects.requireNonNull(stateGraph, "stateGraph cannot be null");
        this.stateGraph = stateGraph;

        var module = new SimpleModule();
        module.addSerializer(InitData.class, new InitDataSerializer(InitData.class));
        objectMapper.registerModule(module);

        var graph = stateGraph.getGraph(GraphRepresentation.Type.MERMAID, title, false);

        initData = new InitData( title, graph.getContent(), args );
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String resultJson = objectMapper.writeValueAsString(initData);

        log.trace( "{}", resultJson);

        // Start asynchronous processing
        final PrintWriter writer = response.getWriter();
        writer.println(resultJson);
        writer.close();
    }
}
