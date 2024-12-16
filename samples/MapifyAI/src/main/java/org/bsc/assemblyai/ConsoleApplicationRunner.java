package org.bsc.assemblyai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.core.io.Resource;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Component: This class implements the CommandLineRunner interface and is part of a Spring application.
 * It initializes and runs the console application using the_agenticFlow_ service and a conversation resource.
 */
@Component
public class ConsoleApplicationRunner implements CommandLineRunner {

    /**
     * Injection: A Resource object for accessing "conversation-test01.txt".
     * This file is used to retrieve conversation content by the application.
     */
    @Value("classpath:conversation-test01.txt")
    private Resource conversation01;

    /**
     * Dependency injection: Final AgenticFlow instance, representing the service responsible for building and compiling flow graphs.
     * The application will use this service to generate outputs based on a conversation.
     */
    final AgenticFlow agenticFlow;

    /**
     * Constructor: Initializes the ConsoleApplicationRunner with an AgenticFlow instance.
     * @param agenticFlow Instance of the AgenticFlow service.
     */
    public ConsoleApplicationRunner(AgenticFlow agenticFlow) {
        this.agenticFlow = agenticFlow;
    }

    /**
     * Operation: Implements the run method from CommandLineRunner interface. 
     * Initializes and runs the application, loading a conversation resource, compiling it with agenticFlow,
     * and generating outputs based on the conversation.
     */
    @Override
    public void run(String... args) throws Exception {
        // Builds and compiles graph using agenticFlow
        var app = agenticFlow.buildGraph().compile();

        // Reads conversation content from resource
        var conversation = conversation01.getContentAsString(StandardCharsets.UTF_8);
        
        // Streams output based on the compiled application and the conversation
        var generator = app.stream(Map.of("conversation", conversation));

        // Prints each generated output to the console
        for (var output : generator) {
            System.out.println(output);
        }
    }
}
