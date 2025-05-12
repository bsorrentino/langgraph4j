package org.bsc.langgraph4j.spring.ai.agentexecutor;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * it should be considered and internal internal
 *
 * @deprecated
 */
@Deprecated
public interface ChatService {

    ChatResponse execute(List<Message> messages );

    List<ToolCallback> tools();

    default Flux<ChatResponse> streamingExecute(List<Message> messages) {
        return Flux.just( execute( messages ));
    }

}
