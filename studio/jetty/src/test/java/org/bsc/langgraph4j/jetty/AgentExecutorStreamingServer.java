package org.bsc.langgraph4j.jetty;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.bsc.langgraph4j.TestTool;
import org.bsc.langgraph4j.agentexecutor.AgentExecutor;
import org.bsc.langgraph4j.studio.jetty.LangGraphStreamingServerJetty;

import java.util.Objects;

public class AgentExecutorStreamingServer {

    public static void main(String[] args) throws Exception {

        var llm = OllamaChatModel.builder()
                .baseUrl( "http://localhost:11434" )
                .temperature(0.0)
                .logRequests(true)
                .logResponses(true)
                .modelName("qwen2.5:7b")
                .build();

        var app = AgentExecutor.builder()
                .chatModel(llm)
                .toolsFromObject( new TestTool() )
                .stateSerializer( AgentExecutor.Serializers.JSON.object() )
                .build();

        var server = LangGraphStreamingServerJetty.builder()
                .port(8080)
                .title("AGENT EXECUTOR")
                .addInputStringArg("messages", true, v -> SystemMessage.from(Objects.toString(v)))
                .stateGraph(app)
                .build();

        server.start().join();

    }

}
