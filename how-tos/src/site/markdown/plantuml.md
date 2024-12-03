# PlantUML diagrams

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
    





    8ab3c38d-cf18-42ff-bf41-b97f3b48806f


