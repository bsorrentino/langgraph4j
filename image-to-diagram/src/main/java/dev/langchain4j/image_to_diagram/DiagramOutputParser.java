package dev.langchain4j.image_to_diagram;

import com.google.gson.Gson;
import dev.langchain4j.model.output.OutputParser;
import lombok.var;

import java.util.regex.Pattern;

public class DiagramOutputParser implements OutputParser<Diagram.Element> {
    @Override
    public Diagram.Element parse(String s) {
        // String pattern = "```json\n(.*?)\n```";
        String pattern = "```json\n(.*?)\n```";

        // Create a Pattern object
        var jsonPattern = Pattern.compile(pattern, Pattern.DOTALL | Pattern.MULTILINE);

        // Create a Matcher object
        var matcher = jsonPattern.matcher(s);

        // Check if a match is found
        if (!matcher.find()) {
            throw new IllegalArgumentException("no diagram provided! in text\n" + s);
        }

        var gson = new Gson();
        return gson.fromJson(matcher.group(1), Diagram.Element.class);
    }

    @Override
    public String formatInstructions() {
        return "extract json from markdown text";
    }
}
