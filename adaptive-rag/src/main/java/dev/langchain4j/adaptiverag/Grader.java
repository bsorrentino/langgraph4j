package dev.langchain4j.adaptiverag;

import dev.langchain4j.model.input.structured.StructuredPrompt;
import dev.langchain4j.model.output.structured.Description;
import dev.langchain4j.service.SystemMessage;

public class Grader {

    /**
     * Binary score for relevance check on retrieved documents.
     */
    public static class Documents {

        @Description("Documents are relevant to the question, 'yes' or 'no'")
        public String binaryScore;
    }

    @StructuredPrompt("Retrieved document: \n\n {{document}} \n\n User question: {{question}}")
    public static class CreateRecipePrompt {

        private String document;
        private String question;
    }

    public interface Retrieval {

        @SystemMessage("You are a grader assessing relevance of a retrieved document to a user question. \n" +
                "    If the document contains keyword(s) or semantic meaning related to the user question, grade it as relevant. \n" +
                "    It does not need to be a stringent test. The goal is to filter out erroneous retrievals. \n" +
                "    Give a binary score 'yes' or 'no' score to indicate whether the document is relevant to the question.")
        Documents invoke(String question);
    }
}
