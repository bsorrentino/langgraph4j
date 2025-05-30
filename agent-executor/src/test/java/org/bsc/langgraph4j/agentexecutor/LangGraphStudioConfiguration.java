package org.bsc.langgraph4j.agentexecutor;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.bsc.langgraph4j.CompileConfig;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.checkpoint.MemorySaver;
import org.bsc.langgraph4j.studio.springboot.AbstractLangGraphStudioConfig;
import org.bsc.langgraph4j.studio.springboot.LangGraphFlow;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class LangGraphStudioConfiguration extends AbstractLangGraphStudioConfig {

    final LangGraphFlow flow;

    public LangGraphStudioConfiguration() throws GraphStateException {

        var chatLanguageModel = OpenAiChatModel.builder()
                .apiKey( System.getenv( "OPENAI_API_KEY" ) )
                .modelName( "gpt-4o-mini" )
                .logResponses(true)
                .maxRetries(2)
                .temperature(0.0)
                .maxTokens(2000)
                .build();

        var workflow =  AgentExecutor.builder()
                .chatModel(chatLanguageModel)
                .toolsFromObject(new TestTool())
                .build();

        this.flow = agentWorkflow( workflow );
    }

    private LangGraphFlow agentWorkflow( StateGraph<AgentExecutor.State> workflow ) throws GraphStateException {

        return  LangGraphFlow.builder()
                .title("LangGraph Studio (LangChain4j)")
                .addInputStringArg( "messages", true, v -> UserMessage.from( Objects.toString(v) ) )
                .stateGraph( workflow )
                .compileConfig( CompileConfig.builder()
                        .checkpointSaver( new MemorySaver() )
                        .build())
                .build();

    }

    @Override
    public LangGraphFlow getFlow() {
        return this.flow;
    }
}
