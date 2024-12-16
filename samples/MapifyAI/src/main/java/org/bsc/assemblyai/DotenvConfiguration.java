package org.bsc.assemblyai;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class forDotenv integration in a Spring environment.
 */
@Configuration
public class DotenvConfiguration {

    /**
     * Creates and returns a bean of type {@link DotEnvConfig}.
     * Initializes the configuration by loading it from the environment or default properties.
     *
     * @return An instance of {@link DotEnvConfig} initialized with the appropriate values.
     */
    @Bean
    public DotEnvConfig dotenv() {
        return DotEnvConfig.load();
    }

}
