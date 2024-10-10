package org.bsc.langgraph4j.langchain4j.serializer.std;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;
import org.bsc.langgraph4j.serializer.Serializer;


public class AiMessageSerializer implements Serializer<AiMessage> {
    @Override
    public void write(AiMessage object, ObjectOutput out) throws IOException {
        boolean hasToolExecutionRequests = object.hasToolExecutionRequests();

        out.writeBoolean( hasToolExecutionRequests );

        if( hasToolExecutionRequests ) {
            out.writeObject( object.toolExecutionRequests() );

        }
        else {
            out.writeUTF(object.text());
        }

    }

    @Override
    public AiMessage read(ObjectInput in) throws IOException, ClassNotFoundException {
        boolean hasToolExecutionRequests = in.readBoolean();
        if( hasToolExecutionRequests ) {
            List<ToolExecutionRequest> toolExecutionRequests = (List<ToolExecutionRequest>)in.readObject();
            return AiMessage.aiMessage( toolExecutionRequests );
        }
        return AiMessage.aiMessage(in.readUTF());
    }
}
