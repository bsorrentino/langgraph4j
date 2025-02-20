package org.bsc.quarkus;

import org.bsc.langgraph4j.GraphStateException;

public interface LangGraphFlowService {

    LangGraphFlow getFlow() throws GraphStateException;

}
