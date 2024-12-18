package org.bsc.spring;

import lombok.extern.slf4j.Slf4j;
import org.bsc.spring.agentexecutor.AgentExecutor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.util.Map;

/**
 * Demonstrates the use of Spring Boot CLI to execute a task using an agent executor.
 */
@Slf4j
@Controller
public class DemoConsoleController implements CommandLineRunner {
    private final AgentExecutor agentExecutor;

    public DemoConsoleController(AgentExecutor agentExecutor) {
        this.agentExecutor = agentExecutor;
    }

    /**
     * Executes the command-line interface to demonstrate a Spring Boot application.
     * This method logs a welcome message, constructs a graph using an agent executor,
     * compiles it into a workflow, invokes the workflow with a specific input,
     * and then logs the final result.
     *
     * @param args Command line arguments (Unused in this context)
     * @throws Exception If any error occurs during execution
     */
    @Override
    public void run(String... args) throws Exception {

        log.info("Welcome to the Spring Boot CLI application!");

        var graph = agentExecutor.graphBuilder()
                                            .build();

        var workflow = graph.compile();

        var result = workflow.invoke( Map.of(AgentExecutor.State.INPUT, "what is the weather in Napoli ?") );

        var finish = result.flatMap(AgentExecutor.State::agentOutcome)
                            .map(AgentExecutor.Outcome::finish)
                            .orElseThrow();

        log.info( "result: {}", finish.returnValues() );
    }
}