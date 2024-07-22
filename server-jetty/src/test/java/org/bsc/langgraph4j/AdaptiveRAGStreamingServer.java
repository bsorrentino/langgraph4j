package org.bsc.langgraph4j;

import dev.langchain4j.adaptiverag.AdaptiveRag;

public class AdaptiveRAGStreamingServer {

    public static void main(String[] args) throws Exception {

        DotEnvConfig.load();

        var openApiKey = DotEnvConfig.valueOf("OPENAI_API_KEY")
                .orElseThrow( () -> new IllegalArgumentException("no OPENAI API KEY provided!"));
        var tavilyApiKey = DotEnvConfig.valueOf("TAVILY_API_KEY")
                .orElseThrow( () -> new IllegalArgumentException("no TAVILY API KEY provided!"));

        var adaptiveRagTest = new AdaptiveRag( openApiKey, tavilyApiKey);

        var app = adaptiveRagTest.buildGraph();

        // [Serializing with Jackson (JSON) - getting "No serializer found"?](https://stackoverflow.com/a/8395924/521197)
        // ObjectMapper objectMapper = new ObjectMapper();
        // objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        System.out.println (
                app.getGraph(GraphRepresentation.Type.MERMAID, "ADAPTIVE RAG EXECUTOR", false)
                        .getContent()
        );

        var server = LangGraphStreamingServer.builder()
                .port(8080)
                //.objectMapper(objectMapper)
                .title("ADAPTIVE RAG EXECUTOR")
                .addInputStringArg("question")
                .build(app);

        server.start().join();

    }

}
