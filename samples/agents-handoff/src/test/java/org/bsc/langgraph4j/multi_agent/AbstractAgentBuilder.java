package org.bsc.langgraph4j.multi_agent;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.tool.ToolExecutor;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.langchain4j.tool.LC4jToolService;
import org.bsc.langgraph4j.multi_agent.executor.AgentExecutor;

import java.util.Map;

public abstract class AbstractAgentBuilder<T> {

    final AgentExecutor.Builder delegate = AgentExecutor.builder();

    public AbstractAgentBuilder<T> chatLanguageModel(ChatLanguageModel model ) {
        delegate.chatLanguageModel(model);
        return this;
    }

    public AbstractAgentBuilder<T> tool(Map.Entry<ToolSpecification, ToolExecutor> entry) {
        delegate.tool(entry);
        return this;
    }

    abstract public T build() throws GraphStateException;


}
