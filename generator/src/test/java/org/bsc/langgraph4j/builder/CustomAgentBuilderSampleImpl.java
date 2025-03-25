package org.bsc.langgraph4j.builder;


import org.bsc.langgraph4j.CompileConfig;
import org.bsc.langgraph4j.RunnableConfig;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.checkpoint.MemorySaver;
import org.bsc.langgraph4j.prebuilt.MessagesState;
import org.bsc.langgraph4j.state.AgentState;

import java.util.Map;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

public class CustomAgentBuilderSampleImpl {


    public static void main( String[] args ) throws GraphStateException {

        var graph = CustomAgentBuilderSample.builder()
                .node_1( node_async(state -> {
                    System.out.println( "edge_1");
                    return Map.of();
                }))
                .edge_1( edge_async( state -> {
                    System.out.println( "edge_1");
                    throw new Error( "Implement me. Returns one of the paths.");
                }))
                .build( MessagesState::new, MessagesState.SCHEMA );


        var compileConfig = CompileConfig.builder()
                        // Add persistence
                        // .checkpointSaver(new MemorySaver())
                        // add interruptions
                        // .interruptAfter( ... )  // Before nodes
                        // .interruptBefore( ) // After nodes
                        .build();

        var workflow = graph.compile( compileConfig );

        var runnableConfig = RunnableConfig.builder()
                        // add thread Id (i.e. execution session id )
                        // valid only if checkpointSaver is enabled (see above)
                        // .threadId( "my_thread" )
                        .build();

        var iterator = workflow.stream( Map.of(), runnableConfig );

        for( var out : iterator ) {
            System.out.println( out );
        }

    }
}
