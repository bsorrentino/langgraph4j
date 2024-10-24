package org.bsc.spring;

import org.bsc.spring.agentexecutor.function.WeatherConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties( WeatherConfig.class)
@SpringBootApplication
public class SpringAiDemoApplication  {

	public static void main(String[] args) {
		SpringApplication.run(SpringAiDemoApplication.class, args);
	}

}

