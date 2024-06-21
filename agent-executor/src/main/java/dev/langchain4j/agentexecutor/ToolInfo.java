package dev.langchain4j.agentexecutor;

import dev.langchain4j.agent.tool.DefaultToolExecutor;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolExecutor;
import dev.langchain4j.agent.tool.ToolSpecification;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static dev.langchain4j.agent.tool.ToolSpecifications.toolSpecificationFrom;
import static java.util.Collections.unmodifiableList;

@Value
@Accessors( fluent = true)
public class ToolInfo {
    @NonNull  ToolSpecification specification;
    @NonNull ToolExecutor executor;

    public static List<ToolInfo> of( Object ...objectsWithTools) {
        return fromArray( (Object[])objectsWithTools );
    }
    public static List<ToolInfo> fromArray( Object[] objectsWithTools ) {
        List<ToolInfo> toolSpecifications = new ArrayList<>();

        for (Object objectWithTools : objectsWithTools) {
            for (Method method : objectWithTools.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(Tool.class)) {
                    ToolSpecification toolSpecification = toolSpecificationFrom(method);
                    ToolExecutor executor = new DefaultToolExecutor(objectWithTools, method);
                    toolSpecifications.add( new ToolInfo( toolSpecification, executor));
                }
            }
        }
        return unmodifiableList(toolSpecifications);
    }
    public static List<ToolInfo> fromList(List<Object> objectsWithTools ) {
        return fromArray(objectsWithTools.toArray());
    }

}