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

import static java.lang.String.format;
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
        processed.nodes().elements.forEach( System.out::println );
        processed.edges().elements.forEach( System.out::println );

        assertEquals( 5, processed.edges().elements.size() );
        assertEquals( 4, processed.nodes().elements.size() );

        var B_B1 = SubGraphNode.formatId( "B", "B1");
        var B_B2 = SubGraphNode.formatId( "B", "B2");

        List.of(
                "Node(A,action)",
                "Node(C,action)",
                format("Node(%s,action)", B_B1 ),
                format("Node(%s,action)", B_B2 )
        ).forEach( n -> assertTrue( processed.nodes().elements.stream().anyMatch(n1 -> Objects.equals(n,n1.toString())) ));

        List.of(
                "Edge[sourceId=__START__, targets=[EdgeValue[id=A, value=null]]]",
                "Edge[sourceId=C, targets=[EdgeValue[id=__END__, value=null]]]",
                format("Edge[sourceId=A, targets=[EdgeValue[id=%s, value=null]]]", B_B1),
                format("Edge[sourceId=%s, targets=[EdgeValue[id=C, value=null]]]", B_B2 ),
                format("Edge[sourceId=%s, targets=[EdgeValue[id=%s, value=null]]]", B_B1, B_B2)
        ).forEach( e -> assertTrue( processed.edges().elements.stream().anyMatch( e1 -> Objects.equals(e,e1.toString()) ) ) );

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
        processed.nodes().elements.forEach( System.out::println );
        processed.edges().elements.forEach( System.out::println );

        assertEquals( 4, processed.nodes().elements.size() );
        assertEquals( 5, processed.edges().elements.size() );

        var B_B1 = SubGraphNode.formatId( "B", "B1");
        var B_B2 = SubGraphNode.formatId( "B", "B2");

        List.of(
            "Node(A,action)",
            "Node(C,action)",
            format("Node(%s,action)", B_B1 ),
            format("Node(%s,action)", B_B2 )
        ).forEach( n -> assertTrue( processed.nodes().elements.stream().anyMatch(n1 -> Objects.equals(n,n1.toString())) ));

        List.of(
            format("Edge[sourceId=__START__, targets=[EdgeValue[id=null, value=EdgeCondition[ action, mapping={a=A, b=%s}]]]", B_B1),
            "Edge[sourceId=C, targets=[EdgeValue[id=__END__, value=null]]]",
            format("Edge[sourceId=A, targets=[EdgeValue[id=%s, value=null]]]", B_B1),
            format("Edge[sourceId=%s, targets=[EdgeValue[id=C, value=null]]]", B_B2 ),
            format("Edge[sourceId=%s, targets=[EdgeValue[id=%s, value=null]]]", B_B1, B_B2)
        ).forEach( e -> assertTrue( processed.edges().elements.stream().anyMatch( e1 -> Objects.equals(e,e1.toString()) ) ) );

        var app = workflowParent.compile();

        var output = app.stream(Map.of())
                .stream()
                .peek(System.out::println)
                .map(NodeOutput::node)
                .toList();

        assertIterableEquals( List.of(
                START,
                "A",
                B_B1,
                B_B2,
                "C",
                END
        ), output );

    }

    @Test
    public void testMergeSubgraph03() throws Exception {

        var workflowChild = new MessagesStateGraph<String>()
                .addNode("B1", makeNode("B1") )
                .addNode("B2", makeNode( "B2" ) )
                .addNode("C", makeNode( "subgraph(C)" ) )
                .addEdge(START, "B1")
                .addEdge("B1", "B2")
                .addConditionalEdges( "B2",
                        edge_async(state -> "c"),
                        Map.of( END, END, "c", "C") )
                .addEdge("C", END)
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
        processed.nodes().elements.forEach( System.out::println );
        processed.edges().elements.forEach( System.out::println );

        assertEquals( 5, processed.nodes().elements.size() );
        assertEquals( 6, processed.edges().elements.size() );

        var B_B1    = SubGraphNode.formatId( "B", "B1");
        var B_B2    = SubGraphNode.formatId( "B", "B2");
        var B_C     = SubGraphNode.formatId( "B", "C");

        List.of(
                "Node(A,action)",
                "Node(C,action)",
                format("Node(%s,action)", B_B1 ),
                format("Node(%s,action)", B_B2 ),
                format("Node(%s,action)", B_C)
        ).forEach( n -> assertTrue( processed.nodes().elements.stream()
                                            .anyMatch(n1 -> Objects.equals(n, n1.toString())),
                                            format( "node \"%s\" doesn't have a match!", n ) ));


        List.of(
            format("Edge[sourceId=__START__, targets=[EdgeValue[id=null, value=EdgeCondition[ action, mapping={a=A, b=%s}]]]", B_B1),
            "Edge[sourceId=C, targets=[EdgeValue[id=__END__, value=null]]]",
            format("Edge[sourceId=A, targets=[EdgeValue[id=%s, value=null]]]", B_B1),
            format("Edge[sourceId=%s, targets=[EdgeValue[id=null, value=EdgeCondition[ action, mapping={c=%s, __END__=C}]]]", B_B2, B_C ),
            format("Edge[sourceId=%s, targets=[EdgeValue[id=C, value=null]]]", B_C ),
            format("Edge[sourceId=%s, targets=[EdgeValue[id=%s, value=null]]]", B_B1, B_B2)
        ).forEach( e -> assertTrue( processed.edges().elements.stream()
                                            .anyMatch( e1 -> Objects.equals(e,e1.toString()) ) ,
                                            format( "edge \"%s\" doesn't have a match!", e ) ) );

        var app = workflowParent.compile();

        var output = app.stream(Map.of())
                .stream()
                .peek(System.out::println)
                .map(NodeOutput::node)
                .toList();

        assertIterableEquals( List.of(
                START,
                "A",
                B_B1,
                B_B2,
                B_C,
                "C",
                END
        ), output );

    }

    @Test
    public void testCheckpointWithSubgraph() throws Exception {

        var compileConfig = CompileConfig.builder().checkpointSaver(new MemorySaver()).build();

        var workflowChild = new MessagesStateGraph<String>()
                .addNode("step_1", makeNode("child:step1") )
                .addNode("step_2", makeNode("child:step2") )
                .addNode("step_3", makeNode("child:step3") )
                .addEdge(START, "step_1")
                .addEdge("step_1", "step_2")
                .addEdge("step_2", "step_3")
                .addEdge("step_3", END)
                //.compile(compileConfig)
                ;

        var workflowParent = new MessagesStateGraph<String>()
                .addNode("step_1", makeNode( "step1"))
                .addNode("step_2", makeNode("step2"))
                .addNode("step_3",  makeNode("step3"))
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
        assertIterableEquals(List.of(
                "step1",
                "step2",
                "child:step1",
                "child:step2",
                "child:step3",
                "step3"), result.get().messages());

    }

}
