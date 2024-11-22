package org.bsc.assemblyai;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotenvConfiguration {

    @Bean
    public DotEnvConfig dotenv() {
        return DotEnvConfig.load();
    }

}

