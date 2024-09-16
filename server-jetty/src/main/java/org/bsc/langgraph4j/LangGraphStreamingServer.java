package org.bsc.langgraph4j;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bsc.langgraph4j.checkpoint.MemorySaver;
import org.bsc.langgraph4j.state.AgentState;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


/**
 * LangGraphStreamingServer is an interface that represents a server that supports streaming
 * of LangGraph.
 * Implementations of this interface can be used to create a web server
 * that exposes an API for interacting with compiled language graphs.
 */
public interface LangGraphStreamingServer {


    static Logger log = LoggerFactory.getLogger(LangGraphStreamingServer.class);

    CompletableFuture<Void> start() throws Exception;

    static Builder builder() {
        return new Builder();
    }

    class Builder {
        private int port = 8080;
        private final Map<String, ArgumentMetadata> inputArgs = new HashMap<>();
        private String title = null;
        private ObjectMapper objectMapper;

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

        public <State extends AgentState> LangGraphStreamingServer build(StateGraph<State> stateGraph) throws Exception {

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

            var initData = new InitData(title, inputArgs);
            context.addServlet(new ServletHolder(new GraphInitServlet<>(stateGraph, initData)), "/init");

            // context.setContextPath("/");
            // Add the streaming servlet
            context.addServlet(new ServletHolder(new GraphStreamServlet<State>(stateGraph, objectMapper)), "/stream");

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

record PersistentConfig(String sessionId, String threadId) {
    public PersistentConfig {
        Objects.requireNonNull(sessionId);
    }

}

class GraphStreamServlet<State extends AgentState> extends HttpServlet {
    Logger log = LangGraphStreamingServer.log;

    final StateGraph<State> stateGraph;
    final ObjectMapper objectMapper;
    final MemorySaver saver = new MemorySaver();
    final Map<PersistentConfig, CompiledGraph<State>> graphCache = new HashMap<>();

    public GraphStreamServlet(StateGraph<State> stateGraph, ObjectMapper objectMapper) {
        Objects.requireNonNull(stateGraph, "stateGraph cannot be null");
        this.stateGraph = stateGraph;
        this.objectMapper = objectMapper;
    }

    private CompileConfig compileConfig(PersistentConfig config) {
        return CompileConfig.builder()
                .checkpointSaver(saver)
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

        final PrintWriter writer = response.getWriter();

        Map<String, Object> dataMap = objectMapper.readValue(request.getInputStream(), new TypeReference<Map<String, Object>>() {
        });

        // Start asynchronous processing
        var asyncContext = request.startAsync();

        try {
            var threadId = request.getParameter("threadId");

            var config = new PersistentConfig( session.getId(), threadId);

            var compiledGraph =  graphCache.get(config);
            if( compiledGraph == null ) {
                compiledGraph = stateGraph.compile( compileConfig(config) );
                graphCache.put( config, compiledGraph );
            }

            compiledGraph.streamSnapshots(dataMap, runnableConfig(config) )
                    .forEachAsync(s -> {
                        try {
                            LangGraphStreamingServer.log.trace("{}", s);

                            writer.print("{");
                            writer.printf("\"node\": \"%s\"", s.node());
                            try {
                                var stateAsString = objectMapper.writeValueAsString(s.state().data());
                                writer.printf(",\"state\": %s", stateAsString);
                            } catch (IOException e) {
                                LangGraphStreamingServer.log.info("error serializing state", e);
                                writer.printf(",\"state\": {}");
                            }
                            writer.print("}");
                            writer.flush();
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    })
                    .thenAccept(v -> writer.close())
                    .thenAccept(v -> asyncContext.complete())
            ;

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}

record ArgumentMetadata(
        String type,
        boolean required) {
}

record InitData(
        String title,
        Map<String, ArgumentMetadata> args) {
}

/**
 * return the graph representation in mermaid format
 */
class GraphInitServlet<State extends AgentState> extends HttpServlet {

    final StateGraph<State> stateGraph;
    final ObjectMapper objectMapper = new ObjectMapper();
    final InitData initData;

    record Result(
            String graph,
            String title,
            Map<String, ArgumentMetadata> args
    ) {

        public Result(GraphRepresentation graph, InitData initData) {
            this(graph.getContent(), initData.title(), initData.args()); // graph.getContent();
        }
    }

    public GraphInitServlet(StateGraph<State> stateGraph, InitData initData) {
        Objects.requireNonNull(stateGraph, "stateGraph cannot be null");
        this.stateGraph = stateGraph;
        this.initData = initData;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        GraphRepresentation graph = stateGraph.getGraph(GraphRepresentation.Type.MERMAID, initData.title(), false);

        final Result result = new Result(graph, initData);
        String resultJson = objectMapper.writeValueAsString(result);
        // Start asynchronous processing
        final PrintWriter writer = response.getWriter();
        writer.println(resultJson);
        writer.close();
    }
}
