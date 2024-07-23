package org.bsc.langgraph4j;

import lombok.Value;
import lombok.var;
import org.bsc.langgraph4j.state.AgentState;

import static java.lang.String.format;

public abstract class DiagramGenerator {

    protected abstract void appendHeader( StringBuilder sb, String title );
    protected abstract void appendFooter(StringBuilder sb) ;
    protected abstract void start( StringBuilder sb, String entryPoint ) ;
    protected abstract void finish( StringBuilder sb, String finishPoint );
    protected abstract void finish( StringBuilder sb, String finishPoint, String description ) ;
    protected abstract void call( StringBuilder sb, String from, String to ) ;
    protected abstract void call( StringBuilder sb, String from, String to, String description );
    protected abstract void declareConditionalStart( StringBuilder sb, String name ) ;
    protected abstract void declareNode( StringBuilder sb, String name ) ;
    protected abstract void declareConditionalEdge( StringBuilder sb, int ordinal ) ;
    protected abstract StringBuilder commentLine( StringBuilder sb, boolean yesOrNo );

    public final <State extends AgentState> String generate( CompiledGraph<State> compiledGraph, String title, boolean printConditionalEdge ) {
        StringBuilder sb = new StringBuilder();

        appendHeader( sb, title );

        compiledGraph.getNodes().keySet()
                .forEach( s -> declareNode( sb, s ) );

        final int[] conditionalEdgeCount = { 0 };

        compiledGraph.getEdges().forEach( (k, v) -> {
            if( v.value() != null ) {
                conditionalEdgeCount[0] += 1;
                declareConditionalEdge( commentLine(sb, !printConditionalEdge), conditionalEdgeCount[0] );
            }
        });


        var entryPoint = compiledGraph.getEntryPoint();
        if( entryPoint.id() != null  ) {
            start( sb, entryPoint.id() );
        }
        else if( entryPoint.value() != null ) {
            String conditionName = "startcondition";
            declareConditionalStart( commentLine(sb, !printConditionalEdge), conditionName );
            edgeCondition( sb, entryPoint.value(), "start", conditionName, printConditionalEdge) ;
        }

        conditionalEdgeCount[0] = 0; // reset

        compiledGraph.getEdges().forEach( (k,v) -> {
            if( v.id() != null ) {
                call( sb, k,  v.id() );
            }
            else if( v.value() != null ) {
                conditionalEdgeCount[0] += 1;
                String conditionName = format("condition%d", conditionalEdgeCount[0]);

                edgeCondition( sb, v.value(), k, conditionName, printConditionalEdge );

            }
        });
        if( compiledGraph.getFinishPoint() != null ) {
           finish( sb, compiledGraph.getFinishPoint() ) ;
        }
        appendFooter( sb );

        return sb.toString();

    }
    private <State extends AgentState> void edgeCondition(StringBuilder sb,
                                                          EdgeCondition<State> condition,
                                                          String k,
                                                          String conditionName,
                                                          boolean printConditionalEdge) {
        call( commentLine(sb, !printConditionalEdge),  k, conditionName);

        condition.mappings().forEach( (cond, to) -> {
            if( to.equals(StateGraph.END) ) {

                finish( commentLine(sb, !printConditionalEdge), conditionName, cond );
                finish( commentLine(sb, printConditionalEdge), k, cond );

            }
            else {
                call( commentLine(sb, !printConditionalEdge), conditionName, to, cond );
                call( commentLine(sb, printConditionalEdge), k, to, cond );
            }
        });
    }

}
