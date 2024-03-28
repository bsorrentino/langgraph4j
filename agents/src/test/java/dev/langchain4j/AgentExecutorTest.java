package dev.langchain4j;

import dev.langchain4j.model.openai.OpenAiChatModel;

import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;

public class AgentExecutorTest {

    public static void main( String[] args)  {

        DotEnvConfig.load();

        var openApiKey = DotEnvConfig.valueOf("OPENAI_API_KEY")
                .orElseThrow( () -> new IllegalArgumentException("no APIKEY provided!"));

        var chatLanguageModel = OpenAiChatModel.builder()
                .apiKey( openApiKey )
                .modelName( "gpt-3.5-turbo-0125" )
                .logResponses(true)
                .maxRetries(2)
                .temperature(0.0)
                .maxTokens(2000)
                .build();

        try {
            var agentExecutor = new AgentExecutor();

            var iterator = agentExecutor.execute(
                    chatLanguageModel,
                    Map.of( "input", "what is the result of test with messages: 'MY FIRST TEST' and the result of test with message: 'MY SECOND TEST'"),
                    //Map.of( "input", "what is the result of test with messages: 'MY FIRST TEST'"),
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
