```java
String userHomeDir = System.getProperty("user.home");
String localRespoUrl = "file://" + userHomeDir + "/.m2/repository/";
String slf4jVersion = "2.0.9";
String langgraph4jVersion = "1.5-SNAPSHOT";
```


```java
%dependency /add-repo local \{localRespoUrl} release|never snapshot|always
// %dependency /list-repos
%dependency /add org.bsc.langgraph4j:langgraph4j-core:\{langgraph4jVersion}
%dependency /add org.slf4j:slf4j-jdk14:\{slf4jVersion}

%dependency /resolve
```

## Initialize Logger 


```java
try( var file = new java.io.FileInputStream("./logging.properties")) {
    java.util.logging.LogManager.getLogManager().readConfiguration( file );
    java.util.logging.Logger.getLogger("").addHandler(new java.util.logging.ConsoleHandler());
}
```

## Test Logger


```java

var log = org.slf4j.LoggerFactory.getLogger(org.bsc.langgraph4j.CompiledGraph.class);


log.trace( "this is a trace message {}", "TRACE2");
log.info( "this is a info message {}", "INFO1");

```

    this is a trace message TRACE2 
    this is a trace message TRACE2 
    this is a info message INFO1 
    this is a info message INFO1 

