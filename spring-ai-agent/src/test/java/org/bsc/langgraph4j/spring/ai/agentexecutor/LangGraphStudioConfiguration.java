package org.bsc.langgraph4j.spring.ai.agentexecutor;

import org.bsc.langgraph4j.CompileConfig;
import org.bsc.langgraph4j.GraphRepresentation;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.checkpoint.MemorySaver;
import org.bsc.langgraph4j.studio.springboot.AbstractLangGraphStudioConfig;
import org.bsc.langgraph4j.studio.springboot.LangGraphFlow;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Objects;

@Configuration
public class LangGraphStudioConfiguration extends AbstractLangGraphStudioConfig {

	final LangGraphFlow flow;

	public LangGraphStudioConfiguration( /* @Qualifier("ollama") */ ChatModel chatModel, List<ToolCallback> tools)
			throws GraphStateException {

		var workflow = AgentExecutor.builder().chatModel(chatModel).tools(tools).build();

		var mermaid = workflow.getGraph(GraphRepresentation.Type.MERMAID, "ReAct Agent", false);
		System.out.println(mermaid.content());

		this.flow = agentWorkflow(workflow);
	}

	private LangGraphFlow agentWorkflow(StateGraph<AgentExecutor.State> workflow) throws GraphStateException {

		return LangGraphFlow.builder()
			.title("LangGraph Studio (Spring AI)")
			.addInputStringArg("messages", true, v -> new UserMessage(Objects.toString(v)))
			.stateGraph(workflow)
			.compileConfig(CompileConfig.builder().checkpointSaver(new MemorySaver()).build())
			.build();

	}

	@Override
	public LangGraphFlow getFlow() {
		return this.flow;
	}

}
