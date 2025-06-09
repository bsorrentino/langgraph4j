package org.bsc.langgraph4j.spring.ai.serializer.std;

import org.bsc.langgraph4j.serializer.Serializer;
import org.springframework.ai.chat.messages.UserMessage;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Map;
import java.util.Objects;

class UserMessageSerializer implements Serializer<UserMessage> {

    @Override
    public void write(UserMessage object, ObjectOutput out) throws IOException {
        Objects.requireNonNull(object.getText(), "text cannot be null");
        out.writeUTF(object.getText());
        out.writeObject(object.getMetadata());
    }

    @Override
    @SuppressWarnings("unchecked")
    public UserMessage read(ObjectInput in) throws IOException, ClassNotFoundException {
        var text = in.readUTF();
        var metadata = (Map<String, Object>) in.readObject();
        return UserMessage.builder().text(text).metadata(metadata).build();
    }
}
