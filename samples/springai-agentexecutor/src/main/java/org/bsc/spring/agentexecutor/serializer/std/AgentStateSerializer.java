package org.bsc.spring.agentexecutor.serializer.std;

import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.serializer.Serializer;
import org.bsc.langgraph4j.serializer.std.NullableObjectSerializer;
import org.bsc.langgraph4j.serializer.std.ObjectStreamStateSerializer;
import org.bsc.spring.agentexecutor.AgentExecutor;
import org.springframework.ai.chat.messages.AssistantMessage;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Map;

/**
 * This class is responsible for serializing and deserializing the state of an agent executor.
 * It extends {@link ObjectStreamStateSerializer} for handling the serialization of the AgentExecutor.State object.
 */
@Slf4j
public class AgentStateSerializer extends ObjectStreamStateSerializer<AgentExecutor.State>  {

    /**
     * Constructor that initializes the serializer with a supplier for creating new AgentExecutor.State instances and registers various serializers for different types.
     */
    public AgentStateSerializer() {
        super(AgentExecutor.State::new);

        mapper().register( AgentExecutor.Outcome.class, new OutcomeSerializer() );
        mapper().register( AgentExecutor.Finish.class, new FinishSerializer() );
        mapper().register( AgentExecutor.Action.class, new ActionSerializer() );
        mapper().register( AgentExecutor.Step.class, new StepSerializer() );

    }

    /**
     * Represents a serializer for the {@link AgentExecutor.Finish} object.
     */
    static class FinishSerializer implements Serializer<AgentExecutor.Finish> {

        /**
         * Writes the return values of an AgentExecutor.Finish object to the provided ObjectOutput stream.
         *
         * @param object the AgentExecutor.Finish object to write
         * @param out    the stream to which the object data is written
         * @throws IOException if an I/O error occurs while writing to the output stream
         */
        @Override
        public void write(AgentExecutor.Finish object, ObjectOutput out) throws IOException {
            out.writeObject(object.returnValues());
            //out.writeUTF(object.log());
        }

        /**
         * Reads an object from the given input stream and returns a new instance of AgentExecutor.Finish.
         *
         * @param in the object input stream to read from
         * @return a new instance of AgentExecutor.Finish containing the data read from the input stream
         * @throws IOException if an I/O error occurs while reading from the input stream
         * @throws ClassNotFoundException if the class of the serialized object cannot be found
         */
        @Override
        public AgentExecutor.Finish read(ObjectInput in) throws IOException, ClassNotFoundException {
            Map<String, Object> returnValues = (Map<String, Object>)in.readObject();
            // String log = in.readUTF();
            return new AgentExecutor.Finish(returnValues);
        }

    }

    /**
     * Represents a serializer for the {@link AgentExecutor.Action} object.
     */
    static class ActionSerializer implements Serializer<AgentExecutor.Action> {

        /**
         * Writes an agent executor action and its associated tool call to the given output stream.
         *
         * @param action The agent executor action to write.
         * @param out    The object output stream to which the data is written.
         * @throws IOException if an I/O error occurs while writing the data.
         */
        @Override
        public void write(AgentExecutor.Action action, ObjectOutput out) throws IOException {
            var ter =  action.toolCall();
            out.writeUTF( ter.id() );
            out.writeUTF( ter.type() );
            out.writeUTF( ter.name() );
            out.writeUTF( ter.arguments() );
            out.writeUTF( action.log() );

        }

        /**
         * Reads an {@link AgentExecutor.Action} object from the given {@link ObjectInput}.
         * 
         * @param in The {@link ObjectInput} from which to read the object.
         * @return An {@link AgentExecutor.Action} object reconstructed from the input.
         * @throws IOException If an I/O error occurs while reading the stream.
         * @throws ClassNotFoundException If a class of a serialized object does not exist anymore.
         */
        @Override
        public AgentExecutor.Action read(ObjectInput in) throws IOException, ClassNotFoundException {
            var toolCall = new AssistantMessage.ToolCall( in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF() );

            return  new AgentExecutor.Action(  toolCall, in.readUTF() );

        }
    }

    /**
     * Represents a nullable object serializer for the {@link AgentExecutor.Outcome} object.
     */
    static class OutcomeSerializer implements NullableObjectSerializer<AgentExecutor.Outcome> {
        /**
         * Writes the provided {@code AgentExecutor.Outcome} object to the given {@code ObjectOutput}.
         *
         * @param object the {@code AgentExecutor.Outcome} object to be written to the output
         * @param out the {@code ObjectOutput} where the object will be stored
         * @throws IOException if an I/O error occurs while writing the object
         */
        @Override
        public void write(AgentExecutor.Outcome object, ObjectOutput out) throws IOException {
            writeNullableObject(object.action(),out);
            writeNullableObject(object.finish(), out);
        }

        /**
         * Reads an object from the input stream and deserializes it into a `AgentExecutor.Outcome` object.
         *
         * @param in An instance of {@link ObjectInput} used to read an object from the output stream.
         * @return A {@link AgentExecutor.Outcome} object containing the action and finish information read from the input stream.
         * @throws IOException If an I/O error occurs during reading.
         * @throws ClassNotFoundException If a class required for deserialization could not be found.
         */
        @Override
        public AgentExecutor.Outcome read(ObjectInput in) throws IOException, ClassNotFoundException {

            var action = readNullableObject(in).map(AgentExecutor.Action.class::cast).orElse(null);;
            var finish = readNullableObject(in).map(AgentExecutor.Finish.class::cast).orElse(null);

            return new AgentExecutor.Outcome(action, finish);
        }
    }

    /**
     * Represents a serializer for the {@link AgentExecutor.Step} object.
     */
    static class StepSerializer implements Serializer<AgentExecutor.Step> {
        /**
         * Writes an AgentExecutor.Step object and its action to the given ObjectOutput stream.
         *
         * @param object The AgentExecutor.Step object to be written. Must not be null.
         * @param out    The ObjectOutput stream to write the object to. Must not be null.
         * @throws IOException if an I/O error occurs while writing to the ObjectOutput stream.
         */
        @Override
        public void write(AgentExecutor.Step object, ObjectOutput out) throws IOException {
            out.writeUTF(object.observation());
            out.writeObject(object.action());
        }

        /**
         * Reads an {@code AgentExecutor.Step} object from an input stream.
         *
         * @param in the input stream to read from
         * @return the deserialized {@code AgentExecutor.Step}
         * @throws IOException if an I/O error occurs
         * @throws ClassNotFoundException if the class of the serialized object cannot be found
         */
        @Override
        public AgentExecutor.Step read(ObjectInput in) throws IOException, ClassNotFoundException {
            String observation = in.readUTF();
            var action = (AgentExecutor.Action)in.readObject();
            return new AgentExecutor.Step(action, observation);
        }
    }


}