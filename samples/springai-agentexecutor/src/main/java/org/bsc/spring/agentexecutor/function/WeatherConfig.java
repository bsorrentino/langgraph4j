package org.bsc.spring.agentexecutor.function;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties( prefix = "weather")
public record WeatherConfig( String apiKey, String apiUrl ) {
}
