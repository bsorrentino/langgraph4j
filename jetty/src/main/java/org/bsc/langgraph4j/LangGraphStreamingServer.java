package org.bsc.langgraph4j;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bsc.langgraph4j.state.AgentState;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


public interface LangGraphStreamingServer {

    CompletableFuture<Void> start() throws Exception;

    static Builder builder() {
        return new Builder();
    }

    class Builder {
        private int port = 8080;
        private Map<String,ArgumentMetadata> inputArgs = new HashMap<>();

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder addInputStringArg(String name, boolean required) {
            inputArgs.put(name, new ArgumentMetadata("string", required) );
            return this;
        }
        public Builder addInputStringArg(String name) {
            inputArgs.put(name, new ArgumentMetadata("string", true) );
            return this;
        }

        public <State extends AgentState> LangGraphStreamingServer build(CompiledGraph<State> compiledGraph) {
            Server server = new Server();
            ServerConnector connector = new ServerConnector(server);
            connector.setPort(port);
            server.addConnector(connector);

            ResourceHandler resourceHandler = new ResourceHandler();

//            Path publicResourcesPath = Paths.get("jetty", "src", "main", "webapp");
//            Resource baseResource = ResourceFactory.of(resourceHandler).newResource(publicResourcesPath));
            Resource baseResource = ResourceFactory.of(resourceHandler).newClassLoaderResource("webapp");
            resourceHandler.setBaseResource(baseResource);

            resourceHandler.setDirAllowed(true);

            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            // Add the streaming servlet
            context.addServlet(new ServletHolder(new GraphExecutionServlet<State>(compiledGraph)), "/stream");
            context.addServlet(new ServletHolder(new GraphInitServlet<State>(compiledGraph, inputArgs)), "/init");

            Handler.Sequence handlerList = new Handler.Sequence(resourceHandler, context);

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


class GraphExecutionServlet<State extends AgentState> extends HttpServlet {
    final CompiledGraph<State> compiledGraph;
    final ObjectMapper objectMapper = new ObjectMapper();

    public GraphExecutionServlet(CompiledGraph<State> compiledGraph) {
        Objects.requireNonNull(compiledGraph, "compiledGraph cannot be null");
        this.compiledGraph = compiledGraph;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Accept", "application/json");
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        final PrintWriter writer = response.getWriter();

        Map<String, Object> dataMap = objectMapper.readValue(request.getInputStream(), new TypeReference<Map<String, Object>>() {
        });

        // Start asynchronous processing
        request.startAsync();

        try {
            compiledGraph.stream(dataMap)
                    .forEachAsync(s -> {
                        writer.println(s.node());
                        writer.flush();

                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }).thenAccept(v -> {
                        writer.close();
                    });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

record ArgumentMetadata (
    String type,
    boolean required
) {}

/**
 * return the graph representation in mermaid format
 */
class GraphInitServlet<State extends AgentState> extends HttpServlet {

    final CompiledGraph<State> compiledGraph;
    final Map<String, ArgumentMetadata> inputArgs;

    public GraphInitServlet(CompiledGraph<State> compiledGraph, Map<String, ArgumentMetadata> inputArgs) {
        Objects.requireNonNull(compiledGraph, "compiledGraph cannot be null");
        this.compiledGraph = compiledGraph;
        this.inputArgs = inputArgs;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        GraphRepresentation result = compiledGraph.getGraph(GraphRepresentation.Type.MERMAID);

        // Start asynchronous processing
        request.startAsync();
        final PrintWriter writer = response.getWriter();
        writer.println(result.getContent());
        writer.close();
    }
}
