package dev.langchain4j.image_to_diagram;

import com.google.gson.Gson;

import java.util.regex.Pattern;

public class DiagramOutputParser {
    public Diagram.Element parse(String s) {
        // String pattern = "```json\n(.*?)\n```";
        String pattern = "```json\n(.*?)\n```";

        // Create a Pattern object
        Pattern jsonPattern = Pattern.compile(pattern, Pattern.DOTALL | Pattern.MULTILINE);

        // Create a Matcher object
        java.util.regex.Matcher matcher = jsonPattern.matcher(s);

        // Check if a match is found
        if (!matcher.find()) {
            throw new IllegalArgumentException("no diagram provided! in text\n" + s);
        }

        Gson gson = new Gson();
        return gson.fromJson(matcher.group(1), Diagram.Element.class);
    }
}
