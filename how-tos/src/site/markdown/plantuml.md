```java
%dependency /add net.sourceforge.plantuml:plantuml-mit:1.2024.6
%dependency /resolve
```

    Adding dependency [0m[1m[32mnet.sourceforge.plantuml:plantuml-mit:1.2024.6
    [0mSolving dependencies
    Resolved artifacts count: 1
    Add to classpath: [0m[32m/Users/bsorrentino/Library/Jupyter/kernels/rapaio-jupyter-kernel/mima_cache/net/sourceforge/plantuml/plantuml-mit/1.2024.6/plantuml-mit-1.2024.6.jar[0m
    [0m


```java
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.FileFormat;

static java.awt.Image plantUML2PNG( String code ) throws IOException { 
    var reader = new SourceStringReader(code);

    try(var imageOutStream = new java.io.ByteArrayOutputStream()) {

        var description = reader.outputImage( imageOutStream, 0, new FileFormatOption(FileFormat.PNG));

        var imageInStream = new java.io.ByteArrayInputStream(  imageOutStream.toByteArray() );

        return javax.imageio.ImageIO.read( imageInStream );

    }
}

```


```java

var code = """
        @startuml
        title my first diagram
        A --> B
        @enduml
        """;

display( plantUML2PNG( code ) );

```


    
![png](plantuml_files/plantuml_2_0.png)
    





    4b9fd63a-6209-4d74-b74b-69356119c518


