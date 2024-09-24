package org.bsc.langgraph4j;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AppendableValue;
import org.bsc.langgraph4j.state.AppenderChannel;
import org.bsc.langgraph4j.state.Channel;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import static org.bsc.langgraph4j.utils.CollectionsUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
@Slf4j
public class StateGraphTest
{
    public static <T> List<Map.Entry<String,T>> sortMap(Map<String,T> map ) {
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toList());
    }
    @Test
    void testValidation() throws Exception {

        var workflow = new StateGraph<>(AgentState::new);
        var exception = assertThrows(GraphStateException.class, workflow::compile);
        System.out.println(exception.getMessage());
        assertEquals( "missing Entry Point", exception.getMessage());

        workflow.addEdge( START,"agent_1");

        exception = assertThrows(GraphStateException.class, workflow::compile);
        System.out.println(exception.getMessage());
        assertEquals( "entryPoint: agent_1 doesn't exist!", exception.getMessage());

        workflow.addNode("agent_1", node_async((state ) -> {
            System.out.print("agent_1 ");
            System.out.println(state);
            return mapOf("prop1", "test");
        }) ) ;

        assertNotNull(workflow.compile());

        workflow.addEdge( "agent_1",  END);

        assertNotNull(workflow.compile());

        exception = assertThrows(GraphStateException.class, () ->
            workflow.addEdge(END, "agent_1") );
        System.out.println(exception.getMessage());

        exception = assertThrows(GraphStateException.class, () ->
                workflow.addEdge("agent_1", "agent_2") );
        System.out.println(exception.getMessage());

        workflow.addNode("agent_2", node_async( state  -> {

            System.out.print( "agent_2: ");
            System.out.println( state );

            return mapOf("prop2", "test");
        }));

        workflow.addEdge("agent_2", "agent_3");

        exception = assertThrows(GraphStateException.class, workflow::compile);
        System.out.println(exception.getMessage());

        exception = assertThrows(GraphStateException.class, () ->
            workflow.addConditionalEdges("agent_1", edge_async(state  -> "agent_3" ), mapOf() )
        );
        System.out.println(exception.getMessage());

    }

    @Test
    public void testRunningOneNode() throws Exception {

        var workflow = new StateGraph<>(AgentState::new)
            .addEdge( START,"agent_1")
            .addNode("agent_1", node_async( state -> {
                System.out.print( "agent_1");
                System.out.println( state );
                return mapOf("prop1", "test");
            }))
            .addEdge( "agent_1",  END)
            ;

        var app = workflow.compile();

        var result = app.invoke( mapOf( "input", "test1") );
        assertTrue( result.isPresent() );

        var  expected = mapOf("input", "test1","prop1","test");

        assertIterableEquals( sortMap(expected), sortMap(result.get().data()) );
        //assertDictionaryOfAnyEqual( expected, result.data )

    }


    static class MessagesState extends AgentState {

        static Map<String, Channel<?>> SCHEMA = mapOf(
            "messages", AppenderChannel.<String>of(ArrayList::new)
        );

        public MessagesState(Map<String, Object> initData) {
            super( initData  );
        }

        int steps() {
            return value("steps", 0);
        }

        List<String> messages() {
            return this.<List<String>>value( "messages" )
                    .orElseThrow( () -> new RuntimeException( "messages not found" ) );
        }

    }

    @Test
    void testWithAppender() throws Exception {

        var workflow = new StateGraph<>( MessagesState.SCHEMA, MessagesState::new)
                .addNode("agent_1", node_async( state -> {
                    System.out.println( "agent_1" );
                    return mapOf("messages", "message1");
                }))
                .addNode("agent_2", node_async( state -> {
                    System.out.println( "agent_2" );
                    return mapOf( "messages", "message2");
                }))
                .addNode("agent_3", node_async( state -> {
                    System.out.println( "agent_3" );
                    var steps = state.messages().size() +1 ;
                    return mapOf("messages", "message3","steps", steps);
                }))
                .addEdge("agent_1", "agent_2")
                .addEdge( "agent_2", "agent_3")
                .addEdge( START, "agent_1")
                .addEdge( "agent_3", END);

        var app = workflow.compile();

        Optional<MessagesState> result = app.invoke( mapOf() );

        assertTrue( result.isPresent() );
        System.out.println( result.get().data() );
        assertEquals( 3, result.get().steps() );
        assertEquals( 3, result.get().messages().size() );
        assertIterableEquals( listOf( "message1", "message2", "message3"), result.get().messages() );

    }

    static class MessagesStateDeprecated extends AgentState {

        public MessagesStateDeprecated(Map<String, Object> initData) {
            super( initData  );
            appendableValue("messages"); // tip: initialize messages
        }

        int steps() {
            return value("steps").map(Integer.class::cast).orElse(0);
        }

        AppendableValue<String> messages() {
            return appendableValue("messages");
        }

    }

    @Test
    void testWithAppenderDeprecated() throws Exception {

        var workflow = new StateGraph<>(MessagesStateDeprecated::new)
                .addNode("agent_1", node_async( state -> {
                    System.out.println( "agent_1" );
                    return mapOf("messages", "message1");
                }))
                .addNode("agent_2", node_async( state -> {
                    System.out.println( "agent_2" );
                    return mapOf( "messages", "message2");
                }))
                .addNode("agent_3", node_async( state -> {
                    System.out.println( "agent_3" );
                    var messages = state.messages();
                    var steps = messages.size() +1 ;
                    return mapOf("messages", "message3","steps", steps);
                }))
                .addEdge("agent_1", "agent_2")
                .addEdge( "agent_2", "agent_3")
                .addEdge( START, "agent_1")
                .addEdge( "agent_3", END);

        var app = workflow.compile();

        Optional<MessagesStateDeprecated> result = app.invoke( mapOf() );

        assertTrue( result.isPresent() );
        assertEquals( 3, result.get().messages().size() );
        assertEquals( 3, result.get().steps() );
        assertIterableEquals( listOf( "message1", "message2", "message3"), result.get().messages().values() );

    }

}
