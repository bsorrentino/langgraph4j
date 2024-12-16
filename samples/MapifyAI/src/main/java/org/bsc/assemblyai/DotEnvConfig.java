package org.bsc.assemblyai;

import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * This class is designed to load environment variables from a `.env` file and make them available as system properties or environment variables.
 */
public class DotEnvConfig {

    /**
     * Loads the `.env` file and its contents into the system properties.
     *
     * @return A new instance of {@link DotEnvConfig} after loading.
     * @throws RuntimeException If no `.env` file is found after searching up to 4 directories deep from the current working directory. 
     */
    static DotEnvConfig load() {

        // Search for .env file
        Path path = Paths.get(".").toAbsolutePath();

        Path filePath = Paths.get( path.toString(), ".env");

        for( int i=0; !filePath.toFile().exists(); ++i ) {
            path = path.getParent();

            filePath = Paths.get(path.toString(), ".env");

            if (i == 3) {
                throw new RuntimeException("no .env file found!");
            }
        }

        // load .env contents in System.properties
        try {
            final java.util.Properties properties = new java.util.Properties();

            try( Reader r = new FileReader(filePath.toFile())) {
                properties.load(r);
            }
            System.getProperties().putAll(properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new DotEnvConfig();
    }


    /**
     * Retrieves the value of an environment variable.
     *
     * @param key The name of the environment variable.
     * @return An {@link Optional} containing the value if it exists, otherwise an empty Optional.
     */
    public Optional<String> valueOf(String key ) {
        String value = System.getenv(key);
        if (value == null) {
            value = System.getProperty(key);
        }
        return ofNullable(value);
    }


}
