package org.bsc.langgraph4j;


import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;
import org.bsc.langgraph4j.state.AgentState;

import java.util.Objects;

import static java.lang.String.format;
import static org.bsc.langgraph4j.StateGraph.START;

/**
 * Abstract class for diagram generation.
 * This class provides a framework for generating textual representations of graphs.
 */
public abstract class DiagramGenerator {

    /**
     * Class that represents a context with various properties and methods.
     * This class is designed to store and manipulate context-specific data such as
     * a string builder, title, and conditional edge printing status. It also provides
     * a method for converting the title to snake case. The class is annotated with {@code @Value},
     * {@code @Accessors(fluent = true)}, and {@code @Builder} to facilitate value semantics,
     * fluent interfaces, and builder patterns respectively.
     */
    @Value
    @Accessors(fluent = true)
    @Builder
    public static class Context {
        StringBuilder sb = new StringBuilder();
        String title;
        boolean printConditionalEdge;
        boolean isSubgraph;

        /**
         * Converts a given title string to snake_case format by replacing all non-alphanumeric characters with underscores.
         *
         * @return the snake_case formatted string
         */
        public String titleToSnakeCase() {
            return title.replaceAll("[^a-zA-Z0-9]", "_");
        }

        /**
         * Returns a string representation of this object by returning the string built in {@link #sb}.
         *
         * @return a string representation of this object.
         */
        @Override
        public String toString() {
            return sb.toString();
        }
    }

    /**
     * Appends a header to the output based on the provided context.
     *
     * @param ctx The {@link Context} containing the information needed for appending the header.
     */
    protected abstract void appendHeader( Context ctx );
    /**
     * Appends a footer to the content.
     *
     * @param ctx Context object containing the necessary information.
     */
    protected abstract void appendFooter(Context ctx) ;
    /**
     * This method is an abstract method that must be implemented by subclasses.
     * It is used to initiate a communication call between two parties identified by their phone numbers.
     *
     * @param ctx The current context in which the call is being made.
     * @param from The phone number of the caller.
     * @param to The phone number of the recipient.
     */
    protected abstract void call( Context ctx, String from, String to ) ;
    /**
     * Abstract method that must be implemented by subclasses to handle the logic of making a call.
     *
     * @param ctx The context in which the call is being made.
     * @param from The phone number of the caller.
     * @param to The phone number of the recipient.
     * @param description A brief description of the call.
     */
    protected abstract void call( Context ctx, String from, String to, String description );
    /**
     * Declares a conditional element in the configuration or template.
     * This method is used to mark the start of a conditional section based on the provided {@code name}.
     * It takes a {@code Context} object that may contain additional parameters necessary for the declaration,
     * and a {@code name} which identifies the type or key associated with the conditional section.
     *
     * @param ctx The context containing contextual information needed for the declaration.
     * @param name The name of the conditional section to be declared.
     */
    protected abstract void declareConditionalStart( Context ctx, String name ) ;
    /**
     * Declares a node in the specified context with the given name.
     *
     * @param ctx  the context in which to declare the node {@code @literal (not null)}
     * @param name the name of the node to be declared {@code @literal (not null, not empty)}
     */
    protected abstract void declareNode( Context ctx, String name ) ;
    /**
     * Declares a conditional edge in the context with a specified ordinal.
     *
     * @param ctx the context
     * @param ordinal the ordinal value
     */
    protected abstract void declareConditionalEdge( Context ctx, int ordinal ) ;
    /**
     * Comment a line in the given context.
     *
     * @param ctx    The context in which the line is to be commented.
     * @param yesOrNo Whether the line should be uncommented ({@literal true}) or commented ({@literal false}).
     */
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

    /**
     * Generates a context based on the given state graph.
     *
     * @param <State>     the type of agent state, constrained to extend {@link AgentState}
     * @param stateGraph  the state graph used to generate the context, which must not be null
     * @param ctx         the initial context, which must not be null
     * @return            the generated context, which will not be null
     */
    protected final <State extends AgentState> Context generate( StateGraph<State> stateGraph, Context ctx) {

        appendHeader( ctx );

        for( var n :  stateGraph.nodes.elements )  {

            if( n instanceof SubGraphNode<?> subGraph ) {

                    Context subgraphCtx = generate( subGraph.subGraph(),
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
        }

        final int[] conditionalEdgeCount = { 0 };

        stateGraph.edges.elements.stream()
            .filter( e -> !Objects.equals(e.sourceId(), START) )
                .filter( e -> !e.isParallel() )
            .forEach( e -> {
                if( e.target().value() != null ) {
                    conditionalEdgeCount[0] += 1;
                    commentLine( ctx, !ctx.printConditionalEdge() );
                    declareConditionalEdge( ctx, conditionalEdgeCount[0] );
                }
            });

        var edgeStart = stateGraph.edges.elements.stream()
                .filter( e -> Objects.equals( e.sourceId(), START) )
                .findFirst()
                .orElseThrow();
        if( edgeStart.isParallel() ) {
            edgeStart.targets().forEach( target -> {
                call( ctx, START, target.id() );
            });
        }
        else if( edgeStart.target().id() != null  ) {
            call( ctx, START, edgeStart.target().id() );
        }
        else if( edgeStart.target().value() != null ) {
            String conditionName = "startcondition";
            commentLine( ctx, !ctx.printConditionalEdge() );
            declareConditionalStart( ctx , conditionName );
            edgeCondition( ctx, edgeStart.target().value(), START, conditionName ) ;
        }

        conditionalEdgeCount[0] = 0; // reset

        stateGraph.edges.elements.stream()
            .filter( e -> !Objects.equals(e.sourceId(), START) )
            .forEach( v -> {

                if( v.isParallel()) {
                    v.targets().forEach( target -> {
                        call(ctx, v.sourceId(), target.id());
                    });
                }
                else if( v.target().id() != null ) {
                    call(ctx, v.sourceId(), v.target().id());
                }
                else if( v.target().value() != null ) {
                    conditionalEdgeCount[0] += 1;
                    String conditionName = format("condition%d", conditionalEdgeCount[0]);

                    edgeCondition( ctx, v.targets().get(0).value(), v.sourceId(), conditionName );
                }
            });

        appendFooter( ctx );

        return ctx;

    }

    /**
     * Evaluates an edge condition based on the given context and condition.
     *
     * @param <State>   the type of state extending {@link AgentState}
     * @param ctx       the current context used for evaluation
     * @param condition the condition to be evaluated
     * @param k         a string identifier for the condition
     * @param conditionName the name of the condition being processed
     */
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