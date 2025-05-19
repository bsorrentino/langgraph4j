# Langgraph4j - Image To Diagram

‼️ **PROJECT HAS BEEN RELOCATED TO [langgraph4j/langgraph4j-examples](https://github.com/langgraph4j/langgraph4j-examples)** ‼️
----

## Generate PlantUML diagram from Image

The "<u>Generate PlantUML diagram from Image</u>" flow  involves structured sequence of steps. Initially, an agent receives an image and is responsible for analyzing and describing its content. This description is then passed to a specialized agent equipped with the skills to translate the description into PlantUML code. To ensure precision in diagram generation, the type of diagram identified within the image dictates the selection of the appropriately skilled agent for the translation task. This ensures that each diagram type is handled by an agent with specific expertise relevant to that diagram. 

### Diagram of solution
```mermaid
---
title: Image to diagram with correction
---
flowchart TD
	__START__((start))
	__END__((stop))
	agent_describer("agent_describer")
	agent_sequence_plantuml("agent_sequence_plantuml")
	agent_generic_plantuml("agent_generic_plantuml")
	%%	condition1{"check state"}
	__START__:::__START__ --> agent_describer:::agent_describer
	%%	agent_describer:::agent_describer --> condition1:::condition1
	%%	condition1:::condition1 -->|sequence| agent_sequence_plantuml:::agent_sequence_plantuml
	agent_describer:::agent_describer -->|sequence| agent_sequence_plantuml:::agent_sequence_plantuml
	%%	condition1:::condition1 -->|generic| agent_generic_plantuml:::agent_generic_plantuml
	agent_describer:::agent_describer -->|generic| agent_generic_plantuml:::agent_generic_plantuml
	agent_sequence_plantuml:::agent_sequence_plantuml --> __END__:::__END__
	agent_generic_plantuml:::agent_generic_plantuml --> __END__:::__END__
```

### Handle translation errors

In the case that there are errors in result of PlantUML code we have established a supplementary flow that provided a correction process consisting of iteration between both verification and rewrite steps as shown below

#### Diagram of solution

```mermaid
---
title: Correction Process
---
flowchart TD
__START__((start))
__END__((stop))
evaluate_result("evaluate_result")
agent_review("agent_review")
%%	condition1{"check state"}
__START__:::__START__ --> evaluate_result:::evaluate_result
agent_review:::agent_review --> evaluate_result:::evaluate_result
%%	evaluate_result:::evaluate_result --> condition1:::condition1
%%	condition1:::condition1 -->|ERROR| agent_review:::agent_review
evaluate_result:::evaluate_result -->|ERROR| agent_review:::agent_review
%%	condition1:::condition1 -->|UNKNOWN| __END__:::__END__
evaluate_result:::evaluate_result -->|UNKNOWN| __END__:::__END__
%%	condition1:::condition1 -->|OK| __END__:::__END__
evaluate_result:::evaluate_result -->|OK| __END__:::__END__
```

### Merge All

Finally we can put all together having a complete flow that include also a refinement process over result.

#### Diagram of solution

```mermaid
---
title: Image to diagram with correction
---
flowchart TD
	__START__((start))
	__END__((stop))
	agent_describer("agent_describer")
	agent_sequence_plantuml("agent_sequence_plantuml")
	agent_generic_plantuml("agent_generic_plantuml")
subgraph evaluate_result
	#__START__@{ shape: start, label: "enter" }
	#__END__@{ shape: stop, label: "exit" }
	#evaluate_result("evaluate_result")
	#agent_review("agent_review")
	%%	#condition1{"check state"}
	#__START__:::__START__ --> #evaluate_result:::evaluate_result
	#agent_review:::agent_review --> #evaluate_result:::evaluate_result
	%%	#evaluate_result:::evaluate_result --> #condition1:::condition1
	%%	#condition1:::condition1 -->|ERROR| #agent_review:::agent_review
	#evaluate_result:::evaluate_result -->|ERROR| #agent_review:::agent_review
	%%	#condition1:::condition1 -->|UNKNOWN| #__END__:::__END__
	#evaluate_result:::evaluate_result -->|UNKNOWN| #__END__:::__END__
	%%	#condition1:::condition1 -->|OK| #__END__:::__END__
	#evaluate_result:::evaluate_result -->|OK| #__END__:::__END__
end
	%%	condition1{"check state"}
	__START__:::__START__ --> agent_describer:::agent_describer
	%%	agent_describer:::agent_describer --> condition1:::condition1
	%%	condition1:::condition1 -->|sequence| agent_sequence_plantuml:::agent_sequence_plantuml
	agent_describer:::agent_describer -->|sequence| agent_sequence_plantuml:::agent_sequence_plantuml
	%%	condition1:::condition1 -->|generic| agent_generic_plantuml:::agent_generic_plantuml
	agent_describer:::agent_describer -->|generic| agent_generic_plantuml:::agent_generic_plantuml
	agent_sequence_plantuml:::agent_sequence_plantuml --> evaluate_result:::evaluate_result
	agent_generic_plantuml:::agent_generic_plantuml --> evaluate_result:::evaluate_result
	evaluate_result:::evaluate_result --> __END__:::__END__

```

[agentexecutor]: agentexecutor.puml.png
[image_to_diagram]: image_to_diagram.puml.png
[image_to_diagram_correction]: image_to_diagram_with_correction.puml.png
[correction_process]: correction_process.puml.png



