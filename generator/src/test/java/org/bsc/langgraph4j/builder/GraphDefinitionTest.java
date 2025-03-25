package org.bsc.langgraph4j.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GraphDefinitionTest {

    @Test
    public void deserializationTest() throws Exception {
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
        objectMapper.disable(SerializationFeature.INDENT_OUTPUT);

        var graph = objectMapper.readValue(json, GraphDefinition.Graph.class);

        assertEquals( json
               .replaceAll(  "\\s*([{},:\\[\\]])\\s*", "$1"),
               objectMapper.writeValueAsString(graph) );

        // Test with invalid json
        var invalidJson = """
                {
                  "name": "CustomAgent",
                  "nodes": [
                    { "name": "start" },
                    { "name": "process" },
                    { "name": "decide" }
                  ],
                  "edges": [
                    { "from": "start", "to": "process" },
                    { "from": "process", "to": "decide" },
                    {
                      "from": "decide",
                      "condition": "check_decision",
                      "paths": [
                        "process",
                        "__end__"
                      ],
                      "to": "process"
                    }
                  ]
                }
                """;

        assertThrows(ValueInstantiationException.class,
                () -> objectMapper.readValue(invalidJson, GraphDefinition.Graph.class));

        // Test with invalid json
        var invalidJson2 = """
                {
                  "entrypoint": "start",
                  "nodes": [
                    { "name": "start" },
                    { "name": "process" },
                    { "name": "decide" }
                  ],
                  "edges": [
                    { "from": "start", "to": "process" },
                    { "from": "process", "to": "decide" },
                    {
                      "from": "decide",
                      "condition": "check_decision"
                    }
                  ]
                }
                """;

        assertThrows(ValueInstantiationException.class,
                () -> objectMapper.readValue(invalidJson2, GraphDefinition.Graph.class));

        // Test with invalid json
        var invalidJson3 = """
                {
                  "entrypoint": "start",
                  "nodes": [
                    { "name": "start" },
                    { "name": "process" },
                    { "name": "decide" }
                  ],
                  "edges": [
                    { "from": "start", "to": "process" },
                    { "from": "process", "to": "decide" },
                    {
                      "from": "decide",
                      "to": null
                    }
                  ]
                }
                """;

        assertThrows(ValueInstantiationException.class,
                () -> objectMapper.readValue(invalidJson3, GraphDefinition.Graph.class));
    }

}
