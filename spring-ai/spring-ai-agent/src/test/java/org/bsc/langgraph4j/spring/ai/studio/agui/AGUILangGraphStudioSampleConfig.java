package org.bsc.langgraph4j.spring.ai.studio.agui;

import org.bsc.langgraph4j.CompileConfig;
import org.bsc.langgraph4j.action.InterruptionMetadata;
import org.bsc.langgraph4j.agui.sdk.AGUIAgentRegistry;
import org.bsc.langgraph4j.checkpoint.MemorySaver;
import org.bsc.langgraph4j.spring.ai.AIModel;
import org.bsc.langgraph4j.spring.ai.agentexecutor.AgentExecutor;
import org.bsc.langgraph4j.spring.ai.agentexecutor.AgentExecutorEx;
import org.bsc.langgraph4j.spring.ai.serializer.jackson.SpringAIJacksonStateSerializer;
import org.bsc.langgraph4j.spring.ai.tool.TestTools;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.studio.agui.AGUILangGraphStudioAgent;
import org.bsc.langgraph4j.studio.serializer.NodeOutputSerializer;
import org.bsc.langgraph4j.studio.springboot.agui.AGUILangGraphStudioBaseConfig;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.function.Function;

@Configuration
public class AGUILangGraphStudioSampleConfig extends AGUILangGraphStudioBaseConfig {

    @Override
    protected AGUIAgentRegistry agentRegistry() throws Exception {

        final var stateSerializer = new SpringAIJacksonStateSerializer<>(AgentState::new);

        aguiSerializer().serializeAttributes(
                Map.of(NodeOutputSerializer.STATE_SERIALIZER, stateSerializer)
        );

        final var chatModel = AIModel.OLLAMA.chatModel("qwen2.5:7b");
        final var tools = new TestTools();

        Function<com.agui.community.core.message.UserMessage, UserMessage> userMessageTextConverter =
                v ->
                    UserMessage.builder()
                            .text(v.content())
                            .build();

        final var agent_graph1 = AgentExecutorEx.builder()
                .chatModel(chatModel)
                .streaming(true)
                .approvalOn( "threadCount", (nodeId, state) ->
                        InterruptionMetadata.builder( nodeId, state ).build())
                .toolsFromObject(tools)
                .build();

        final var agent_graph2 = AgentExecutor.builder()
                .chatModel(chatModel)
                .streaming(true)
                .toolsFromObject(tools)
                .build();

        final var agent1 = AGUILangGraphStudioAgent.builder()
                .id("agent_executor_ex")
                .title("LangGraph Studio (Agent Executor Extension)")
                .stateGraph(agent_graph1)
                .addInputStringArg( "messages", true, userMessageTextConverter )

                .compileConfig( CompileConfig.builder()
                        .checkpointSaver( new MemorySaver() )
                        .build())
                .build();

        final var agent2 = AGUILangGraphStudioAgent.builder()
                .id("agent_executor")
                .title("LangGraph Studio (Agent Executor)")
                .stateGraph(agent_graph2)
                .addInputStringArg( "messages", true, userMessageTextConverter )
                .compileConfig( CompileConfig.builder()
                        .checkpointSaver( new MemorySaver() )
                                .build())
                        .build();

        return new AGUIAgentRegistry( agent1, agent2 );
    }

}
