package org.bsc.langgraph4j.multi_agent;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.structured.Description;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import org.bsc.langgraph4j.action.NodeAction;

import java.util.Map;

public class HandoffProcessor implements NodeAction<MultiAgentHandoff.State> {

    record HandoffEvaluationResult(
            @Description("Is it and handoff request") @JsonProperty(required = true) Boolean isHandoff,
            @Description("handoff function name - (example: handoff_to_<agent name>)") @JsonProperty(required = true) String handoffFunction,
            @Description("comprehensive handoff information") @JsonProperty(required = true) String handoffInput ) {}

    interface Service {

        @SystemMessage("""
                You must evaluate input to evaluate if request needs handoff to another agent
                """)
        HandoffEvaluationResult evaluate( String agentResponse );
    }

    private final Service service;

    public HandoffProcessor(ChatLanguageModel model ) {
        this.service = AiServices.create( Service.class, model);
    }

    @Override
    public Map<String, Object> apply(MultiAgentHandoff.State state) throws Exception {
        var agentResponse = state.lastAgentResponse().orElseThrow();

        var result = service.evaluate( agentResponse );

        return Map.of();
    }
}
