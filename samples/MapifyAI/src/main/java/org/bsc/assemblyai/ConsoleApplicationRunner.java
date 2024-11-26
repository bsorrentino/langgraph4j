package org.bsc.assemblyai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.core.io.Resource;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class ConsoleApplicationRunner implements CommandLineRunner {

    @Value("classpath:conversation-test01.txt")
    private Resource conversation01;

    final AgenticFlow agenticFlow;

    public ConsoleApplicationRunner( AgenticFlow agenticFlow ) {
        this.agenticFlow = agenticFlow;
    }


    @Override
    public void run(String... args) throws Exception {

        var app = agenticFlow.buildGraph().compile();

        var conversation = conversation01.getContentAsString(StandardCharsets.UTF_8);
        var generator = app.stream( Map.of("conversation", conversation) );

        for( var output : generator ) {
            System.out.println( output );
        }
    }


}