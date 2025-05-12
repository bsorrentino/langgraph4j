package org.bsc.langgraph4j.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.reflect.ReflectionObjectHandler;

import java.io.*;
import java.util.Map;

public class Generator {

	private static class MapMethodReflectionHandler extends ReflectionObjectHandler {

		@Override
		protected boolean areMethodsAccessible(Map<?, ?> map) {
			return true;
		}

	}

	public record Result(String stub, String implementation) {
	}

	final Mustache customAgentBuilderTemplate;

	final Mustache customAgentBuilderImplTemplate;

	public static void main(String[] args) throws Exception {

		var definitionBuilder = new StringBuilder();
		try (var reader = new BufferedReader(new InputStreamReader(System.in))) {
			// Read one line
			var line = "";
			while ((line = reader.readLine()) != null) {
				definitionBuilder.append(line).append('\n');
				;
			}
		}

		if (definitionBuilder.isEmpty()) {
			throw new IllegalArgumentException("expected input!");
		}

		var gen = new Generator();

		var objectMapperYAML = new ObjectMapper((new YAMLFactory()));

		var graph = objectMapperYAML.readValue(definitionBuilder.toString(), GraphDefinition.Graph.class);

		var stub = gen.generateBuilderFromDefinition(graph);

		var implementation = gen.generateBuilderImplementationFromDefinition(graph);

		var result = new Result(stub, implementation);

		var objectMapper = new ObjectMapper();

		System.out.println(objectMapper.writeValueAsString(result));
	}

	public Generator() {
		var factory = new DefaultMustacheFactory() {

			/**
			 * skip value encoding
			 */
			@Override
			public void encode(String value, Writer writer) {
				try {
					writer.write(value);
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		};
		factory.setObjectHandler(new MapMethodReflectionHandler());
		this.customAgentBuilderTemplate = factory.compile("CustomAgentBuilder.mustache");
		this.customAgentBuilderImplTemplate = factory.compile("CustomAgentBuilderImpl.mustache");

	}

	public String generateBuilderFromDefinition(GraphDefinition.Graph graph) throws IOException {

		var out = new StringWriter();

		customAgentBuilderTemplate.execute(out, graph).flush();

		return out.toString();
	}

	public String generateBuilderImplementationFromDefinition(GraphDefinition.Graph graph) throws IOException {

		var out = new StringWriter();

		customAgentBuilderImplTemplate.execute(out, graph).flush();

		return out.toString();
	}

}
