```java
String userHomeDir = System.getProperty("user.home");
String localRespoUrl = "file://" + userHomeDir + "/.m2/repository/";
String langchain4jVersion = "0.35.0"
```


```java
%dependency /add-repo local \{localRespoUrl} release|never snapshot|always
%dependency /list-repos
```

    [0mRepository [1m[32mlocal[0m url: [1m[32mfile:///Users/bsorrentino/.m2/repository/[0m added.
    [0mRepositories count: 4
    [0mname: [1m[32mcentral [0murl: [1m[32mhttps://repo.maven.apache.org/maven2/ [0mrelease:[32mtrue [0mupdate:[32mnever [0msnapshot:[32mfalse [0mupdate:[32mnever 
    [0m[0mname: [1m[32mjboss [0murl: [1m[32mhttps://repository.jboss.org/nexus/content/repositories/releases/ [0mrelease:[32mtrue [0mupdate:[32mnever [0msnapshot:[32mfalse [0mupdate:[32mnever 
    [0m[0mname: [1m[32matlassian [0murl: [1m[32mhttps://packages.atlassian.com/maven/public [0mrelease:[32mtrue [0mupdate:[32mnever [0msnapshot:[32mfalse [0mupdate:[32mnever 
    [0m[0mname: [1m[32mlocal [0murl: [1m[32mfile:///Users/bsorrentino/.m2/repository/ [0mrelease:[32mtrue [0mupdate:[32mnever [0msnapshot:[32mtrue [0mupdate:[32malways 
    [0m

Remove installed package from Jupiter cache


```bash
%%bash 
rm -rf \{userHomeDir}/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/bsc/
```


```java
%dependency /add org.slf4j:slf4j-jdk14:2.0.9
%dependency /add org.bsc.langgraph4j:langgraph4j-core-jdk8:1.0-SNAPSHOT
%dependency /add org.bsc.langgraph4j:langgraph4j-langchain4j:1.0-SNAPSHOT
%dependency /add dev.langchain4j:langchain4j:\{langchain4jVersion}
%dependency /add dev.langchain4j:langchain4j-open-ai:\{langchain4jVersion}

%dependency /resolve
```

    Adding dependency [0m[1m[32morg.slf4j:slf4j-jdk14:2.0.9
    [0mAdding dependency [0m[1m[32morg.bsc.langgraph4j:langgraph4j-core-jdk8:1.0-SNAPSHOT
    [0mAdding dependency [0m[1m[32morg.bsc.langgraph4j:langgraph4j-langchain4j:1.0-SNAPSHOT
    [0mAdding dependency [0m[1m[32mdev.langchain4j:langchain4j:0.35.0
    [0mAdding dependency [0m[1m[32mdev.langchain4j:langchain4j-open-ai:0.35.0
    [0mSolving dependencies
    Resolved artifacts count: 26
    Add to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/slf4j/slf4j-jdk14/2.0.9/slf4j-jdk14-2.0.9.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/bsc/langgraph4j/langgraph4j-core-jdk8/1.0-SNAPSHOT/langgraph4j-core-jdk8-1.0-SNAPSHOT.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/bsc/async/async-generator-jdk8/2.2.0/async-generator-jdk8-2.2.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/bsc/langgraph4j/langgraph4j-langchain4j/1.0-SNAPSHOT/langgraph4j-langchain4j-1.0-SNAPSHOT.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/dev/langchain4j/langchain4j/0.35.0/langchain4j-0.35.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/dev/langchain4j/langchain4j-core/0.35.0/langchain4j-core-0.35.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/apache/opennlp/opennlp-tools/1.9.4/opennlp-tools-1.9.4.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/dev/langchain4j/langchain4j-open-ai/0.35.0/langchain4j-open-ai-0.35.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/dev/ai4j/openai4j/0.22.0/openai4j-0.22.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/squareup/retrofit2/retrofit/2.9.0/retrofit-2.9.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/squareup/retrofit2/converter-jackson/2.9.0/converter-jackson-2.9.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/fasterxml/jackson/core/jackson-databind/2.17.2/jackson-databind-2.17.2.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/fasterxml/jackson/core/jackson-annotations/2.17.2/jackson-annotations-2.17.2.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/fasterxml/jackson/core/jackson-core/2.17.2/jackson-core-2.17.2.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/squareup/okhttp3/okhttp/4.12.0/okhttp-4.12.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/squareup/okio/okio/3.6.0/okio-3.6.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/squareup/okio/okio-jvm/3.6.0/okio-jvm-3.6.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/squareup/okhttp3/okhttp-sse/4.12.0/okhttp-sse-4.12.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/jetbrains/kotlin/kotlin-stdlib-jdk8/1.9.10/kotlin-stdlib-jdk8-1.9.10.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/jetbrains/kotlin/kotlin-stdlib/1.9.10/kotlin-stdlib-1.9.10.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/jetbrains/kotlin/kotlin-stdlib-common/1.9.10/kotlin-stdlib-common-1.9.10.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/jetbrains/annotations/13.0/annotations-13.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/jetbrains/kotlin/kotlin-stdlib-jdk7/1.9.10/kotlin-stdlib-jdk7-1.9.10.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/com/knuddels/jtokkit/1.1.0/jtokkit-1.1.0.jar[0m
    [0m

### Initialize Logger


```java
try( var file = new java.io.FileInputStream("./logging.properties")) {
    var lm = java.util.logging.LogManager.getLogManager();
    lm.checkAccess(); 
    lm.readConfiguration( file );
}

var log = org.slf4j.LoggerFactory.getLogger("llm-streaming");

```


```java
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.output.Response;
import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;
import org.bsc.langgraph4j.langchain4j.generators.LLMStreamingGenerator;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.streaming.StreamingOutput;

var generator = LLMStreamingGenerator.<AiMessage,AgentState>builder()
                        .mapResult( r -> Map.of( "content", r.content() ) )
                        .build();

StreamingChatLanguageModel model = OpenAiStreamingChatModel.builder()
    .apiKey(System.getenv("OPENAI_API_KEY"))
    .modelName(GPT_4_O_MINI)
    .build();

String userMessage = "Tell me a joke";

model.generate(userMessage, generator.handler() );

for( var r : generator ) {
    log.info( "{}", r);
}
  
log.info( "RESULT: {}", generator.resultValue().orElse(null) );
  
//Thread.sleep( 1000 );
```

    StreamingOutput{chunk=} 
    StreamingOutput{chunk=Why} 
    StreamingOutput{chunk= did} 
    StreamingOutput{chunk= the} 
    StreamingOutput{chunk= scare} 
    StreamingOutput{chunk=crow} 
    StreamingOutput{chunk= win} 
    StreamingOutput{chunk= an} 
    StreamingOutput{chunk= award} 
    StreamingOutput{chunk=?
    
    } 
    StreamingOutput{chunk=Because} 
    StreamingOutput{chunk= he} 
    StreamingOutput{chunk= was} 
    StreamingOutput{chunk= outstanding} 
    StreamingOutput{chunk= in} 
    StreamingOutput{chunk= his} 
    StreamingOutput{chunk= field} 
    StreamingOutput{chunk=!} 
    RESULT: {content=AiMessage { text = "Why did the scarecrow win an award?
    
    Because he was outstanding in his field!" toolExecutionRequests = null }} 

