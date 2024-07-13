package org.bsc.langgraph4j;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bsc.async.AsyncGenerator;
import org.bsc.langgraph4j.state.AgentState;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.ResourceFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class Langgraph4jStreamingServer {


    public static void main(String[] args) throws Exception {

    }

    public static <State extends AgentState> void start(CompiledGraph<State> compiledGraph) throws Exception {

        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.addConnector(connector);

        ResourceHandler resourceHandler = new ResourceHandler();
        Path publicResourcesPath = Paths.get( "jetty", "src", "main", "webapp" );
        resourceHandler.setBaseResource(ResourceFactory.of(resourceHandler).newResource(publicResourcesPath));
        resourceHandler.setDirAllowed(true);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        // Add the streaming servlet
        context.addServlet(new ServletHolder(new StreamingServlet(compiledGraph)), "/stream");

        Handler.Sequence handlerList = new Handler.Sequence(resourceHandler, context );

        server.setHandler(handlerList);

        server.start();
        server.join();
    }

    public static class StreamingServlet<State extends AgentState> extends HttpServlet {
        final CompiledGraph<State> compiledGraph;
        final ObjectMapper objectMapper = new ObjectMapper();

        public StreamingServlet( CompiledGraph<State> compiledGraph ) {
            Objects.requireNonNull(compiledGraph, "compiledGraph cannot be null");
            this.compiledGraph = compiledGraph;
        }

        private AsyncGenerator<String> streamTest() {

            List<String> chunks = Arrays.asList(
                    "a", "b", "c", "d", "e",
                    "f", "g", "h", "i", "j",
                    "k", "l", "m", "n", "o",
                    "p", "q", "r", "s", "t",
                    "u", "v", "w", "x", "y", "z");

            return AsyncGenerator.map( chunks,
                    s -> CompletableFuture.completedFuture( "chunk " + s) );

        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setHeader("Accept", "application/json");
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");

            final PrintWriter writer = response.getWriter();

            Map<String, Object> dataMap = objectMapper.readValue(request.getInputStream(), new TypeReference<Map<String, Object>>() {});

            // Start asynchronous processing
            request.startAsync();

            try {
                compiledGraph.stream(dataMap)
                    .forEachAsync( s -> {
                        writer.println(s.node());
                        writer.flush();
                }).thenAccept( v -> {
                    writer.close();
                });

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * return the graph representation in mermaid format
     */
    public static class GraphServlet<State extends AgentState> extends HttpServlet {

        final CompiledGraph<State> compiledGraph;

        public GraphServlet( CompiledGraph<State> compiledGraph ) {
            Objects.requireNonNull(compiledGraph, "compiledGraph cannot be null");
            this.compiledGraph = compiledGraph;
        }

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");

            GraphRepresentation result = compiledGraph.getGraph(GraphRepresentation.Type.MERMAID);

            // Start asynchronous processing
            request.startAsync();
            final PrintWriter writer = response.getWriter();
            writer.println(result);
            writer.close();
        }
    }

}