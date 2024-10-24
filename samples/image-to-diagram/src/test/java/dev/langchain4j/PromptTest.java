package dev.langchain4j;

import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.bsc.langgraph4j.utils.CollectionsUtils.mapOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PromptTest {

    private String readPromptTemplate( String resourceName ) throws Exception {
        final ClassLoader classLoader = getClass().getClassLoader();
        final InputStream inputStream = classLoader.getResourceAsStream(resourceName);
        if (inputStream == null) {
            throw new IllegalArgumentException("File not found: " + resourceName);
        }
        try( final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream)) ) {
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append('\n');
            }
            return result.toString();
        }
    }
    @Test
    void loadPrompt() throws Exception {

        String template = readPromptTemplate("prompt_template.txt");
        assertNotNull(template);

        Prompt systemMessage = PromptTemplate.from( template )
                .apply( mapOf( "format_instructions", "{ schema: true }"));

        assertNotNull(systemMessage);
        String result = "describe the diagram in the image step by step so we can translate it into diagram-as-code syntax. \n" +
                "\n" +
                " { schema: true }\n" +
                "Must not include the \"JSON schema\" in the response\n";
        assertEquals( result, systemMessage.text() );
    }
}
