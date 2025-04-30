package org.bsc.langgraph4j.multi_agent;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.bsc.langgraph4j.action.EdgeAction;
import org.bsc.langgraph4j.multi_agent.executor.AgentExecutor;
import org.bsc.langgraph4j.prebuilt.MessagesState;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MultiAgentHandoff {

    class State extends MessagesState<ChatMessage> {

        public static final String AGENT_RESPONSE = AgentExecutor.State.FINAL_RESPONSE;
        public static final String HANDOFF_FLAG = "handoff_flag";
        public static final String HANDOFF_FUNCTION = "handoff_function";
        public static final String HANDOFF_INPUT = "handoff_input";

        public State(Map<String, Object> initData) {
            super(initData);
        }

        Optional<String> lastAgentResponse() {
            return this.<String>value(AGENT_RESPONSE);
        }

        boolean isHandoff() {
            return this.<Boolean>value(HANDOFF_FLAG).orElse(false);
        }

        Optional<String> handoffFunction() {
            return this.<String>value(HANDOFF_FUNCTION);
        }

        Optional<String> handoffInput() {
            return this.<String>value(HANDOFF_INPUT);
        }
    }

    record AgentMetadata( String node, String description) {}

    class StartRoutingEdge implements EdgeAction<State> {

        final ChatLanguageModel model ;
        final List<AgentMetadata> agents ;

        public StartRoutingEdge(List<AgentMetadata> agents, ChatLanguageModel model) {
            this.model = model;
            this.agents = agents;
        }

        @Override
        public String apply(State t) throws Exception {
            return "";
        }
    }

    class HandoffRoutingEdge implements EdgeAction<State> {
        final ChatLanguageModel model ;
        final List<AgentMetadata> agents ;

        public HandoffRoutingEdge(List<AgentMetadata> agents, ChatLanguageModel model) {
            this.model = model;
            this.agents = agents;
        }

        @Override
        public String apply(State t) throws Exception {
            return "";
        }
    }

}
