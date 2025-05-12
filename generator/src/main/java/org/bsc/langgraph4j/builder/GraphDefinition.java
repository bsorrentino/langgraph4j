package org.bsc.langgraph4j.builder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

public class GraphDefinition {

	private static boolean isStart(String name) {
		return Objects.equals(name, "__start__");
	}

	private static boolean isEnd(String name) {
		return Objects.equals(name, "__end__");
	}

	private static String formatValue(String value) {
		if (value == null)
			return null;
		if (isStart(value)) {
			return "START";
		}
		if (isEnd(value)) {
			return "END";
		}

		return format("\"%s\"", value);
	}

	private static String formatName(String name) {
		if (name == null)
			return null;
		return name.replace(' ', '_');
	}

	/**
	 * Represents a node in the graph.
	 *
	 * @param name The name of the node.
	 */
	public record Node(String name) {
		@JsonCreator
		public Node(@JsonProperty("name") String name) {
			this.name = Objects.requireNonNull(name, "name cannot be null");
		}

		@Override
		public String name() {
			return formatName(name);
		}

		public String name$() {
			return formatValue(name);
		}
	}

	/**
	 * Represents an edge in the graph. It can be a simple edge or a conditional edge.
	 *
	 * @param from The source node of the edge.
	 * @param to The target node of the edge (for simple edges).
	 * @param condition The condition for conditional edges.
	 * @param paths The paths for conditional edges.
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Edge(String from, String to, String condition, /* Map<String, String> */ List<String> paths) {

		@JsonCreator
		public Edge(@JsonProperty("from") String from,
				@JsonProperty("to") @JsonInclude(JsonInclude.Include.NON_NULL) String to,
				@JsonProperty("condition") @JsonInclude(JsonInclude.Include.NON_NULL) String condition,
				@JsonProperty("paths") @JsonInclude(JsonInclude.Include.NON_NULL) List<String> paths) {
			this.from = Objects.requireNonNull(from, "'from' cannot be null");
			this.to = to;
			this.condition = condition;
			this.paths = paths;

			if (condition == null) {
				if (to == null) {
					throw new IllegalArgumentException("'to' cannot be null when condition is not specified");
				}
			}
			else {
				if (paths == null || paths.isEmpty()) {
					throw new IllegalArgumentException("'paths' cannot be null or empty when condition is specified");
				}
				if (to != null) {
					throw new IllegalArgumentException("'to' must be null when condition is specified");
				}
			}
		}

		@Override
		public String from() {
			return formatName(from);
		}

		public String from$() {
			return formatValue(from());
		}

		@Override
		public String to() {
			return formatName(to);
		}

		public String to$() {
			return formatValue(to());
		}

		@Override
		public String condition() {
			if (condition == null)
				return null;
			return formatName(condition);
		}

		@Override
		public List<String> paths() {
			if (paths == null)
				return null;
			return paths.stream().map(GraphDefinition::formatName).toList();
		}

		public List<String> paths$() {
			if (paths() == null)
				return null;
			return paths().stream().map(GraphDefinition::formatValue).toList();
		}

	}

	/**
	 * Represents the overall graph definition.
	 *
	 * @param name The name of graph.
	 * @param nodes The list of nodes in the graph.
	 * @param edges The list of edges in the graph.
	 */
	public record Graph(String name, List<Node> nodes, List<Edge> edges) {
		@JsonCreator
		public Graph(@JsonProperty("name") String name, @JsonProperty("nodes") List<Node> nodes,
				@JsonProperty("edges") List<Edge> edges) {
			this.name = name;
			this.nodes = Objects.requireNonNull(nodes, "nodes cannot be null");
			this.edges = Objects.requireNonNull(edges, "edges cannot be null");
		}
	}

}