package org.bsc.langgraph4j.multi_agent.lc4j;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.tool.ToolExecutor;
import dev.langchain4j.service.AiServices;

import java.util.Map;

public abstract class AbstractAgentService<B extends AbstractAgentService.Builder<B>> extends AbstractAgent<B> {

    public static abstract class Builder<B extends AbstractAgentService.Builder<B>> extends AbstractAgent.Builder<B> {

        final AiServices<Service> delegate ;

        public Builder() {
            this.delegate = AiServices.builder( Service.class );
        }

        public B chatModel(ChatModel model) {
            delegate.chatModel(model);
            return result();
        }

        public B tools(Map.Entry<ToolSpecification, ToolExecutor> entry) {
            delegate.tools( entry );
            return result();
        }

        public B toolFromObject( Object objectWithTools ) {
            delegate.tools(objectWithTools);
            return result();
        }

        public B systemMessage(SystemMessage message) {
            delegate.systemMessageProvider( ( param ) -> message.text() );
            return result();
        }
    }

    interface Service {

        String execute(@UserMessage String message, @MemoryId Object memoryId);
    }

    private final Service agentService;

    public AbstractAgentService( Builder<B> builder )  {
        super( builder );

        agentService = builder.delegate.build();
    }

    @Override
    public String execute(ToolExecutionRequest toolExecutionRequest, Object o) {

        return agentService.execute( toolExecutionRequest.arguments(), o );

    }

}
