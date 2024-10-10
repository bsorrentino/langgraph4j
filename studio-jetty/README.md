# LangGraph4j Studio

It is available an **embed playground webapp** that is able to run a Langgraph4j workflow in visual way.

## Features

- [x] Show graph diagram 
- [x] Run a workflow 
- [x] show which step is currently running
- [x] Show state data for each executed step
- [x] Allow edit state data and resume execution
- [] Manage Interruptions

## Maven

```xml
<dependency>
    <groupId>org.bsc.langgraph4j</groupId>
    <artifactId>langgraph4j-studio-jetty</artifactId>
    <version>_last_release_</version>
<dependency>
```

## Sample

### Code
```java
StateGraph<AgentState> workflow = new StateGraph<>( AgentState::new );

// define your workflow   

...

var saver = new MemorySaver();
// connect playgroud webapp to workflow
var server = LangGraphStreamingServer.builder()
                                      .port(8080)
                                      .title("LANGGRAPH4j - TEST")
                                      .stateGraph( workflow )
                                      .checkpointSaver(saver)
                                      .addInputStringArg("input")
                                      .build();
// start playground
server.start().join();

```
### Demo
![result](../assets/playground-demo.gif)
