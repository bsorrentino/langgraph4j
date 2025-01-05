package org.bsc.assemblyai;

import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * <p>The DotEnvConfig class loads environment variables from a .env file in the project's root directory.</p>
 */
public class DotEnvConfig {

    /**
     * Loads and configures the_dot_env_file_ by reading properties from it into System Properties.
     *
     * @return A new instance of DotEnvConfig after loading the environment variables.
     * @throws RuntimeException if no .env file is found up to 3 parent directories.
     */
    static DotEnvConfig load() {
        // Search for .env file
        Path path = Paths.get(".").toAbsolutePath();
        Path filePath = Paths.get(path.toString(), ".env");

        for (int i = 0; !filePath.toFile().exists(); ++i) {
            path = path.getParent();
            filePath = Paths.get(path.toString(), ".env");
            if (i == 3) {
                throw new RuntimeException("no .env file found!");
            }
        }

        // load .env contents in System.properties
        try {
            final java.util.Properties properties = new java.util.Properties();

            try (Reader r = new FileReader(filePath.toFile())) {
                properties.load(r);
            }
            System.getProperties().putAll(properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new DotEnvConfig();
    }


    /**
     * Retrieves the value of a specific environment variable.
     *
     * @param key The environment variable key.
     * @return An Optional containing the value if found, or an empty Optional otherwise.
     */
    public Optional<String> valueOf(String key) {
        String value = System.getenv(key);
        if (value == null) {
            value = System.getProperty(key);
        }
        return ofNullable(value);
    }

}

