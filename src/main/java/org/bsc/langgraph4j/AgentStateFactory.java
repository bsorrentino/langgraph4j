package org.bsc.langgraph4j;


import java.util.Map;
import java.util.function.Function;

public interface AgentStateFactory<State extends AgentState> extends Function<Map<String,Object>, State> {

}
