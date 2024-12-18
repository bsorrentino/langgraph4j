package dev.langchain4j.image_to_diagram.state;

import java.util.List;

// diagram schema
/*
const participantSchema = z.object({
    name: z.string().describe('participant name'),
    shape: z.string().describe('participant shape'), // can be an enum ?
    description: z.string().describe('participant description')
});

const relationSchema = z.object({
    source: z.string().describe('source participant'),
    target: z.string().describe('target participant'),
    description: z.string().describe('relation description')
});

const containerSchema = z.object({
    name: z.string().describe('container name'),
    children: z.array(z.string()).describe('list of contained elements name'), // can be an enum ?
    description: z.string().describe('container description')
});

export const diagramSchema = z.object({
    type: z.string().describe('Diagram tipology (one word). Eg. "sequence", "class", "process", etc."'),
    title: z.string().describe("Diagram summary (max one line) or title (if any)"),
    participants: z.array(participantSchema).describe("list of participants in the diagram "),
    relations: z.array(relationSchema).describe("list of relations in the diagram"),
    containers: z.array(containerSchema).describe("list of participants that contain other ones in the diagram"),
    description: z.array(z.string()).describe("Step by step description of the diagram with clear indication of participants and actions between them."),
});

*/

/**
 * Represents a Diagram containing various elements, participants, relations, and containers.
 */
public class Diagram {

    /**
     * Represents a participant in the diagram with a name, shape, and description.
     */
    public record Participant (String name, String shape, String description) {
    }

    /**
     * Represents a relation between two participants with a source, target, and description.
     */
    public record Relation (String source, String target, String description) {
    }

    /**
     * Represents a container that can hold child elements with a name, children list, and description.
     */
    public record Container(String name, List<String> children, String description) {
    }

    /**
     * Represents an element in the diagram with various properties including type, title, participants, relations,
     * containers, and descriptions.
     */
    public record Element(
        String type,
        String title,
        List<Participant> participants,
        List<Relation> relations,
        List<Container> containers,
        List<String> description
    ) {}

}
