package org.bsc.assemblyai;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Class responsible for configuring the Dotenv environment.
 * 
 * @configuration This class is annotated with @Configuration to indicate a Spring configuration class.
 */
@Configuration
public class DotenvConfiguration {

    /**
     * Bean method that initializes and returns a new instance of DotEnvConfig.
     *
     * @bean This method is annotated with @Bean to declare it as a bean in the Spring application context.
     * @returns A new instance of DotEnvConfig initialized by calling its load() method.
     */
    @Bean
    public DotEnvConfig dotenv() {
        return DotEnvConfig.load();
    }

}
