package org.bsc.langgraph4j.jetty;

import org.bsc.langgraph4j.CompileConfig;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.action.EdgeAction;
import org.bsc.langgraph4j.action.NodeAction;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.studio.jetty.LangGraphStreamingServerJetty;
import org.bsc.langgraph4j.utils.EdgeMappings;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

public class LangGraphStreamingServerTest {

    static class State extends AgentState {

        public static final String AGENT_RESPONSE = "agent_response";
        public static final String ACTION_RESPONSE = "action_response";

        public State(Map<String, Object> initData) {
            super(initData);
        }

        String input() {
            return this.<String>value("input").orElseThrow();
        }

        Optional<String> agentResponse() {
            return this.value(AGENT_RESPONSE);
        }

        Optional<String> actionResponse() {
            return this.value(ACTION_RESPONSE);
        }
    }

    EdgeAction<State> conditionalAge = (state) -> {
        System.out.println("condition");
        System.out.println(state);

        return state.agentResponse()
                .filter(res ->!res.isBlank())
                .map(res -> "end" )
                .orElse("action");
    };


    NodeAction<State> agentNode = (state ) -> {

        System.out.println("agent ");
        System.out.println(state);

        Thread.sleep( 2000 );

        if( state.actionResponse().map( res -> !res.isBlank() ).orElse(false) ) {
            return Map.of(State.AGENT_RESPONSE, "We have successfully completed your request: " + state.input());
        }

        return Map.of( State.AGENT_RESPONSE, "");


    };

    AsyncNodeAction<State> actionNode = (state ) -> {

        var result = new CompletableFuture<Map<String,Object>>();

        System.out.println("action ");
        System.out.println(state);

        if( state.agentResponse().map( res -> !res.isBlank() ).orElse(false) ) {
            result.complete(Map.of(State.ACTION_RESPONSE, "skipped"));
        }
        else {
            try {
                Thread.sleep(2000);

                result.complete(Map.of(State.ACTION_RESPONSE, "the job request '" + state.input() + "' has been completed"));

            } catch (InterruptedException e) {
                result.completeExceptionally(e);
            }
        }
        return result;

    };

    StateGraph<State> build() throws GraphStateException {
        return new StateGraph<>(State::new)
                .addNode("agent", node_async(agentNode) )
                .addNode("action", actionNode )
                .addEdge(START, "agent")
                .addEdge("action", "agent" )
                .addConditionalEdges("agent",
                        edge_async(conditionalAge),
                        EdgeMappings.builder()
                                .to("action")
                                .toEND( "end" )
                                .build() )
                ;

    }

    static class NoReleaseThread {

        public static void main(String[] args) throws Exception {

            var workflow = new LangGraphStreamingServerTest().build();


            var server = LangGraphStreamingServerJetty.builder()
                    .port(8080)
                    .title("LANGGRAPH4j STUDIO - DEMO")
                    .addInputStringArg("input")
                    .stateGraph(workflow)
                    .compileConfig(CompileConfig.builder()
                            //.interruptBefore( "action" )
                            .build())
                    .build();

            server.start().join();

        }

    }

    static class AutoReleaseThread {

        public static void main(String[] args) throws Exception {

            var workflow = new LangGraphStreamingServerTest().build();

            var server = LangGraphStreamingServerJetty.builder()
                    .port(8080)
                    .title("LANGGRAPH4j STUDIO - Auto Release Thread")
                    .addInputStringArg("input")
                    .stateGraph(workflow)
                    .compileConfig(CompileConfig.builder()
                            .releaseThread(true)
                            .build())
                    .build();

            server.start().join();

        }

    }

    static class WithInterruption {

        public static void main(String[] args) throws Exception {

            var workflow = new LangGraphStreamingServerTest().build();

            var server = LangGraphStreamingServerJetty.builder()
                    .port(8080)
                    .title("LANGGRAPH4j STUDIO - With Interruption")
                    .addInputStringArg("input")
                    .stateGraph(workflow)
                    .compileConfig(CompileConfig.builder()
                            .releaseThread(true)
                            .interruptBefore("action")
                            .build())
                    .build();

            server.start().join();

        }

    }

}
