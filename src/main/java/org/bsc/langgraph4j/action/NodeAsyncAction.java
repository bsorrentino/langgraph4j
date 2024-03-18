package org.bsc.langgraph4j.action;

import org.bsc.langgraph4j.state.AgentState;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;


@FunctionalInterface
public interface NodeAsyncAction<T extends AgentState> extends Function<T, CompletableFuture<Map<String, Object>>> {
    CompletableFuture<Map<String, Object>> apply(T t) ;

}
