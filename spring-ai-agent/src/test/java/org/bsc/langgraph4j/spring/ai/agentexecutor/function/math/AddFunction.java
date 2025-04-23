package org.bsc.langgraph4j.spring.ai.agentexecutor.function.math;

import java.util.function.Function;

public class AddFunction implements Function<AddFunction.Request, AddFunction.Response> {

    public record Request(Number op1, Number op2) {}

    /**
     * Record representing the response object returned by the WeatherFunction.
     * Contains location and current weather details.
     */
    public record Response( Number  result ) {}

    @Override
    public Response apply(Request request) {

        return new Response( request.op1().doubleValue() + request.op2().doubleValue() );
    }


}
