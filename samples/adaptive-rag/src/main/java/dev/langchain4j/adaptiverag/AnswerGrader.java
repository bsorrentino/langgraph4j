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
 * Class to grade answers based on whether they address a given question.
 * Implements the Function interface to take in Arguments and output a Score.
 */
public record AnswerGrader(String openApiKey) implements Function<AnswerGrader.Arguments,AnswerGrader.Score> {
    /**
     * Binary score to assess answer addresses question.
     */
    public static class Score {

        @Description("Answer addresses the question, 'yes' or 'no'")
        public String binaryScore;
    }

    /**
     * Represents the arguments for a structured prompt, encapsulating both a user's question and an LLM-generated response.
     */
    @StructuredPrompt("User question: \n\n {{question}} \n\n LLM generation: {{generation}}")
    public record Arguments(
        String question,
        String generation) {}


    /**
     * Interface for service operations.
     *
     * @author [Your Name]
     * @version 1.0
     */
    interface Service {
        /**
         * Evaluates if the provided user message addresses and/or resolves the given question.
         *
         * @param userMessage The user's input that needs to be evaluated.
         * @return A binary score indicating whether the user message resolves the question ('yes') or not ('no').
         */
        @SystemMessage( "You are a grader assessing whether an answer addresses and/or resolves a question. \n\n" + 
                        "Give a binary score 'yes' or 'no'. Yes, means that the answer resolves the question otherwise return 'no'")
        Score invoke(String userMessage);
    }

    /**
     * Applies the given arguments to generate a score using a language model.
     *
     * @param args The input arguments for the prompt.
     * @return The generated score based on the prompt and model configuration.
     */
    @Override
    public Score apply(Arguments args) {
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