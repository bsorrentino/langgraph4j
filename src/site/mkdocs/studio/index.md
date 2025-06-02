# 🦜🕸️ LangGraph4j Studio { .no-toc }

**Studio** is a specialized agent IDE that enables visualization, interaction, and debugging of agentic systems that runs a **Langgraph4j workflow**.

![result](../images/studio-demo.gif)

### Features

- Show graph diagram
- Run a workflow
- show which step is currently running
- Show state data for each executed step
- Allow edit state data and resume execution
- Manage Interruptions

### Demo Code 

#### Use Jetty implementation 

**Add dependency to your project**

```xml
<dependency>
    <groupId>org.bsc.langgraph4j</groupId>
    <artifactId>langgraph4j-studio-jetty</artifactId>
    <version>1.6-SNAPSHOT</version>
</dependency>
```

**Create a workflow and connect it to the playground webapp**

```java
public static void main(String[] args) throws Exception {


    var workflow = new StateGraph<>(AgentState::new);

    // define your workflow   


    var saver = new MemorySaver();
    // connect playgroud webapp to workflow
    var server = LangGraphStreamingServerJetty.builder()
                    .port(8080)
                    .title("LANGGRAPH4j - TEST")
                    .stateGraph(workflow)
                    .checkpointSaver(saver)
                    .addInputStringArg("input")
                    .build();
    // start playground
    server.start().join();
}

```

#### Use Spring Boot implementation 

**Add dependency to your project**

```xml
<dependency>
    <groupId>org.bsc.langgraph4j</groupId>
    <artifactId>langgraph4j-studio-springboot</artifactId>
    <version>1.6-SNAPSHOT</version>
</dependency>
```

**Create a Custom Configuration Bean**

```java
import org.bsc.langgraph4j.studio.springboot.AbstractLangGraphStudioConfig;

@Configuration
public class LangGraphStudioSampleConfig extends AbstractLangGraphStudioConfig {

    final LangGraphFlow flow;

    public LangGraphStudioSampleConfig() throws GraphStateException {

        var workflow = new StateGraph<>(AgentState::new);

        // define your workflow   
        this.flow = LangGraphFlow.builder()
                .title("LangGraph Studio")
                .stateGraph(workflow)
                .build();

    }

    @Override
    public LangGraphFlow getFlow() {
        return this.flow;
    }
}

```

**Create and Run a Standard Spring Boot Application**

```java
@SpringBootApplication
public class LangGraphStudioApplication {

	public static void main(String[] args) {

		SpringApplication.run(LangGraphStudioApplication.class, args);
	}

}
```
