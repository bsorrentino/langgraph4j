# 🦜🕸️ LangGraph4j Agent Executor implementation




```java
public void main( String args[] ) throws Exception 
{

        var chatLanguageModel = OpenAiChatModel.builder()
                .apiKey( System.getenv( "OPENAI_API_KEY" ) )
                .modelName( "gpt-4o-mini" )
                .logResponses(true)
                .maxRetries(2)
                .temperature(0.0)
                .maxTokens(2000)
                .build();

        return AgentExecutor.graphBuilder()
                .chatLanguageModel(chatLanguageModel)
                .toolSpecification(new TestTool())
                .build();

}
```


    