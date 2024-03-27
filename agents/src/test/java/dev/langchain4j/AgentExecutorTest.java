package dev.langchain4j;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

public class AgentExecutorTest {

    public static void main( String[] args)  {

        DotEnvConfig.load();

        var agentExecutor = new AgentExecutor();

        try {
            var iterator = agentExecutor.execute(
                    Map.of( "input", "what is the result of test with message: 'MY FIRST TEST'?"),
                    List.of(new TestTool()) );

           AgentExecutor.State output = null;

            for( var i : iterator ) {
                output = i.state();
                System.out.println(i.node());
            }
            System.out.println( "Finished! " + ofNullable(output)
                                                .flatMap(AgentExecutor.State::agentOutcome)
                                                .map(AgentExecutor.AgentOutcome::finish)
                                                .map(AgentExecutor.AgentFinish::returnValues)
                                                .orElse(Map.of( "result", "state undefined!")) );

        } catch (Exception e) {
            System.out.println( "ERROR! "  + e.getMessage() );
        }
        System.exit(0);

    }
}
