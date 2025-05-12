package org.bsc.langgraph4j.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.junit.jupiter.api.Test;

public class CustomAgentBuilderTest {

	@Test
	public void generateFromJSONTest() throws Exception {

		var json = """
				{
				  "name": "CustomAgent",
				  "nodes": [
				  { "name": "start"  },
				  { "name": "process"  },
				  { "name": "decide"  }
				  ],
				  "edges": [
				    { "from": "start",    "to": "process" },
				    { "from": "process",  "to": "decide" },
				    {
				      "from": "decide",
				      "condition": "check_decision",
				      "paths": [
				        "process",
				        "__end__"
				      ]
				    }
				  ]
				}
				""";
		var objectMapper = new ObjectMapper();

		var graph = objectMapper.readValue(json, GraphDefinition.Graph.class);

		var gen = new Generator();

		var result1 = gen.generateBuilderFromDefinition(graph);

		System.out.println(result1);

		var result2 = gen.generateBuilderImplementationFromDefinition(graph);

		System.out.println(result2);

	}

	@Test
	public void generateFromYAMLTest() throws Exception {

		var yaml = """
				name: CustomAgent
				nodes:
				  - name: model
				  - name: tools
				edges:
				  - from: __start__
				    to: model
				  - from: tools
				    to: model
				  - from: model
				    condition: route_after_model
				    paths: [tools, __end__]
				""";
		var objectMapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));

		var graph = objectMapper.readValue(yaml, GraphDefinition.Graph.class);

		var gen = new Generator();

		var result1 = gen.generateBuilderFromDefinition(graph);

		System.out.println(result1);

		var result2 = gen.generateBuilderImplementationFromDefinition(graph);

		System.out.println(result2);

	}

}
