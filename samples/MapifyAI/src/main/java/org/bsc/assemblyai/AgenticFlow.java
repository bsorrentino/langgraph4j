package org.bsc.assemblyai;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import org.bsc.assemblyai.actions.ExtractKeypointsFromTranscript;
import org.bsc.assemblyai.actions.GeneratePlantUMLImage;
import org.bsc.assemblyai.actions.GeneratePlantUMLMindmap;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.langchain4j.serializer.std.AiMessageSerializer;
import org.bsc.langgraph4j.langchain4j.serializer.std.UserMessageSerializer;
import org.bsc.langgraph4j.serializer.StateSerializer;
import org.bsc.langgraph4j.serializer.std.ObjectStreamStateSerializer;
import org.bsc.langgraph4j.state.AgentState;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

@Service
public class AgenticFlow {

    public static class State extends AgentState {

        public State(Map<String, Object> initData) {
            super(initData);
        }

        public static StateSerializer<State> serializer() {
            var serializer = new ObjectStreamStateSerializer<>(State::new);
            serializer.mapper().register(UserMessage.class, new UserMessageSerializer());
            serializer.mapper().register(AiMessage.class, new AiMessageSerializer());
            return serializer;
        }
    }

    final ExtractKeypointsFromTranscript extractKeypointsFromTranscript;
    final GeneratePlantUMLMindmap generatePlantUMLMindmap;
    final GeneratePlantUMLImage generatePlantUMLImage;

    public AgenticFlow( ExtractKeypointsFromTranscript extractKeypointsFromTranscript,
                        GeneratePlantUMLMindmap generatePlantUMLMindmap,
                        GeneratePlantUMLImage generatePlantUMLImage ) {
        this.extractKeypointsFromTranscript = extractKeypointsFromTranscript;
        this.generatePlantUMLMindmap = generatePlantUMLMindmap;
        this.generatePlantUMLImage = generatePlantUMLImage;
    }

    public StateGraph<State> buildGraph() throws Exception {

        return new StateGraph<>( State.serializer() )
                .addNode( "agent", node_async( extractKeypointsFromTranscript ) )
                .addNode( "mindmap", node_async( generatePlantUMLMindmap ) )
                .addNode( "mindmap-to-image", node_async( generatePlantUMLImage ) )
                .addEdge(START,"agent")
                .addEdge("agent", "mindmap")
                .addEdge("mindmap", "mindmap-to-image")
/*
                .addConditionalEdges(
                        "agent",
                        edge_async( state -> {
                            if (state.agentOutcome().map(AgentOutcome::finish).isPresent()) {
                                return "end";
                            }
                            return "continue";
                        }),
                        mapOf("continue", "action", "end", END)
                )
                .addEdge("action", "agent")
*/
                .addEdge("mindmap-to-image", END)
                ;


    }

}
