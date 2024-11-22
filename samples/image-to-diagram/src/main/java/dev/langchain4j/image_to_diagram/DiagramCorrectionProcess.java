package dev.langchain4j.image_to_diagram;

import dev.langchain4j.image_to_diagram.actions.correction.EvaluateResult;
import dev.langchain4j.image_to_diagram.actions.correction.ReviewResult;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.bsc.async.AsyncGenerator;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.NodeOutput;

import java.util.Map;

import static java.util.Optional.ofNullable;
import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.utils.CollectionsUtils.mapOf;

@Slf4j( topic="DiagramCorrectionProcess" )
public class DiagramCorrectionProcess implements ImageToDiagram {

    @Getter(lazy = true)
    private final OpenAiChatModel LLM = newLLM();

    private OpenAiChatModel newLLM( ) {
        String openApiKey = ofNullable( System.getProperty("OPENAI_API_KEY") )
                .orElseThrow( () -> new IllegalArgumentException("no OPENAI_API_KEY provided!") );

        return OpenAiChatModel.builder()
                .apiKey( openApiKey )
                .modelName( "gpt-3.5-turbo" )
                .logRequests(true)
                .logResponses(true)
                .maxRetries(2)
                .temperature(0.0)
                .maxTokens(2000)
                .build();

    }

    private  String routeEvaluationResult( State state )  {
        EvaluationResult evaluationResult = state.evaluationResult()
                .orElseThrow(() -> new IllegalArgumentException("no evaluationResult provided!"));

        if( evaluationResult == EvaluationResult.ERROR ) {
            if( state.isExecutionError() ) {
                log.warn("evaluation execution error: [{}]", state.evaluationError().orElse("unknown") );
                return EvaluationResult.UNKNOWN.name();
            }
            if (state.lastTwoDiagramsAreEqual()) {
                log.warn("correction failed! ");
                return EvaluationResult.UNKNOWN.name();
            }
        }

        return evaluationResult.name();
    };

    @Override
    public AsyncGenerator<NodeOutput<State>> execute(Map<String, Object> inputs) throws Exception {

        StateGraph<State> workflow = new StateGraph<>(State::new);

        workflow.addNode( "evaluate_result", EvaluateResult.of());
        workflow.addNode( "agent_review", ReviewResult.of( getLLM()) );
        workflow.addEdge( "agent_review", "evaluate_result" );
        workflow.addConditionalEdges(
                "evaluate_result",
                edge_async(this::routeEvaluationResult),
                mapOf(  "OK", END,
                        "ERROR", "agent_review",
                        "UNKNOWN", END )
        );
        workflow.addEdge( START, "evaluate_result");

        org.bsc.langgraph4j.CompiledGraph<State> app = workflow.compile();

        return app.stream( inputs );

    }
}
