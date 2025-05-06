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
import java.util.List;
import java.util.function.Function;

/**
 * Provides functionality to grade the groundedness of an LLM generation with respect to a set of facts. 
 * The class implements the {@link Function} interface, which accepts parameters of type {@link Arguments}
 * and returns a score indicating whether the generation is grounded or not.
 */
public record HallucinationGrader( String openApiKey ) implements Function<HallucinationGrader.Arguments,HallucinationGrader.Score> {

    /**
     * Binary score for hallucination present in generation answer.
     */
    public static class Score {

        @Description("Answer is grounded in the facts, 'yes' or 'no'")
        public String binaryScore;
    }

    /**
     * Represents a set of facts and an associated LLM generation.
     * 
     * <p>
     * This class is intended to encapsulate the necessary parameters for processing structured prompts,
     * including a list of documents and a generated language model response (LLM).
     * </p>
     */
    @StructuredPrompt("Set of facts: \\n\\n {{documents}} \\n\\n LLM generation: {{generation}}")
    public record Arguments(
        List<String> documents,
        String generation ) {}

    /**
     * Interface for service operations.
     */
    interface Service {

        /**
         * Calculates the grounding score for a given user message based on whether it is grounded in/out supported by the retrieved facts.
         *
         * @param userMessage The user's message to be evaluated.
         * @return A binary score 'yes' or 'no', indicating if the answer is grounded in/supported by the set of facts.
         */
        @SystemMessage(
                "You are a grader assessing whether an LLM generation is grounded in / supported by a set of retrieved facts. \n" +
                "Give a binary score 'yes' or 'no'. 'Yes' means that the answer is grounded in / supported by the set of facts.")
        Score invoke(String userMessage);
    }

    /** Applies the provided arguments to a chat language model and returns the score. Uses OpenAI's GPT-3.5-Turbo model with specified settings. Logs requests and responses, retries twice, and has a max token limit of 2000. 
     * @param args The arguments to be applied.
     * @return A Score object from the AI grader's invocation. */
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

        Service grader = AiServices.create(Service.class, chatLanguageModel);

        Prompt prompt = StructuredPromptProcessor.toPrompt(args);

        return grader.invoke(prompt.text());

    }

}