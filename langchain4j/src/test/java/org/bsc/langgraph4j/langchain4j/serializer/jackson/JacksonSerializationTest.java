package org.bsc.langgraph4j.langchain4j.serializer.jackson;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import org.bsc.langgraph4j.prebuilt.MessagesState;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class JacksonSerializationTest {

    public static class State extends MessagesState<ChatMessage> {
        public State(Map<String, Object> initData) {
            super(initData);
        }
    }

    @Test
    public void StateSerializerTest() throws Exception {

        var serializer = new JacksonMessagesStateSerializer<>( State::new );

        var state = new State(Map.of( "system", SystemMessage.from("Buddy"),
                                        "user", UserMessage.from( "Hello") ));

        var stream = new ByteArrayOutputStream();
        try(var out = new ObjectOutputStream( stream ) ) {

            serializer.write(state, out);
        }

        var result = serializer.read( new ObjectInputStream( new ByteArrayInputStream( stream.toByteArray() )));

        assertNotNull( result );
        assertEquals( 2, result.data().size() );
        var message = result.data().get("system");
        assertInstanceOf( SystemMessage.class, message );
        assertEquals( "Buddy", ((SystemMessage) message).text() );
        message = result.data().get("user");
        assertInstanceOf( UserMessage.class, message );
        assertEquals( "Hello", ((UserMessage) message).singleText() );



    }

    @Test
    public void MessagesStateSerializerTest() throws Exception {

        var serializer = new JacksonMessagesStateSerializer<>( State::new );

        var state = new State( Map.of(
                "messages", List.of(SystemMessage.from("Buddy"), UserMessage.from( "Hello") ),
                "intent", "myIntent")
        );

        var stream = new ByteArrayOutputStream();
        try(var out = new ObjectOutputStream( stream ) ) {

            serializer.write(state, out);
        }

        var result = serializer.read( new ObjectInputStream( new ByteArrayInputStream( stream.toByteArray() )));

        assertNotNull( result );
        assertEquals( 2, result.data().size() );
        var messages = result.data().get("messages");
        assertInstanceOf( List.class, messages );
        var messagesList = (List<?>)messages;
        assertEquals( 2, messagesList.size() );
        assertInstanceOf( SystemMessage.class, messagesList.get(0) );
        assertEquals( "Buddy", ((SystemMessage) messagesList.get(0)).text() );



    }

}
