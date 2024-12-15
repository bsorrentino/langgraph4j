package org.bsc.langgraph4j.agentexecutor.serializer.std;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import org.bsc.langgraph4j.agentexecutor.*;
import org.bsc.langgraph4j.agentexecutor.state.AgentAction;
import org.bsc.langgraph4j.agentexecutor.state.AgentFinish;
import org.bsc.langgraph4j.agentexecutor.state.AgentOutcome;
import org.bsc.langgraph4j.agentexecutor.state.IntermediateStep;
import org.bsc.langgraph4j.langchain4j.serializer.std.ToolExecutionRequestSerializer;
import org.bsc.langgraph4j.serializer.Serializer;
import org.bsc.langgraph4j.serializer.std.NullableObjectSerializer;
import org.bsc.langgraph4j.serializer.std.ObjectStreamStateSerializer;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Map;



/**
 * The STDStateSerializer class is responsible for serializing the state of the AgentExecutor.
 * It extends the ObjectStreamStateSerializer with a specific type of AgentExecutor.State.
 */
public class STDStateSerializer extends ObjectStreamStateSerializer<AgentExecutor.State> {

    /**
     * Constructs a new instance of STDStateSerializer.
     * It initializes the serializer by registering various classes with their corresponding serializers.
     */
    public STDStateSerializer() {
        super(AgentExecutor.State::new);

        mapper().register(ToolExecutionRequest.class, new ToolExecutionRequestSerializer());
        mapper().register(AgentAction.class, new AgentActionSerializer());
        mapper().register(AgentFinish.class, new AgentFinishSerializer());
        mapper().register(AgentOutcome.class, new AgentOutcomeSerializer());
        mapper().register(IntermediateStep.class, new IntermediateStepSerializer());
    }
}


/**
 * The AgentActionSerializer class implements the Serializer interface for the AgentAction type.
 * It provides methods to serialize and deserialize AgentAction objects.
 */
class AgentActionSerializer implements Serializer<AgentAction> {

    /**
     * Serializes the given AgentAction object to the specified output stream.
     *
     * @param action the AgentAction object to serialize
     * @param out the output stream to write the serialized data to
     * @throws IOException if an I/O error occurs during serialization
     */
    @Override
    public void write(AgentAction action, ObjectOutput out) throws IOException {
        ToolExecutionRequest ter =  action.toolExecutionRequest();
        out.writeUTF( ter.id() );
        out.writeUTF( ter.name() );
        out.writeUTF( ter.arguments() );
        out.writeUTF( action.log() );
    }

    /**
     * Deserializes an AgentAction object from the specified input stream.
     *
     * @param in the input stream to read the serialized data from
     * @return the deserialized AgentAction object
     * @throws IOException if an I/O error occurs during deserialization
     * @throws ClassNotFoundException if the class of a serialized object cannot be found
     */
    @Override
    public AgentAction read(ObjectInput in) throws IOException, ClassNotFoundException {
        ToolExecutionRequest ter = ToolExecutionRequest.builder()
                .id(in.readUTF())
                .name(in.readUTF())
                .arguments(in.readUTF())
                .build();

        return  new AgentAction(  ter, in.readUTF() );
    }
}


/**
 * The AgentFinishSerializer class implements the Serializer interface for the AgentFinish type.
 * It provides methods to serialize and deserialize AgentFinish objects.
 */
class AgentFinishSerializer implements Serializer<AgentFinish> {

    /**
     * Serializes the given AgentFinish object to the specified ObjectOutput.
     *
     * @param object the AgentFinish object to serialize
     * @param out the ObjectOutput to write the serialized data to
     * @throws IOException if an I/O error occurs during serialization
     */
    @Override
    public void write(AgentFinish object, ObjectOutput out) throws IOException {
        out.writeObject(object.returnValues());
        out.writeUTF(object.log());
    }

    /**
     * Deserializes an AgentFinish object from the specified ObjectInput.
     *
     * @param in the ObjectInput to read the serialized data from
     * @return the deserialized AgentFinish object
     * @throws IOException if an I/O error occurs during deserialization
     * @throws ClassNotFoundException if the class of a serialized object cannot be found
     */
    @Override
    @SuppressWarnings("unchecked")
    public AgentFinish read(ObjectInput in) throws IOException, ClassNotFoundException {
        Map<String, Object> returnValues = (Map<String, Object>)in.readObject();
        String log = in.readUTF();
        return new AgentFinish(returnValues, log);
    }

}


/**
 * The AgentOutcomeSerializer class is responsible for serializing and deserializing 
 * AgentOutcome objects. It implements the NullableObjectSerializer interface, 
 * providing methods to write and read nullable objects.
 */
class AgentOutcomeSerializer implements NullableObjectSerializer<AgentOutcome> {
    
    /**
     * Serializes the given AgentOutcome object to the specified output stream.
     *
     * @param object the AgentOutcome object to serialize
     * @param out the output stream to write the serialized object to
     * @throws IOException if an I/O error occurs during serialization
     */
    @Override
    public void write(AgentOutcome object, ObjectOutput out) throws IOException {
        writeNullableObject(object.action(), out);
        writeNullableObject(object.finish(), out);
    }

    /**
     * Deserializes an AgentOutcome object from the specified input stream.
     *
     * @param in the input stream to read the serialized object from
     * @return the deserialized AgentOutcome object
     * @throws IOException if an I/O error occurs during deserialization
     * @throws ClassNotFoundException if the class of a serialized object cannot be found
     */
    @Override
    public AgentOutcome read(ObjectInput in) throws IOException, ClassNotFoundException {
        AgentAction action = readNullableObject(in).map(AgentAction.class::cast).orElse(null);
        AgentFinish finish = readNullableObject(in).map(AgentFinish.class::cast).orElse(null);
        return new AgentOutcome(action, finish);
    }
}


/**
 * The IntermediateStepSerializer class implements the Serializer interface for the IntermediateStep type.
 * It provides methods to serialize and deserialize IntermediateStep objects.
 */
class IntermediateStepSerializer implements Serializer<IntermediateStep> {
    
    /**
     * Serializes the given IntermediateStep object to the specified ObjectOutput.
     *
     * @param object the IntermediateStep object to serialize
     * @param out the ObjectOutput to write the serialized data to
     * @throws IOException if an I/O error occurs during serialization
     */
    @Override
    public void write(IntermediateStep object, ObjectOutput out) throws IOException {
        out.writeUTF(object.observation());
        out.writeObject(object.action());
    }

    /**
     * Deserializes an IntermediateStep object from the specified ObjectInput.
     *
     * @param in the ObjectInput to read the serialized data from
     * @return the deserialized IntermediateStep object
     * @throws IOException if an I/O error occurs during deserialization
     * @throws ClassNotFoundException if the class of a serialized object cannot be found
     */
    @Override
    public IntermediateStep read(ObjectInput in) throws IOException, ClassNotFoundException {
        String observation = in.readUTF();
        AgentAction action = (AgentAction)in.readObject();
        return new IntermediateStep(action, observation);
    }
}
