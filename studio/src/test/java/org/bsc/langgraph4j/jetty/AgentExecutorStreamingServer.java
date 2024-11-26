package org.bsc.langgraph4j.jetty;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bsc.langgraph4j.DotEnvConfig;
import org.bsc.langgraph4j.TestTool;
import org.bsc.langgraph4j.agentexecutor.AgentExecutor;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.bsc.langgraph4j.studio.jetty.LangGraphStreamingServerJetty;

public class AgentExecutorStreamingServer {

    public static void main(String[] args) throws Exception {

        DotEnvConfig.load();

        var openApiKey = DotEnvConfig.valueOf("OPENAI_API_KEY")
                .orElseThrow( () -> new IllegalArgumentException("no APIKEY provided!"));

        var llm = OpenAiChatModel.builder()
                .apiKey( openApiKey )
                .modelName( "gpt-4o-mini" )
                .logResponses(true)
                .maxRetries(2)
                .temperature(0.0)
                .maxTokens(2000)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        // [Serializing with Jackson (JSON) - getting "No serializer found"?](https://stackoverflow.com/a/8395924/521197)
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        var app = AgentExecutor.builder()
                .chatLanguageModel(llm)
                .toolSpecification( new TestTool() )
                .stateSerializer( AgentExecutor.Serializers.JSON.object() )
                .build();


        var server = LangGraphStreamingServerJetty.builder()
                .port(8080)
                .objectMapper(objectMapper)
                .title("AGENT EXECUTOR")
                .addInputStringArg("input")
                .stateGraph(app)
                .build();

        server.start().join();

    }

}
