package org.bsc.langgraph4j.multi_agent.lc4j;

import dev.langchain4j.model.chat.ChatModel;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.agentexecutor.AgentExecutor;

import java.util.Objects;

public interface AgentHandoff {

   class Builder {
        final AgentExecutor.Builder delegate = AgentExecutor.builder();

        public Builder chatModel(ChatModel model) {
            delegate.chatModel(model);
            return this;
        }

        public <B extends AbstractAgent.Builder<B>> Builder agent(AbstractAgent<B> agent) {
            delegate.tool( Objects.requireNonNull(agent, "agent cannot be null").asTool() );
            return this;
        }

        public StateGraph<AgentExecutor.State> build() throws GraphStateException {
            return delegate.build();
        }

    }

    static Builder builder() {
        return new Builder();
    }


}
