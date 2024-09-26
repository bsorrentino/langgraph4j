package org.bsc.langgraph4j.langchain4j.serializer;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;
import org.bsc.langgraph4j.serializer.BaseSerializer;


class AiMessageSerializer extends BaseSerializer<AiMessage> {
    @Override
    public void write(AiMessage object, ObjectOutput out) throws IOException {
        boolean hasToolExecutionRequests = object.hasToolExecutionRequests();

        out.writeBoolean( hasToolExecutionRequests );

        if( hasToolExecutionRequests ) {
            writeObjectWithSerializer( object.toolExecutionRequests(), out);

        }
        else {
            out.writeUTF(object.text());
        }

    }

    @Override
    public AiMessage read(ObjectInput in) throws IOException, ClassNotFoundException {
        boolean hasToolExecutionRequests = in.readBoolean();
        if( hasToolExecutionRequests ) {
            List<ToolExecutionRequest> toolExecutionRequests = readObjectWithSerializer(in);
            return AiMessage.aiMessage( toolExecutionRequests );
        }
        return AiMessage.aiMessage(in.readUTF());
    }
}
