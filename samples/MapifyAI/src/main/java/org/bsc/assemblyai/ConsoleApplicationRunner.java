package org.bsc.assemblyai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.core.io.Resource;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * ConsoleApplicationRunner is a {@link CommandLineRunner} implementation designed to run the application.
 * It reads a conversation file, processes it using the {@link AgenticFlow}, and prints the generated outputs to the console.
 *
 * <p>This class is annotated with {@code @Component} to indicate that it should be managed by Spring's dependency injection container.</p>
 */
@Component
public class ConsoleApplicationRunner implements CommandLineRunner {

    /**
     * The conversation file resource to be used for processing.
     * This field is annotated with {@code @Value} to inject the value from the application properties.
     */
    @Value("classpath:conversation-test01.txt")
    private Resource conversation01;

    /**
     * An instance of {@link AgenticFlow}, which is responsible for processing the conversation data and generating outputs.
     */
    final AgenticFlow agenticFlow;

    /**
     * Constructs a new ConsoleApplicationRunner with an instance of {@link AgenticFlow}.
     *
     * @param agenticFlow The {@link AgenticFlow} instance to be used for processing.
     *                    This parameter is annotated with {@code @Autowired} to enable constructor-based dependency injection.
     */
    public ConsoleApplicationRunner( AgenticFlow agenticFlow ) {
        this.agenticFlow = agenticFlow;
    }

    /**
     * Runs the application logic.
     *
     * <p>This method reads the conversation file, processes it using the {@link AgenticFlow}, and prints the generated outputs to the console.</p>
     *
     * @param args Command line arguments passed to the application.
     * @throws Exception if an error occurs during execution.
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
