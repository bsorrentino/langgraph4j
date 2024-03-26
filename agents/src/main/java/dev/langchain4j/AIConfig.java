package dev.langchain4j;

import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class AIConfig {

    public static AIConfig load()  {
        var path = Paths.get(".").toAbsolutePath();

        var filePath = Paths.get( path.toString(), ".env");

        for( int i=0; !filePath.toFile().exists(); ++i ) {
            path = path.getParent();

            filePath = Paths.get(path.toString(), ".env");

            if (i == 3) {
                throw new RuntimeException("no .env file found!");
            }
        }

        try {
            return new AIConfig( filePath );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private AIConfig( Path envFilePath ) throws Exception {
        final java.util.Properties properties = new java.util.Properties();

        try( Reader r = new FileReader(envFilePath.toFile())) {
            properties.load(r);
        }
        System.getProperties().putAll(properties);
    }

    public Optional<String> valueOf(String key ) {
        return Optional.ofNullable(System.getenv( key ))
                .or( () -> Optional.ofNullable(System.getProperty(key)));
    }


}
