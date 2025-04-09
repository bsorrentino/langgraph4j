package org.bsc.langgraph4j.studio.jetty;

import org.bsc.langgraph4j.*;
import org.bsc.langgraph4j.checkpoint.MemorySaver;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.studio.LangGraphStreamingServer;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.ResourceFactory;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;


/**
 * Represents a streaming server for LangGraph using Jetty.
 * LangGraphStreamingServer is an interface that represents a server that supports streaming
 * of LangGraph. Implementations of this interface can be used to create a web server that exposes an API for interacting with compiled language graphs.
 */
public class LangGraphStreamingServerJetty implements LangGraphStreamingServer {

    final Server server;

    /**
     * Constructs a LangGraphStreamingServerJetty with the specified server.
     *
     * @param server the server to be used by the streaming server
     * @throws NullPointerException if the server is null
     */
    private LangGraphStreamingServerJetty(Server server) {
        Objects.requireNonNull(server, "server cannot be null");
        this.server = server;
    }

    /**
     * Starts the streaming server asynchronously.
     *
     * @return a CompletableFuture that completes when the server has started
     * @throws Exception if an error occurs while starting the server
     */
    public CompletableFuture<Void> start() throws Exception {
        return CompletableFuture.runAsync(() -> {
            try {
                server.start();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, Runnable::run);
    }

    /**
     * Creates a new Builder for LangGraphStreamingServerJetty.
     *
     * @return a new Builder instance
     */
    static public Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for constructing LangGraphStreamingServerJetty instances.
     */
    public static class Builder {
        private int port = 8080;
        private final List<ArgumentMetadata> inputArgs = new ArrayList<>();
        private String title = null;
        private StateGraph<? extends AgentState> stateGraph;
        private CompileConfig compileConfig;

        /**
         * Sets the port for the server.
         *
         * @param port the port number
         * @return the Builder instance
         */
        public Builder port(int port) {
            this.port = port;
            return this;
        }

        /**
         * Sets the title for the server.
         *
         * @param title the title to be set
         * @return the Builder instance
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder addInputStringArg(String name, boolean required, Function<Object,Object> converter) {
            inputArgs.add(new ArgumentMetadata(name, ArgumentMetadata.ArgumentType.STRING, required, converter));
            return this;
        }

        /**
         * Adds an input string argument to the server configuration.
         *
         * @param name the name of the argument
         * @param required whether the argument is required
         * @return the Builder instance
         */
        public Builder addInputStringArg(String name, boolean required) {
            return addInputStringArg( name, required, null);
        }

        /**
         * Adds an input string argument to the server configuration with required set to true.
         *
         * @param name the name of the argument
         * @return the Builder instance
         */
        public Builder addInputStringArg(String name) {
            return addInputStringArg(name, true, null);
        }

        /**
         * Adds an input image argument to the server configuration.
         *
         * @param name the name of the argument
         * @param required whether the argument is required
         * @return the Builder instance
         */
        public Builder addInputImageArg(String name, boolean required) {
            inputArgs.add(new ArgumentMetadata(name, ArgumentMetadata.ArgumentType.IMAGE, required));
            return this;
        }

        /**
         * Adds an input image argument to the server configuration with required set to true.
         *
         * @param name the name of the argument
         * @return the Builder instance
         */
        public Builder addInputImageArg(String name) {
            return addInputImageArg(name, true);
        }

        /**
         * Sets the state graph for the server.
         *
         * @param stateGraph the state graph to be used
         * @param <State> the type of the state
         * @return the Builder instance
         */
        public <State extends AgentState> Builder stateGraph(StateGraph<State> stateGraph) {
            this.stateGraph = stateGraph;
            return this;
        }

        public Builder compileConfig(CompileConfig compileConfig) {
            this.compileConfig = compileConfig;
            return this;
        }

        /**
         * Builds and returns a LangGraphStreamingServerJetty instance.
         *
         * @return a new LangGraphStreamingServerJetty instance
         * @throws NullPointerException if the stateGraph is null
         * @throws Exception if an error occurs during server setup
         */
        public LangGraphStreamingServerJetty build() throws Exception {
            Objects.requireNonNull(stateGraph, "stateGraph cannot be null");

            if( compileConfig == null ) {
                compileConfig = CompileConfig.builder()
                        .checkpointSaver(  new MemorySaver() )
                        .build();
            }

            if( compileConfig.checkpointSaver().isEmpty() ) {
                compileConfig = CompileConfig.builder(compileConfig)
                        .checkpointSaver(  new MemorySaver() )
                        .build();
            }

            Server server = new Server();

            ServerConnector connector = new ServerConnector(server);
            connector.setPort(port);
            server.addConnector(connector);

            var resourceHandler = new ResourceHandler();

            var baseResource = ResourceFactory.of(resourceHandler).newClassLoaderResource("webapp");
            resourceHandler.setBaseResource(baseResource);

            resourceHandler.setDirAllowed(true);

            var context = new ServletContextHandler(ServletContextHandler.SESSIONS);

            context.setSessionHandler(new org.eclipse.jetty.ee10.servlet.SessionHandler());

            context.addServlet(new ServletHolder(new GraphInitServlet(stateGraph, title, inputArgs)), "/init");

            context.addServlet(new ServletHolder(new GraphStreamServlet(stateGraph, compileConfig, inputArgs)), "/stream");

            var handlerList = new Handler.Sequence(resourceHandler, context);

            server.setHandler(handlerList);

            return new LangGraphStreamingServerJetty(server);
        }
    }
}
