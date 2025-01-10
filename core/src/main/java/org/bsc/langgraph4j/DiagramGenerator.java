package org.bsc.langgraph4j;


import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;
import org.bsc.langgraph4j.state.AgentState;

import static java.lang.String.format;
import static org.bsc.langgraph4j.StateGraph.START;

public abstract class DiagramGenerator {

    @Value
    @Accessors(fluent = true)
    @Builder
    public static class Context {
        StringBuilder sb = new StringBuilder();
        String title;
        boolean printConditionalEdge;
        boolean isSubgraph;

        public String titleToSnakeCase() {
            return title.replaceAll("[^a-zA-Z0-9]", "_");
        }

        @Override
        public String toString() {
            return sb.toString();
        }
    }

    protected abstract void appendHeader( Context ctx );
    protected abstract void appendFooter(Context ctx) ;
    protected abstract void call( Context ctx, String from, String to ) ;
    protected abstract void call( Context ctx, String from, String to, String description );
    protected abstract void declareConditionalStart( Context ctx, String name ) ;
    protected abstract void declareNode( Context ctx, String name ) ;
    protected abstract void declareConditionalEdge( Context ctx, int ordinal ) ;
    protected abstract void commentLine( Context ctx,  boolean yesOrNo );

    /**
     * Generate a textual representation of the given graph.
     *
     * @param stateGraph The graph to generate a diagram from.
     * @param title The title of the graph.
     * @param printConditionalEdge Whether to print the conditional edge condition.
     * @return A string representation of the graph.
     */
    public final <State extends AgentState> String generate( StateGraph<State> stateGraph, String title, boolean printConditionalEdge ) {

        return generate( stateGraph, Context.builder()
                                        .title( title )
                                        .isSubgraph( false )
                                        .printConditionalEdge( printConditionalEdge )
                                        .build() ).toString();

    }

    protected final <State extends AgentState> Context generate( StateGraph<State> stateGraph, Context ctx) {

        appendHeader( ctx );

        stateGraph.nodes
                .forEach( n -> {
                    var action =  n.actionFactory().apply( CompileConfig.builder().build() );
                    if( action instanceof SubgraphNodeAction<?>  subgraphNodeAction) {
                        Context subgraphCtx = generate( subgraphNodeAction.subGraph.stateGraph,
                                                        Context.builder()
                                                                .title( n.id() )
                                                                .printConditionalEdge( ctx.printConditionalEdge )
                                                                .isSubgraph( true )
                                                                .build() );
                        ctx.sb().append( subgraphCtx );
                    }
                    else {
                        declareNode(ctx, n.id());
                    }
                });

        final int[] conditionalEdgeCount = { 0 };

        stateGraph.edges.forEach( e -> {
            if( e.target().value() != null ) {
                conditionalEdgeCount[0] += 1;
                commentLine( ctx, !ctx.printConditionalEdge() );
                declareConditionalEdge( ctx, conditionalEdgeCount[0] );
            }
        });

        EdgeValue<State> entryPoint = stateGraph.getEntryPoint();
        if( entryPoint.id() != null  ) {
            call( ctx, START, entryPoint.id() );
        }
        else if( entryPoint.value() != null ) {
            String conditionName = "startcondition";
            commentLine( ctx, !ctx.printConditionalEdge() );
            declareConditionalStart( ctx , conditionName );
            edgeCondition( ctx, entryPoint.value(), START, conditionName ) ;
        }

        conditionalEdgeCount[0] = 0; // reset

        stateGraph.edges.forEach( v -> {
            if( v.target().id() != null ) {
                call(ctx, v.sourceId(), v.target().id());
            }
            else if( v.target().value() != null ) {
                conditionalEdgeCount[0] += 1;
                String conditionName = format("condition%d", conditionalEdgeCount[0]);

                edgeCondition( ctx, v.target().value(), v.sourceId(), conditionName );

            }
        });

        appendFooter( ctx );

        return ctx;

    }

    private <State extends AgentState> void edgeCondition(Context ctx,
                                                          EdgeCondition<State> condition,
                                                          String k,
                                                          String conditionName) {
        commentLine( ctx, !ctx.printConditionalEdge() );
        call( ctx,  k, conditionName);

        condition.mappings().forEach( (cond, to) -> {
                commentLine( ctx, !ctx.printConditionalEdge() );
                call( ctx, conditionName, to, cond );
                commentLine( ctx, ctx.printConditionalEdge() );
                call( ctx, k, to, cond );
        });
    }

}
