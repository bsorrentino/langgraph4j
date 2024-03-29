package org.bsc.langgraph4j;

import org.bsc.langgraph4j.state.AgentState;

record EdgeValue<State extends AgentState>(String id, EdgeCondition<State> value) {}
