package org.bsc.langgraph4j.multi_agent;

import dev.langchain4j.data.message.AiMessage;
import org.bsc.langgraph4j.action.NodeAction;

import java.util.Map;

public class HandoffPostProcessor implements NodeAction<MultiAgentHandoff.State>  {


    @Override
    public Map<String, Object> apply(MultiAgentHandoff.State state) throws Exception {
        var handoffInput = state.handoffInput()
                .map( input -> AiMessage.builder().text(input).build() )
                .orElseThrow();

        return Map.of( "messages", handoffInput );
    }
}
