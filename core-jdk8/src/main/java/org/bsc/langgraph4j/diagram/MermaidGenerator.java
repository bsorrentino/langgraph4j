package org.bsc.langgraph4j.diagram;

import org.bsc.langgraph4j.DiagramGenerator;

import static java.lang.String.format;
import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;

/**
 * This class represents a MermaidGenerator that extends DiagramGenerator. It generates a flowchart using Mermaid syntax.
 * The flowchart includes various nodes such as start, stop, web_search, retrieve, grade_documents, generate, transform_query,
 * and different conditional states.
 */
public class MermaidGenerator extends DiagramGenerator {

    @Override
    protected void appendHeader(StringBuilder sb, String title) {
       sb
         .append( format("---\ntitle: %s\n---\n", title))
         .append( "flowchart TD\n")
         .append( format("\t%s((start))\n", START))
         .append( format("\t%s((stop))\n",END))
       ;
    }

    @Override
    protected void appendFooter(StringBuilder sb) {
        // do nothing
    }

   @Override
   protected void declareConditionalStart(StringBuilder sb, String name) {
       sb.append( format("\t%s{\"check state\"}\n", name) );
   }

   @Override
   protected void declareNode(StringBuilder sb, String name) {
      sb.append( format( "\t%s(\"%s\")\n", name, name ) );
   }

   @Override
   protected void declareConditionalEdge(StringBuilder sb, int ordinal) {
        sb.append( format("\tcondition%d{\"check state\"}\n", ordinal) );
   }

    @Override
    protected StringBuilder commentLine(StringBuilder sb, boolean yesOrNo) {
        return (yesOrNo) ? sb.append( "\t%%" ) : sb;
    }

    @Override
    protected void start(StringBuilder sb, String entryPoint) {
        call( sb, START, entryPoint );
    }

    @Override
    protected void finish(StringBuilder sb, String finishPoint) {
        call( sb, finishPoint, END );
    }

    @Override
    protected void finish(StringBuilder sb, String finishPoint, String description) {
        call( sb, finishPoint, END, description );
    }

    @Override
    protected void call(StringBuilder sb, String from, String to) {
        sb.append( format("\t%1$s:::%1$s --> %2$s:::%2$s\n", from, to) );
    }

    @Override
    protected void call(StringBuilder sb, String from, String to, String description) {
        sb.append(format("\t%1$s:::%1$s -->|%2$s| %3$s:::%3$s\n", from, description, to));
    }
}
