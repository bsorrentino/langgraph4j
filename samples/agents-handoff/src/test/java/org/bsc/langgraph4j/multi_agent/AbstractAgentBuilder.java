package org.bsc.langgraph4j.multi_agent;

import dev.langchain4j.model.chat.ChatLanguageModel;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.langchain4j.tool.LC4jToolService;
import org.bsc.langgraph4j.multi_agent.executor.AgentExecutor;

public abstract class AbstractAgentBuilder<T> {

    final AgentExecutor.Builder delegate = AgentExecutor.builder();

    public AbstractAgentBuilder<T> chatLanguageModel(ChatLanguageModel model ) {
        delegate.chatLanguageModel(model);
        return this;
    }

    public AbstractAgentBuilder<T> toolSpecification(LC4jToolService.Specification tool ) {
        delegate.toolSpecification(tool);
        return this;
    }

    abstract public T build() throws GraphStateException;


}
