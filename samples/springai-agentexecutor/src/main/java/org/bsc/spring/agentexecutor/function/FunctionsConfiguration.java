package org.bsc.spring.agentexecutor.function;

import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

/**
 * Configuration class for various functions related to weather.
 * This configuration class provides methods to define and execute weather-related functions.
 */
@Configuration
public class FunctionsConfiguration {

    /**
     * Weather configuration properties.
     */
    private final WeatherConfig props;

    /**
     * Constructor for creating a new instance of FunctionsConfiguration.
     * @param props Weather configuration properties.
     */
    public FunctionsConfiguration(WeatherConfig props) {
        this.props = props;
    }

    /**
     * Bean method to define the current weather function.
     * @return A Function object that retrieves weather conditions for a given city.
     */
    @Bean
    @Description("Get the current weather conditions for the given city.")
    public Function<WeatherFunction.Request, WeatherFunction.Response> currentWeatherFunction() {
        return new WeatherFunction(props);
    }

    /**
     * Bean method to define the weather function callback.
     * @return A FunctionCallback object that wraps a WeatherFunction and provides metadata about it.
     */
    @Bean
    public FunctionCallback weatherFunctionCallback() {

        return AgentFunctionCallbackWrapper.<WeatherFunction.Request, WeatherFunction.Response>builder( new WeatherFunction(props) )
                .withName("weatherFunctionCallback") // (1) function name
                .withDescription("Get the weather in location") // (2) function description
                .build();
    }

}
