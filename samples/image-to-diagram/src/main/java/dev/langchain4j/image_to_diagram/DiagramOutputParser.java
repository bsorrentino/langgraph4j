package dev.langchain4j.image_to_diagram;

import com.google.gson.Gson;
import dev.langchain4j.image_to_diagram.state.Diagram;

import java.util.regex.Pattern;

/**
 * This class provides functionality to parse a JSON formatted string and convert it into a `Diagram.Element` object.
 */
public class DiagramOutputParser {
    /**
     * Parses a JSON string and converts it into a Diagram.Element object.
     *
     * @param s The JSON string to be parsed.
     * @return A Diagram.Element object representing the parsed JSON data.
     * @throws IllegalArgumentException If no valid JSON is found in the input string.
     */
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