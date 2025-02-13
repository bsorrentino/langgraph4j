package org.bsc.langgraph4j.studio.springboot;

import org.bsc.langgraph4j.studio.LangGraphStreamingServer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

public abstract class AbstractLangGraphStudioConfig {

    public abstract LangGraphFlow getFlow();

    @Bean
    public ServletRegistrationBean<LangGraphStreamingServer.GraphInitServlet> initServletBean() {

        var flow = getFlow();

        var initServlet = new LangGraphStreamingServer.GraphInitServlet(flow.stateGraph(), flow.title(), flow.inputArgs());
        var bean = new ServletRegistrationBean<>(
                initServlet, "/init");
        bean.setLoadOnStartup(1);
        return bean;
    }

    @Bean
    public ServletRegistrationBean<LangGraphStreamingServer.GraphStreamServlet> streamingServletBean() {

        var flow = getFlow();

        var initServlet = new LangGraphStreamingServer.GraphStreamServlet(flow.stateGraph(), flow.saver());
        var bean = new ServletRegistrationBean<>(
                initServlet, "/stream");
        bean.setLoadOnStartup(1);
        return bean;
    }

}
