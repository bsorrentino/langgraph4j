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

/**
 * AgenticFlow is a service class responsible for orchestrating the flow of an agent's actions.
 */
@Service
public class AgenticFlow {

    /**
     * State class extending AgentState, representing the state of the agent in the flow.
     */
    public static class State extends AgentState {
        // Constructor for initializing the State with initial data
        public State(Map<String, Object> initData) {
            super(initData);
        }

        // Retrieves the serializer for the State class
        public static StateSerializer<State> serializer() {
            var serializer = new ObjectStreamStateSerializer<>(State::new);
            serializer.mapper().register(UserMessage.class, new UserMessageSerializer());
            serializer.mapper().register(AiMessage.class, new AiMessageSerializer());
            return serializer;
        }
    }

    // Dependency for extracting keypoints from transcripts
    final ExtractKeypointsFromTranscript extractKeypointsFromTranscript;

    // Dependency for generating PlantUML mindmaps
    final GeneratePlantUMLMindmap generatePlantUMLMindmap;

    // Dependency for generating PlantUML images
    final GeneratePlantUMLImage generatePlantUMLImage;

    // Constructor initializing the dependencies
    public AgenticFlow(ExtractKeypointsFromTranscript extractKeypointsFromTranscript,
                        GeneratePlantUMLMindmap generatePlantUMLMindmap,
                        GeneratePlantUMLImage generatePlantUMLImage) {
        this.extractKeypointsFromTranscript = extractKeypointsFromTranscript;
        this.generatePlantUMLMindmap = generatePlantUMLMindmap;
        this.generatePlantUMLImage = generatePlantUMLImage;
    }

    /**
     * Builds and returns a state graph representing the flow of the agent.
     *
     * @return The constructed StateGraph
     * @throws Exception If an error occurs during the graph construction
     */
    public StateGraph<State> buildGraph() throws Exception {
        return new StateGraph<>(State.serializer())
                .addNode("agent", node_async(extractKeypointsFromTranscript))
                .addNode("mindmap", node_async(generatePlantUMLMindmap))
                .addNode("mindmap-to-image", node_async(generatePlantUMLImage))
                .addEdge(START, "agent")
                .addEdge("agent", "mindmap")
                .addEdge("mindmap", "mindmap-to-image")
                .addEdge("mindmap-to-image", END);
    }
}
