# F001 - New feature: Pass a constant value declared in annotation to the annotated method.

## Instructions

I want to create a new feature that allows passing a constant value declared in an annotation to the annotated method. This feature will enable developers to define constant values in annotations and have those values automatically passed to the methods they annotate.
This feature must be applied to the package [annotation](langgraph4j-core/src/main/java/org/bsc/langgraph4j/annotation) which is used to annotate methods that represent actions performed by nodes in a distributed system.
The `@NodeAction` annotation should be enhanced to allow developers to specify constant values that will be passed as parameters to the annotated method.
Below an example of how this feature must be used:

```java

class MyActionCatalog implements ActionCatalog {

    @NodeAction(name = "agent_1", context = @Context( name="test", value="this is test" ))
    public Map<String,Object> agentAction1( AgentState state, RunnableConfig config,  String test ) {

        assertTrue( "this is test", test );
        var currentNode = config.nodeId();
        assertEquals( "agent_1", currentNode);

        log.info( "{}", currentNode );
        return Map.of("prop1", "test");
    }

    @NodeAction(name = "agent_2", context = @Context( name="isTest", value=false ))
    public Map<String,Object> agentAction2( AgentState state, RunnableConfig config, boolean isTest ) {

        assertFalse( isTest );
        var currentNode = config.nodeId();
        assertEquals( "agent_2", currentNode);

        log.info( "{}", currentNode );
        return Map.of("prop1", "test");
    }

}
```

You must create a nexted annotation `@Context` that allows defining a name and a value.
The value can be of any type (String, boolean, int, etc.).
The annotated method should receive the value of the `@Context` annotation as a parameter where the name matches the parameter name.

Apply this feature to the `@NodeAction` annotation, so that when a method is annotated with `@NodeAction`, it can also specify a `@Context` annotation to pass constant values to the method parameters.
`context` is an optional parameter in the `@NodeAction` annotation. If it is not provided, the method will not receive any additional parameters.

