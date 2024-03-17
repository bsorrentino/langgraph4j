package org.bsc.langgraph4j;

import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface EdgeAction<S extends AgentState> {

    String apply(S t) throws Exception;
}
