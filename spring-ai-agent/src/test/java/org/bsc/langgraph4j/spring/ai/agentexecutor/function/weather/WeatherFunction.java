package org.bsc.langgraph4j.spring.ai.agentexecutor.function.weather;

import org.springframework.web.client.RestClient;
import java.util.function.Function;

/**
 * Class representing the WeatherFunction which implements a
 * Function interface to retrieve weather data from an external API.
 */
public class WeatherFunction implements Function<WeatherFunction.Request, WeatherFunction.Response> {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WeatherFunction.class);


    /**
     * Record representing the request object for WeatherFunction.
     * Only contains the city for which weather data is requested.
     */
    public record Request(String city) {}

    /**
     * Record representing the response object returned by the WeatherFunction.
     * Contains location and current weather details.
     */
    public record Response(Location location, Current current) {}

    /**
     * Record representing location information as part of the weather response.
     * Includes name, region, country, latitude, and longitude.
     */
    public record Location(String name, String region, String country, Long lat, Long lon) {}

    /**
     * Record representing current weather details as part of the weather response.
     * Includes temperature in Fahrenheit, condition description, wind speed in miles per hour, and humidity.
     */
    public record Current(String temp_f, Condition condition, String wind_mph, String humidity) {}

    /**
     * Record representing weather conditions described in text form.
     */
    public record Condition(String text) {}

    /**
     * RestClient instance for making HTTP requests.
     */
    private final RestClient restClient;

    /**
     * Configuration properties for the WeatherFunction, including API URL and key.
     */
    private final WeatherConfig weatherProps;

    /**
     * Constructor for the WeatherFunction class that initializes the necessary components
     * based on provided configuration properties and logs relevant debug information.
     * @param props Configuration properties for initializing the RestClient.
     */
    public WeatherFunction(WeatherConfig props) {
        this.weatherProps = props;
        log.debug("Weather API URL: {}", weatherProps.apiUrl());
        log.debug("Weather API Key: {}", weatherProps.apiKey());
        this.restClient = RestClient.create(weatherProps.apiUrl());
    }

    /**
     * Main method to apply the function, which retrieves weather data for a given city.
     * @param weatherRequest The request object containing the city for which weather data is required.
     * @return The response object containing the retrieved weather data.
     */
    @Override
    public WeatherFunction.Response apply(WeatherFunction.Request weatherRequest) {
        log.info("Weather Request: {}", weatherRequest);

        var response = restClient.get()
                .uri("/current.json?key={key}&q={q}", weatherProps.apiKey(), weatherRequest.city())
                .retrieve()
                .body(Response.class);
        log.info("Weather API Response:\n{}", response);

        return response;
    }
}
