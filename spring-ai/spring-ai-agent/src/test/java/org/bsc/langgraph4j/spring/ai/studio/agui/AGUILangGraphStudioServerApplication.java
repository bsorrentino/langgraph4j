package org.bsc.langgraph4j.spring.ai.studio.agui;

import org.bsc.langgraph4j.spring.ai.CORSFilterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({AGUILangGraphStudioSampleConfig.class,
        CORSFilterConfig.class})
public class AGUILangGraphStudioServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AGUILangGraphStudioServerApplication.class, args);
    }
}