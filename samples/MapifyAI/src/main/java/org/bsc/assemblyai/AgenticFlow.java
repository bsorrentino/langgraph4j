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
        
        /**
         * Constructs a new instance of State with initial data.
         *
         * @param initData Initial data for the state
         */
        public State(Map<String, Object> initData) {
            super(initData);
        }

        /**
         * Retrieves the serializer for the State class.
         *
         * @return The StateSerializer for serializing State objects
         */
        public static StateSerializer<State> serializer() {
            var serializer = new ObjectStreamStateSerializer<>(State::new);
            serializer.mapper().register(UserMessage.class, new UserMessageSerializer());
            serializer.mapper().register(AiMessage.class, new AiMessageSerializer());
            return serializer;
        }
    }

    /**
     * Dependency for extracting keypoints from transcripts.
     */
    final ExtractKeypointsFromTranscript extractKeypointsFromTranscript;

    /**
     * Dependency for generating PlantUML mindmaps.
     */
    final GeneratePlantUMLMindmap generatePlantUMLMindmap;

    /**
     * Dependency for generating PlantUML images.
     */
    final GeneratePlantUMLImage generatePlantUMLImage;

    /**
     * Constructor initializing the dependencies.
     *
     * @param extractKeypointsFromTranscript The ExtractKeypointsFromTranscript dependency
     * @param generatePlantUMLMindmap      The GeneratePlantUMLMindmap dependency
     * @param generatePlantUMLImage        The GeneratePlantUMLImage dependency
     */
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
/*
                .addConditionalEdges(
                        "agent",
                        edge_async(state -> {
                            if (state.agentOutcome().map(AgentOutcome::finish).isPresent()) {
                                return "end";
                            }
                            return "continue";
                        }),
                        mapOf("continue", "action", "end", END)
                )
                .addEdge("action", "agent")
*/
                .addEdge("mindmap-to-image", END);
    }
}
