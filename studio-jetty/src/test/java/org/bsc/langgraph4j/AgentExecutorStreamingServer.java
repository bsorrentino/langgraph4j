package org.bsc.langgraph4j;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bsc.langgraph4j.agentexecutor.AgentExecutor;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.bsc.langgraph4j.agentexecutor.serializer.json.JSONStateSerializer;

import static org.bsc.langgraph4j.utils.CollectionsUtils.listOf;

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

        var app = new AgentExecutor().graphBuilder()
                .chatLanguageModel(llm)
                .objectsWithTools(listOf(new TestTool()))
                .stateSerializer( JSONStateSerializer.of(objectMapper) )
                .build();


        var server = LangGraphStreamingServer.builder()
                .port(8080)
                .objectMapper(objectMapper)
                .title("AGENT EXECUTOR")
                .addInputStringArg("input")
                .stateGraph(app)
                .build();

        server.start().join();

    }

}
