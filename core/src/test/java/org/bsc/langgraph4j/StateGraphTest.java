package org.bsc.langgraph4j;

import lombok.extern.slf4j.Slf4j;

import org.bsc.langgraph4j.action.AsyncNodeAction;
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
import static org.bsc.langgraph4j.utils.CollectionsUtils.listOf;
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

        StateGraph<AgentState> workflow = new StateGraph<>(AgentState::new);
        GraphStateException exception = assertThrows(GraphStateException.class, workflow::compile);
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

        StateGraph<AgentState> workflow = new StateGraph<>(AgentState::new)
            .addEdge( START,"agent_1")
            .addNode("agent_1", node_async( state -> {
                System.out.print( "agent_1");
                System.out.println( state );
                return mapOf("prop1", "test");
            }))
            .addEdge( "agent_1",  END)
            ;

        CompiledGraph<AgentState> app = workflow.compile();

        Optional<AgentState> result = app.invoke( mapOf( "input", "test1") );
        assertTrue( result.isPresent() );

        Map<String, String> expected = mapOf("input", "test1","prop1","test");

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
            return this.<Integer>value("steps").orElse(0);
        }

        List<String> messages() {
            return this.<List<String>>value( "messages" )
                    .orElseThrow( () -> new RuntimeException( "messages not found" ) );
        }

    }

    @Test
    void testWithAppender() throws Exception {

        StateGraph<MessagesState> workflow = new StateGraph<>( MessagesState.SCHEMA, MessagesState::new)
                .addNode("agent_1", node_async( state -> {
                    System.out.println( "agent_1" );
                    return mapOf("messages", "message1");
                }))
                .addNode("agent_2", node_async( state -> {
                    System.out.println( "agent_2" );
                    return mapOf( "messages", new String[]{ "message2" });
                }))
                .addNode("agent_3", node_async( state -> {
                    System.out.println( "agent_3" );
                    int steps = state.messages().size() +1 ;
                    return mapOf("messages", "message3","steps", steps );
                }))
                .addEdge("agent_1", "agent_2")
                .addEdge( "agent_2", "agent_3")
                .addEdge( START, "agent_1")
                .addEdge( "agent_3", END);

        CompiledGraph<MessagesState> app = workflow.compile();

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

        StateGraph<MessagesStateDeprecated> workflow = new StateGraph<>(MessagesStateDeprecated::new)
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
                    AppendableValue<String> messages = state.messages();
                    int steps = messages.size() +1 ;
                    return mapOf("messages", "message3","steps", steps);
                }))
                .addEdge("agent_1", "agent_2")
                .addEdge( "agent_2", "agent_3")
                .addEdge( START, "agent_1")
                .addEdge( "agent_3", END);

        CompiledGraph<MessagesStateDeprecated> app = workflow.compile();

        Optional<MessagesStateDeprecated> result = app.invoke( mapOf() );

        assertTrue( result.isPresent() );
        assertEquals( 3, result.get().messages().size() );
        assertEquals( 3, result.get().steps() );
        assertIterableEquals( listOf( "message1", "message2", "message3"), result.get().messages().values() );

    }
    public static void main(String[] args) throws Exception {
        AsyncNodeAction<MessagesState> childStep1 = node_async(state -> Map.of( "messages", "child:step1") );

        AsyncNodeAction<MessagesState> childStep2 = node_async(state -> Map.of( "messages", "child:step2") );

        AsyncNodeAction<MessagesState> childStep3 = node_async(state -> Map.of( "messages", "child:step3") );


        var workflowChild = new StateGraph<>(MessagesState.SCHEMA, MessagesState::new)
                .addNode("child:step_1", childStep1)
                .addNode("child:step_2", childStep2)
                .addNode("child:step_3", childStep3)
                .addEdge(START, "child:step_1")
                .addEdge("child:step_1", "child:step_2")
                .addEdge("child:step_2", "child:step_3")
                .addEdge("child:step_3", END)
                .compile();
        AsyncNodeAction<MessagesState> step1 = node_async(state -> Map.of( "messages", "step1") );

        AsyncNodeAction<MessagesState> step2 = node_async(state -> Map.of( "messages", "step2") );

        AsyncNodeAction<MessagesState> step3 = node_async(state -> Map.of( "messages", "step3") );


        var workflowParent = new StateGraph<>(MessagesState.SCHEMA, MessagesState::new)
                .addNode("step_1", step1)
                .addNode("step_2", step2)
                .addNode("step_3", step3)
                .addSubgraph( "subgraph", workflowChild )
                .addEdge(START, "step_1")
                .addEdge("step_1", "step_2")
                .addEdge("step_2", "subgraph")
                .addEdge("subgraph", "step_3")
                .addEdge("step_3", END)
                .compile();
        for( var step : workflowParent.stream( Map.of() )) {
            System.out.println( step );
        }
        /**
         * NodeOutput{node=__START__, state={messages=[]}}
         * NodeOutput{node=step_1, state={messages=[step1]}}
         * NodeOutput{node=step_2, state={messages=[step1, step2]}}
         * NodeOutput{node=__START__, state={messages=[step1, step2]}}
         * NodeOutput{node=child:step_1, state={messages=[step1, step2, child:step1]}}
         * NodeOutput{node=child:step_2, state={messages=[step1, step2, child:step1, child:step2]}}
         * NodeOutput{node=child:step_3, state={messages=[step1, step2, child:step1, child:step2, child:step3]}}
         * NodeOutput{node=__END__, state={messages=[step1, step2, child:step1, child:step2, child:step3]}}
         * NodeOutput{node=subgraph, state={messages=[step1, step2, child:step1, child:step2, child:step3]}}
         * NodeOutput{node=step_3, state={messages=[step1, step2, child:step1, child:step2, child:step3, step3]}}
         * NodeOutput{node=__END__, state={messages=[step1, step2, child:step1, child:step2, child:step3, step3]}}
         */
    }
}
