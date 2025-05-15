package org.bsc.langgraph4j.action;

import java.util.Map;

public record Command(String gotoNode, Map<String,Object> update) {}
