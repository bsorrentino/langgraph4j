package org.bsc.langgraph4j;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import java.util.concurrent.TimeUnit;

public class JettyStreamingServer {

    public static void main(String[] args) throws Exception {
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
        context.addServlet(new ServletHolder(new StreamingServlet()), "/stream");

        Handler.Sequence handlerList = new Handler.Sequence(resourceHandler, context );

        server.setHandler(handlerList);

        server.start();
        server.join();
    }

    public static class StreamingServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");

            final PrintWriter writer = response.getWriter();

            // Start asynchronous processing
            request.startAsync();

            // Simulate a long-running process
            new Thread(() -> {
                try {
                    for (int i = 0; i < 10; i++) {
                        writer.println("Line " + i);
                        writer.flush();
                        TimeUnit.SECONDS.sleep(1);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    writer.close();
                }
            }).start();
        }
    }
}