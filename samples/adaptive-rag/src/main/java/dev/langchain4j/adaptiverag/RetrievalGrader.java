package dev.langchain4j.adaptiverag;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.structured.StructuredPrompt;
import dev.langchain4j.model.input.structured.StructuredPromptProcessor;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.structured.Description;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import java.time.Duration;
import java.util.function.Function;

/**
 * The RetrievalGrader class implements a function to assess the relevance of a retrieved document
 * to a user's question. It uses an AI service to generate a binary score ('yes' or 'no') indicating relevance.
 * This class relies on external services for processing and grading documents, making it dependent
 * on network availability and the stability of the used API.
 */
public record RetrievalGrader( String openApiKey ) implements Function<RetrievalGrader.Arguments, RetrievalGrader.Score> {

    /**
     * A static class that represents a score in a binary format.
     * <p>
     * The {@code binaryScore} field indicates relevance to a given query,
     * with 'yes' representing relevance and 'no' representing irrelevance.
     */
    public static class Score {

        @Description("Documents are relevant to the question, 'yes' or 'no'")
        public String binaryScore;
    }

    /**
     * Represents a structure for holding the arguments needed to process a user's query.
     * This class contains the `document` and `question` which are essential components
     * for any query handling system.
     *
     */
    @StructuredPrompt("Retrieved document: \n\n {{document}} \n\n User question: {{question}}")
    public record Arguments(
        String question,
        String document) {}

    /**
     * Defines an interface for providing service responses based on user queries.
     */
    interface Service {

        /**
         * Evaluates if a retrieved document is relevant to a user's question.
         * 
         * @param question The user's query string.
         * @return A binary score of 'yes' or 'no' to indicate relevance.
         */
        @SystemMessage("You are a grader assessing relevance of a retrieved document to a user question. \n" +
                "    If the document contains keyword(s) or semantic meaning related to the user question, grade it as relevant. \n" +
                "    It does not need to be a stringent test. The goal is to filter out erroneous retrievals. \n" +
                "    Give a binary score 'yes' or 'no' score to indicate whether the document is relevant to the question.")
        Score invoke(String question);
    }




    /**
     * Applies the provided arguments to generate a score using a chat language model.
     *
     * @param args The arguments containing the necessary information for processing.
     * @return A {@link Score} object representing the result of the processing.
     */
    @Override
    public Score apply(Arguments args ) {

        ChatModel chatLanguageModel = OpenAiChatModel.builder()
                .apiKey( openApiKey )
                .modelName( "gpt-3.5-turbo-0125" )
                .timeout(Duration.ofMinutes(2))
                .logRequests(true)
                .logResponses(true)
                .maxRetries(2)
                .temperature(0.0)
                .maxTokens(2000)
                .build();


        Service service = AiServices.create(Service.class, chatLanguageModel);

        Prompt prompt = StructuredPromptProcessor.toPrompt(args);

        return service.invoke(prompt.text());

    }

}