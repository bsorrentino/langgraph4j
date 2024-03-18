package org.bsc.langgraph4j.action;

import org.bsc.langgraph4j.state.AgentState;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface EdgeAsyncAction<S extends AgentState> extends Function<S, CompletableFuture<String>> {

    CompletableFuture<String> apply(S t);
}
