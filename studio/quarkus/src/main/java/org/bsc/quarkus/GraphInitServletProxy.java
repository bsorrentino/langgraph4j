package org.bsc.quarkus;

import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.studio.LangGraphStreamingServer;

import java.io.IOException;

@WebServlet(name = "GraphInitServlet", urlPatterns = "/init")
public class GraphInitServletProxy extends HttpServlet {

    private final Servlet servlet;

    public GraphInitServletProxy( LangGraphFlow flow ) {
        super();
        servlet = new LangGraphStreamingServer.GraphInitServlet(flow.stateGraph(), flow.title(), flow.inputArgs());
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        servlet.init(config);
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        servlet.service(req, res);
    }
}