package dev.langchain4j.image_to_diagram;

import dev.langchain4j.image_to_diagram.state.Diagram;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.openai.OpenAiChatModel;
import net.sourceforge.plantuml.ErrorUmlType;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AppenderChannel;
import org.bsc.langgraph4j.state.Channel;
import org.bsc.langgraph4j.state.Channels;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.*;

import static java.util.Optional.ofNullable;
import static org.bsc.langgraph4j.utils.CollectionsUtils.*;

/**
 * Represents the functionality to convert images to diagrams.
 */
public interface ImageToDiagram {

    /**
     * Represents the state of an agent, specifically tailored for managing diagram-related data and processes.
     */
    class State extends AgentState {
        static Map<String, Channel<?>> SCHEMA = Map.of(
                "diagramCode", Channels.appender(ArrayList::new)
        );

        /**
         * Constructs a new `State` object with the provided initialization data.
         *
         * @param initData a map containing initial state data
         */
        public State(Map<String, Object> initData) {
            super(initData);
        }

        /**
         * Retrieves the image data as an {@link ImageToDiagramProcess.ImageUrlOrData}.
         *
         * @return An {@link Optional} containing the image data, or empty if not available.
         */
        public Optional<ImageToDiagramProcess.ImageUrlOrData> imageData() {
            return value("imageData");
        }
        /**
         * Retrieves the diagram element associated with this object.
         *
         * @return an {@link Optional} containing the diagram element if it exists, otherwise an empty optional.
         */
        public Optional<Diagram.Element> diagram() {
            return value("diagram");
        }
        /**
         * Returns a list of diagram code.
         * If the "diagramCode" value is not present, returns an empty list.
         *
         * @return List of String representing the diagram code
         */
        public List<String> diagramCode() {
            return this.<List<String>>value("diagramCode").orElseGet(Collections::emptyList);
        }
        /**
        * Retrieves the evaluation result as an {@link ImageToDiagramProcess.EvaluationResult}.
        * 
        * @return An {@link Optional} containing the evaluation result, or empty if not available.
        */
        public Optional<ImageToDiagramProcess.EvaluationResult> evaluationResult() {
            return value("evaluationResult" );
        }
        /**
         * Returns an {@link Optional} containing the value associated with "evaluationError".
         *
         * @return an {@link Optional} containing the value associated with "evaluationError"
         */
        public Optional<String> evaluationError() {
            return value("evaluationError" );
        }
        /**
         * Retrieves the type of evaluation error.
         *
         * @return an {@link Optional} containing the error UML type, or an empty optional if not available
         */
        public Optional<ErrorUmlType> evaluationErrorType() {
            return value("evaluationErrorType" );
        }

        /**
        * Checks if an error of execution type has occurred.
        * @return true if the error type is execution error, false otherwise
        **/
        public boolean isExecutionError() {
            return evaluationErrorType()
                    .map( type -> type == ErrorUmlType.EXECUTION_ERROR )
                    .orElse(false);
        }

        /**
         * Checks if the last two diagrams in the code are equal.
         *
         * @return true if the last two diagrams are equal; false otherwise
         */
        public boolean lastTwoDiagramsAreEqual() {
            if( diagramCode().size() < 2 ) return false;

            String last = last( diagramCode() )
                    .map(String::trim)
                    .orElseThrow( () -> new IllegalStateException( "last() is null!" ) );
            String prev = lastMinus( diagramCode(), 1)
                    .map(String::trim)
                    .orElseThrow( () -> new IllegalStateException( "last(-1) is null!" ) );

            return last.equals(prev);
        }

    }

    enum EvaluationResult {
        OK,
        ERROR,
        UNKNOWN
    }

    /**
     * Retrieves an instance of the {@link OpenAiChatModel} configured for vision tasks.
     *
     * @return An {@link OpenAiChatModel} object with predefined settings suitable for processing images and videos.
     * @throws IllegalArgumentException if no OPENAI_API_KEY is provided in the system properties.
     */
    default OpenAiChatModel getVisionModel( ) {

        var openApiKey = ofNullable( System.getProperty("OPENAI_API_KEY") )
                .orElseThrow( () -> new IllegalArgumentException("no OPENAI_API_KEY provided!") );

        return OpenAiChatModel.builder()
                .apiKey( openApiKey )
                .modelName( "gpt-4o" )
                .timeout(Duration.ofMinutes(2))
                .logRequests(true)
                .logResponses(true)
                .maxRetries(2)
                .temperature(0.0)
                .maxTokens(2000)
                .build();

    }

    /**
     * Returns an instance of the default {@link OpenAiChatModel}.
     * 
     * @return a configured {@link OpenAiChatModel} with the API key obtained from the system property "OPENAI_API_KEY".
     * @throws IllegalArgumentException if no OPENAI_API_KEY is provided as a system property.
     */
    default OpenAiChatModel getModel( ) {
        var openApiKey = ofNullable( System.getProperty("OPENAI_API_KEY") )
                .orElseThrow( () -> new IllegalArgumentException("no OPENAI_API_KEY provided!") );

        return OpenAiChatModel.builder()
                .apiKey( openApiKey )
                .modelName( "gpt-4o-mini" )
                .logRequests(true)
                .logResponses(true)
                .maxRetries(2)
                .temperature(0.0)
                .maxTokens(2000)
                .build();

    }

    /**
     * Loads a {@link PromptTemplate} from the specified resource name.
     *
     * @param resourceName  the name of the resource file, must not be null or empty
     * @return              the loaded {@code PromptTemplate}
     * @throws Exception      if the resource is not found or an error occurs during reading
     */
    static PromptTemplate loadPromptTemplate(String resourceName ) throws Exception {
        final ClassLoader classLoader = ImageToDiagram.class.getClassLoader();
        final InputStream inputStream = classLoader.getResourceAsStream(resourceName);
        if (inputStream == null) {
            throw new IllegalArgumentException("File not found: " + resourceName);
        }
        try( final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream), 4*1024) ) {
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append('\n');
            }
            return PromptTemplate.from( result.toString() );
        }
    }

}