package org.bsc.langgraph4j.spring.ai.agentexecutor;

import org.bsc.langgraph4j.spring.ai.agentexecutor.function.weather.WeatherConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Main application class for the SpringAI Demo project. This class is annotated with
 * {@link org.springframework.boot.autoconfigure.SpringBootApplication} to enable
 * auto-configuration, component scanning, and error handling. It also utilizes
 * {@link org.springframework.boot.context.properties.EnableConfigurationProperties} to
 * register configuration properties specific to the {@link WeatherConfig} class.
 */
@EnableConfigurationProperties(WeatherConfig.class)
@SpringBootApplication
public class SpringAiDemoApplication {

	/**
	 * The entry point of the application. This method is executed when the application
	 * starts.
	 * @param args An array of strings that are arguments passed to the application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(SpringAiDemoApplication.class, args);
	}

}