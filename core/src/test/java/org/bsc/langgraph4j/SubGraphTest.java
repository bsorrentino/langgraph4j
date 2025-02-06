package org.bsc.langgraph4j;

import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.checkpoint.MemorySaver;
import org.bsc.langgraph4j.prebuilt.MessagesState;
import org.bsc.langgraph4j.prebuilt.MessagesStateGraph;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;
import java.util.logging.LogManager;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class SubGraphTest {


    @BeforeAll
    public static void initLogging() throws IOException {
        try( var is = StateGraphPersistenceTest.class.getResourceAsStream("/logging.properties") ) {
            LogManager.getLogManager().readConfiguration(is);
        }
    }

    private AsyncNodeAction<MessagesState<String>> makeNode( String id ) {
        return node_async(state ->
                Map.of("messages", id)
        );
    }

    @Test
    public void testMergeSubgraph01() throws Exception {

        var workflowChild = new MessagesStateGraph<String>()
                .addNode("B1", makeNode("B1") )
                .addNode("B2", makeNode( "B2" ) )
                .addEdge(START, "B1")
                .addEdge("B1", "B2")
                .addEdge("B2", END)
                ;

        var workflowParent = new MessagesStateGraph<String>()
                .addNode("A", makeNode("A") )
                .addSubgraph("B",  workflowChild )
                .addNode("C", makeNode("C") )
                .addEdge(START, "A")
                .addEdge("A", "B")
                .addEdge("B", "C")
                .addEdge("C", END)
                //.compile(compileConfig)
                ;

        var processed = StateGraphNodesAndEdges.process( workflowParent );

        assertEquals( 5, processed.edges().elements.size() );
        assertEquals( 4, processed.nodes().elements.size() );
    }

    @Test
    public void testMergeSubgraph02() throws Exception {

        var workflowChild = new MessagesStateGraph<String>()
                .addNode("B1", makeNode("B1") )
                .addNode("B2", makeNode( "B2" ) )
                .addEdge(START, "B1")
                .addEdge("B1", "B2")
                .addEdge("B2", END)
                ;

        var workflowParent = new MessagesStateGraph<String>()
                .addNode("A", makeNode("A") )
                .addSubgraph("B",  workflowChild )
                .addNode("C", makeNode("C") )
                .addConditionalEdges(START,
                        edge_async(state -> "a"),
                        Map.of( "a", "A", "b", "B") )
                .addEdge("A", "B")
                .addEdge("B", "C")
                .addEdge("C", END)
                //.compile(compileConfig)
                ;

        var processed = StateGraphNodesAndEdges.process( workflowParent );

        assertEquals( 5, processed.edges().elements.size() );
        var startEdge = processed.edges().edgeBySourceId(START);
        assertTrue( startEdge.isPresent() );
        assertTrue(startEdge.get().target().value().mappings().containsValue("B@B1"),
                "conditional edges 'START' doesn't contain 'B@B1'");
        assertEquals( 4, processed.nodes().elements.size() );


        var app = workflowParent.compile();
        for( var output :  app.stream( Map.of()) ) {

            System.out.println( output );
        };


    }

    @Test
    public void testCheckpointWithSubgraph() throws Exception {

        var childStep1 = makeNode("child:step1");
        var childStep2 = makeNode("child:step2");
        var childStep3 = makeNode("child:step3");

        var compileConfig = CompileConfig.builder().checkpointSaver(new MemorySaver()).build();

        var workflowChild = new MessagesStateGraph<String>()
                .addNode("child:step_1", childStep1 )
                .addNode("child:step_2", childStep2 )
                .addNode("child:step_3", childStep3 )
                .addEdge(START, "child:step_1")
                .addEdge("child:step_1", "child:step_2")
                .addEdge("child:step_2", "child:step_3")
                .addEdge("child:step_3", END)
                //.compile(compileConfig)
                ;

        var step1 = makeNode( "step1");
        var step2 = makeNode("step2");
        var step3 = makeNode("step3");

        var workflowParent = new MessagesStateGraph<String>()
                .addNode("step_1", step1)
                .addNode("step_2", step2)
                .addNode("step_3", step3)
                .addSubgraph("subgraph", workflowChild)
                .addEdge(START, "step_1")
                .addEdge("step_1", "step_2")
                .addEdge("step_2", "subgraph")
                .addEdge("subgraph", "step_3")
                .addEdge("step_3", END)
                .compile(compileConfig);


        var result = workflowParent.stream(Map.of())
                .stream()
                .peek( n -> log.info("{}", n) )
                .reduce((a, b) -> b)
                .map(NodeOutput::state);

        assertTrue(result.isPresent());
        assertIterableEquals(List.of("step1", "step2", "child:step1", "child:step2", "child:step3", "step3"), result.get().messages());

    }

}
