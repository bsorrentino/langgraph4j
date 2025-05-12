# Adaptive RAG

**Initialize Logger**


```java
try( var file = new java.io.FileInputStream("./logging.properties")) {
    java.util.logging.LogManager.getLogManager().readConfiguration( file );
}

var log = org.slf4j.LoggerFactory.getLogger("AdaptiveRag");

```

## Test Issue [#32](https://github.com/bsorrentino/langgraph4j/issues/32)

Issue concerns a problem on `AdaptiveRag` implementation referred to `AnswerGrader` task


```java
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.structured.StructuredPrompt;
import dev.langchain4j.model.input.structured.StructuredPromptProcessor;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.structured.Description;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import java.time.Duration;
import java.util.function.Function;


public class AnswerGrader implements Function<AnswerGrader.Arguments,AnswerGrader.Score> {

    static final String MODELS[] =  { "gpt-3.5-turbo-0125", "gpt-4o-mini" };

    /**
     * Binary score to assess answer addresses question.
     */
    public static class Score {

        @Description("Answer addresses the question, 'yes' or 'no'")
        public String binaryScore;

        @Override
        public String toString() {
            return "Score: " + binaryScore;
        }
    }

    @StructuredPrompt("""
User question: 

{{question}}

LLM generation: 

{{generation}}
""")
    record Arguments(String question, String generation) {
    }

    interface Service {

        @SystemMessage("""
You are a grader assessing whether an answer addresses and/or resolves a question. 

Give a binary score 'yes' or 'no'. Yes, means that the answer resolves the question otherwise return 'no'
        """)
        Score invoke(String userMessage);
    }

    String openApiKey;

    @Override
    public Score apply(Arguments args) {
        ChatLanguageModel chatLanguageModel = OpenAiChatModel.builder()
                .apiKey( System.getenv("OPENAI_API_KEY")  )
                .modelName( MODELS[1] )
                .timeout(Duration.ofMinutes(2))
                .logRequests(true)
                .logResponses(true)
                .maxRetries(2)
                .temperature(0.0)
                .maxTokens(2000)
                .build();


        Service service = AiServices.create(Service.class, chatLanguageModel);

        Prompt prompt = StructuredPromptProcessor.toPrompt(args);

        log.trace( "prompt: {}", prompt.text() );
        
        return service.invoke(prompt.text());
    }

}

```


```java

var grader = new AnswerGrader();

var args = new AnswerGrader.Arguments( "What are the four operations ? ", "LLM means Large Language Model" );
grader.apply( args );

```

    prompt: User question:
    
    What are the four operations ? 
    
    LLM generation:
    
    LLM means Large Language Model
     





    Score: no




```java
var args = new AnswerGrader.Arguments( "What are the four operations", "There are four basic operations: addition, subtraction, multiplication, and division." );   
grader.apply( args );

```

    prompt: User question:
    
    What are the four operations
    
    LLM generation:
    
    There are four basic operations: addition, subtraction, multiplication, and division.
     





    Score: yes




```java
var args = new AnswerGrader.Arguments( "What player at the Bears expected to draft first in the 2024 NFL draft?", "The Bears selected USC quarterback Caleb Williams with the No. 1 pick in the 2024 NFL Draft." );   
grader.apply( args );

```

    prompt: User question:
    
    What player at the Bears expected to draft first in the 2024 NFL draft?
    
    LLM generation:
    
    The Bears selected USC quarterback Caleb Williams with the No. 1 pick in the 2024 NFL Draft.
     





    Score: yes


