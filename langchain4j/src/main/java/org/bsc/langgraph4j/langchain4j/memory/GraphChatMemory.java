package org.bsc.langgraph4j.langchain4j.memory;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.memory.ChatMemory;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.Channel;
import org.bsc.langgraph4j.state.RemoveByHash;

import javax.naming.spi.StateFactory;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Represents a memory shared by ChatModel during the Graph lifecycle contained within Graph State.
 *
 * @param <State> the state type that extends {@link AgentState} and contains the memory data
 */
public class GraphChatMemory<State extends AgentState> implements ChatMemory {

    private State state;
    private final String stateAttribute;
    private final Map<String, Channel<?>> channels;

    /**
     *
     * @param stateAttribute name of State attribute containing memory
     * @param channels State SCHEMA
     */
    public GraphChatMemory( String stateAttribute, Map<String, Channel<?>> channels ) {
        Objects.requireNonNull( stateAttribute, "the stateAttribute name is null!");
        Objects.requireNonNull( channels, "the channels are null!");
        this.stateAttribute = stateAttribute;
        this.channels = channels;
    }

    /**
     * set the state on which the memory operate
     * due that the state could be re-created during the graph lifecycle this method MUST be called within {@link StateFactory }
     * @param state the state instance
     */
    public void setState( State state ) {
        this.state = state ;
    }

    /**
     * Retrieves the unique identifier for the current state.
     * <p>
     * This method returns a constant string value identifying this particular
     * instance of {@code State}. The returned value is intended to be unique within
     * the application context and can be used for logging, debugging, or other purposes
     * where a stable identifier is required.
     *
     * @return A constant string representing the state's identifier ("state")
     */
    @Override
    public Object id() {
        return "state";
    }

    /**
     * Adds a {@code ChatMessage} to the message list. 
     * If the provided message is an instance of {@link SystemMessage}, 
     * it checks if there is already a system message in the list. If so, and it's not equal to the provided one,
     * it replaces the existing system message with the new one. Otherwise, it adds the provided message to the list.
     *
     * @param message The message to be added.
     */
    @Override
    public void add(ChatMessage message) {
        Objects.requireNonNull(state, "state is null!");
        if( message instanceof SystemMessage systemMessage ) {
            var prevSystemMessage = messages().stream()
                    .filter(m -> m instanceof SystemMessage)
                    .map(SystemMessage.class::cast)
                    .findAny()
                    ;
            if( prevSystemMessage.isPresent() &&  !prevSystemMessage.get().equals(systemMessage) ) {

                // Replace System Message in memory
                AgentState.updateState(state,
                        Map.of(stateAttribute, List.of(RemoveByHash.of(prevSystemMessage.get()), message)),
                        channels);
                return;
            }
        }
        // Add System Message in memory
        AgentState.updateState( state, Map.of( stateAttribute, message ), channels );

    }

    /**
     * Retrieves the list of chat messages.
     *
     * @return a non-null list of chat messages; if no messages are available, an empty list is returned.
     */
    @Override
    public List<ChatMessage> messages() {
        Objects.requireNonNull(state);
        return state.<List<ChatMessage>>value(stateAttribute)
                .orElseGet( List::of );
    }

    /**
     * Clears the memory. If called, it throws an {@link UnsupportedOperationException}
     * because the memory clear functionality has not been implemented yet.
     */
    @Override
    public void clear() {
        throw new UnsupportedOperationException("memory clear is not implemented yet!");
    }
}