package org.bsc.langgraph4j;

import lombok.var;
import org.bsc.langgraph4j.state.AgentState;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.utils.CollectionsUtils.mapOf;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GraphTest {

    CompletableFuture<Map<String,Object>> dummyNodeAction(AgentState state ) {
        return CompletableFuture.completedFuture(mapOf());
    }
    CompletableFuture<String> dummyCondition(AgentState state ) {
        return CompletableFuture.completedFuture("");
    }

    @Test
    public void testSimpleGraph() throws Exception {

        var workflow = new StateGraph<>(AgentState::new);
        workflow.setEntryPoint("agent_1");
        workflow.setFinishPoint("agent_2");

        workflow.addNode("agent_3", this::dummyNodeAction);
        workflow.addNode("agent_1", this::dummyNodeAction);
        workflow.addNode("agent_2", this::dummyNodeAction);

        workflow.addEdge( "agent_1",  "agent_3");
        workflow.addEdge( "agent_3",  "agent_2");

        var app = workflow.compile();

        var result = app.getGraph(GraphRepresentation.Type.PLANTUML);
        assertEquals( GraphRepresentation.Type.PLANTUML, result.getType() );

        assertEquals( "@startuml unnamed.puml\n" +
                "skinparam usecaseFontSize 14\n" +
                "skinparam usecaseStereotypeFontSize 12\n" +
                "skinparam hexagonFontSize 14\n" +
                "skinparam hexagonStereotypeFontSize 12\n" +
                "title \"Graph Diagram\"\n" +
                "footer\n" +
                "\n" +
                "powered by langgraph4j\n" +
                "end footer\n" +
                "circle start<<input>>\n" +
                "circle stop\n" +
                "usecase \"agent_3\"<<Node>>\n" +
                "usecase \"agent_1\"<<Node>>\n" +
                "usecase \"agent_2\"<<Node>>\n" +
                "start -down-> \"agent_1\"\n" +
                "\"agent_1\" -down-> \"agent_3\"\n" +
                "\"agent_3\" -down-> \"agent_2\"\n" +
                "\"agent_2\" -down-> stop\n" +
                "@enduml\n", result.getContent() );

        System.out.println( result.getContent() );
    }

    @Test
    public void testCorrectionProcessGraph() throws Exception {

        var workflow = new StateGraph<>(AgentState::new);

        workflow.addNode( "evaluate_result", this::dummyNodeAction);
        workflow.addNode( "agent_review", this::dummyNodeAction );
        workflow.addEdge( "agent_review", "evaluate_result" );
        workflow.addConditionalEdges(
                "evaluate_result",
                this::dummyCondition,
                mapOf(  "OK", END,
                        "ERROR", "agent_review",
                        "UNKNOWN", END )
        );
        workflow.setEntryPoint("evaluate_result");

        var app = workflow.compile();

        var result = app.getGraph(GraphRepresentation.Type.PLANTUML);
        assertEquals( GraphRepresentation.Type.PLANTUML, result.getType() );

        assertEquals( "@startuml unnamed.puml\n" +
                "skinparam usecaseFontSize 14\n" +
                "skinparam usecaseStereotypeFontSize 12\n" +
                "skinparam hexagonFontSize 14\n" +
                "skinparam hexagonStereotypeFontSize 12\n" +
                "title \"Graph Diagram\"\n" +
                "footer\n" +
                "\n" +
                "powered by langgraph4j\n" +
                "end footer\n" +
                "circle start<<input>>\n" +
                "circle stop\n" +
                "usecase \"evaluate_result\"<<Node>>\n" +
                "usecase \"agent_review\"<<Node>>\n" +
                "hexagon \"check state\" as condition1<<Condition>>\n" +
                "start -down-> \"evaluate_result\"\n" +
                "\"agent_review\" -down-> \"evaluate_result\"\n" +
                "\"evaluate_result\" -down-> condition1\n" +
                "condition1 --> \"agent_review\": \"ERROR\"\n" +
                "condition1 --> stop: \"UNKNOWN\"\n" +
                "condition1 --> stop: \"OK\"\n" +
                "@enduml\n", result.getContent() );

        System.out.println( result.getContent() );


    }
    @Test
    public void GenerateAgentExecutorGraph() throws Exception {
        var workflow = new StateGraph<>(AgentState::new);

        workflow.setEntryPoint("agent");

        workflow.addNode( "agent", this::dummyNodeAction);
        workflow.addNode( "action", this::dummyNodeAction);

        workflow.addConditionalEdges(
                "agent",
                this::dummyCondition,
                mapOf("continue", "action", "end", END)
        );

        workflow.addEdge("action", "agent");

        var app = workflow.compile();

        var result = app.getGraph(GraphRepresentation.Type.PLANTUML);
        assertEquals( GraphRepresentation.Type.PLANTUML, result.getType() );

        assertEquals( "@startuml unnamed.puml\n" +
                "skinparam usecaseFontSize 14\n" +
                "skinparam usecaseStereotypeFontSize 12\n" +
                "skinparam hexagonFontSize 14\n" +
                "skinparam hexagonStereotypeFontSize 12\n" +
                "title \"Graph Diagram\"\n" +
                "footer\n" +
                "\n" +
                "powered by langgraph4j\n" +
                "end footer\n" +
                "circle start<<input>>\n" +
                "circle stop\n" +
                "usecase \"agent\"<<Node>>\n" +
                "usecase \"action\"<<Node>>\n" +
                "hexagon \"check state\" as condition1<<Condition>>\n" +
                "start -down-> \"agent\"\n" +
                "\"agent\" -down-> condition1\n" +
                "condition1 --> \"action\": \"continue\"\n" +
                "condition1 --> stop: \"end\"\n" +
                "\"action\" -down-> \"agent\"\n" +
                "@enduml\n", result.getContent() );

        System.out.println( result.getContent() );
    }

    @Test
    public void GenerateImageToDiagramGraph() throws Exception {
        var workflow = new StateGraph<>(AgentState::new);

        workflow.addNode("agent_describer", this::dummyNodeAction );
        workflow.addNode("agent_sequence_plantuml", this::dummyNodeAction );
        workflow.addNode("agent_generic_plantuml", this::dummyNodeAction );
        workflow.addConditionalEdges(
                "agent_describer",
                this::dummyCondition,
                mapOf( "sequence", "agent_sequence_plantuml",
                        "generic", "agent_generic_plantuml" )
        );
        workflow.addNode( "evaluate_result", this::dummyNodeAction );
        workflow.addEdge("agent_sequence_plantuml", "evaluate_result");
        workflow.addEdge("agent_generic_plantuml", "evaluate_result");
        workflow.setEntryPoint("agent_describer");
        workflow.setFinishPoint("evaluate_result");

        var app = workflow.compile();

        var result = app.getGraph( GraphRepresentation.Type.PLANTUML );
        assertEquals( GraphRepresentation.Type.PLANTUML, result.getType() );

        assertEquals( "@startuml unnamed.puml\n" +
                "skinparam usecaseFontSize 14\n" +
                "skinparam usecaseStereotypeFontSize 12\n" +
                "skinparam hexagonFontSize 14\n" +
                "skinparam hexagonStereotypeFontSize 12\n" +
                "title \"Graph Diagram\"\n" +
                "footer\n" +
                "\n" +
                "powered by langgraph4j\n" +
                "end footer\n" +
                "circle start<<input>>\n" +
                "circle stop\n" +
                "usecase \"agent_describer\"<<Node>>\n" +
                "usecase \"agent_sequence_plantuml\"<<Node>>\n" +
                "usecase \"agent_generic_plantuml\"<<Node>>\n" +
                "usecase \"evaluate_result\"<<Node>>\n" +
                "hexagon \"check state\" as condition1<<Condition>>\n" +
                "start -down-> \"agent_describer\"\n" +
                "\"agent_describer\" -down-> condition1\n" +
                "condition1 --> \"agent_sequence_plantuml\": \"sequence\"\n" +
                "condition1 --> \"agent_generic_plantuml\": \"generic\"\n" +
                "\"agent_sequence_plantuml\" -down-> \"evaluate_result\"\n" +
                "\"agent_generic_plantuml\" -down-> \"evaluate_result\"\n" +
                "\"evaluate_result\" -down-> stop\n" +
                "@enduml\n", result.getContent() );

        System.out.println( result.getContent() );
    }
}
