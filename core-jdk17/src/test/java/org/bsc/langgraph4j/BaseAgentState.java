package org.bsc.langgraph4j;

import org.bsc.langgraph4j.state.AgentState;

import java.util.Map;

record BaseAgentState(Map<String,Object> data ) implements AgentState {}
