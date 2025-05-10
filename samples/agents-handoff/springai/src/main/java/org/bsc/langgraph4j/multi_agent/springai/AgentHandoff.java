package org.bsc.langgraph4j.multi_agent.springai;


import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.spring.ai.agentexecutor.AgentExecutor;
import org.bsc.langgraph4j.serializer.StateSerializer;
import org.springframework.ai.chat.model.ChatModel;

import java.util.Objects;

public interface AgentHandoff {

    class Builder {

        AgentExecutor.Builder delegate = AgentExecutor.builder();

        public Builder chatModel(ChatModel chatClient) {
            this.delegate.chatModel(chatClient);
            return this;
        }

        public Builder defaultSystem(String systemMessage) {
            this.delegate.defaultSystem(systemMessage);
            return this;
        }

        public Builder stateSerializer(StateSerializer<AgentExecutor.State> stateSerializer) {
            this.delegate.stateSerializer(stateSerializer);
            return this;
        }

        public <I,O,B extends AbstractAgent.Builder<B>> Builder agent(AbstractAgent<I,O,B> agent) {
            this.delegate.tool( Objects.requireNonNull(agent, "agent cannot be null").asTool() );
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
