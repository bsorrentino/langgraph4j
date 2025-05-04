package dev.langchain4j.adaptiverag;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import java.time.Duration;
import java.util.Map;
import java.util.function.Function;

/**
 * This class implements a {@link Function} to rewrite questions for better vectorstore retrieval.
 * It uses an AI language model to rephrase input questions based on semantic intent and meaning.
 *
 */
public record QuestionRewriter( String openApiKey ) implements Function<String, String> {

    /**
     * Interface representing a service for interfacing with a Language Model.
     * <p>This interface provides a method to invoke the language model with a given question,
     * aiming to rephrase it into an optimized format for vector store retrieval.</p>
     */
    interface LLMService {

        /**
         * Converts an input question to a better version optimized for vector store retrieval.
         * 
         * @param question The input question to be rewritten.
         * @return A rewritten version of the question that is more suitable for vector store retrieval.
         */
        @SystemMessage(
                "You a question re-writer that converts an input question to a better version that is optimized \n" +
                "for vectorstore retrieval. Look at the input and try to reason about the underlying semantic intent / meaning.")
        String invoke(String question);
    }


//    private QuestionRewriter( String openApiKey ) {
//        this.openApiKey = openApiKey;
//    }

    /**
    * Applies a natural language processing pipeline to improve a given question.
    * This method uses a ChatModel with specified configuration and an LLMService to generate and invoke a new, improved question based on the provided input.
    *
    * @param question The original question to be improved.
    * @return A string representing the improved question.
    */
    @Override
    public String apply(String question) {

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


        LLMService service = AiServices.create(LLMService.class, chatLanguageModel);

        PromptTemplate template = PromptTemplate.from("Here is the initial question: \n\n {{question}} \n Formulate an improved question.");

        Prompt prompt = template.apply( Map.of( "question", question ) );

        return service.invoke( prompt.text() );
    }


}