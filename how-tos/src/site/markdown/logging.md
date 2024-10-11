```java
String userHomeDir = System.getProperty("user.home");
String localRespoUrl = "file://" + userHomeDir + "/.m2/repository/";
String slf4jVersion = "2.0.9"
```


```java
%dependency /add-repo local \{localRespoUrl} release|never snapshot|always
%dependency /list-repos
```


    RuntimeException: Existing maven repository: local
    

       at org.rapaio.jupyter.kernel.core.magic.dependencies.MimaDependencyManager.addMavenRepository(MimaDependencyManager.java:60)

       at org.rapaio.jupyter.kernel.core.magic.handlers.DependencyHandler.evalLineAddRepo(DependencyHandler.java:160)

       at org.rapaio.jupyter.kernel.core.magic.MagicHandler.eval(MagicHandler.java:40)

       at org.rapaio.jupyter.kernel.core.magic.MagicEngine.eval(MagicEngine.java:95)

       at org.rapaio.jupyter.kernel.core.RapaioKernel.handleExecuteRequest(RapaioKernel.java:191)

       at org.rapaio.jupyter.kernel.channels.ShellChannel.lambda$bind$0(ShellChannel.java:52)

       at org.rapaio.jupyter.kernel.channels.LoopThread.run(LoopThread.java:21)



```java
%dependency /add org.bsc.langgraph4j:langgraph4j-core-jdk8:1.0-SNAPSHOT
%dependency /add org.slf4j:slf4j-jdk14:\{slf4jVersion}

%dependency /resolve
```

## Initialize Logger 


```java
var lm = java.util.logging.LogManager.getLogManager();
lm.checkAccess(); 
try( var file = new java.io.FileInputStream("./logging.properties")) {
    lm.readConfiguration( file );
    java.util.logging.Logger.getLogger("").addHandler(new java.util.logging.ConsoleHandler());
}
```

## Test Logger


```java

var log = org.slf4j.LoggerFactory.getLogger(org.bsc.langgraph4j.CompiledGraph.class);


log.trace( "this is a trace message {}", "TRACE2");
log.info( "this is a info message {}", "INFO1");

```

    2024-09-24 17:07:12 INFO REPL.$JShell$15 do_it$ this is a info message INFO1
    2024-09-24 17:07:12 INFO REPL.$JShell$15 do_it$ this is a info message INFO1

