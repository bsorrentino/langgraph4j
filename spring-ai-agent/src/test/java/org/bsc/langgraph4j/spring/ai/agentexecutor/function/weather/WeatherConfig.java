package org.bsc.langgraph4j.spring.ai.agentexecutor.function.weather;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for weather services.
 *
 */
@ConfigurationProperties(prefix = "weather")
public record WeatherConfig(String apiKey, String apiUrl) {
}