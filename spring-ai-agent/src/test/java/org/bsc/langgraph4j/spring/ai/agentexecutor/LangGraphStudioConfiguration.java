package org.bsc.langgraph4j.spring.ai.agentexecutor;

import org.bsc.langgraph4j.CompileConfig;
import org.bsc.langgraph4j.GraphRepresentation;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.checkpoint.MemorySaver;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.studio.springboot.AbstractLangGraphStudioConfig;
import org.bsc.langgraph4j.studio.springboot.LangGraphFlow;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class LangGraphStudioConfiguration extends AbstractLangGraphStudioConfig {

    final LangGraphFlow flow;

    public LangGraphStudioConfiguration( /*@Qualifier("ollama")*/ ChatModel chatModel ) throws GraphStateException {

        var workflow = AgentExecutorEx.builder()
                .chatModel( chatModel )
                .toolsFromObject( new TestTool() )
                .build()
                ;

        var mermaid = workflow.getGraph( GraphRepresentation.Type.MERMAID, "ReAct Agent", false );
        System.out.println( mermaid.content() );

        this.flow = agentWorkflow( workflow );
    }

    private LangGraphFlow agentWorkflow( StateGraph<? extends AgentState> workflow ) throws GraphStateException {

        return  LangGraphFlow.builder()
                .title("LangGraph Studio (Spring AI)")
                .addInputStringArg( "messages", true, v -> new UserMessage( Objects.toString(v) ) )
                .stateGraph( workflow )
                .compileConfig( CompileConfig.builder()
                        .checkpointSaver( new MemorySaver() )
                        .releaseThread(true)
                        .build())
                .build();

    }

    @Override
    public LangGraphFlow getFlow() {
        return this.flow;
    }
}
