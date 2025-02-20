package org.bsc.quarkus;

import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.studio.LangGraphStreamingServer;

import java.io.IOException;

@WebServlet(name = "GraphStreamServlet", urlPatterns = "/stream", asyncSupported = true)
public class GraphStreamServletProxy extends HttpServlet {

    private Servlet servlet;

    @Inject
    LangGraphFlowService flowService;

    public GraphStreamServletProxy() {
        super();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            var flow = flowService.getFlow();

            servlet = new LangGraphStreamingServer.GraphStreamServlet(flow.stateGraph(), flow.saver());

            servlet.init(config);
        } catch (GraphStateException e) {
            throw new ServletException(e);
        }
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        servlet.service(req, res);
    }

}
