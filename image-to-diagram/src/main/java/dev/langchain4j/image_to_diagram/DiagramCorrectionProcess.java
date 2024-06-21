package dev.langchain4j.image_to_diagram;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.bsc.async.AsyncGenerator;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.NodeOutput;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.util.Optional.ofNullable;
import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.utils.CollectionsUtils.mapOf;

@Slf4j( topic="DiagramCorrectionProcess" )
public class DiagramCorrectionProcess implements ImageToDiagram {

    @Getter(lazy = true)
    private final OpenAiChatModel LLM = newLLM();

    private OpenAiChatModel newLLM( ) {
        var openApiKey = ofNullable( System.getProperty("OPENAI_API_KEY") )
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

    CompletableFuture<Map<String,Object>> reviewResult(State state)  {
        CompletableFuture<Map<String,Object>> future = new CompletableFuture<>();
        try {

            var diagramCode = state.diagramCode().last()
                    .orElseThrow(() -> new IllegalArgumentException("no diagram code provided!"));

            var error = state.evaluationError()
                    .orElseThrow(() -> new IllegalArgumentException("no evaluation error provided!"));

            log.trace("evaluation error: {}", error);

            Prompt systemPrompt = loadPromptTemplate( "review_diagram.txt" )
                    .apply( mapOf( "evaluationError", error, "diagramCode", diagramCode));
            var response = getLLM().generate( new SystemMessage(systemPrompt.text()) );

            var result = response.content().text();

            log.trace("review result: {}", result);

            future.complete(mapOf("diagramCode", result ) );

        } catch (Exception e) {
            future.completeExceptionally(e);
        }

        return future;
    }

    private CompletableFuture<Map<String,Object>> evaluateResult(State state) {

        var diagramCode = state.diagramCode().last()
                .orElseThrow(() -> new IllegalArgumentException("no diagram code provided!"));

        return PlantUMLAction.validate( diagramCode )
                .thenApply( v -> mapOf( "evaluationResult", (Object) EvaluationResult.OK ) )
                .exceptionally( e -> {
                    if( e.getCause() instanceof PlantUMLAction.Error ) {
                        return mapOf("evaluationResult", EvaluationResult.ERROR,
                                "evaluationError",  e.getCause().getMessage(),
                                "evaluationErrorType", ((PlantUMLAction.Error)e.getCause()).getType());
                    }
                    throw new RuntimeException(e);
                });

    }

    private  String routeEvaluationResult( State state )  {
        var evaluationResult = state.evaluationResult()
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

        var workflow = new StateGraph<>(State::new);

        workflow.addNode( "evaluate_result", this::evaluateResult);
        workflow.addNode( "agent_review", this::reviewResult );
        workflow.addEdge( "agent_review", "evaluate_result" );
        workflow.addConditionalEdges(
                "evaluate_result",
                edge_async(this::routeEvaluationResult),
                mapOf(  "OK", END,
                        "ERROR", "agent_review",
                        "UNKNOWN", END )
        );
        workflow.setEntryPoint("evaluate_result");

        var app = workflow.compile();

        return app.stream( inputs );

    }
}
