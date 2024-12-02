package dev.langchain4j.image_to_diagram.serializer.gson;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import dev.langchain4j.image_to_diagram.ImageToDiagramProcess;
import dev.langchain4j.image_to_diagram.state.Diagram;
import dev.langchain4j.image_to_diagram.ImageToDiagram;
import net.sourceforge.plantuml.ErrorUmlType;
import org.bsc.langgraph4j.serializer.plain_text.gson.GsonStateSerializer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONStateSerializer extends GsonStateSerializer<ImageToDiagram.State> {

    public JSONStateSerializer() {
        super(ImageToDiagram.State::new, new GsonBuilder()
                .registerTypeAdapter(ImageToDiagram.State.class, new StateDeserializer())
                //.registerTypeAdapter(Diagram.Element.class, new ElementDeserializer())
                .create());
    }

}

class StateDeserializer implements JsonDeserializer<ImageToDiagram.State> {

    @Override
    public ImageToDiagram.State deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonElement dataElement = json.getAsJsonObject().get("data");

        JsonElement imageDataElement = dataElement.getAsJsonObject().get("imageData");
        JsonElement diagramCodeElement = dataElement.getAsJsonObject().getAsJsonArray("diagramCode");
        JsonElement diagramElement = dataElement.getAsJsonObject().getAsJsonObject("diagram");
        JsonPrimitive evaluationResultElement = dataElement.getAsJsonObject().getAsJsonPrimitive("evaluationResult");
        JsonPrimitive evaluationErrorElement = dataElement.getAsJsonObject().getAsJsonPrimitive("evaluationError");
        JsonPrimitive evaluationErrorTypeElement = dataElement.getAsJsonObject().getAsJsonPrimitive("evaluationErrorType");

        Map<String,Object> data = new HashMap<>();

        var gson = new Gson();

        if( imageDataElement != null ) {
            if( imageDataElement.isJsonPrimitive()  ) {
                var base64 = imageDataElement.getAsString();
                var imageData = ImageToDiagramProcess.ImageUrlOrData.of(base64);
                data.put( "imageData", imageData );
            }
            else if ( imageDataElement.isJsonObject() ) {
                var imageUrlOrData = imageDataElement.getAsJsonObject();
                var imageData = gson.fromJson(imageUrlOrData, new TypeToken<ImageToDiagramProcess.ImageUrlOrData>() {
                }.getType());
                data.put("imageData", imageData);
            }
        }

        List<String> diagramCode = gson.fromJson(diagramCodeElement, new TypeToken<List<String>>() {}.getType());
        data.put( "diagramCode", diagramCode );

        if( diagramElement != null  ) {
            var diagram = gson.fromJson(diagramElement, new TypeToken<Diagram.Element>() {}.getType());
            data.put( "diagram", diagram );
        }

        if( evaluationResultElement != null ) {
            var evaluationResult = gson.fromJson(evaluationResultElement, new TypeToken<ImageToDiagram.EvaluationResult>() {}.getType() );
            data.put( "evaluationResult", evaluationResult );
        }

        if( evaluationErrorElement != null ) {
            var evaluationError = evaluationErrorElement.getAsString();
            data.put( "evaluationError", evaluationError );
        }

        if( evaluationErrorTypeElement != null ) {
            var evaluationErrorType = gson.fromJson(evaluationErrorTypeElement, new TypeToken<ErrorUmlType>() {}.getType() );
            data.put( "evaluationErrorType", evaluationErrorType );
        }

        return new ImageToDiagram.State(data);
    }
}

class ElementDeserializer implements JsonDeserializer<Diagram.Element> {
    @Override
    public Diagram.Element deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonElement typeElement = json.getAsJsonObject().get("type");
        JsonElement titleElement = json.getAsJsonObject().get("title");
        JsonElement participantsElement = json.getAsJsonObject().get("participants");
        JsonElement relationsElement = json.getAsJsonObject().get("relations");
        JsonElement containersElement = json.getAsJsonObject().get("containers");
        JsonElement descriptionElement = json.getAsJsonObject().get("description");

        var type = typeElement.getAsString();
        var title = titleElement.getAsString();

        var gson = new Gson();
        List<Diagram.Participant> participants = gson.fromJson(participantsElement, new TypeToken<List<Diagram.Participant>>() {}.getType());
        List<Diagram.Relation> relations = gson.fromJson(relationsElement, new TypeToken<List<Diagram.Relation>>() {}.getType());
        List<Diagram.Container> containers = gson.fromJson(containersElement, new TypeToken<List<Diagram.Container>>() {}.getType());
        List<String> description = gson.fromJson(descriptionElement, new TypeToken<List<String>>() {}.getType());

        return new Diagram.Element(type, title, participants, relations, containers, description);
    }
}