package dev.langchain4j.adaptiverag;

import dev.langchain4j.model.output.structured.Description;
import dev.langchain4j.service.SystemMessage;

/**
 * Router for user queries to the most relevant datasource.
 */
public class Router {

    public enum Type {
        vectorstore,
        websearch
    }
    /**
    * Route a user query to the most relevant datasource.
    */
    public static class DataSource {

        @Description("Given a user question choose to route it to web search or a vectorstore.")
        Type datasource;
    }


    public interface Extractor {

        @SystemMessage("You are an expert at routing a user question to a vectorstore or web search.\n" +
                "The vectorstore contains documents related to agents, prompt engineering, and adversarial attacks.\n" +
                "Use the vectorstore for questions on these topics. Otherwise, use web-search.")
        DataSource route(String question);
    }
}
