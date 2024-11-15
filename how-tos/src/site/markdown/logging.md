```java
String userHomeDir = System.getProperty("user.home");
String localRespoUrl = "file://" + userHomeDir + "/.m2/repository/";
String slf4jVersion = "2.0.9"
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


```java
%dependency /add org.bsc.langgraph4j:langgraph4j-core-jdk8:1.0-SNAPSHOT
%dependency /add org.slf4j:slf4j-jdk14:\{slf4jVersion}

%dependency /resolve
```

    Adding dependency [0m[1m[32morg.bsc.langgraph4j:langgraph4j-core-jdk8:1.0-SNAPSHOT
    [0mAdding dependency [0m[1m[32morg.slf4j:slf4j-jdk14:2.0.9
    [0mSolving dependencies
    Resolved artifacts count: 4
    Add to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/bsc/langgraph4j/langgraph4j-core-jdk8/1.0-SNAPSHOT/langgraph4j-core-jdk8-1.0-SNAPSHOT.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/bsc/async/async-generator-jdk8/2.2.0/async-generator-jdk8-2.2.0.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar[0m
    [0mAdd to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/org/slf4j/slf4j-jdk14/2.0.9/slf4j-jdk14-2.0.9.jar[0m
    [0m

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

    this is a trace message TRACE2 
    this is a trace message TRACE2 
    this is a info message INFO1 
    this is a info message INFO1 

