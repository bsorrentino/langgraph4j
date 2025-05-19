# MapifyAI

‼️ **PROJECT HAS BEEN RELOCATED TO [langgraph4j/langgraph4j-examples](https://github.com/langgraph4j/langgraph4j-examples)** ‼️
----

From a Audio or Transcription to Mind-Map Diagram with this AI-powered tool.
This Application allows from an Audio or Transcription concerning a discussion, a meeting, etc ... to generate a "_meaningful mind-map diagram_", that represent the touched key points. 
This representation joined with summary provide a more complete and understandable informations 

## Journey
To implement process from audio to diagram I have create several skilled agents described below:

1. **transcribe-from-audio**: this agent _use AssemblyAI transcripts API_ to transcribe the provided audio.

1. **keypoints-from-transcript**: this Agent use OpenAI (_got-4o-mini_) to extract the Keypoints inside the given transcription

1. **summary-to-mindmap**: this agent use OpenAI (_got-4o-mini_) to arrange the key points in a kind of ontology providing a hierarchical representation of information

1. **mindmap-to-mermaid**: last agent transform the mind-map representation in a [mermaid](https://mermaid.js.org) syntax ready for the visualization

### Diagram of Agentic Architecture

```mermaid
flowchart TD
    start([__start__])
    stop([__end__])
    start -->|Audio| transcribe[transcribe-from-audio]
    transcribe -->|Transcription| keypoints[keypoints-from-transcript]
    start -->|Transcription| keypoints

    keypoints -->|Keypoints summary| summary[summary-to-mindmap]
    summary -->|Mindmap structure| mindmap[mindmap-to-mermaid]
    mindmap -->|Mermaid script| stop
```

[AssemblyAI]: https://www.assemblyai.com
[OpenAI]: https://openai.com/api/
