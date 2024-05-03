package dev.langchain4j.image_to_diagram;

import lombok.Data;

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

public class Diagram {

    @Data
    static public class Participant {
        String name;
        String shape;
        String description;
    }

    @Data
    static public class Relation {
        String source;
        String target;
        String description;
    }

    @Data
    static public class Container {
        String name;
        List<String> children;
        String description;
    }

    @Data
    static public class Element {
        String type;
        String title;
        List<Participant> participants;
        List<Relation> relations;
        List<Container> containers;
        List<String> description;
    }

}