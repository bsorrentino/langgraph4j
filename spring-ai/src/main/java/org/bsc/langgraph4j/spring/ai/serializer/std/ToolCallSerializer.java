package org.bsc.langgraph4j.spring.ai.serializer.std;

import org.bsc.langgraph4j.serializer.std.NullableObjectSerializer;
import org.springframework.ai.chat.messages.AssistantMessage;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

class ToolCallSerializer implements NullableObjectSerializer<AssistantMessage.ToolCall> {

    @Override
    public void write(AssistantMessage.ToolCall object, ObjectOutput out) throws IOException {

        writeNullableUTF( object.id() , out);
        writeNullableUTF( object.type() , out);
        writeNullableUTF( object.name() , out);
        writeNullableUTF( object.arguments() , out);

    }

    @Override
    public AssistantMessage.ToolCall read(ObjectInput in) throws IOException, ClassNotFoundException {
        var id = readNullableUTF(in);
        var type = readNullableUTF(in);
        var name = readNullableUTF(in);
        var arguments = readNullableUTF(in);

        return new AssistantMessage.ToolCall(
                id.orElse(null),
                type.orElse(null),
                name.orElse(null),
                arguments.orElse(null)
        );
    }
}
