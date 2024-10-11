package org.bsc.langgraph4j.langchain4j.serializer.std;

import dev.langchain4j.data.message.*;

import org.bsc.langgraph4j.serializer.Serializer;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class ChatMesssageSerializer implements Serializer<ChatMessage> {
    final AiMessageSerializer ai = new AiMessageSerializer();
    final UserMessageSerializer user = new UserMessageSerializer();
    final SystemMessageSerializer system = new SystemMessageSerializer();
    final ToolExecutionResultMessageSerializer toolExecutionResult = new ToolExecutionResultMessageSerializer();

    @Override
    public void write(ChatMessage object, ObjectOutput out) throws IOException {
        out.writeObject(object.type());
        switch (object.type()) {
            case AI:
                ai.write((AiMessage) object, out);
                break;
            case USER:
                user.write((UserMessage) object, out);
                break;
            case SYSTEM:
                system.write((SystemMessage) object, out);
                break;
            case TOOL_EXECUTION_RESULT:
                toolExecutionResult.write((ToolExecutionResultMessage) object, out);
        }
    }

    @Override
    public ChatMessage read(ObjectInput in) throws IOException, ClassNotFoundException {

        ChatMessageType type = (ChatMessageType) in.readObject();

        switch (type) {
            case AI:
                return ai.read(in);
            case USER:
                return user.read(in);
            case SYSTEM:
                return system.read(in);
            case TOOL_EXECUTION_RESULT:
                return toolExecutionResult.read(in);
        }

        throw new IllegalArgumentException("Unsupported chat message type: " + type);
    }
}
