package org.bsc.langgraph4j;

import java.util.Map;
import java.util.concurrent.CompletableFuture;


@FunctionalInterface
public interface NodeAsyncAction<T extends AgentState> {
    CompletableFuture<Map<String, Object>> apply(T t) throws Exception;

}
